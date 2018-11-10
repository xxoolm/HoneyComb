package github.tornaco.practice.honeycomb.locker.ui.verify;

import android.Manifest;
import android.app.Application;
import android.content.pm.PackageManager;
import android.os.CountDownTimer;

import org.newstand.logger.Logger;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.os.CancellationSignal;
import androidx.databinding.Observable;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableInt;
import androidx.lifecycle.AndroidViewModel;
import github.tornaco.practice.honeycomb.locker.app.LockerContext;
import github.tornaco.practice.honeycomb.locker.app.LockerManager;
import github.tornaco.practice.honeycomb.locker.server.verify.VerifyResult;
import github.tornaco.practice.honeycomb.locker.util.fingerprint.FingerprintManagerCompat;
import lombok.Setter;

import static github.tornaco.practice.honeycomb.locker.server.verify.VerifyResult.REASON_USER_CANCEL;
import static github.tornaco.practice.honeycomb.locker.server.verify.VerifyResult.REASON_USER_FP_INCORRECT;
import static github.tornaco.practice.honeycomb.locker.server.verify.VerifyResult.REASON_USER_KEY_NOT_SET;

public class VerifyViewModel extends AndroidViewModel {
    private static final long PROGRESS_MAX = LockerContext.LOCKER_VERIFY_TIMEOUT_MILLS;
    @Setter
    private int requestCode;
    @Setter
    public String pkg;
    public ObservableBoolean verified = new ObservableBoolean(false);
    public ObservableInt progress = new ObservableInt((int) PROGRESS_MAX);
    public ObservableInt progressMax = new ObservableInt((int) PROGRESS_MAX);
    public ObservableInt failCount = new ObservableInt(0);
    private CancellationSignal cancellationSignal;

    public VerifyViewModel(@NonNull Application application) {
        super(application);
    }

    public void start() {
        if (!isCurrentLockMethodKeySet()) {
            pass(REASON_USER_KEY_NOT_SET);
            return;
        }
        setupFingerPrint();
        checkTimeout();
    }

    public void verify(String input) {
        if (!isInputCorrect(input)) {
            failOnce();
        } else {
            pass(VerifyResult.REASON_USER_INPUT_CORRECT);
        }
    }

    private void pass(int reason) {
        LockerContext lockerContext = LockerContext.createContext();
        LockerManager lockerManager = lockerContext.getLockerManager();
        Objects.requireNonNull(lockerManager).setVerifyResult(requestCode, VerifyResult.PASS, reason);
        verified.set(true);
    }

    private void failOnce() {
        failCount.set(failCount.get() + 1);
    }

    public void failFinally(int reason) {
        LockerContext lockerContext = LockerContext.createContext();
        LockerManager lockerManager = lockerContext.getLockerManager();
        Objects.requireNonNull(lockerManager).setVerifyResult(requestCode, VerifyResult.FAIL, reason);
        verified.set(true);
    }

    public void cancel() {
        if (!verified.get()) {
            LockerContext lockerContext = LockerContext.createContext();
            LockerManager lockerManager = lockerContext.getLockerManager();
            Objects.requireNonNull(lockerManager).setVerifyResult(requestCode, VerifyResult.FAIL, REASON_USER_CANCEL);
            verified.set(true);
        }
    }

    private int getLockMethod() {
        LockerContext lockerContext = LockerContext.createContext();
        LockerManager lockerManager = lockerContext.getLockerManager();
        return Objects.requireNonNull(lockerManager).getLockerMethod();
    }

    private boolean isInputCorrect(String input) {
        LockerContext lockerContext = LockerContext.createContext();
        LockerManager lockerManager = lockerContext.getLockerManager();
        return Objects.requireNonNull(lockerManager).isLockerKeyValid(getLockMethod(), input);
    }

    private boolean isCurrentLockMethodKeySet() {
        LockerContext lockerContext = LockerContext.createContext();
        LockerManager lockerManager = lockerContext.getLockerManager();
        return Objects.requireNonNull(lockerManager).isLockerKeySet(getLockMethod());
    }

    private void setupFingerPrint() {
        LockerContext lockerContext = LockerContext.createContext();
        LockerManager lockerManager = lockerContext.getLockerManager();
        if (lockerManager == null || !lockerManager.isFingerPrintEnabled()) {
            return;
        }
        cancelFingerPrint();
        cancellationSignal = authenticateFingerPrint(
                new FingerprintManagerCompat.AuthenticationCallback() {
                    @Override
                    public void onAuthenticationSucceeded(
                            FingerprintManagerCompat.AuthenticationResult result) {
                        super.onAuthenticationSucceeded(result);
                        Logger.d("onAuthenticationSucceeded:" + result);
                        pass(REASON_USER_FP_INCORRECT);
                    }

                    @Override
                    public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
                        super.onAuthenticationHelp(helpMsgId, helpString);
                        Logger.i("onAuthenticationHelp:" + helpString);
                    }

                    @Override
                    public void onAuthenticationFailed() {
                        super.onAuthenticationFailed();
                        Logger.d("onAuthenticationFailed");
                    }

                    @Override
                    public void onAuthenticationError(int errMsgId, CharSequence errString) {
                        super.onAuthenticationError(errMsgId, errString);
                        Logger.d("onAuthenticationError:" + errString);
                    }
                });
    }

    private void cancelFingerPrint() {
        if (cancellationSignal != null) {
            cancellationSignal.cancel();
        }
    }

    private CancellationSignal authenticateFingerPrint(FingerprintManagerCompat.AuthenticationCallback callback) {
        if (ActivityCompat.checkSelfPermission(getApplication(), Manifest.permission.USE_FINGERPRINT)
                != PackageManager.PERMISSION_GRANTED) {
            Logger.w("FP Permission is missing...");
            return null;
        }
        if (!FingerprintManagerCompat.from(getApplication()).isHardwareDetected()) {
            Logger.w("FP HW is missing...");
            return null;
        }
        CancellationSignal cancellationSignal = new CancellationSignal();
        FingerprintManagerCompat.from(getApplication())
                .authenticate(null, 0, cancellationSignal, callback, null);
        Logger.i("FP authenticate...");
        return cancellationSignal;
    }

    private void checkTimeout() {
        // 60FPS
        // 1000 / 60 ~= 16.7ms
        CountDownTimer countDownTimer = new CountDownTimer(
                LockerContext.LOCKER_VERIFY_TIMEOUT_MILLS, 17) {
            @Override
            public void onTick(long l) {
                progress.set((int) l);
            }

            @Override
            public void onFinish() {
                onTimeout();
            }
        };
        countDownTimer.start();
        verified.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                countDownTimer.cancel();
            }
        });
    }

    private void onTimeout() {
        cancel();
    }
}
