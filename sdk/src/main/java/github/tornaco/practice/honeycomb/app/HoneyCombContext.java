package github.tornaco.practice.honeycomb.app;

import android.annotation.NonNull;
import android.annotation.Nullable;
import android.content.IntentFilter;

import github.tornaco.practice.honeycomb.HoneyCombManager;
import github.tornaco.practice.honeycomb.am.ActivityManager;
import github.tornaco.practice.honeycomb.annotations.AvailableAfterOnStart;
import github.tornaco.practice.honeycomb.data.PreferenceManager;
import github.tornaco.practice.honeycomb.event.IEventSubscriber;
import github.tornaco.practice.honeycomb.pm.PackageManager;
import github.tornaco.practice.honeycomb.sdk.BuildConfig;

public interface HoneyCombContext {

    String HONEY_COMB_SERVICE = android.content.Context.TV_INPUT_SERVICE;
    String PACKAGE_MANAGER_SERVICE = "package";
    String PREFERENCE_MANAGER_SERVICE = "pref";
    String ACTIVITY_MANAGER_SERVICE = "activity";

    @AvailableAfterOnStart
    @NonNull
    HoneyCombManager getHoneyCombManager();

    @Nullable
    @AvailableAfterOnStart
    PackageManager getPackageManager();

    @Nullable
    @AvailableAfterOnStart
    PreferenceManager getPreferenceManager();

    @Nullable
    @AvailableAfterOnStart
    ActivityManager getActivityManager();

    @AvailableAfterOnStart
    void registerEventSubscriber(IntentFilter filter, IEventSubscriber subscriber);

    @AvailableAfterOnStart
    void unRegisterEventSubscriber(IEventSubscriber subscriber);

    void recycle();

    @AvailableAfterOnStart
    static HoneyCombContext createContext() {
        return new HoneyCombContextImpl();
    }

    interface HoneyCombConfigs {
        boolean DEBUG = BuildConfig.DEBUG;
        boolean DEF_SHOW_CURRENT_COMPONENT_ENABLED = BuildConfig.DEBUG;
    }

    interface HoneyCombKeys {
        String KEY_PREFIX = BuildConfig.APPLICATION_ID.replace(".", "_") + "_";
        String KEY_SHOW_CURRENT_COMPONENT_ENABLED = KEY_PREFIX + "show_current_component_view_enabled";
    }
}
