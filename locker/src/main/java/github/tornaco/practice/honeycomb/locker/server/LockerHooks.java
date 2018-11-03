package github.tornaco.practice.honeycomb.locker.server;

import org.newstand.logger.LogAdapter;
import org.newstand.logger.Logger;
import org.newstand.logger.Settings;

import androidx.annotation.Keep;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import github.tornaco.practice.honeycomb.locker.BuildConfig;
import github.tornaco.practice.honeycomb.locker.server.hooks.ActivityStartHookDelegate;
import github.tornaco.practice.honeycomb.locker.server.hooks.AmsCoreHook;
import github.tornaco.practice.honeycomb.locker.server.hooks.LauncherAppServiceHook;
import github.tornaco.practice.honeycomb.locker.server.hooks.TaskMoverHookDelegate;
import github.tornaco.practice.honeycomb.util.OSUtils;

@Keep
public class LockerHooks implements IXposedHookLoadPackage, IXposedHookZygoteInit {

    public LockerHooks() {
        Logger.config(Settings.builder()
                .tag("Locker")
                .logAdapter(new XposedBridgeLogAdapter())
                .logLevel(BuildConfig.DEBUG ? Logger.LogLevel.ALL : Logger.LogLevel.WARN)
                .build());
    }

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {

        Locker c = new Locker();
        new AmsCoreHook(c).handleLoadPackage(lpparam);
        new ActivityStartHookDelegate(c.getVerifier()).handleLoadPackage(lpparam);
        new TaskMoverHookDelegate(c.getVerifier()).handleLoadPackage(lpparam);
        if (OSUtils.isNOrAbove()) {
            new LauncherAppServiceHook(c.getVerifier()).handleLoadPackage(lpparam);
        }
    }

    @Override
    public void initZygote(StartupParam startupParam) throws Throwable {

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
