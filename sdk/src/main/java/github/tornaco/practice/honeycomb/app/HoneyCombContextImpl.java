package github.tornaco.practice.honeycomb.app;

import android.os.ServiceManager;

import github.tornaco.practice.honeycomb.HoneyCombManager;
import github.tornaco.practice.honeycomb.IHoneyComb;
import github.tornaco.practice.honeycomb.pm.IPackageManager;
import github.tornaco.practice.honeycomb.pm.PackageManager;

class HoneyCombContextImpl implements HoneyCombContext {

    private HoneyCombManager honeyCombManager;
    private PackageManager packageManager;

    HoneyCombContextImpl() {
        IHoneyComb hb = IHoneyComb.Stub.asInterface(ServiceManager.getService(HONEY_COMB_SERVICE));
        this.honeyCombManager = new HoneyCombManager(hb);
        if (honeyCombManager.isPresent() && honeyCombManager.hasService(PACKAGE_MANAGER_SERVICE)) {
            IPackageManager pm = IPackageManager.Stub.asInterface(honeyCombManager.getService(PACKAGE_MANAGER_SERVICE));
            this.packageManager = new PackageManager(pm);
        }
    }

    @Override
    public HoneyCombManager getHoneyCombManager() {
        return honeyCombManager;
    }

    @Override
    public PackageManager getPackageManager() {
        return packageManager;
    }
}
