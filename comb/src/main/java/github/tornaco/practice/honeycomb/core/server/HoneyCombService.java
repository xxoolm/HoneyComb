package github.tornaco.practice.honeycomb.core.server;

import android.app.IApplicationThread;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.os.ServiceManager;

import org.newstand.logger.Logger;

import github.tornaco.practice.honeycomb.BuildConfig;
import github.tornaco.practice.honeycomb.IHoneyComb;
import github.tornaco.practice.honeycomb.annotations.SystemInit;
import github.tornaco.practice.honeycomb.app.HoneyCombContext;
import github.tornaco.practice.honeycomb.core.server.am.ActivityManagerService;
import github.tornaco.practice.honeycomb.core.server.data.PreferenceManagerService;
import github.tornaco.practice.honeycomb.core.server.device.PowerManagerService;
import github.tornaco.practice.honeycomb.core.server.event.EventBus;
import github.tornaco.practice.honeycomb.core.server.i.HoneyComb;
import github.tornaco.practice.honeycomb.core.server.pm.ModuleManagerService;
import github.tornaco.practice.honeycomb.core.server.pm.PackageManagerService;
import github.tornaco.practice.honeycomb.event.Event;
import github.tornaco.practice.honeycomb.event.IEventSubscriber;
import lombok.Getter;

import static github.tornaco.practice.honeycomb.util.PkgUtils.PACKAGE_NAME_ANDROID;

public class HoneyCombService implements HoneyComb {

    @Getter
    private Context systemContext;
    @Getter
    private ActivityManagerService activityManager;
    @Getter
    private PowerManagerService powerManager;
    @Getter
    private PackageManagerService packageManager;
    private ModuleManagerService moduleManagerService;
    private PreferenceManagerService preferenceManager;

    @Override
    @SystemInit
    public void onStart(Context context) {
        Logger.w("HoneyCombService start with context %s", context);
        this.systemContext = context;
        this.activityManager = new ActivityManagerService();
        this.powerManager = new PowerManagerService();
        this.packageManager = new PackageManagerService();
        this.moduleManagerService = new ModuleManagerService();
        // TODO Split for diff pkg.
        this.preferenceManager = new PreferenceManagerService(PACKAGE_NAME_ANDROID);

        publish();
        publishInternal();

        this.activityManager.onStart(context);
        this.powerManager.onStart(context);
        this.packageManager.onStart(context);
        this.moduleManagerService.onStart(context);
        this.preferenceManager.onStart(context);
    }

    @Override
    @SystemInit
    public void onSystemReady() {
        Logger.d("onSystemReady!!!");
        this.activityManager.onSystemReady();
        this.powerManager.onSystemReady();
        this.packageManager.onSystemReady();
        this.moduleManagerService.onSystemReady();
        this.preferenceManager.onSystemReady();
    }

    @Override
    @SystemInit
    public void onShutDown() {
        this.activityManager.onShutDown();
        this.powerManager.onShutDown();
        this.packageManager.onShutDown();
        this.moduleManagerService.onShutDown();
        this.preferenceManager.onShutDown();
    }

    @Override
    public boolean allowBroadcastIntentSending(IApplicationThread applicationThread, Intent intent) {
        Logger.i("allowBroadcastIntentSending? %s %s", applicationThread, intent);
        if (intent.getAction() == null) {
            return true;
        }
        EventBus.getInstance().publishEventToSubscribersAsync(new Event(intent));
        return true;
    }

    @SystemInit
    private void publish() {
        try {
            ServiceManager.addService(Context.TV_INPUT_SERVICE, new ServiceStub());
            Logger.w("HoneyCombService publish success %s", this.toString());
        } catch (Throwable e) {
            Logger.e(e, "Fail publish HoneyCombService");
        }
    }

    @SystemInit
    private void publishInternal() {
        HoneyCombServiceManager.addService(HoneyCombContext.PACKAGE_MANAGER_SERVICE, packageManager.asBinder());
        HoneyCombServiceManager.addService(HoneyCombContext.PREFERENCE_MANAGER_SERVICE, preferenceManager.asBinder());
        HoneyCombServiceManager.addService(HoneyCombContext.ACTIVITY_MANAGER_SERVICE, activityManager.asBinder());
        HoneyCombServiceManager.addService(HoneyCombContext.MODULE_MANAGER_SERVICE, moduleManagerService.asBinder());
    }

    private class ServiceStub extends IHoneyComb.Stub {
        @Override
        public String getVersion() {
            return BuildConfig.VERSION_NAME;
        }

        @Override
        public int getStatus() {
            return 0;
        }

        @Override
        public void addService(String name, IBinder binder) {
            HoneyCombServiceManager.addService(name, binder);
        }

        @Override
        public void deleteService(String name) {
            HoneyCombServiceManager.deleteService(name);
        }

        @Override
        public IBinder getService(String name) {
            return HoneyCombServiceManager.getService(name);
        }

        @Override
        public boolean hasService(String name) {
            return HoneyCombServiceManager.hasService(name);
        }

        @Override
        public void registerEventSubscriber(IntentFilter filter, IEventSubscriber subscriber) {
            EventBus.getInstance().registerEventSubscriber(filter, subscriber);
        }

        @Override
        public void unRegisterEventSubscriber(IEventSubscriber subscriber) {
            EventBus.getInstance().unRegisterEventSubscriber(subscriber);
        }
    }
}
