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

import com.andrognito.patternlockview.PatternLockView;
import com.andrognito.patternlockview.listener.PatternLockViewListener;
import com.andrognito.patternlockview.utils.PatternLockUtils;

import java.util.List;

import androidx.databinding.BindingAdapter;
import androidx.databinding.Observable;
import github.tornaco.honeycomb.common.util.PaletteColorUtils;
import github.tornaco.practice.honeycomb.locker.ui.verify.VerifyViewModel;

public class PatternLockViewBindings {

    @BindingAdapter("app:pkgStateColor")
    public static void bindWithPkg(PatternLockView view, VerifyViewModel verifyViewModel) {
        PaletteColorUtils.pickPrimaryColor(view.getContext(),
                new PaletteColorUtils.PickReceiver() {
                    @Override
                    public void onColorReady(int color) {
                        view.setNormalStateColor(color);
                    }
                }, verifyViewModel.pkg, Color.TRANSPARENT);
        view.addPatternLockListener(new PatternLockViewListener() {
            @Override
            public void onStarted() {
                //Noop.
            }

            @Override
            public void onProgress(List<PatternLockView.Dot> progressPattern) {
                // Noop.
            }

            @Override
            public void onComplete(List<PatternLockView.Dot> pattern) {
                String str = PatternLockUtils.patternToString(view, pattern);
                verifyViewModel.verify(str);
            }

            @Override
            public void onCleared() {
                // Noop
            }
        });
        verifyViewModel.failCount.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                view.clearPattern();
            }
        });
        verifyViewModel.verified.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                view.clearPattern();
            }
        });
    }
}
