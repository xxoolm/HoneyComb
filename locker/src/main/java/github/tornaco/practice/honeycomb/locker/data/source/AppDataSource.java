package github.tornaco.practice.honeycomb.locker.data.source;

import java.util.List;

import github.tornaco.practice.honeycomb.pm.AppInfo;

public interface AppDataSource {

    interface AppsLoadCallback {
        void onAppsLoaded(List<AppInfo> appInfoList);

        void onDataNotAvailable();
    }

    boolean isLockerEnabled();

    void getApps(int flags, AppsLoadCallback callback);
}
