package github.tornaco.practice.honeycomb.core.server;

import android.content.Context;
import android.os.IBinder;
import android.os.ServiceManager;

import org.newstand.logger.Logger;

import github.tornaco.practice.honeycomb.BuildConfig;
import github.tornaco.practice.honeycomb.IHoneyComb;
import github.tornaco.practice.honeycomb.am.IActivityManager;
import github.tornaco.practice.honeycomb.core.server.am.ActivityManagerService;
import github.tornaco.practice.honeycomb.core.server.data.PreferenceManagerService;
import github.tornaco.practice.honeycomb.core.server.device.PowerManagerService;
import github.tornaco.practice.honeycomb.core.server.i.HoneyComb;
import github.tornaco.practice.honeycomb.core.server.pm.PackageManagerService;
import github.tornaco.practice.honeycomb.data.IPreferenceManager;
import github.tornaco.practice.honeycomb.device.IPowerManager;
import github.tornaco.practice.honeycomb.pm.IPackageManager;
import github.tornaco.practice.honeycomb.annotations.SystemProcess;
import lombok.Getter;

public class HoneyCombService implements HoneyComb {

    @Getter
    private Context systemContext;
    @Getter
    private IActivityManager activityManager;
    @Getter
    private IPowerManager powerManager;
    @Getter
    private IPackageManager packageManager;

    @SystemProcess
    public void onStart(Context context) {
        this.systemContext = context;
        this.activityManager = new ActivityManagerService();
        this.powerManager = new PowerManagerService();
        this.packageManager = new PackageManagerService();
        publish();
    }

    @SystemProcess
    private void publish() {
        try {
            ServiceManager.addService(Context.TV_INPUT_SERVICE, new ServiceStub());
            Logger.w("HoneyCombService publish success %s", this.toString());
        } catch (Throwable e) {
            Logger.e(e, "Fail publish HoneyCombService");
        }
    }

    @SystemProcess
    public void systemReady() {

    }

    @SystemProcess
    public void shutDown() {

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
        public IPreferenceManager getPreferenceManager(String packageName) {
            return new PreferenceManagerService(packageName);
        }

        @Override
        public IActivityManager getActivityManager() {
            return HoneyCombService.this.getActivityManager();
        }

        @Override
        public IPowerManager getPowerManager() {
            return HoneyCombService.this.getPowerManager();
        }

        @Override
        public IPackageManager getPackageManager() {
            return HoneyCombService.this.getPackageManager();
        }
    }
}
