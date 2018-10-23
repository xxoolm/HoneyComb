package github.tornaco.practice.honeycomb.core.server;

import com.google.common.collect.Lists;

import java.util.List;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import github.tornaco.practice.honeycomb.core.server.hooks.AMSCoreHook;
import github.tornaco.practice.honeycomb.core.server.hooks.ActivityRecordHook;
import github.tornaco.practice.honeycomb.core.server.i.HoneyComb;

public class HoneyCombHooks implements IXposedHookLoadPackage, IXposedHookZygoteInit {

    private static final HoneyComb COMB = new HoneyCombService();

    private List<IXposedHookLoadPackage> XPOSED_LOAD_PKG_HOOKS = Lists.newArrayList(
            new AMSCoreHook(COMB),
            new ActivityRecordHook(COMB)
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
