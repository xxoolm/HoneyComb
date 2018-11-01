package github.tornaco.practice.honeycomb.locker.ui.verify;

import android.app.Application;
import android.os.CountDownTimer;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.databinding.Observable;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableInt;
import androidx.lifecycle.AndroidViewModel;
import github.tornaco.practice.honeycomb.locker.app.LockerContext;
import github.tornaco.practice.honeycomb.locker.app.LockerManager;
import github.tornaco.practice.honeycomb.locker.server.verify.VerifyResult;
import lombok.Setter;

import static github.tornaco.practice.honeycomb.locker.server.verify.VerifyResult.REASON_USER_CANCEL;
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

    public VerifyViewModel(@NonNull Application application) {
        super(application);
    }

    public void start() {
        if (!isCorrentLockMethodKeySet()) {
            pass(REASON_USER_KEY_NOT_SET);
            return;
        }
        checkTimeout();
    }

    public void verify(String input) {
        if (!isInputCorrect(input)) {
            failOnce();
        }
    }

    public void pass(int reason) {
        LockerContext lockerContext = LockerContext.createContext();
        LockerManager lockerManager = lockerContext.getLockerManager();
        Objects.requireNonNull(lockerManager).setVerifyResult(requestCode, VerifyResult.PASS, reason);
        verified.set(true);
    }

    public void failOnce() {
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

    public int getLockMethod() {
        LockerContext lockerContext = LockerContext.createContext();
        LockerManager lockerManager = lockerContext.getLockerManager();
        return Objects.requireNonNull(lockerManager).getLockerMethod();
    }

    public boolean isInputCorrect(String input) {
        LockerContext lockerContext = LockerContext.createContext();
        LockerManager lockerManager = lockerContext.getLockerManager();
        return Objects.requireNonNull(lockerManager).isLockerKeyValid(getLockMethod(), input);
    }

    public boolean isCorrentLockMethodKeySet() {
        LockerContext lockerContext = LockerContext.createContext();
        LockerManager lockerManager = lockerContext.getLockerManager();
        return Objects.requireNonNull(lockerManager).isLockerKeySet(getLockMethod());
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
