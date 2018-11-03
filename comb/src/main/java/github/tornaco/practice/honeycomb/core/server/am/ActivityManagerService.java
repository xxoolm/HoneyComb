package github.tornaco.practice.honeycomb.core.server.am;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;

import org.newstand.logger.Logger;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import androidx.annotation.Nullable;
import github.tornaco.practice.honeycomb.am.IActivityManager;
import github.tornaco.practice.honeycomb.annotations.AppReporting;
import github.tornaco.practice.honeycomb.core.server.util.PkgUtils;
import lombok.Getter;

public class ActivityManagerService extends IActivityManager.Stub {

    @Getter
    private Context systemContext;

    private AtomicReference<ComponentName> frontUIAppComponentName
            = new AtomicReference<>(null);

    public ActivityManagerService(Context systemContext) {
        this.systemContext = systemContext;
    }

    @Override
    @AppReporting
    public void onActivityLaunching(Intent intent, String reason) {
        Logger.i("onActivityLaunching %s %s", reason, intent);
        if (intent != null) {
            ComponentName name = intent.getComponent();
            frontUIAppComponentName.set(name);
        }
    }

    @Override
    public void onTaskRemoving(int callingUid, int taskId) {
        String pkgName = getPackageNameForTaskIdInternal(taskId);
        Logger.i("onTaskRemoving %s %s %s", callingUid, callingUid, pkgName);
    }

    @Override
    public String getFrontUIAppPackageName() {
        return frontUIAppComponentName.get() == null
                ? null
                : frontUIAppComponentName.get().getPackageName();
    }

    @Override
    public boolean isAppUIInFront(String pkg) {
        return pkg != null && pkg.equals(getFrontUIAppPackageName());
    }

    @SuppressWarnings("deprecation")
    @Nullable
    private String getPackageNameForTaskIdInternal(int taskId) {
        long ident = Binder.clearCallingIdentity();
        try {
            ActivityManager am = (ActivityManager) getSystemContext().getSystemService(Context.ACTIVITY_SERVICE);
            if (am != null) {
                List<ActivityManager.RecentTaskInfo> tasks = am.getRecentTasks(99,
                        ActivityManager.RECENT_WITH_EXCLUDED);
                if (tasks != null) {
                    for (ActivityManager.RecentTaskInfo rc : tasks) {
                        if (rc != null && rc.persistentId == taskId) {
                            return PkgUtils.getPackageNameFromIntent(rc.baseIntent);
                        }
                    }
                }
            }
            return null;
        } finally {
            Binder.restoreCallingIdentity(ident);
        }
    }
}
