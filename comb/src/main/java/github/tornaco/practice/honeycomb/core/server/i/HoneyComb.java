package github.tornaco.practice.honeycomb.core.server.i;

import android.app.IApplicationThread;
import android.content.Context;
import android.content.Intent;

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

    // Check if this broadcast is allowed to be sent.
    boolean allowBroadcastIntentSending(IApplicationThread applicationThread, Intent intent);
}
