package github.tornaco.practice.honeycomb.modules;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import github.tornaco.honeycomb.common.ui.BaseDefaultMenuItemHandlingAppCompatActivity;
import github.tornaco.practice.honeycomb.ViewModelFactory;
import github.tornaco.practice.honeycomb.databinding.ActivityModulesBinding;
import github.tornaco.practice.honeycomb.modules.adapter.BeeAdapter;

public class ModuleListActivity extends BaseDefaultMenuItemHandlingAppCompatActivity {

    private ActivityModulesBinding binding;

    public static void start(Context context) {
        Intent starter = new Intent(context, ModuleListActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityModulesBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        setupRecycler();
        setupViewModel();
        showHomeAsUpNavigator();
    }

    private void setupViewModel() {
        ModulesViewModel viewModel = obtainViewModel(this);
        binding.setViewmodel(viewModel);
        binding.executePendingBindings();
        viewModel.start();
    }

    private void setupRecycler() {
        setSupportActionBar(binding.toolbar);
        RecyclerView recyclerView = binding.recycler;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        BeeAdapter adapter = new BeeAdapter();
        adapter.setViewModel(obtainViewModel(this));
        recyclerView.setAdapter(adapter);
    }

    public static ModulesViewModel obtainViewModel(FragmentActivity activity) {
        ViewModelFactory factory = ViewModelFactory.getInstance(activity.getApplication());
        return ViewModelProviders.of(activity, factory).get(ModulesViewModel.class);
    }
}
