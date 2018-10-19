package github.tornaco.practice.honeycomb.core.server;

import android.content.Context;

import org.newstand.logger.Logger;

import java.util.Set;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import github.tornaco.practice.honeycomb.core.server.HoneyCombService;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class HoneyCombHook implements IXposedHookLoadPackage {

    @Getter
    private HoneyCombService service;

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        if ("android".equals(lpparam.packageName)) {
            hookAMSStart(lpparam);
            hookAMSSystemReady(lpparam);
        }
    }

    private void hookAMSStart(XC_LoadPackage.LoadPackageParam lpparam) {
        Logger.d("hookAMSStart...");
        try {
            Class ams = XposedHelpers.findClass("com.android.server.am.ActivityManagerService",
                    lpparam.classLoader);
            Set unHooks = XposedBridge.hookAllMethods(ams, "start", new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);
                    Context context = (Context) XposedHelpers.getObjectField(param.thisObject, "mContext");
                    service.onStart(context);
                }
            });
            Logger.i("hookAMSStart OK:" + unHooks);
        } catch (Throwable e) {
            Logger.wtf("Fail hook hookAMSStart %s", e);
        }
    }

    private void hookAMSSystemReady(XC_LoadPackage.LoadPackageParam lpparam) {
        Logger.d("hookAMSSystemReady...");
        try {
            Class ams = XposedHelpers.findClass("com.android.server.am.ActivityManagerService",
                    lpparam.classLoader);
            Set unHooks = XposedBridge.hookAllMethods(ams, "systemReady", new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);
                    service.systemReady();
                }
            });
            Logger.i("hookAMSSystemReady OK:" + unHooks);
        } catch (Throwable e) {
            Logger.wtf("Fail hookAMSSystemReady %s", e);
        }
    }

    private void hookAMSShutdown(XC_LoadPackage.LoadPackageParam lpparam) {
        Logger.d("hookAMSShutdown...");
        try {
            Class ams = XposedHelpers.findClass("com.android.server.am.ActivityManagerService",
                    lpparam.classLoader);
            Set unHooks = XposedBridge.hookAllMethods(ams, "shutdown", new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);
                    service.shutDown();
                }
            });
            Logger.i("hookAMSShutdown OK:" + unHooks);
        } catch (Throwable e) {
            Logger.wtf("Fail hookAMSShutdown %s", e);
        }
    }
}
