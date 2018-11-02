package github.tornaco.practice.honeycomb.am;

import android.content.Intent;

import com.google.common.base.Optional;

import lombok.SneakyThrows;

@SuppressWarnings("Guava")
public class ActivityManager {

    private IActivityManager server;

    public ActivityManager(IActivityManager server) {
        this.server = server;
    }

    private Optional<IActivityManager> requireActivityManager() {
        return Optional.fromNullable(server);
    }

    public boolean isPresent() {
        return requireActivityManager().isPresent();
    }

    @SneakyThrows
    public void onActivityLaunching(Intent intent, String reason) {
        server.onActivityLaunching(intent, reason);
    }

    @SneakyThrows
    public void onTaskRemoving(int callingUid, int taskId) {
        server.onTaskRemoving(callingUid, taskId);
    }

    @SneakyThrows
    public String getFrontUIAppPackageName() {
        return server.getFrontUIAppPackageName();
    }

    @SneakyThrows
    public boolean isAppUIInFront(String pkg) {
        return server.isAppUIInFront(pkg);
    }
}
