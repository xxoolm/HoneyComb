package github.tornaco.practice.honeycomb.locker.ui.setup;

import android.app.Application;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableBoolean;
import androidx.lifecycle.AndroidViewModel;
import github.tornaco.practice.honeycomb.locker.app.LockerContext;
import github.tornaco.practice.honeycomb.locker.app.LockerManager;

public class SetupViewModel extends AndroidViewModel {
    public ObservableBoolean setupComplete = new ObservableBoolean(false);

    public SetupViewModel(@NonNull Application application) {
        super(application);
    }

    public void start() {
    }

    public void cancel() {

    }

    public int getLockMethod() {
        LockerContext lockerContext = LockerContext.createContext();
        LockerManager lockerManager = lockerContext.getLockerManager();
        return Objects.requireNonNull(lockerManager).getLockerMethod();
    }


}
