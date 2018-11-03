package github.tornaco.practice.honeycomb.locker.ui.start;

import android.app.Application;
import android.view.View;

import org.newstand.logger.Logger;

import java.util.List;
import java.util.Objects;

import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;
import androidx.databinding.ObservableList;
import androidx.lifecycle.AndroidViewModel;
import github.tornaco.honeycomb.common.util.ActivityUtils;
import github.tornaco.practice.honeycomb.locker.BuildConfig;
import github.tornaco.practice.honeycomb.locker.app.LockerContext;
import github.tornaco.practice.honeycomb.locker.app.LockerManager;
import github.tornaco.practice.honeycomb.locker.data.source.AppCategories;
import github.tornaco.practice.honeycomb.locker.data.source.AppDataSource;
import github.tornaco.practice.honeycomb.locker.data.source.AppsRepo;
import github.tornaco.practice.honeycomb.locker.ui.setup.SetupActivity;
import github.tornaco.practice.honeycomb.locker.ui.verify.VerifyActivity;
import github.tornaco.practice.honeycomb.pm.AppInfo;

public class StartViewModel extends AndroidViewModel {
    private AppsRepo appsRepo;

    public ObservableList<AppInfo> apps = new ObservableArrayList<>();
    public ObservableField<AppCategories> appCategories = new ObservableField<>(AppCategories.User);
    public ObservableBoolean isLockerEnabled = new ObservableBoolean(false);
    public ObservableBoolean isDataLoading = new ObservableBoolean(false);
    public ObservableBoolean isDataLoadingError = new ObservableBoolean(false);
    public ObservableInt columnCount = new ObservableInt(1);
    public final ObservableBoolean empty = new ObservableBoolean(false);
    public final ObservableBoolean debug = new ObservableBoolean(false && BuildConfig.DEBUG);

    public StartViewModel(Application application, AppsRepo appsRepo) {
        super(application);
        this.appsRepo = appsRepo;
    }

    public void start() {
        loadState();
        loadApps();
    }

    public void loadState() {
        isLockerEnabled.set(isLockerEnabled());
    }

    public void loadApps() {
        isDataLoading.set(true);
        appsRepo.getApps(getApplication(),
                appCategories.get() == AppCategories.User
                        ? AppInfo.FLAGS_USER
                        : AppInfo.FLAGS_SYSTEM, new AppDataSource.AppsLoadCallback() {
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

    public void onFabClick(View actionButton) {
        ActivityUtils.startActivity(getApplication(), VerifyActivity.class);
    }

    public void setAppCategories(AppCategories categories) {
        this.appCategories.set(categories);
    }

    public void setLockerEnabled(boolean enabled) {
        isLockerEnabled.set(enabled);
        LockerContext lockerContext = LockerContext.createContext();
        Objects.requireNonNull(lockerContext.getLockerManager()).setEnabled(enabled);
    }

    public void setPackageLocked(String pkg, boolean locked) {
        LockerContext lockerContext = LockerContext.createContext();
        Objects.requireNonNull(lockerContext.getLockerManager()).setPackageLocked(pkg, locked);
    }

    public boolean isCurrentLockMethodKeySet() {
        LockerContext lockerContext = LockerContext.createContext();
        LockerManager lockerManager = lockerContext.getLockerManager();
        return Objects.requireNonNull(lockerManager).isLockerKeySet(lockerManager.getLockerMethod());
    }

    public boolean isLockerServerPresent() {
        LockerContext lockerContext = LockerContext.createContext();
        LockerManager lockerManager = lockerContext.getLockerManager();
        return lockerManager != null && lockerManager.isPresent();
    }

    public void startSetupActivity() {
        ActivityUtils.startActivity(getApplication(), SetupActivity.class);
    }

    private boolean isLockerEnabled() {
        LockerContext lockerContext = LockerContext.createContext();
        return lockerContext.getLockerManager() != null && lockerContext.getLockerManager().isEnabled();
    }
}
