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


import android.graphics.Color;
import android.view.View;

import androidx.databinding.BindingAdapter;
import github.tornaco.honeycomb.common.util.PaletteColorUtils;
import github.tornaco.practice.honeycomb.locker.ui.verify.VerifyViewModel;

public class VerifyPatternBackgroundBindings {

    @BindingAdapter("app:patternBackground")
    public static void bindWithPkg(View view, VerifyViewModel verifyViewModel) {
        PaletteColorUtils.pickPrimaryColor(view.getContext(),
                new PaletteColorUtils.PickReceiver() {
                    @Override
                    public void onColorReady(int color) {
                        view.setBackgroundColor(color);

                    }
                }, verifyViewModel.pkg, Color.TRANSPARENT);
    }
}
