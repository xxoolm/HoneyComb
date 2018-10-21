package github.tornaco.practice.honeycomb.locker.server.hooks;

import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import github.tornaco.practice.honeycomb.locker.server.verify.Verifier;

/**
 * Created by guohao4 on 2017/10/31.
 * Email: Tornaco@163.com
 */

class ActivityStartHookV25 extends ActivityStartHook {

    ActivityStartHookV25(Verifier verifier) {
        super(verifier);
    }

    @Override
    Class clzForStartActivityMayWait(XC_LoadPackage.LoadPackageParam lpparam)
            throws ClassNotFoundException {
        return XposedHelpers.findClass("com.android.server.am.ActivityStarter", lpparam.classLoader);
    }
}
