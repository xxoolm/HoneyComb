package github.tornaco.practice.honeycomb.databingding;

import android.content.res.ColorStateList;
import android.widget.ImageView;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.Objects;

import androidx.databinding.BindingAdapter;
import androidx.databinding.ObservableBoolean;
import androidx.recyclerview.widget.RecyclerView;
import github.tornaco.practice.honeycomb.R;
import github.tornaco.practice.honeycomb.data.Bee;

public class DataBindingAdapters {

    @BindingAdapter("android:bees")
    public static void seBees(RecyclerView recyclerView, List<Bee> list) {
        BeeWireable beeAdapter = (BeeWireable) recyclerView.getAdapter();
        Objects.requireNonNull(beeAdapter).updateBee(list);
    }

    @BindingAdapter("android:beeIcon")
    public static void setBeeIcon(ImageView imageView, Bee bee) {
        int iconId = getDrawableResId(bee.getIcon());
        if (iconId > 0) {
            imageView.setImageResource(iconId);
        } else {
            imageView.setImageResource(R.drawable.ic_android_line);
        }
        int backgroundId = getDrawableResId(bee.getIconBackground());
        if (backgroundId > 0) {
            imageView.setBackgroundResource(backgroundId);
        } else {
            imageView.setBackgroundResource(R.drawable.circle_bg_blue);
        }
    }

    @BindingAdapter("android:activeStatusFabImage")
    public static void setActiveStatusFabImage(FloatingActionButton fab, ObservableBoolean isActive) {
        fab.setImageResource(isActive.get()
                ? R.drawable.ic_checkbox_circle_fill
                : R.drawable.ic_forbid_fill);
        if (!isActive.get()) {
            fab.setSupportBackgroundTintList(ColorStateList.valueOf(fab.getResources().getColor(R.color.md_red_600)));
        } else {
            fab.setSupportBackgroundTintList(ColorStateList.valueOf(fab.getResources().getColor(R.color.md_green_600)));
        }
    }

    @BindingAdapter("android:fabAlignmentMode")
    public static void setFabAlignMode(BottomAppBar bottomAppBar, ObservableBoolean isActive) {
        bottomAppBar.setFabAlignmentMode(isActive.get()
                ? BottomAppBar.FAB_ALIGNMENT_MODE_CENTER
                : BottomAppBar.FAB_ALIGNMENT_MODE_CENTER);
    }

    private static int getDrawableResId(String fieldName) {
        int id = -1;
        try {
            Class cls = R.drawable.class;
            id = (Integer) cls.getField(fieldName).get(null);
        } catch (Exception ignored) {
        }
        return id;
    }
}
