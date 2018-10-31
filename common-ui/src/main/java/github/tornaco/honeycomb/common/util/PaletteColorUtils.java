package github.tornaco.honeycomb.common.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListenerAdapter;

import androidx.palette.graphics.Palette;
import github.tornaco.android.common.util.ColorUtil;

/**
 * Created by guohao4 on 2017/10/28.
 * Email: Tornaco@163.com
 */

public abstract class PaletteColorUtils {

    public interface PickReceiver {
        void onColorReady(int color);
    }

    public static void pickPrimaryColor(Context context, final PickReceiver receiver,
                                        String pkg, final int defColor) {
        ImageLoader.getInstance().loadImage("launcher://" + pkg,
                new ImageLoadingListenerAdapter() {
                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        Palette.from(loadedImage)
                                .generate(palette -> {
                                    if (palette != null) {
                                        int main = palette.getDominantColor(defColor);
                                        // Burn 3 time to make it darker!
                                        receiver.onColorReady(
                                                ColorUtil.colorBurn(
                                                        ColorUtil.colorBurn(
                                                                ColorUtil.colorBurn(main))));
                                        receiver.onColorReady(main);
                                    }

                                });
                    }
                });
    }
}
