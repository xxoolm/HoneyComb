package github.tornaco.practice.honeycomb.settings;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceFragment;

import androidx.annotation.Nullable;
import github.tornaco.honeycomb.common.ui.BaseDefaultMenuItemHandlingAppCompatActivity;
import github.tornaco.practice.honeycomb.R;


public class SettingsActivity extends BaseDefaultMenuItemHandlingAppCompatActivity {

    public static void start(Context context) {
        Intent starter = new Intent(context, SettingsActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setSupportActionBar(findViewById(R.id.toolbar));
        showHomeAsUpNavigator();
        getFragmentManager().beginTransaction()
                .replace(R.id.container, new SettingsFragment())
                .commit();
    }


    public static class SettingsFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.settings);
        }
    }
}
