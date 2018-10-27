package github.tornaco.practice.honeycomb.core.server.pm;

import com.google.common.collect.Lists;

import java.util.List;

import github.tornaco.practice.honeycomb.pm.AppInfo;
import github.tornaco.practice.honeycomb.pm.IPackageManager;
import github.tornaco.practice.honeycomb.pm.IPackageUnInstallCallback;

public class PackageManagerService extends IPackageManager.Stub {

    @Override
    public void forceStop(String pkg) {

    }

    @Override
    public void unInstall(String pkg, IPackageUnInstallCallback callback) {

    }

    @Override
    public List<AppInfo> getInstalledApps(int flags) {
        AppInfo appInfo = new AppInfo();
        appInfo.setAppLabel("Dummy");
        return Lists.newArrayList(appInfo);
    }
}
