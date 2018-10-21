package github.tornaco.practice.honeycomb.core.server.am;

import android.content.ComponentName;
import android.content.Intent;

import org.newstand.logger.Logger;

import java.util.concurrent.atomic.AtomicReference;

import github.tornaco.practice.honeycomb.am.IActivityManager;

public class ActivityManagerService extends IActivityManager.Stub {

    private AtomicReference<ComponentName> frontUIAppComponentName
            = new AtomicReference<>(null);

    @Override
    public void onActivityLaunching(Intent intent, String reason) {
        Logger.i("onActivityLaunching %s %s", reason, intent);
        if (intent != null) {
            ComponentName name = intent.getComponent();
            frontUIAppComponentName.set(name);
        }
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
}
