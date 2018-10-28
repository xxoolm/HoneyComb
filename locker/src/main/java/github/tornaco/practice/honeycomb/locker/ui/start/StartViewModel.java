package github.tornaco.practice.honeycomb.locker.ui.start;

import org.newstand.logger.Logger;

import java.util.List;

import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableList;
import androidx.lifecycle.ViewModel;
import github.tornaco.practice.honeycomb.locker.data.source.AppDataSource;
import github.tornaco.practice.honeycomb.locker.data.source.AppsRepo;
import github.tornaco.practice.honeycomb.pm.AppInfo;

public class StartViewModel extends ViewModel {
    private AppsRepo appsRepo;

    public ObservableList<AppInfo> apps = new ObservableArrayList<>();
    public ObservableBoolean isLockerEnabled = new ObservableBoolean(false);
    public ObservableBoolean isDataLoading = new ObservableBoolean(false);
    public ObservableBoolean isDataLoadingError = new ObservableBoolean(false);
    public final ObservableBoolean empty = new ObservableBoolean(false);

    public StartViewModel(AppsRepo appsRepo) {
        this.appsRepo = appsRepo;
    }

    public void start() {
        loadState();
        loadApps();
    }

    private void loadState() {
        isLockerEnabled.set(appsRepo.isLockerEnabled());
    }

    private void loadApps() {
        isDataLoading.set(true);
        appsRepo.getApps(AppInfo.FLAGS_NONE, new AppDataSource.AppsLoadCallback() {
            @Override
            public void onAppsLoaded(List<AppInfo> appInfoList) {
                Logger.i("onAppsLoaded: " + appInfoList);
                isDataLoading.set(false);
                isDataLoadingError.set(false);
                apps.clear();
                apps.addAll(appInfoList);
                empty.set(apps.isEmpty());
            }

            @Override
            public void onDataNotAvailable() {
                isDataLoading.set(false);
                isDataLoadingError.set(true);
            }
        });
    }
}
