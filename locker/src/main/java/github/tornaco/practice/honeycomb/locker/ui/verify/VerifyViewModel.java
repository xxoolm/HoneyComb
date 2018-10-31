package github.tornaco.practice.honeycomb.locker.ui.verify;

import android.app.Application;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableBoolean;
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

    public VerifyViewModel(@NonNull Application application) {
        super(application);
    }

    public void verify() {
        LockerContext lockerContext = LockerContext.createContext();
        LockerManager lockerManager = lockerContext.getLockerManager();
        // Objects.requireNonNull(lockerManager).setVerifyResult(requestCode, VerifyResult.PASS, REASON_USER_INPUT_CORRECT);
        // verified.set(true);
    }

    public void cancel() {
        LockerContext lockerContext = LockerContext.createContext();
        LockerManager lockerManager = lockerContext.getLockerManager();
        Objects.requireNonNull(lockerManager).setVerifyResult(requestCode, VerifyResult.FAIL, REASON_USER_CANCEL);
        verified.set(true);
    }
}
