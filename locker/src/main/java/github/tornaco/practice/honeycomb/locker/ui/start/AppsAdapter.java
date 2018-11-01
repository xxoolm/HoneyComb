/*
 *  Copyright 2017 Google Inc.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package github.tornaco.practice.honeycomb.locker.ui.start;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import github.tornaco.practice.honeycomb.locker.databinding.AppItemBinding;
import github.tornaco.practice.honeycomb.pm.AppInfo;
import lombok.Getter;


public class AppsAdapter extends RecyclerView.Adapter<AppsAdapter.AppItemViewHolder> {

    private final StartViewModel startViewModel;

    private List<AppInfo> apps;

    AppsAdapter(List<AppInfo> tasks,
                StartViewModel tasksViewModel) {
        startViewModel = tasksViewModel;
        setList(tasks);
    }

    public void replaceData(List<AppInfo> tasks) {
        setList(tasks);
    }

    @NonNull
    @Override
    public AppItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AppItemViewHolder(AppItemBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AppItemViewHolder holder, int position) {
        AppItemBinding binding = holder.getBinding();
        Objects.requireNonNull(binding).setListener(new AppItemViewActionListener() {
            @Override
            public void onAppItemClick(AppInfo appInfo) {
                binding.itemSwitch.performClick();
            }

            @Override
            public void onAppItemSwitchStateChange(AppInfo appInfo, boolean checked) {
                appInfo.setSelected(checked);
                startViewModel.setPackageLocked(appInfo.getPkgName(), checked);
            }
        });
        Objects.requireNonNull(binding).setApp(apps.get(position));
        binding.setViewmodel(startViewModel);
        binding.executePendingBindings();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return apps != null ? apps.size() : 0;
    }

    private void setList(List<AppInfo> tasks) {
        apps = tasks;
        notifyDataSetChanged();
    }

    @Getter
    public class AppItemViewHolder extends RecyclerView.ViewHolder {
        private final AppItemBinding binding;

        AppItemViewHolder(AppItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Object obj) {
            binding.executePendingBindings();
        }
    }
}
