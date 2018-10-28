package github.tornaco.practice.honeycomb.locker.data.source;

import java.text.Collator;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import github.tornaco.practice.honeycomb.app.HoneyCombContext;
import github.tornaco.practice.honeycomb.locker.app.LockerContext;
import github.tornaco.practice.honeycomb.locker.app.LockerManager;
import github.tornaco.practice.honeycomb.locker.util.ExecutorUtils;
import github.tornaco.practice.honeycomb.pm.AppInfo;
import github.tornaco.practice.honeycomb.pm.PackageManager;

public class AppsRepo implements AppDataSource {

    @Override
    public void getApps(int flags, AppDataSource.AppsLoadCallback callback) {
        ExecutorUtils.worker().execute(() -> {
            HoneyCombContext honeyCombContext = HoneyCombContext.createContext();
            PackageManager packageManager = honeyCombContext.getPackageManager();
            if (packageManager == null || !packageManager.isPresent()) {
                callback.onDataNotAvailable();
                return;
            }
            LockerContext lockerContext = LockerContext.createContext();
            LockerManager lockerManager = lockerContext.getLockerManager();
            if (lockerManager == null) {
                callback.onDataNotAvailable();
                return;
            }
            List<AppInfo> appInfoList = packageManager.getInstalledApps(flags);
            for (AppInfo info : appInfoList) {
                info.setSelected(lockerManager.isPackageLocked(info.getPkgName()));
            }

            Collections.sort(appInfoList, (a, b) -> Collator.getInstance(Locale.CHINESE).compare(a.getAppLabel(), b.getAppLabel()));

            callback.onAppsLoaded(appInfoList);
        });
    }
}
