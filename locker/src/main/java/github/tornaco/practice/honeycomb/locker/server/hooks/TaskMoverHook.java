package github.tornaco.practice.honeycomb.locker.server.hooks;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.util.Log;

import org.newstand.logger.Logger;

import java.lang.reflect.Method;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import github.tornaco.practice.honeycomb.locker.server.verify.Verifier;
import github.tornaco.practice.honeycomb.locker.server.verify.VerifyResult;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by guohao4 on 2017/10/31.
 * Email: Tornaco@163.com
 */

@AllArgsConstructor
class TaskMoverHook implements IXposedHookLoadPackage {

    @Getter
    private Verifier verifier;

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        if ("android".equals(lpparam.packageName)) {
            hookTaskMover(lpparam);
        }
    }

    private void hookTaskMover(XC_LoadPackage.LoadPackageParam lpparam) {
        Logger.wtf("hookTaskMover...");
        try {
            final Method moveToFront = methodForTaskMover(lpparam);
            XC_MethodHook.Unhook unhook = XposedBridge.hookMethod(moveToFront, new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(final MethodHookParam param) throws Throwable {
                    super.beforeHookedMethod(param);
                    try {
                        String pkgName = null;
                        ComponentName componentName = null;
                        Object realActivityObj = XposedHelpers.getObjectField(param.args[0], "realActivity");
                        if (realActivityObj == null) {
                            Logger.e("realActivityObj is null!!!");
                            return;
                        }
                        componentName = (ComponentName) realActivityObj;
                        pkgName = componentName.getPackageName();
                        Logger.v("findTaskToMoveToFrontLocked:" + pkgName);

                        // Package has been passed.
                        if (!getVerifier().shouldVerify(componentName, "findTaskToMoveToFrontLocked")) {
                            return;
                        }

                        getVerifier().verify(null, pkgName, componentName, 0, 0,
                                (verifyResult, reason) -> {
                                    if (verifyResult == VerifyResult.PASS) try {
                                        XposedBridge.invokeOriginalMethod(moveToFront,
                                                param.thisObject, param.args);
                                    } catch (Exception e) {
                                        Logger.e("Error@"
                                                + Log.getStackTraceString(e));
                                    }
                                });

                        param.setResult(null);

                    } catch (Exception e) {
                        Logger.wtf("Error@hookTaskMover- findTaskToMoveToFrontLocked:" + Log.getStackTraceString(e));
                    }
                }
            });
            Logger.d("hookTaskMover OK:" + unhook);
        } catch (Exception e) {
            Logger.d("hookTaskMover" + Log.getStackTraceString(e));
        }
    }

    @SuppressLint("PrivateApi")
    Method methodForTaskMover(XC_LoadPackage.LoadPackageParam lpparam) throws ClassNotFoundException, NoSuchMethodException {
        throw new IllegalStateException("Need impl here");
    }
}
