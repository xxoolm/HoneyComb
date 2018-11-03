package github.tornaco.practice.honeycomb.core.server.hooks;

import android.app.ActivityManager;
import android.app.IApplicationThread;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.util.Log;

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
import github.tornaco.practice.honeycomb.app.HoneyCombContext;
import github.tornaco.practice.honeycomb.core.server.i.HoneyComb;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class AmsCoreHook implements IXposedHookLoadPackage {

    private final HoneyComb honeyComb;

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
            hookAMSShutdown(lpparam);
            hookBroadcastIntent(lpparam);
            hookRemoveTask(lpparam);
        }
    }

    private void hookAMSStart(XC_LoadPackage.LoadPackageParam lpparam) {
        Logger.d("hookAMSStart...");
        try {
            Class ams = XposedHelpers.findClass("com.android.server.am.ActivityManagerService",
                    lpparam.classLoader);
            Set unHooks = XposedBridge.hookAllMethods(ams, "start", new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    super.beforeHookedMethod(param);
                    Context context = (Context) XposedHelpers.getObjectField(param.thisObject, "mContext");
                    honeyComb.onStart(context);
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
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    super.beforeHookedMethod(param);
                    honeyComb.systemReady();
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
                    honeyComb.shutDown();
                }
            });
            Logger.i("hookAMSShutdown OK:" + unHooks);
        } catch (Throwable e) {
            Logger.wtf("Fail hookAMSShutdown %s", e);
        }
    }

    private void hookBroadcastIntent(XC_LoadPackage.LoadPackageParam lpparam) {
        Logger.v("hookBroadcastIntent...");
        try {
            Class ams = XposedHelpers.findClass("com.android.server.am.ActivityManagerService",
                    lpparam.classLoader);
            Set unHooks = XposedBridge.hookAllMethods(ams, "broadcastIntent",
                    new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            super.beforeHookedMethod(param);
                            IApplicationThread applicationThread = (IApplicationThread) param.args[0];
                            Intent intent = (Intent) param.args[1];
                            if (intent == null) return;
                            boolean allow = honeyComb.allowBroadcastIntentSending(applicationThread, intent);
                            if (!allow) {
                                param.setResult(ActivityManager.BROADCAST_SUCCESS);
                                Logger.wtf("broadcastIntent set result to ActivityManager.BROADCAST_SUCCESS");
                            }
                        }
                    });
            Logger.d("hookBroadcastIntent OK:" + unHooks);
        } catch (Exception e) {
            Logger.e("Fail hookBroadcastIntent: " + Log.getStackTraceString(e));
        }
    }

    private void hookRemoveTask(XC_LoadPackage.LoadPackageParam lpparam) {
        Logger.v("hookRemoveTask...");
        try {
            Class ams = XposedHelpers.findClass("com.android.server.am.ActivityManagerService",
                    lpparam.classLoader);
            Set unHooks = XposedBridge.hookAllMethods(ams, "removeTask",
                    new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            super.beforeHookedMethod(param);
                            HoneyCombContext honeyCombContext = HoneyCombContext.createContext();
                            if (honeyCombContext.getActivityManager() != null
                                    && honeyCombContext.getActivityManager().isPresent()) {
                                int callingUid = Binder.getCallingUid();
                                int taskId = (int) param.args[0];
                                honeyCombContext.getActivityManager().onTaskRemoving(callingUid, taskId);
                            }

                        }
                    });
            Logger.v("hookRemoveTask OK:" + unHooks);
        } catch (Exception e) {
            Logger.e("Fail hookRemoveTask: " + Log.getStackTraceString(e));
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
