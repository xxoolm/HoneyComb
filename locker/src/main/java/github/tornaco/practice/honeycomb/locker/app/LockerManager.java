package github.tornaco.practice.honeycomb.locker.app;

import android.util.Log;

import com.google.common.base.Optional;

import github.tornaco.practice.honeycomb.locker.ILocker;
import lombok.SneakyThrows;

@SuppressWarnings("Guava")
public class LockerManager {

    private final static DummyLockerManagerService DUMMY = new DummyLockerManagerService();
    private ILocker locker;

    LockerManager(ILocker locker) {
        this.locker = locker;
    }

    private Optional<ILocker> requireLocker() {
        return Optional.fromNullable(locker);
    }

    public boolean isPresent() {
        return requireLocker().isPresent();
    }

    @SneakyThrows
    public void setEnabled() {
        requireLocker().or(DUMMY).setEnabled();
    }

    @SneakyThrows
    public boolean isEnabled() {
        return requireLocker().or(DUMMY).isEnabled();
    }

    @SneakyThrows
    public boolean isPackageLocked(String pkg) {
        return requireLocker().or(DUMMY).isPackageLocked(pkg);
    }

    @SneakyThrows
    public void setPackageLocked(String pkg, boolean locked) {
        requireLocker().or(DUMMY).setPackageLocked(pkg, locked);
    }

    private static class DummyLockerManagerService extends ILocker.Stub {

        private static final String TAG = "DummyLMS";

        @Override
        public void setEnabled() {
            Log.w(TAG, "setEnabled@DummyLockerManagerService");
        }

        @Override
        public boolean isEnabled() {
            Log.w(TAG, "setEnabled@DummyLockerManagerService");
            return false;
        }

        @Override
        public boolean isPackageLocked(String pkg) {
            Log.w(TAG, "setEnabled@DummyLockerManagerService");
            return false;
        }

        @Override
        public void setPackageLocked(String pkg, boolean locked) {
            Log.w(TAG, "setEnabled@DummyLockerManagerService");
        }
    }
}
