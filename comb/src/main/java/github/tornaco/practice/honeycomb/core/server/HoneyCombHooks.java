package github.tornaco.practice.honeycomb.core.server;

import org.newstand.logger.LogAdapter;
import org.newstand.logger.Logger;
import org.newstand.logger.Settings;

import androidx.annotation.Keep;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import github.tornaco.practice.honeycomb.BuildConfig;
import github.tornaco.practice.honeycomb.core.server.hooks.ActivityRecordHook;
import github.tornaco.practice.honeycomb.core.server.hooks.AmsCoreHook;
import github.tornaco.practice.honeycomb.core.server.hooks.CombModuleLoadHook;
import github.tornaco.practice.honeycomb.core.server.i.HoneyComb;

@Keep
public class HoneyCombHooks implements IXposedHookLoadPackage, IXposedHookZygoteInit {

    public HoneyCombHooks() {
        Logger.config(Settings.builder()
                .tag("HoneyComb")
                .logAdapter(new XposedBridgeLogAdapter())
                .logLevel(BuildConfig.DEBUG ? Logger.LogLevel.ALL : Logger.LogLevel.WARN)
                .build());
    }

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        HoneyComb c = new HoneyCombService();
        new AmsCoreHook(c).handleLoadPackage(lpparam);
        new ActivityRecordHook(c).handleLoadPackage(lpparam);
        new CombModuleLoadHook().handleLoadPackage(lpparam);
    }

    @Override
    public void initZygote(StartupParam startupParam) throws Throwable {

    }

    @Keep
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
            // XposedBridge.log(s + "\t" + s1);
        }

        @Override
        public void wtf(String s, String s1) {
            XposedBridge.log(s + "\t" + s1);
        }
    }
}
