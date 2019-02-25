package github.tornaco.practice.honeycomb.start.databingding;

import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import java.util.List;
import java.util.Objects;

import androidx.cardview.widget.CardView;
import androidx.databinding.BindingAdapter;
import androidx.databinding.ObservableBoolean;
import androidx.recyclerview.widget.RecyclerView;
import github.tornaco.practice.honeycomb.R;
import github.tornaco.practice.honeycomb.data.Bee;
import github.tornaco.practice.honeycomb.start.StartViewModel;
import github.tornaco.practice.honeycomb.start.adapter.BeeAdapter;

public class StartDataBinding {

    private final static DisplayImageOptions OPTIONS = new DisplayImageOptions.Builder()
            .resetViewBeforeLoading(true)
            .cacheOnDisk(true)
            .displayer(new FadeInBitmapDisplayer(300))
            .build();

    @BindingAdapter("android:bees")
    public static void setBees(RecyclerView recyclerView, List<Bee> list) {
        BeeAdapter beeAdapter = (BeeAdapter) recyclerView.getAdapter();
        Objects.requireNonNull(beeAdapter).updateBee(list);
    }

    @BindingAdapter("android:beeIcon")
    public static void setBeeIcon(ImageView imageView, Bee bee) {
        ImageLoader.getInstance().displayImage("launcher://" + bee.getPkgName(), imageView, OPTIONS);
    }

    @BindingAdapter("android:statusCardBackgroundColor")
    public static void setStatusCardBackgroundColor(CardView card, ObservableBoolean isActive) {
        card.setCardBackgroundColor(isActive.get()
                ? card.getResources().getColor(R.color.md_green_600)
                : card.getResources().getColor(R.color.md_red_600));
    }

    @BindingAdapter("android:statusImage")
    public static void setStatusImage(ImageView imageView, ObservableBoolean isActive) {
        imageView.setImageResource(isActive.get()
                ? R.drawable.ic_check_circle_white_24dp
                : R.drawable.ic_info_white_24dp);
    }

    @BindingAdapter("android:statusText")
    public static void setStatusText(TextView textView, StartViewModel viewModel) {
        textView.setText(viewModel.getIsCombActivated().get()
                ? textView.getResources().getString(R.string.status_active, viewModel.getCombVersionName())
                : textView.getResources().getString(R.string.status_not_active));
        textView.setTextColor(viewModel.getIsCombActivated().get()
                ? textView.getResources().getColor(R.color.md_green_600)
                : textView.getResources().getColor(R.color.md_red_600));
    }
}
