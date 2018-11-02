package github.tornaco.practice.honeycomb.locker.server.hooks;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;

import java.lang.reflect.Method;

import de.robv.android.xposed.callbacks.XC_LoadPackage;
import github.tornaco.practice.honeycomb.locker.server.verify.Verifier;

/**
 * Created by guohao4 on 2017/10/31.
 * Email: Tornaco@163.com
 */

class TaskMoverHookV24 extends TaskMoverHook {
    TaskMoverHookV24(Verifier verifier) {
        super(verifier);
    }

    @SuppressLint("PrivateApi")
    @Override
    Method methodForTaskMover(XC_LoadPackage.LoadPackageParam lpparam) throws ClassNotFoundException, NoSuchMethodException {
        @SuppressLint("PrivateApi")
        Class taskRecordClass = Class.forName("com.android.server.am.TaskRecord", false, lpparam.classLoader);
        @SuppressLint("PrivateApi") final Method moveToFront
                = Class.forName("com.android.server.am.ActivityStackSupervisor",
                false, lpparam.classLoader)
                .getDeclaredMethod("findTaskToMoveToFrontLocked",
                        taskRecordClass, int.class, ActivityOptions.class,
                        String.class, boolean.class);
        return moveToFront;
    }
}
