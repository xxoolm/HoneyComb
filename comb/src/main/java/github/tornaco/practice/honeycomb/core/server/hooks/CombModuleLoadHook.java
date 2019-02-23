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

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) {
        if (PACKAGE_NAME_ANDROID.equals(lpparam.packageName)) {
            PreferenceManagerService preferenceManagerService
                    = new PreferenceManagerService(HoneyCombContext.HoneyCombConfigs.ENABLED_BEE_MODULES_PREF_NAME);
            List<String> moduleNames = preferenceManagerService.getSettingNames();
            Logger.d("enabled modules %s", Arrays.toString(moduleNames.toArray()));
            for (String pkgName : moduleNames) {
                String path = preferenceManagerService.getString(pkgName, null);
                if (!TextUtils.isEmpty(path)) {
                    Logger.d("Loading comb module: %s", path);
                    HoneyCombBridge.loadModule(lpparam, path);
                }
            }
        }
    }
}
