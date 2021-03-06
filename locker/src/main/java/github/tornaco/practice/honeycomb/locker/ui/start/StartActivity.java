package github.tornaco.practice.honeycomb.locker.ui.start;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import github.tornaco.honeycomb.common.ui.BaseDefaultMenuItemHandlingAppCompatActivity;
import github.tornaco.practice.honeycomb.locker.R;
import github.tornaco.practice.honeycomb.locker.ViewModelFactory;
import github.tornaco.practice.honeycomb.locker.ui.start.StartFragment;
import github.tornaco.practice.honeycomb.locker.ui.start.StartViewModel;

public class StartActivity extends BaseDefaultMenuItemHandlingAppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, StartFragment.newInstance())
                    .commitNow();
        }
        showHomeAsUpNavigator();
    }

    public static StartViewModel obtainViewModel(FragmentActivity activity) {
        // Use a Factory to inject dependencies into the ViewModel
        ViewModelFactory factory = ViewModelFactory.getInstance(activity.getApplication());
        return ViewModelProviders.of(activity, factory).get(StartViewModel.class);
    }
}
