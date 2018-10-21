package github.tornaco.practice.honeycomb.core.server.i;

import android.content.Context;

import androidx.annotation.Nullable;
import github.tornaco.practice.honeycomb.am.IActivityManager;
import github.tornaco.practice.honeycomb.device.IPowerManager;
import github.tornaco.practice.honeycomb.pm.IPackageManager;

public interface HoneyComb {
    void onStart(Context context);

    void systemReady();

    void shutDown();

    @Nullable
    IActivityManager getActivityManager();

    @Nullable
    IPowerManager getPowerManager();

    @Nullable
    IPackageManager getPackageManager();
}
