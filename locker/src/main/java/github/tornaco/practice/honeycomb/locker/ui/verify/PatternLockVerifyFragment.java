package github.tornaco.practice.honeycomb.locker.ui.verify;

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

public class PatternLockVerifyFragment extends Fragment {

    private VerifyViewModel verifyViewModel;
    private PatternLockVerifyFragmentBinding patternLockVerifyFragmentBinding;

    public static PatternLockVerifyFragment newInstance() {
        return new PatternLockVerifyFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        patternLockVerifyFragmentBinding = PatternLockVerifyFragmentBinding.inflate(inflater, container, false);
        verifyViewModel = VerifyActivity.obtainViewModel(Objects.requireNonNull(getActivity()));
        patternLockVerifyFragmentBinding.setViewmodel(verifyViewModel);
        setHasOptionsMenu(true);
        return patternLockVerifyFragmentBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        verifyViewModel.verified.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                Objects.requireNonNull(getActivity()).finish();
            }
        });
        verifyViewModel.verify();
    }
}
