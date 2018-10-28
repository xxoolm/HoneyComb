package github.tornaco.practice.honeycomb.core.server;

import android.content.Context;
import android.os.IBinder;
import android.os.ServiceManager;

import org.newstand.logger.Logger;

import github.tornaco.practice.honeycomb.BuildConfig;
import github.tornaco.practice.honeycomb.IHoneyComb;
import github.tornaco.practice.honeycomb.am.IActivityManager;
import github.tornaco.practice.honeycomb.annotations.SystemProcess;
import github.tornaco.practice.honeycomb.app.HoneyCombContext;
import github.tornaco.practice.honeycomb.core.server.am.ActivityManagerService;
import github.tornaco.practice.honeycomb.core.server.data.PreferenceManagerService;
import github.tornaco.practice.honeycomb.core.server.device.PowerManagerService;
import github.tornaco.practice.honeycomb.core.server.i.HoneyComb;
import github.tornaco.practice.honeycomb.core.server.pm.PackageManagerService;
import github.tornaco.practice.honeycomb.data.IPreferenceManager;
import github.tornaco.practice.honeycomb.device.IPowerManager;
import github.tornaco.practice.honeycomb.pm.IPackageManager;
import lombok.Getter;

import static github.tornaco.practice.honeycomb.core.server.util.PkgUtils.PACKAGE_NAME_ANDROID;

public class HoneyCombService implements HoneyComb {

    @Getter
    private Context systemContext;
    @Getter
    private IActivityManager activityManager;
    @Getter
    private IPowerManager powerManager;
    @Getter
    private IPackageManager packageManager;
    private IPreferenceManager preferenceManager;

    @SystemProcess
    public void onStart(Context context) {
        Logger.w("HoneyCombService start with context %s", context);
        this.systemContext = context;
        this.activityManager = new ActivityManagerService();
        this.powerManager = new PowerManagerService();
        this.packageManager = new PackageManagerService(context);
        // TODO Split for diff pkg.
        this.preferenceManager = new PreferenceManagerService(PACKAGE_NAME_ANDROID);
        publish();
        publishInternal();
    }

    @SystemProcess
    public void systemReady() {

    }

    @SystemProcess
    public void shutDown() {

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
    private void publishInternal() {
        HoneyCombServiceManager.addService(HoneyCombContext.PACKAGE_MANAGER_SERVICE, packageManager.asBinder());
        HoneyCombServiceManager.addService(HoneyCombContext.PREFERENCE_MANAGER_SERVICE, preferenceManager.asBinder());
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
    }
}
