package github.tornaco.practice.honeycomb.core.server.hooks;

import org.newstand.logger.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;

import dalvik.system.PathClassLoader;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import github.tornaco.practice.honeycomb.util.ReflectionUtils;
import lombok.AllArgsConstructor;

import static de.robv.android.xposed.XposedBridge.BOOTCLASSLOADER;

@AllArgsConstructor
class HoneyCombBridge {

    /**
     * Load a module from an APK by calling the init(String) method for all classes defined
     * in <code>assets/xposed_init</code>.
     */
    static void loadModule(XC_LoadPackage.LoadPackageParam lpparam, String apk) {
        Logger.d("Loading modules from " + apk);
        if (!new File(apk).exists()) {
            Logger.d("Apk File %s does not exist", apk);
            return;
        }

        ClassLoader mcl = new PathClassLoader(apk, BOOTCLASSLOADER);
        InputStream is = mcl.getResourceAsStream("assets/comb_module_init");
        if (is == null) {
            Logger.e("assets/xposed_init not found in the APK");
            return;
        }

        BufferedReader moduleClassesReader = new BufferedReader(new InputStreamReader(is));
        try {
            String moduleClassName;
            while ((moduleClassName = moduleClassesReader.readLine()) != null) {
                moduleClassName = moduleClassName.trim();
                if (moduleClassName.isEmpty() || moduleClassName.startsWith("#")) {
                    continue;
                }
                try {
                    Logger.d("Loading class " + moduleClassName);
                    Class<?> moduleClass = mcl.loadClass(moduleClassName);
                    final Object moduleInstance = moduleClass.newInstance();
                    Logger.d("moduleInstance %s", moduleInstance);
                    Method onStart = ReflectionUtils.findMethod(moduleClass, "handleLoadPackage", XC_LoadPackage.LoadPackageParam.class);
                    ReflectionUtils.invokeMethod(onStart, moduleInstance, lpparam);
                } catch (Throwable t) {
                    Logger.e("Error loading class %s", Logger.getStackTraceString(t));
                }
            }
        } catch (IOException e) {
            Logger.e("Error loading apk %s", Logger.getStackTraceString(e));
        } finally {
            try {
                is.close();
            } catch (IOException ignored) {
            }
        }
    }
}
