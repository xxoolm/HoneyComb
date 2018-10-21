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
import github.tornaco.practice.honeycomb.core.server.i.HoneyComb;
import lombok.AllArgsConstructor;
import lombok.Getter;

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
        if ("android".equals(lpparam.packageName)) {
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
                            if (intent != null && getHoneyComb().getActivityManager() != null) {
                                getHoneyComb().getActivityManager().onActivityLaunching(intent, "startLaunchTickingLocked");
                            }
                        }
                    });
            Logger.v("hookStartLaunchTickingLocked OK:" + unHooks);
        } catch (Exception e) {
            Logger.v("Fail hookStartLaunchTickingLocked: " + Log.getStackTraceString(e));
        }
    }
}
