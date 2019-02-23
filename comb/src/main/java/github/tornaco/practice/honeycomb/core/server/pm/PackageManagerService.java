package github.tornaco.practice.honeycomb.core.server.pm;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import org.newstand.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import github.tornaco.practice.honeycomb.core.server.event.EventBus;
import github.tornaco.practice.honeycomb.core.server.i.SystemService;
import github.tornaco.practice.honeycomb.pm.AppInfo;
import github.tornaco.practice.honeycomb.pm.IPackageManager;
import github.tornaco.practice.honeycomb.pm.IPackageUnInstallCallback;
import lombok.Getter;

public class PackageManagerService extends IPackageManager.Stub implements SystemService {
    @Getter
    private Context systemContext;

    @Override
    public void onStart(Context context) {
        this.systemContext = context;
        Logger.d("Package manager start");
    }

    @Override
    public void onSystemReady() {
        registerReceivers();
    }

    @Override
    public void onShutDown() {

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
        boolean user = (flags & AppInfo.FLAGS_USER) != 0;
        if (user) {
            res.addAll(getUserApps());
        }
        boolean system = (flags & AppInfo.FLAGS_SYSTEM) != 0;
        if (system) {
            res.addAll(getSystemApps());
        }
        return res;
    }

    private List<AppInfo> getUserApps() {
        List<AppInfo> res = new ArrayList<>();
        final PackageManager pm = this.getSystemContext().getPackageManager();
        List<ApplicationInfo> applicationInfos =
                android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N ?
                        pm.getInstalledApplications(android.content.pm.PackageManager.MATCH_UNINSTALLED_PACKAGES)
                        : pm.getInstalledApplications(android.content.pm.PackageManager.GET_UNINSTALLED_PACKAGES);
        for (ApplicationInfo applicationInfo : applicationInfos) {
            if ((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
                continue;
            }
            AppInfo appInfo = new AppInfo();
            appInfo.setPkgName(applicationInfo.packageName);
            String appLabel = String.valueOf(applicationInfo.loadLabel(pm));
            appInfo.setAppLabel(appLabel);
            appInfo.setVersionCode(applicationInfo.versionCode);
            appInfo.setFlags(AppInfo.FLAGS_USER);
            res.add(appInfo);
        }
        return res;
    }

    private List<AppInfo> getSystemApps() {
        List<AppInfo> res = new ArrayList<>();
        final PackageManager pm = this.getSystemContext().getPackageManager();
        List<ApplicationInfo> applicationInfos =
                android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N ?
                        pm.getInstalledApplications(android.content.pm.PackageManager.MATCH_UNINSTALLED_PACKAGES)
                        : pm.getInstalledApplications(android.content.pm.PackageManager.GET_UNINSTALLED_PACKAGES);
        for (ApplicationInfo applicationInfo : applicationInfos) {
            if ((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                continue;
            }
            AppInfo appInfo = new AppInfo();
            appInfo.setPkgName(applicationInfo.packageName);
            String appLabel = String.valueOf(applicationInfo.loadLabel(pm));
            appInfo.setAppLabel(appLabel);
            appInfo.setVersionCode(applicationInfo.versionCode);
            appInfo.setFlags(AppInfo.FLAGS_SYSTEM);
            res.add(appInfo);
        }
        return res;
    }

    private void registerReceivers() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_PACKAGE_ADDED);
        intentFilter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        intentFilter.addAction(Intent.ACTION_PACKAGE_CHANGED);
        intentFilter.addDataScheme("package");
        EventBus.getInstance().registerEventSubscriber(intentFilter, new PackageChangeListener());
    }
}
