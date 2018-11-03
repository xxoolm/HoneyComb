package github.tornaco.practice.honeycomb.locker.server;

import com.google.common.collect.Lists;

import java.util.List;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import github.tornaco.practice.honeycomb.locker.server.hooks.AmsCoreHook;
import github.tornaco.practice.honeycomb.locker.server.hooks.ActivityStartHookDelegate;
import github.tornaco.practice.honeycomb.locker.server.hooks.TaskMoverHookDelegate;

public class LockerHooks implements IXposedHookLoadPackage, IXposedHookZygoteInit {

    private static final Locker LOCKER = new Locker();

    private final static List<IXposedHookLoadPackage> XPOSED_LOAD_PKG_HOOKS = Lists.newArrayList(
            new AmsCoreHook(LOCKER),
            new ActivityStartHookDelegate(LOCKER.getVerifier()),
            new TaskMoverHookDelegate(LOCKER.getVerifier())
    );
    private final static List<IXposedHookZygoteInit> XPOSED_ZYGOTE_INIT_HOOKS = Lists.newArrayList(

    );

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        for (IXposedHookLoadPackage hookLoadPackage : XPOSED_LOAD_PKG_HOOKS) {
            hookLoadPackage.handleLoadPackage(lpparam);
        }
    }

    @Override
    public void initZygote(StartupParam startupParam) throws Throwable {
        for (IXposedHookZygoteInit zygoteInit : XPOSED_ZYGOTE_INIT_HOOKS) {
            zygoteInit.initZygote(startupParam);
        }
    }
}
