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

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.List;

import androidx.databinding.BindingAdapter;
import androidx.databinding.Observable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import github.tornaco.practice.honeycomb.locker.R;
import github.tornaco.practice.honeycomb.locker.data.source.AppCategories;
import github.tornaco.practice.honeycomb.locker.ui.start.AppsAdapter;
import github.tornaco.practice.honeycomb.locker.ui.start.StartViewModel;
import github.tornaco.practice.honeycomb.pm.AppInfo;

/**
 * Contains {@link BindingAdapter}s for the {@link AppInfo} list.
 */
public class AppsListBindings {

    @SuppressWarnings("unchecked")
    @BindingAdapter("app:items")
    public static void setItems(RecyclerView recyclerView, List<AppInfo> items) {
        AppsAdapter adapter = (AppsAdapter) recyclerView.getAdapter();
        if (adapter != null) {
            adapter.replaceData(items);
        }
    }

    @BindingAdapter("app:layoutView")
    public static void setLayoutManagerAndView(RecyclerView recyclerView, StartViewModel viewModel) {
        recyclerView.setLayoutManager(new GridLayoutManager(recyclerView.getContext(),
                viewModel.columnCount.get()));
        viewModel.columnCount.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                recyclerView.setLayoutManager(new GridLayoutManager(recyclerView.getContext(),
                        viewModel.columnCount.get()));
            }
        });
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(),
                DividerItemDecoration.VERTICAL));
    }

    @BindingAdapter("app:spinnerItems")
    public static void setSpinnerItems(Spinner spinner, StartViewModel startViewModel) {
        String[] categories = spinner.getContext().getResources().getStringArray(R.array.app_list_filter_categories);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(spinner.getContext(),
                android.R.layout.simple_spinner_dropdown_item, categories);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                AppCategories c = AppCategories.belong(i);
                startViewModel.setAppCategories(c);
                startViewModel.loadApps();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // Noop.
            }
        });
    }
}