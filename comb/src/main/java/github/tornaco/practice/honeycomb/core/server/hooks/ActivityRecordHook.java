package github.tornaco.practice.honeycomb.core.server.hooks;

import android.content.Intent;
import android.util.Log;

import org.newstand.logger.Logger;

import java.util.Set;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import github.tornaco.practice.honeycomb.am.ActivityManager;
import github.tornaco.practice.honeycomb.app.HoneyCombContext;
import github.tornaco.practice.honeycomb.core.server.i.HoneyComb;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static github.tornaco.practice.honeycomb.util.PkgUtils.PACKAGE_NAME_ANDROID;

/**
 * Created by guohao4 on 2017/10/31.
 * Email: Tornaco@163.com
 */

@AllArgsConstructor
public class ActivityRecordHook implements IXposedHookLoadPackage {

    @Getter
    private final HoneyComb honeyComb;

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) {
        if (PACKAGE_NAME_ANDROID.equals(lpparam.packageName)) {
            hookStartLaunchTickingLocked(lpparam);
        }
    }

    private void hookStartLaunchTickingLocked(XC_LoadPackage.LoadPackageParam lpparam) {
        Logger.v("hookStartLaunchTickingLocked...");
        try {
            Class c = XposedHelpers.findClass("com.android.server.am.ActivityRecord",
                    lpparam.classLoader);
            Set unHooks = XposedBridge.hookAllMethods(c, "startLaunchTickingLocked",
                    new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            super.beforeHookedMethod(param);
                            Object ar = param.thisObject;
                            Intent intent = (Intent) XposedHelpers.getObjectField(ar, "intent");
                            HoneyCombContext c = HoneyCombContext.createContext();
                            ActivityManager am = c.getActivityManager();
                            if (HoneyCombContext.HoneyCombConfigs.DEBUG) {
                                Logger.v("startLaunchTickingLocked %s %s", am, intent);
                            }
                            if (intent != null && am != null) {
                                am.onActivityLaunching(intent, "startLaunchTickingLocked");
                                c.recycle();
                            }
                        }
                    });
            Logger.v("hookStartLaunchTickingLocked OK:" + unHooks);
        } catch (Exception e) {
            Logger.v("Fail hookStartLaunchTickingLocked: " + Log.getStackTraceString(e));
        }
    }
}
