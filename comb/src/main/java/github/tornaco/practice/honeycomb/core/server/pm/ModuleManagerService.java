package github.tornaco.practice.honeycomb.core.server.pm;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.RemoteException;

import org.newstand.logger.Logger;

import github.tornaco.practice.honeycomb.app.HoneyCombContext;
import github.tornaco.practice.honeycomb.core.server.data.PreferenceManagerService;
import github.tornaco.practice.honeycomb.core.server.i.SystemService;
import github.tornaco.practice.honeycomb.pm.IModuleManager;

public class ModuleManagerService extends IModuleManager.Stub implements SystemService {

    private Context context;
    private PreferenceManagerService preferenceManagerService;

    @Override
    public void onStart(Context context) {
        this.context = context;
        this.preferenceManagerService = new PreferenceManagerService(HoneyCombContext.HoneyCombConfigs.ENABLED_BEE_MODULES_PREF_NAME);
    }

    @Override
    public void onSystemReady() {

    }

    @Override
    public void onShutDown() {

    }

    @Override
    public boolean isModuleActivated(String pkgName) throws RemoteException {
        return this.preferenceManagerService.getString(pkgName, null) != null;
    }

    @Override
    public void setModuleActive(String pkgName) throws RemoteException {
        enforceCallingPermissions();
        PackageManager pm = context.getPackageManager();
        try {
            ApplicationInfo applicationInfo = pm.getApplicationInfo(pkgName, PackageManager.GET_META_DATA);
            preferenceManagerService.putString(pkgName, applicationInfo.publicSourceDir);
        } catch (PackageManager.NameNotFoundException e) {
            Logger.e("setModuleActive NameNotFoundException %s", e);
        }
    }

    private void enforceCallingPermissions() {
        // TODO: 2019/2/24 Check perm.
    }
}
