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

import androidx.databinding.BindingAdapter;
import androidx.databinding.Observable;
import github.tornaco.honeycomb.common.widget.SwitchBar;

public class SwitchBarBindings {

    @BindingAdapter("app:switch_bar_state")
    public static void setSwitchState(SwitchBar view, final StartViewModel viewModel) {
        view.setChecked(viewModel.isLockerEnabled.get());
        viewModel.isLockerEnabled.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                view.setChecked(viewModel.isLockerEnabled.get());
            }
        });
        view.addOnSwitchChangeListener((switchView, isChecked) -> {
            viewModel.setLockerEnabled(isChecked);
        });
    }

}
