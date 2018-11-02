package github.tornaco.practice.honeycomb.locker.server.hooks;

import org.newstand.logger.Logger;

import de.robv.android.xposed.callbacks.XC_LoadPackage;
import github.tornaco.practice.honeycomb.locker.server.verify.Verifier;

/**
 * Created by guohao4 on 2017/10/31.
 * Email: Tornaco@163.com
 */

class TaskMoverHookEmpty extends TaskMoverHook {

    TaskMoverHookEmpty(Verifier verifier) {
        super(verifier);
    }

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        Logger.e("Empty TaskMoverHookEmpty!!!");
    }
}
