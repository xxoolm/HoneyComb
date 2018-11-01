package github.tornaco.practice.honeycomb.locker.app;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import github.tornaco.practice.honeycomb.locker.BuildConfig;

public interface LockerContext {

    String LOCKER_SERVICE = "locker";
    long LOCKER_VERIFY_TIMEOUT_MILLS = 60 * 1000;

    @Nullable
    LockerManager getLockerManager();

    @NonNull
    static LockerContext createContext() {
        return new LockerContextImpl();
    }

    interface LockerMethod {
        int NONE = -1;
        int PIN = 0;
        int PATTERN = 1;
    }

    interface LockerKeys {
        String KEY_PREFIX = BuildConfig.APPLICATION_ID.replace(".", "_") + "_";
        String KEY_LOCKER_ENABLED = KEY_PREFIX + "locker_enabled";
        String KEY_LOCKER_METHOD = KEY_PREFIX + "locker_method";
        String KEY_LOCKER_KEY_PREFIX = KEY_PREFIX + "locker_key_";
    }

    interface LockerIntents {
        String LOCKER_VERIFY_ACTION = "github.tornaco.practice.honeycomb.locker.action.VERIFY";
        String LOCKER_VERIFY_EXTRA_PACKAGE = "pkg";
        String LOCKER_VERIFY_EXTRA_REQUEST_CODE = "request_code";
    }
}
