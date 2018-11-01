package github.tornaco.practice.honeycomb.locker.ui.start;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import github.tornaco.practice.honeycomb.locker.R;
import github.tornaco.practice.honeycomb.locker.databinding.StartFragmentBinding;
import github.tornaco.practice.honeycomb.locker.util.ExecutorUtils;

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
        tellUserIfKeyNotSet();
    }

    private void setupListAdapter() {
        RecyclerView recyclerView = startFragmentBinding.list;
        AppsAdapter appsAdapter = new AppsAdapter(
                new ArrayList<>(0),
                startViewModel
        );
        recyclerView.setAdapter(appsAdapter);
    }

    private void tellUserIfKeyNotSet() {
        if (!startViewModel.isCorrentLockMethodKeySet()) {
            Runnable showTip = new Runnable() {
                @Override
                public void run() {
                    Snackbar.make(startFragmentBinding.list, R.string.locker_key_not_set, Snackbar.LENGTH_INDEFINITE)
                            .setAction(R.string.locker_key_setup_now,
                                    new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            startViewModel.startSetupActivity();
                                        }
                                    })
                            .show();
                }
            };
            ExecutorUtils.uiHandler().postDelayed(showTip, 3000);
        }
    }
}
