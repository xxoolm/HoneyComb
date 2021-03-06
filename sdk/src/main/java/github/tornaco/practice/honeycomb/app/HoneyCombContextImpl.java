package github.tornaco.practice.honeycomb.app;

import android.content.IntentFilter;
import android.os.ServiceManager;

import github.tornaco.practice.honeycomb.HoneyCombManager;
import github.tornaco.practice.honeycomb.IHoneyComb;
import github.tornaco.practice.honeycomb.am.ActivityManager;
import github.tornaco.practice.honeycomb.am.IActivityManager;
import github.tornaco.practice.honeycomb.data.IPreferenceManager;
import github.tornaco.practice.honeycomb.data.PreferenceManager;
import github.tornaco.practice.honeycomb.event.IEventSubscriber;
import github.tornaco.practice.honeycomb.pm.IModuleManager;
import github.tornaco.practice.honeycomb.pm.IPackageManager;
import github.tornaco.practice.honeycomb.pm.ModuleManager;
import github.tornaco.practice.honeycomb.pm.PackageManager;
import lombok.Getter;

@Getter
class HoneyCombContextImpl implements HoneyCombContext {

    private HoneyCombManager honeyCombManager;
    private PackageManager packageManager;
    private PreferenceManager preferenceManager;
    private ActivityManager activityManager;
    private ModuleManager moduleManager;

    HoneyCombContextImpl() {
        IHoneyComb hb = IHoneyComb.Stub.asInterface(ServiceManager.getService(HONEY_COMB_SERVICE));
        this.honeyCombManager = new HoneyCombManager(hb);

        if (honeyCombManager.isPresent() && honeyCombManager.hasService(PACKAGE_MANAGER_SERVICE)) {
            IPackageManager pm = IPackageManager.Stub.asInterface(honeyCombManager.getService(PACKAGE_MANAGER_SERVICE));
            this.packageManager = new PackageManager(pm);
        }

        if (honeyCombManager.isPresent() && honeyCombManager.hasService(PREFERENCE_MANAGER_SERVICE)) {
            IPreferenceManager pref = IPreferenceManager.Stub.asInterface(honeyCombManager.getService(PREFERENCE_MANAGER_SERVICE));
            this.preferenceManager = new PreferenceManager(pref);
        }

        if (honeyCombManager.isPresent() && honeyCombManager.hasService(ACTIVITY_MANAGER_SERVICE)) {
            IActivityManager activity = IActivityManager.Stub.asInterface(honeyCombManager.getService(ACTIVITY_MANAGER_SERVICE));
            this.activityManager = new ActivityManager(activity);
        }

        if (honeyCombManager.isPresent() && honeyCombManager.hasService(MODULE_MANAGER_SERVICE)) {
            IModuleManager module = IModuleManager.Stub.asInterface(honeyCombManager.getService(MODULE_MANAGER_SERVICE));
            this.moduleManager = new ModuleManager(module);
        }
    }

    @Override
    public void registerEventSubscriber(IntentFilter filter, IEventSubscriber subscriber) {
        if (honeyCombManager != null) {
            honeyCombManager.registerEventSubscriber(filter, subscriber);
        }
    }

    @Override
    public void unRegisterEventSubscriber(IEventSubscriber subscriber) {
        if (honeyCombManager != null) {
            honeyCombManager.unRegisterEventSubscriber(subscriber);
        }
    }

    @Override
    public void recycle() {
        this.activityManager = null;
        this.honeyCombManager = null;
        this.packageManager = null;
        this.preferenceManager = null;
        this.moduleManager = null;
    }
}
