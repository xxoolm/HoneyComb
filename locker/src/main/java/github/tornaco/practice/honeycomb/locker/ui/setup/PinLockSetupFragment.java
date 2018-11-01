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
import github.tornaco.practice.honeycomb.locker.databinding.PinLockVerifyFragmentBinding;

public class PinLockSetupFragment extends Fragment {

    private SetupViewModel setupViewModel;
    private PinLockVerifyFragmentBinding pinLockVerifyFragmentBinding;

    public static PinLockSetupFragment newInstance() {
        return new PinLockSetupFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        pinLockVerifyFragmentBinding = PinLockVerifyFragmentBinding.inflate(inflater, container, false);
        setupViewModel = SetupActivity.obtainViewModel(Objects.requireNonNull(getActivity()));
        pinLockVerifyFragmentBinding.setViewmodel(setupViewModel);
        setHasOptionsMenu(true);
        return pinLockVerifyFragmentBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupViewModel.verified.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                Objects.requireNonNull(getActivity()).finish();
            }
        });
        setupViewModel.start();
    }
}
