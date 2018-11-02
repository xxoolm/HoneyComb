package github.tornaco.practice.honeycomb.locker.ui.binding;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.databinding.BindingAdapter;
import github.tornaco.practice.honeycomb.locker.R;
import github.tornaco.practice.honeycomb.locker.ui.setup.SetupViewModel;

public class FabViewBindings {

    @BindingAdapter("app:fabAction")
    public static void bindFabAction(FloatingActionButton fab,
                                     SetupViewModel setupViewModel) {
        fab.setImageResource(R.drawable.ic_arrow_forward_white_24dp);
        fab.setOnClickListener(view -> {
            setupViewModel.onInputConfirm();
            fab.setImageResource(setupViewModel.stage.get()
                    == SetupViewModel.SetupStage.First ? R.drawable.ic_arrow_forward_white_24dp
                    : R.drawable.ic_check_white_24dp);
        });
    }
}
