package github.tornaco.practice.honeycomb.locker.data.source;

import android.content.Context;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import github.tornaco.practice.honeycomb.app.HoneyCombContext;
import github.tornaco.practice.honeycomb.locker.app.LockerContext;
import github.tornaco.practice.honeycomb.locker.app.LockerManager;
import github.tornaco.practice.honeycomb.locker.util.ExecutorUtils;
import github.tornaco.practice.honeycomb.locker.util.PkgUtils;
import github.tornaco.practice.honeycomb.pm.AppInfo;
import github.tornaco.practice.honeycomb.pm.PackageManager;

public class AppsRepo implements AppDataSource {

    @Override
    public void getApps(Context context, int flags, AppDataSource.AppsLoadCallback callback) {
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
            List<AppInfo> appInfoListFilter = new ArrayList<>();
            for (AppInfo info : appInfoList) {
                if (!PkgUtils.hasActivity(context, info.getPkgName())) {
                    continue;
                }
                info.setSelected(lockerManager.isPackageLocked(info.getPkgName()));
                appInfoListFilter.add(info);
            }

            Collections.sort(appInfoListFilter, (a, b) -> Collator.getInstance(Locale.CHINESE).compare(a.getAppLabel(), b.getAppLabel()));
            Collections.sort(appInfoListFilter, (a, b) -> (a.isSelected() == b.isSelected() ? 0 : a.isSelected() ? -1 : 1));

            callback.onAppsLoaded(appInfoListFilter);
        });
    }
}
