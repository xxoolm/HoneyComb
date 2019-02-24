package github.tornaco.practice.honeycomb.start.databingding;

import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import java.util.List;
import java.util.Objects;

import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.RecyclerView;
import github.tornaco.practice.honeycomb.data.Bee;
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
}
