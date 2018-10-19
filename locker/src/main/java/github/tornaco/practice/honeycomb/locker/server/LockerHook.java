package github.tornaco.practice.honeycomb.locker.server;

import android.content.Context;

import org.newstand.logger.Logger;

import java.util.Set;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class LockerHook implements IXposedHookLoadPackage {

    private Locker locker;

    public LockerHook(Locker locker) {
        this.locker = locker;
    }

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        hookAMSStart(lpparam);
        hookAMSSystemReady(lpparam);
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
                    locker.onStart(context);
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
                    locker.systemReady();
                }
            });
            Logger.i("hookAMSSystemReady OK:" + unHooks);
        } catch (Throwable e) {
            Logger.wtf("Fail hookAMSSystemReady %s", e);
        }
    }
}
