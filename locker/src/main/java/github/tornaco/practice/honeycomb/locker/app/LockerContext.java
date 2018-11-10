package github.tornaco.practice.honeycomb.locker.app;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import github.tornaco.practice.honeycomb.locker.BuildConfig;
import github.tornaco.practice.honeycomb.locker.ui.verify.VerifyActivity;

public interface LockerContext {

    String LOCKER_PKG_NAME = BuildConfig.APPLICATION_ID;
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

    interface LockerConfigs {
        boolean DEBUG = BuildConfig.DEBUG;
        boolean DEF_LOCKER_ENABLED = false;
        int DEF_LOCKER_METHOD = LockerMethod.NONE;
        boolean DEF_RE_VERIFY_ON_SCREEN_OFF = true;
        boolean DEF_RE_VERIFY_ON_TASK_REMOVED = true;
        boolean DEF_RE_VERIFY_ON_APP_SWITCH = false;
        long DEF_VERIFY_RES_WORKAROUND_DELAY = 300;
        boolean DEF_VERIFY_RES_WORKAROUND_ENABLED = true;
        boolean DEF_FP_ENABLED = true;
    }

    interface LockerKeys {
        String KEY_PREFIX = BuildConfig.APPLICATION_ID.replace(".", "_") + "_";
        String KEY_LOCKER_ENABLED = KEY_PREFIX + "locker_enabled";
        String KEY_LOCKER_METHOD = KEY_PREFIX + "locker_method";
        String KEY_RE_VERIFY_ON_SCREEN_OFF = KEY_PREFIX + "re_verify_on_screen_off";
        String KEY_RE_VERIFY_ON_APP_SWITCH = KEY_PREFIX + "re_verify_on_app_switch";
        String KEY_RE_VERIFY_ON_TASK_REMOVED = KEY_PREFIX + "re_verify_on_task_removed";
        String KEY_LOCKER_KEY_PREFIX = KEY_PREFIX + "locker_key_";
        String KEY_VERIFY_RES_WORKAROUND_DELAY = KEY_PREFIX + "locker_verify_workaround_delay_";
        String KEY_VERIFY_RES_WORKAROUND_ENABLED = KEY_PREFIX + "locker_verify_workaround_enabled_";
        String KEY_FP_ENABLED = KEY_PREFIX + "locker_verify_fp_enabled_";
    }

    interface LockerIntents {
        String LOCKER_VERIFY_CLASS_NAME = VerifyActivity.class.getName();
        String LOCKER_VERIFY_ACTION = "github.tornaco.practice.honeycomb.locker.action.VERIFY";
        String LOCKER_VERIFY_EXTRA_PACKAGE = "pkg";
        String LOCKER_VERIFY_EXTRA_REQUEST_CODE = "request_code";
    }
}
