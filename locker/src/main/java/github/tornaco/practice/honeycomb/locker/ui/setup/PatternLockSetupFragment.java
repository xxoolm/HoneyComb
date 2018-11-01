package github.tornaco.practice.honeycomb.locker.ui.setup;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.Observable;
import androidx.fragment.app.Fragment;
import github.tornaco.practice.honeycomb.locker.databinding.PatternLockVerifyFragmentBinding;

public class PatternLockSetupFragment extends Fragment {

    private SetupViewModel setupViewModel;
    private PatternLockVerifyFragmentBinding patternLockVerifyFragmentBinding;

    public static PatternLockSetupFragment newInstance() {
        return new PatternLockSetupFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        patternLockVerifyFragmentBinding = PatternLockVerifyFragmentBinding.inflate(inflater, container, false);
        setupViewModel = SetupActivity.obtainViewModel(Objects.requireNonNull(getActivity()));
        patternLockVerifyFragmentBinding.setViewmodel(setupViewModel);
        setHasOptionsMenu(true);
        return patternLockVerifyFragmentBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupViewModel.verified.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (getActivity() != null) {
                    getActivity().finish();
                }
            }
        });
        setupViewModel.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        setupViewModel.cancel();
    }
}
