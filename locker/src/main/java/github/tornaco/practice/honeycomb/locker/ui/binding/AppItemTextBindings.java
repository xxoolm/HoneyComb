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

package github.tornaco.practice.honeycomb.locker.ui.binding;

import android.util.TypedValue;
import android.widget.TextView;

import com.andrognito.pinlockview.ResourceUtils;

import androidx.databinding.BindingAdapter;
import androidx.databinding.Observable;
import github.tornaco.practice.honeycomb.locker.R;
import github.tornaco.practice.honeycomb.locker.ui.start.StartViewModel;

public class AppItemTextBindings {

    @BindingAdapter("app:textSize")
    public static void displayAppIcon(TextView view, StartViewModel viewModel) {
        setTextSizeFromColumnCount(view, viewModel.columnCount.get());
        viewModel.columnCount.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                setTextSizeFromColumnCount(view, viewModel.columnCount.get());
            }
        });
    }

    private static void setTextSizeFromColumnCount(TextView view, int columnCount) {
        view.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                ResourceUtils.getDimensionInPx(view.getContext(),
                        columnCount > 1 ?
                                R.dimen.list_item_main_text_size_small
                                : R.dimen.list_item_main_text_size_normal));
    }
}
