package github.tornaco.practice.honeycomb.pm;

import github.tornaco.practice.honeycomb.pm.IPackageUnInstallCallback;
import github.tornaco.practice.honeycomb.pm.AppInfo;

interface IPackageManager {
    void forceStop(String pkg);
    void unInstall(String pkg, IPackageUnInstallCallback callback);

    List<AppInfo> getInstalledApps(int flags);
}
