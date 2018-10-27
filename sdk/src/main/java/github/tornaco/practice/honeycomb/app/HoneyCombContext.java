package github.tornaco.practice.honeycomb.app;

import android.annotation.Nullable;

import github.tornaco.practice.honeycomb.HoneyCombManager;
import github.tornaco.practice.honeycomb.annotations.AvailableAfterSystemReady;
import github.tornaco.practice.honeycomb.pm.PackageManager;

public interface HoneyCombContext {

    String HONEY_COMB_SERVICE = android.content.Context.TV_INPUT_SERVICE;
    String PACKAGE_MANAGER_SERVICE = "package";

    HoneyCombManager getHoneyCombManager();

    @Nullable
    PackageManager getPackageManager();

    @AvailableAfterSystemReady
    static HoneyCombContext createContext() {
        return new HoneyCombContextImpl();
    }
}
