package github.tornaco.practice.honeycomb.pm;

import github.tornaco.practice.honeycomb.pm.IPackageUnInstallCallback;

interface IPackageManager {
    void forceStop(String pkg);
    void unInstall(String pkg, IPackageUnInstallCallback callback);
}
