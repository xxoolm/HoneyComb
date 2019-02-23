package github.tornaco.practice.honeycomb.core.server.hooks;

import android.text.TextUtils;

import org.newstand.logger.Logger;

import java.util.Arrays;
import java.util.List;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import github.tornaco.practice.honeycomb.app.HoneyCombContext;
import github.tornaco.practice.honeycomb.core.server.data.PreferenceManagerService;

import static github.tornaco.practice.honeycomb.util.PkgUtils.PACKAGE_NAME_ANDROID;

public class CombModuleLoadHook implements IXposedHookLoadPackage {

    private PreferenceManagerService preferenceManagerService;

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) {
        if (PACKAGE_NAME_ANDROID.equals(lpparam.packageName)) {
            createPreferenceManager();
        }

        createPreferenceManager();

        // Android is not loaded, skip calling modules.
        if (preferenceManagerService == null) {
            Logger.d("handleLoadPackage %s, preferenceManagerService is null", lpparam.packageName);
            return;
        }
        List<String> moduleNames = preferenceManagerService.getSettingNames();
        Logger.d("handleLoadPackage %s, enabled modules %s", lpparam.packageName, Arrays.toString(moduleNames.toArray()));
        for (String pkgName : moduleNames) {
            String path = preferenceManagerService.getString(pkgName, null);
            if (!TextUtils.isEmpty(path)) {
                Logger.d("Loading comb module: %s %s", pkgName, path);
                HoneyCombBridge.loadModule(lpparam, path);
            }
        }
    }

    private void createPreferenceManager() {
        this.preferenceManagerService
                = new PreferenceManagerService(HoneyCombContext.HoneyCombConfigs.ENABLED_BEE_MODULES_PREF_NAME);
    }
}
