package github.tornaco.practice.honeycomb.locker.ui.setup;

import android.os.Bundle;

import androidx.annotation.Nullable;
import github.tornaco.honeycomb.common.ui.BaseDefaultMenuItemHandlingAppCompatActivity;
import github.tornaco.practice.honeycomb.locker.R;

public class SettingsActivity extends BaseDefaultMenuItemHandlingAppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        showHomeAsUpNavigator();
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .replace(R.id.container, SettingsFragment.newInstance())
                    .commit();
        }
    }
}
