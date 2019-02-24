package github.tornaco.practice.honeycomb.pm;

import android.util.Log;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;

import java.util.List;

import lombok.SneakyThrows;

@SuppressWarnings("Guava")
public class PackageManager {

    private final static DummyPackageManagerServer DUMMY = new DummyPackageManagerServer();
    private IPackageManager server;

    public PackageManager(IPackageManager server) {
        this.server = server;
    }

    private Optional<IPackageManager> requirePackageManager() {
        return Optional.fromNullable(server);
    }

    public boolean isPresent() {
        return requirePackageManager().isPresent();
    }

    @SneakyThrows
    public void forceStop(String pkg) {
        requirePackageManager().or(DUMMY).forceStop(pkg);
    }

    @SneakyThrows
    public void unInstall(String pkg, IPackageUnInstallCallback callback) {
        requirePackageManager().or(DUMMY).unInstall(pkg, callback);
    }

    @SneakyThrows
    public List<AppInfo> getInstalledApps(int flags) {
        return requirePackageManager().or(DUMMY).getInstalledApps(flags);
    }

    private static class DummyPackageManagerServer extends IPackageManager.Stub {
        private static final String TAG = "DummyPMS";

        @Override
        public void forceStop(String pkg) {
            Log.w(TAG, "forceStop@DummyPackageManagerServer");
        }

        @Override
        public void unInstall(String pkg, IPackageUnInstallCallback callback) {
            Log.w(TAG, "unInstall@DummyPackageManagerServer");
        }

        @Override
        public List<AppInfo> getInstalledApps(int flags) {
            Log.w(TAG, "getInstalledApps@DummyPackageManagerServer");
            return Lists.newArrayListWithCapacity(0);
        }
    }
}
