package github.tornaco.practice.honeycomb.start;

import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import github.tornaco.practice.honeycomb.ViewModelFactory;
import github.tornaco.practice.honeycomb.databinding.ActivityStartBinding;
import github.tornaco.practice.honeycomb.start.adapter.BeeAdapter;

public class StartActivity extends AppCompatActivity {

    private ActivityStartBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStartBinding.inflate(
                LayoutInflater.from(this), null, false);
        setContentView(binding.getRoot());
        setupView();
        setupViewModel();
    }

    private void setupViewModel() {
        StartViewModel startViewModel = obtainViewModel(this);
        binding.setViewmodel(startViewModel);
        startViewModel.start();
        binding.executePendingBindings();
    }

    private void setupView() {
        RecyclerView recyclerView = binding.recyclerActivated;
        recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(new BeeAdapter());

        recyclerView = binding.recyclerNotActivated;
        recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(new BeeAdapter());
    }

    public static StartViewModel obtainViewModel(FragmentActivity activity) {
        ViewModelFactory factory = ViewModelFactory.getInstance(activity.getApplication());
        return ViewModelProviders.of(activity, factory).get(StartViewModel.class);
    }
}
