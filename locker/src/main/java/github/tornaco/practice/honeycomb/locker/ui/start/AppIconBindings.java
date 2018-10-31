/*
 * Copyright 2016, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package github.tornaco.practice.honeycomb.locker.ui.start;

import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import androidx.databinding.BindingAdapter;
import github.tornaco.practice.honeycomb.pm.AppInfo;

/**
 * Contains {@link BindingAdapter}s for the {@link AppInfo} list.
 */
public class AppIconBindings {

    private final static DisplayImageOptions options = new DisplayImageOptions.Builder()
            .resetViewBeforeLoading(true)
            .cacheOnDisk(true)
            .displayer(new FadeInBitmapDisplayer(300))
            .build();

    @SuppressWarnings("unchecked")
    @BindingAdapter("app:pkg")
    public static void setPkg(ImageView imageView, String pkg) {
        ImageLoader.getInstance().displayImage("launcher://" + pkg, imageView, options);
    }
}
