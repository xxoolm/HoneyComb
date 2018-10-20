package github.tornaco.practice.honeycomb.locker.server.hooks;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.util.Log;

import org.newstand.logger.Logger;

import java.lang.reflect.Method;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import github.tornaco.practice.honeycomb.locker.server.verify.Verifier;
import github.tornaco.practice.honeycomb.locker.server.verify.VerifyCallback;
import github.tornaco.practice.honeycomb.locker.server.verify.VerifyResult;
import github.tornaco.practice.honeycomb.locker.util.IntentUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by guohao4 on 2017/10/31.
 * Email: Tornaco@163.com
 */

@AllArgsConstructor
class ActivityStartHook implements IXposedHookLoadPackage {

    @Getter
    private Verifier verifier;

    private void hookStartActivityMayWait(XC_LoadPackage.LoadPackageParam lpparam) {
        Logger.v("hookStartActivityMayWait...");
        try {
            Class clz = clzForStartActivityMayWait(lpparam);

            // Search method.
            String targetMethodName = "startActivityMayWait";
            int matchCount = 0;
            int activityOptsIndex = -1;
            int intentIndex = -1;
            Method startActivityMayWaitMethod = null;
            if (clz != null) {
                for (Method m : clz.getDeclaredMethods()) {
                    if (m.getName().equals(targetMethodName)) {
                        startActivityMayWaitMethod = m;
                        startActivityMayWaitMethod.setAccessible(true);
                        matchCount++;

                        Class[] classes = m.getParameterTypes();
                        for (int i = 0; i < classes.length; i++) {
                            if (Bundle.class == classes[i]) {
                                activityOptsIndex = i;
                            } else if (Intent.class == classes[i]) {
                                intentIndex = i;
                            }
                        }

                        if (activityOptsIndex >= 0 && intentIndex >= 0) {
                            break;
                        }
                    }
                }
            }

            if (startActivityMayWaitMethod == null) {
                Logger.wtf("*** FATAL can not find startActivityMayWait method ***");
                return;
            }

            if (matchCount > 1) {
                Logger.wtf("*** FATAL more than 1 startActivityMayWait method ***");
                // return;
            }

            if (intentIndex < 0) {
                Logger.wtf("*** FATAL can not find intentIndex ***");
                return;
            }

            Logger.d("startActivityMayWait method:" + startActivityMayWaitMethod);
            Logger.d("intentIndex index:" + intentIndex);
            Logger.d("activityOptsIndex index:" + activityOptsIndex);

            final int finalActivityOptsIndex = activityOptsIndex;
            final int finalIntentIndex = intentIndex;

            final Method finalStartActivityMayWaitMethod = startActivityMayWaitMethod;
            XC_MethodHook.Unhook unhook = XposedBridge.hookMethod(startActivityMayWaitMethod, new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(final MethodHookParam param) throws Throwable {
                    super.beforeHookedMethod(param);
                    try {
                        Intent intent =
                                finalIntentIndex > 0 ?
                                        (Intent) param.args[finalIntentIndex]
                                        : null;
                        if (intent == null) return;

                        // Use checked Intent instead of previous one.
                        Intent checkedIntent = getVerifier().getCheckedActivityIntent(intent);
                        if (checkedIntent != null) {
                            intent = checkedIntent;
                            param.args[finalIntentIndex] = intent;
                            Binder.restoreCallingIdentity(getVerifier()
                                    .wrapCallingUidForIntent(Binder.clearCallingIdentity(), intent));
                        } else {
                            param.setResult(ActivityManager.START_SUCCESS);
                            return;
                        }

                        ComponentName componentName = intent.getComponent();
                        if (componentName == null) return;

                        // Incas the component is disabled.
                        boolean itrp = getVerifier()
                                .isActivityStartShouldBeInterrupted(componentName);
                        if (itrp) {
                            param.setResult(ActivityManager.START_SUCCESS);
                            return;
                        }

                        final String pkgName = componentName.getPackageName();

                        boolean isHomeIntent = IntentUtils.isHomeIntent(intent);
                        if (isHomeIntent) {
                            return;
                        }

                        // Package has been passed.
                        if (!getVerifier().shouldVerify(pkgName, "startActivityMayWait")) {
                            return;
                        }

                        Bundle options =
                                finalActivityOptsIndex > 0 ?
                                        (Bundle) param.args[finalActivityOptsIndex]
                                        : null;

                        Logger.w("Verifying %s", componentName);

                        getVerifier().verify(options, pkgName, componentName, 0, 0,
                                new VerifyCallback() {
                                    @Override
                                    public void onVerifyResult(int verifyResult, String reason) {
                                        if (verifyResult == VerifyResult.PASS) try {
                                            XposedBridge.invokeOriginalMethod(finalStartActivityMayWaitMethod,
                                                    param.thisObject, param.args);
                                        } catch (Exception e) {
                                            Logger.wtf("Error@" + Log.getStackTraceString(e));
                                        }
                                    }
                                });
                        param.setResult(ActivityManager.START_SUCCESS);
                    } catch (Throwable e) {
                        // replacing did not work.. but no reason to crash the VM! Log the error and go on.
                        Logger.wtf("Error@startActivityMayWaitMethod:" + Log.getStackTraceString(e));
                    }
                }
            });
            Logger.d("hookStartActivityMayWait OK: " + unhook);
        } catch (Exception e) {
            Logger.wtf("Fail hookStartActivityMayWait:" + e);
        }
    }

    Class clzForStartActivityMayWait(XC_LoadPackage.LoadPackageParam lpparam) throws ClassNotFoundException {
        throw new IllegalStateException("Need impl here");
    }

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        if ("android".equals(lpparam.packageName)) {
            hookStartActivityMayWait(lpparam);
        }
    }
}
