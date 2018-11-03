package github.tornaco.practice.honeycomb.locker.server.hooks;

import android.os.Bundle;
import android.util.Log;

import org.newstand.logger.Logger;

import java.lang.reflect.Method;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import github.tornaco.practice.honeycomb.locker.app.LockerContext;
import github.tornaco.practice.honeycomb.locker.server.verify.Verifier;
import github.tornaco.practice.honeycomb.locker.server.verify.VerifyResult;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by guohao4 on 2017/10/31.
 * Email: Tornaco@163.com
 */

@AllArgsConstructor
public class LauncherAppServiceHook implements IXposedHookLoadPackage {

    @Getter
    private final Verifier verifier;

    private void hookVerifyCallingPackage(XC_LoadPackage.LoadPackageParam lpparam) {
        Logger.v("LauncherAppServiceSubModule hookVerifyCallingPackage...");
        try {
            Class clz = XposedHelpers.findClass("com.android.server.pm.LauncherAppsService$LauncherAppsImpl",
                    lpparam.classLoader);

            Method verifyMethod = null;

            for (Method m : clz.getDeclaredMethods()) {
                if ("verifyCallingPackage".equals(m.getName())) {
                    verifyMethod = m;
                }
            }

            Logger.d("verifyCallingPackage method: " + verifyMethod);

            if (verifyMethod == null) {
                return;
            }

            Object unHooks = XposedBridge.hookMethod(verifyMethod, new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(final MethodHookParam param) throws Throwable {
                    super.beforeHookedMethod(param);
                    Logger.v("LauncherAppService verifyCallingPackage: " + param.args[0]);
                    param.setResult(null);
                }
            });
            Logger.v("LauncherAppServiceSubModule hookVerifyCallingPackage OK:" + unHooks);
        } catch (Exception e) {
            Logger.v("LauncherAppServiceSubModule Fail hookVerifyCallingPackage: " + Log.getStackTraceString(e));
        }
    }

    private void hookStartShortcut(XC_LoadPackage.LoadPackageParam lpparam) {
        Logger.v("LauncherAppServiceSubModule hookStartShortcut...");
        try {
            Class clz = XposedHelpers.findClass("com.android.server.pm.LauncherAppsService$LauncherAppsImpl",
                    lpparam.classLoader);

            Method startShortcutMethod = null;

            for (Method m : clz.getDeclaredMethods()) {
                if ("startShortcut".equals(m.getName())) {
                    startShortcutMethod = m;
                }
            }

            Logger.d("startShortcut method: " + startShortcutMethod);

            if (startShortcutMethod == null) {
                return;
            }

            final Method finalStartShortcutMethod = startShortcutMethod;
            Object unHooks = XposedBridge.hookMethod(startShortcutMethod, new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(final MethodHookParam param) throws Throwable {
                    super.beforeHookedMethod(param);
                    String pkgName = (String) param.args[1];
                    Bundle op = (Bundle) param.args[4];

                    if (LockerContext.LockerConfigs.DEBUG) {
                        Logger.v("startShortcut: %s %s", pkgName, op);
                    }

                    if (pkgName == null) {
                        return;
                    }

                    if (!getVerifier().shouldVerify(null, pkgName, "startShortcut")) {
                        return;
                    }

                    getVerifier().verify(op, pkgName, null, 0, 0, (res, reason) -> {
                        if (res == VerifyResult.PASS) {
                            try {
                                XposedBridge.invokeOriginalMethod(finalStartShortcutMethod, param.thisObject, param.args);
                            } catch (Exception e) {
                                Logger.wtf("Error@" + Log.getStackTraceString(e));
                            }
                        }
                    });
                    param.setResult(true);
                }
            });
            Logger.v("LauncherAppServiceSubModule hookStartShortcut OK:" + unHooks);
        } catch (Exception e) {
            Logger.v("LauncherAppServiceSubModule Fail hookStartShortcut: " + Log.getStackTraceString(e));
        }
    }

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        if ("android".equals(lpparam.packageName)) {
            hookStartShortcut(lpparam);
            hookVerifyCallingPackage(lpparam);
        }
    }
}
