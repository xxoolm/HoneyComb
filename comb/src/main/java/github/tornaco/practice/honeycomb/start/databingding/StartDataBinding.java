package github.tornaco.practice.honeycomb.start.databingding;

import android.widget.ImageView;

import java.util.List;
import java.util.Objects;

import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.RecyclerView;
import github.tornaco.practice.honeycomb.data.Bee;
import github.tornaco.practice.honeycomb.start.adapter.BeeAdapter;

public class StartDataBinding {

    @BindingAdapter("android:bees")
    public static void setBees(RecyclerView recyclerView, List<Bee> list) {
        BeeAdapter beeAdapter = (BeeAdapter) recyclerView.getAdapter();
        Objects.requireNonNull(beeAdapter).updateBee(list);
    }

    @BindingAdapter("android:beeIcon")
    public static void setBeeIcon(ImageView imageView, Bee bee) {
        imageView.setImageResource(bee.getIcon());
    }
}
