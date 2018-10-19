package github.tornaco.practice.honeycomb.core.server;

import android.content.Context;

import org.newstand.logger.LogAdapter;
import org.newstand.logger.Logger;
import org.newstand.logger.Settings;

import java.util.Set;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import github.tornaco.practice.honeycomb.BuildConfig;

public class HoneyCombHook implements IXposedHookLoadPackage {

    private static final HoneyCombService HONEY_COMB_SERVICE = new HoneyCombService();

    static {
        Logger.config(Settings.builder()
                .tag("HoneyComb")
                .logAdapter(new XposedBridgeLogAdapter())
                .logLevel(BuildConfig.DEBUG ? Logger.LogLevel.ALL : Logger.LogLevel.WARN)
                .build());
    }

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
                    HONEY_COMB_SERVICE.onStart(context);
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
                    HONEY_COMB_SERVICE.systemReady();
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
                    HONEY_COMB_SERVICE.shutDown();
                }
            });
            Logger.i("hookAMSShutdown OK:" + unHooks);
        } catch (Throwable e) {
            Logger.wtf("Fail hookAMSShutdown %s", e);
        }
    }

    private static class XposedBridgeLogAdapter implements LogAdapter {
        @Override
        public void d(String s, String s1) {
            XposedBridge.log(s + "\t" + s1);
        }

        @Override
        public void e(String s, String s1) {
            XposedBridge.log(s + "\t" + s1);
        }

        @Override
        public void w(String s, String s1) {
            XposedBridge.log(s + "\t" + s1);
        }

        @Override
        public void i(String s, String s1) {
            XposedBridge.log(s + "\t" + s1);
        }

        @Override
        public void v(String s, String s1) {
            XposedBridge.log(s + "\t" + s1);
        }

        @Override
        public void wtf(String s, String s1) {
            XposedBridge.log(s + "\t" + s1);
        }
    }
}
