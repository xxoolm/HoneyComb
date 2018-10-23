package github.tornaco.practice.honeycomb.pm;

interface IPackageUnInstallCallback {
    void onPackageUnInstalled();
    void onPackageUnInstallFailed(int errorCode);
}
