package github.tornaco.practice.honeycomb.start;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import github.tornaco.practice.honeycomb.R;
import github.tornaco.practice.honeycomb.ViewModelFactory;
import github.tornaco.practice.honeycomb.databinding.ActivityStartBinding;
import github.tornaco.practice.honeycomb.modules.ModuleListActivity;
import github.tornaco.practice.honeycomb.start.adapter.BeeAdapter;

public class StartActivity extends AppCompatActivity {

    private ActivityStartBinding binding;
    private StartViewModel startViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStartBinding.inflate(
                LayoutInflater.from(this), null, false);
        setContentView(binding.getRoot());
        setupView();
        setupViewModel();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.starts, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            case R.id.action_modules:
                ModuleListActivity.start(this);
                return true;
            default:
                return false;
        }
    }

    private void setupViewModel() {
        startViewModel = obtainViewModel(this);
        binding.setViewmodel(startViewModel);
        startViewModel.start();
        binding.setFabClickListener(v -> showCombActivateInfo());
        binding.executePendingBindings();
    }

    private void setupView() {
        setSupportActionBar(binding.bottomAppBar);
        RecyclerView recyclerView = binding.recyclerActivated;
        recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(new BeeAdapter());
    }

    private void showCombActivateInfo() {
        Snackbar.make(binding.bottomAppBar,
                startViewModel.getIsCombActivated().get()
                        ? getString(R.string.status_active, startViewModel.getCombVersionName())
                        : getString(R.string.status_not_active),
                Snackbar.LENGTH_LONG).show();
    }

    public static StartViewModel obtainViewModel(FragmentActivity activity) {
        ViewModelFactory factory = ViewModelFactory.getInstance(activity.getApplication());
        return ViewModelProviders.of(activity, factory).get(StartViewModel.class);
    }
}
