package github.tornaco.practice.honeycomb.locker;

import android.annotation.SuppressLint;
import android.app.Application;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import github.tornaco.practice.honeycomb.locker.data.source.AppsRepo;
import github.tornaco.practice.honeycomb.locker.ui.start.StartViewModel;
import github.tornaco.practice.honeycomb.locker.ui.verify.VerifyViewModel;

public class ViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    @SuppressLint("StaticFieldLeak")
    private static volatile ViewModelFactory INSTANCE;

    private Application application;
    private AppsRepo appsRepo;

    private ViewModelFactory(Application application, AppsRepo appsRepo) {
        this.application = application;
        this.appsRepo = appsRepo;
    }

    public static ViewModelFactory getInstance(Application application) {

        if (INSTANCE == null) {
            synchronized (ViewModelFactory.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ViewModelFactory(application, new AppsRepo());
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(StartViewModel.class)) {
            //noinspection unchecked
            return (T) new StartViewModel(application, appsRepo);
        }
        if (modelClass.isAssignableFrom(VerifyViewModel.class)) {
            //noinspection unchecked
            return (T) new VerifyViewModel(application);
        }
        throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
    }
}
