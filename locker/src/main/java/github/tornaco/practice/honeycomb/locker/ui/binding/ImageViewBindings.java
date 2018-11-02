package github.tornaco.practice.honeycomb.locker.ui.binding;

import android.widget.ImageView;

import androidx.databinding.BindingAdapter;
import androidx.databinding.Observable;
import github.tornaco.practice.honeycomb.locker.R;
import github.tornaco.practice.honeycomb.locker.ui.setup.SetupViewModel;

public class ImageViewBindings {

    @BindingAdapter("app:image")
    public static void bindImageView(ImageView imageView, SetupViewModel setupViewModel) {
        setupViewModel.stage.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                imageView.setImageResource(setupViewModel
                .stage.get()==SetupViewModel.SetupStage.First?
                R.drawable.ic_looks_one_white_24dp
                :R.drawable.ic_looks_two_white_24dp);
            }
        });
    }
}
