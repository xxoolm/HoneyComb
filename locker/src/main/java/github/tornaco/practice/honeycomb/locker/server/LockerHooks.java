package github.tornaco.practice.honeycomb.locker.server;

import com.google.common.collect.Lists;

import java.util.List;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import github.tornaco.practice.honeycomb.locker.server.hooks.AMSCoreHook;
import github.tornaco.practice.honeycomb.locker.server.hooks.ActivityStartHookDelegate;

public class LockerHooks implements IXposedHookLoadPackage, IXposedHookZygoteInit {

    private static final Locker LOCKER = new Locker();

    private List<IXposedHookLoadPackage> XPOSED_LOAD_PKG_HOOKS = Lists.newArrayList(
            new AMSCoreHook(LOCKER),
            new ActivityStartHookDelegate(LOCKER.getVerifier())
    );
    private List<IXposedHookZygoteInit> XPOSED_ZYGOTE_INIT_HOOKS = Lists.newArrayList(

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
