package github.tornaco.practice.honeycomb.locker.ui.setup;

import android.app.Application;

import org.newstand.logger.Logger;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;
import androidx.lifecycle.AndroidViewModel;
import github.tornaco.practice.honeycomb.locker.app.LockerContext;
import github.tornaco.practice.honeycomb.locker.app.LockerManager;

public class SetupViewModel extends AndroidViewModel {

    public enum SetupStage {
        /**
         * 第一次输入
         */
        First,
        /**
         * 第二次输入
         */
        Confirm,
        /**
         * 完成
         */
        Complete
    }

    public static final int SETUP_ERROR_KEY_NOT_MATCH = 1;
    public static final int SETUP_ERROR_KEY_NONE = 0;

    private ObservableField<String> firstKey = new ObservableField<>(),
            secondKey = new ObservableField<>();

    public ObservableField<SetupStage> stage = new ObservableField<>(SetupStage.First);
    ObservableBoolean setupComplete = new ObservableBoolean(false);
    public ObservableInt setupError = new ObservableInt(SETUP_ERROR_KEY_NONE);

    public SetupViewModel(@NonNull Application application) {
        super(application);
    }

    public void start() {

    }

    public void cancel() {

    }

    private int getLockMethod() {
        LockerContext lockerContext = LockerContext.createContext();
        LockerManager lockerManager = lockerContext.getLockerManager();
        return Objects.requireNonNull(lockerManager).getLockerMethod();
    }

    public void onStartInput() {
        setupError.set(SETUP_ERROR_KEY_NONE);
    }

    public void onInputComplete(String input) {
        Logger.d("onInputComplete %s %s", input, stage.get());
        switch (Objects.requireNonNull(stage.get())) {
            case First:
                firstKey.set(input);
                break;
            case Confirm:
                secondKey.set(input);
                break;
            case Complete:
                break;
            default:
                break;
        }
    }

    public void onInputConfirm() {
        Logger.d("onInputConfirm %s %s %s", firstKey.get(), secondKey.get(), stage.get());
        switch (Objects.requireNonNull(stage.get())) {
            case First:
                stage.set(SetupStage.Confirm);
                break;
            case Confirm:
                if (Objects.requireNonNull(secondKey.get()).equals(firstKey.get())) {
                    stage.set(SetupStage.Complete);
                    onSetupComplete();
                } else {
                    setupError.set(SETUP_ERROR_KEY_NOT_MATCH);
                    stage.set(SetupStage.First);
                    firstKey.set(null);
                    secondKey.set(null);
                }
                break;
            case Complete:
                break;
            default:
                break;
        }
    }

    private void onSetupComplete() {
        Logger.d("onSetupComplete %s %s %s", firstKey.get(), secondKey.get(), stage.get());
        LockerContext lockerContext = LockerContext.createContext();
        LockerManager lockerManager = lockerContext.getLockerManager();
        Objects.requireNonNull(lockerManager).setLockerKey(getLockMethod(), firstKey.get());
        setupComplete.set(true);
    }
}
