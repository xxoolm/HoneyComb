package github.tornaco.practice.honeycomb.locker.data.source;

import github.tornaco.practice.honeycomb.app.HoneyCombContext;
import github.tornaco.practice.honeycomb.locker.util.ExecutorUtils;
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
            callback.onAppsLoaded(packageManager.getInstalledApps(flags));
        });
    }
}
