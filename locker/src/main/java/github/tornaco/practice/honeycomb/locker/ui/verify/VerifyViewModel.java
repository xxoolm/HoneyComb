package github.tornaco.practice.honeycomb.locker.ui.verify;

import android.app.Application;
import android.os.Handler;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableInt;
import androidx.lifecycle.AndroidViewModel;
import github.tornaco.practice.honeycomb.locker.app.LockerContext;
import github.tornaco.practice.honeycomb.locker.app.LockerManager;
import github.tornaco.practice.honeycomb.locker.server.verify.VerifyResult;
import lombok.Setter;

import static github.tornaco.practice.honeycomb.locker.server.verify.VerifyResult.REASON_USER_CANCEL;

public class VerifyViewModel extends AndroidViewModel {
    @Setter
    private int requestCode;
    @Setter
    public String pkg;
    public ObservableBoolean verified = new ObservableBoolean(false);
    public ObservableInt progress = new ObservableInt(100);

    public VerifyViewModel(@NonNull Application application) {
        super(application);
    }

    public void verify() {
        LockerContext lockerContext = LockerContext.createContext();
        LockerManager lockerManager = lockerContext.getLockerManager();
        // Objects.requireNonNull(lockerManager).setVerifyResult(requestCode, VerifyResult.PASS, REASON_USER_INPUT_CORRECT);
        // verified.set(true);
        checkTimeout();
    }

    public void cancel() {
        LockerContext lockerContext = LockerContext.createContext();
        LockerManager lockerManager = lockerContext.getLockerManager();
        Objects.requireNonNull(lockerManager).setVerifyResult(requestCode, VerifyResult.FAIL, REASON_USER_CANCEL);
        verified.set(true);
    }

    private void checkTimeout() {
        Handler h = new Handler();
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (progress.get() == 0) {
                    onTimeout();
                    return;
                }
                progress.set(progress.get() - 1);
                if (!verified.get()) {
                    h.postDelayed(this, 1000);
                }
            }
        }, 1000);
    }

    private void onTimeout() {
        cancel();
    }
}
