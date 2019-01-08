package github.tornaco.practice.honeycomb;

import android.annotation.SuppressLint;
import android.app.Application;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import github.tornaco.practice.honeycomb.data.BeeRepo;
import github.tornaco.practice.honeycomb.start.StartViewModel;

public class ViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    @SuppressLint("StaticFieldLeak")
    private static volatile ViewModelFactory INSTANCE;

    private Application application;
    private BeeRepo beeRepo;

    private ViewModelFactory(Application application, BeeRepo beeRepo) {
        this.application = application;
        this.beeRepo = beeRepo;
    }

    public static ViewModelFactory getInstance(Application application) {

        if (INSTANCE == null) {
            synchronized (ViewModelFactory.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ViewModelFactory(application, new BeeRepo());
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(StartViewModel.class)) {
            //noinspection unchecked
            return (T) new StartViewModel(application, beeRepo);
        }
        throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
    }
}
