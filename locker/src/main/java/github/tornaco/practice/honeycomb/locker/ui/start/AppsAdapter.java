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
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import org.newstand.logger.Logger;

import java.util.List;
import java.util.Objects;

import androidx.databinding.DataBindingUtil;
import github.tornaco.practice.honeycomb.locker.databinding.AppItemBinding;
import github.tornaco.practice.honeycomb.pm.AppInfo;


public class AppsAdapter extends BaseAdapter {

    private final StartViewModel startViewModel;

    private List<AppInfo> apps;

    public AppsAdapter(List<AppInfo> tasks,
                       StartViewModel tasksViewModel) {
        startViewModel = tasksViewModel;
        setList(tasks);
    }

    public void replaceData(List<AppInfo> tasks) {
        setList(tasks);
    }

    @Override
    public int getCount() {
        return apps != null ? apps.size() : 0;
    }

    @Override
    public AppInfo getItem(int position) {
        return apps.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, final View view, final ViewGroup viewGroup) {
        AppItemBinding binding;
        if (view == null) {
            // Inflate
            LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

            // Create the binding
            binding = AppItemBinding.inflate(inflater, viewGroup, false);
        } else {
            // Recycling view
            binding = DataBindingUtil.getBinding(view);
        }

        Objects.requireNonNull(binding).setListener(new AppItemViewActionListener() {
            @Override
            public void onAppItemClick(AppInfo appInfo) {
                binding.itemSwitch.performClick();
            }

            @Override
            public void onAppItemSwitchStateChange(AppInfo appInfo, boolean checked) {
                Logger.v("onAppItemSwitchStateChange %s %s %s", appInfo, checked, appInfo == getItem(position));
                appInfo.setSelected(checked);
                startViewModel.setPackageLocked(appInfo.getPkgName(), checked);
            }
        });
        Objects.requireNonNull(binding).setApp(apps.get(position));
        binding.executePendingBindings();
        Logger.w("app %s", binding.getApp());
        return binding.getRoot();
    }


    private void setList(List<AppInfo> tasks) {
        apps = tasks;
        notifyDataSetChanged();
    }
}
