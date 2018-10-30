package github.tornaco.practice.honeycomb.locker.ui.start;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import github.tornaco.practice.honeycomb.locker.databinding.StartFragmentBinding;
import github.tornaco.practice.honeycomb.pm.AppInfo;

public class StartFragment extends Fragment {

    private StartViewModel startViewModel;
    private StartFragmentBinding startFragmentBinding;

    public static StartFragment newInstance() {
        return new StartFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        startFragmentBinding = StartFragmentBinding.inflate(inflater, container, false);
        startViewModel = StartActivity.obtainViewModel(Objects.requireNonNull(getActivity()));
        startFragmentBinding.setViewmodel(startViewModel);
        setHasOptionsMenu(true);
        return startFragmentBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupListAdapter();
    }

    @Override
    public void onResume() {
        super.onResume();
        startViewModel.start();
    }

    private void setupListAdapter() {
        ListView listView = startFragmentBinding.list;
        AppsAdapter appsAdapter = new AppsAdapter(
                new ArrayList<AppInfo>(0),
                startViewModel
        );
        listView.setAdapter(appsAdapter);
    }
}
