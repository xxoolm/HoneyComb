package github.tornaco.practice.honeycomb.locker.ui.start;

import androidx.databinding.ObservableBoolean;
import androidx.lifecycle.ViewModel;

public class StartViewModel extends ViewModel {

    private ObservableBoolean lockerEnabled = new ObservableBoolean(false);
    private ObservableBoolean dataLoading = new ObservableBoolean(false);

    public void start() {
        loadApps();
    }

    private void loadApps() {
        dataLoading.set(true);
    }
}
