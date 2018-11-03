package github.tornaco.practice.honeycomb.core.server.am;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;

import org.newstand.logger.Logger;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import androidx.annotation.Nullable;
import github.tornaco.practice.honeycomb.am.IActivityManager;
import github.tornaco.practice.honeycomb.annotations.AppReporting;
import github.tornaco.practice.honeycomb.app.HoneyCombContext;
import github.tornaco.practice.honeycomb.core.server.i.SystemService;
import github.tornaco.practice.honeycomb.data.PreferenceManager;
import github.tornaco.practice.honeycomb.util.HandlerUtils;
import github.tornaco.practice.honeycomb.util.PkgUtils;
import lombok.Getter;

import static github.tornaco.practice.honeycomb.app.HoneyCombContext.HoneyCombConfigs.DEF_SHOW_CURRENT_COMPONENT_ENABLED;
import static github.tornaco.practice.honeycomb.app.HoneyCombContext.HoneyCombKeys.KEY_SHOW_CURRENT_COMPONENT_ENABLED;

public class ActivityManagerService extends IActivityManager.Stub implements SystemService {

    @Getter
    private Context systemContext;
    @Getter
    private HoneyCombContext honeyCombContext;
    private Handler h;
    private CurrentComponentView currentComponentView;
    private ShowCurrentComponentViewR showCurrentComponentViewR;
    private AtomicReference<ComponentName> frontUIAppComponentName
            = new AtomicReference<>(null);
    private AtomicBoolean systemReady = new AtomicBoolean(false);

    @Override
    public void onStart(Context context) {
        this.systemContext = context;
        this.honeyCombContext = HoneyCombContext.createContext();
        this.h = HandlerUtils.newHandlerOfNewThread("ActivityManagerService");
        this.showCurrentComponentViewR = new ShowCurrentComponentViewR();
    }

    @Override
    public void onSystemReady() {
        systemReady.set(true);
        // View 需要在系统启动完成后初始化
        this.currentComponentView = new CurrentComponentView(systemContext,
                new CurrentComponentViewCallback(systemContext));
    }

    @Override
    public void onShutDown() {
        systemReady.set(false);
    }

    @Override
    @AppReporting
    public void onActivityLaunching(Intent intent, String reason) {
        if (!systemReady.get()) {
            Logger.e(new Throwable(), "system not ready");
            return;
        }
        Logger.i("onActivityLaunching %s %s", reason, intent);
        if (intent != null) {
            ComponentName name = intent.getComponent();
            frontUIAppComponentName.set(name);
            if (isShowCurrentComponentViewEnabled()) {
                showCurrentComponentView();
            }
        }
    }

    @Override
    public void onTaskRemoving(int callingUid, int taskId) {
        if (!systemReady.get()) {
            Logger.e(new Throwable(), "system not ready");
            return;
        }
        String pkgName = getPackageNameForTaskId(taskId);
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

    private boolean isShowCurrentComponentViewEnabled() {
        PreferenceManager preferenceManager = honeyCombContext.getPreferenceManager();
        return preferenceManager.getBoolean(KEY_SHOW_CURRENT_COMPONENT_ENABLED, DEF_SHOW_CURRENT_COMPONENT_ENABLED);
    }

    private void showCurrentComponentView() {
        Logger.v("showCurrentComponentView %s", currentComponentView);
        h.removeCallbacks(showCurrentComponentViewR);
        showCurrentComponentViewR.setName(frontUIAppComponentName.get());
        showCurrentComponentViewR.setView(currentComponentView);
        h.post(showCurrentComponentViewR);
    }

    @SuppressWarnings({"deprecation", "AliDeprecation"})
    @Nullable
    private String getPackageNameForTaskId(int taskId) {
        long ident = Binder.clearCallingIdentity();
        try {
            ActivityManager am = (ActivityManager) getSystemContext().getSystemService(Context.ACTIVITY_SERVICE);
            if (am != null) {
                List<ActivityManager.RecentTaskInfo> tasks = am.getRecentTasks(99, ActivityManager.RECENT_WITH_EXCLUDED);
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
