package github.tornaco.practice.honeycomb.locker.ui.setup;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.Observable;
import androidx.fragment.app.Fragment;
import github.tornaco.practice.honeycomb.locker.R;
import github.tornaco.practice.honeycomb.locker.databinding.PatternLockSetupFragmentBinding;

public class PatternLockSetupFragment extends Fragment {

    private SetupViewModel setupViewModel;
    private PatternLockSetupFragmentBinding patternLockSetupFragmentBinding;

    public static PatternLockSetupFragment newInstance() {
        return new PatternLockSetupFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        patternLockSetupFragmentBinding = PatternLockSetupFragmentBinding.inflate(inflater, container, false);
        setupViewModel = SetupActivity.obtainViewModel(Objects.requireNonNull(getActivity()));
        patternLockSetupFragmentBinding.setViewmodel(setupViewModel);
        return patternLockSetupFragmentBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupViewModel.setupComplete.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (getActivity() != null) {
                    getActivity().finish();
                }
            }
        });
        setupViewModel.start();
        setupViewModel.setupComplete.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (getActivity()!=null)Toast.makeText(getActivity().getApplicationContext(),
                        R.string.setup_complete,Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        setupViewModel.cancel();
    }
}
