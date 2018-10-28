package github.tornaco.practice.honeycomb.locker.app;

import github.tornaco.practice.honeycomb.locker.BuildConfig;

public interface LockerKeys {
    String KEY_PREFIX = BuildConfig.APPLICATION_ID.replace(".", "_") + "_";
    String KEY_LOCKER_ENABLED = KEY_PREFIX + "locker_enabled";
}
