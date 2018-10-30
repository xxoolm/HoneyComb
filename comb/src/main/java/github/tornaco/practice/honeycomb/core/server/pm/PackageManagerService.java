package github.tornaco.practice.honeycomb.core.server.pm;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import java.util.ArrayList;
import java.util.List;

import github.tornaco.practice.honeycomb.pm.AppInfo;
import github.tornaco.practice.honeycomb.pm.IPackageManager;
import github.tornaco.practice.honeycomb.pm.IPackageUnInstallCallback;
import lombok.Getter;

public class PackageManagerService extends IPackageManager.Stub {
    @Getter
    private Context systemContext;

    public PackageManagerService(Context systemContext) {
        this.systemContext = systemContext;
    }

    @Override
    public void forceStop(String pkg) {

    }

    @Override
    public void unInstall(String pkg, IPackageUnInstallCallback callback) {

    }

    @Override
    public List<AppInfo> getInstalledApps(int flags) {
        List<AppInfo> res = new ArrayList<>();
        final PackageManager pm = this.getSystemContext().getPackageManager();
        List<ApplicationInfo> applicationInfos =
                android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N ?
                        pm.getInstalledApplications(android.content.pm.PackageManager.MATCH_UNINSTALLED_PACKAGES)
                        : pm.getInstalledApplications(android.content.pm.PackageManager.GET_UNINSTALLED_PACKAGES);
        for (ApplicationInfo applicationInfo : applicationInfos) {
            AppInfo appInfo = new AppInfo();
            appInfo.setPkgName(applicationInfo.packageName);
            String appLabel = String.valueOf(applicationInfo.loadLabel(pm));
            appInfo.setAppLabel(appLabel);
            appInfo.setVersionCode(applicationInfo.versionCode);
            res.add(appInfo);
        }
        return res;
    }
}