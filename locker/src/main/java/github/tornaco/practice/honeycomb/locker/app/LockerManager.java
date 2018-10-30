package github.tornaco.practice.honeycomb.locker.app;

import android.os.RemoteException;
import android.util.Log;

import com.google.common.base.Optional;

import github.tornaco.practice.honeycomb.locker.ILocker;
import github.tornaco.practice.honeycomb.locker.ILockerWatcher;
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
    public void setEnabled(boolean enabled) {
        requireLocker().or(DUMMY).setEnabled(enabled);
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

    @SneakyThrows
    public void setVerifyResult(int request, int result, int reason) {
        requireLocker().or(DUMMY).setVerifyResult(request, result, reason);
    }

    private static class DummyLockerManagerService extends ILocker.Stub {

        private static final String TAG = "DummyLMS";

        @Override
        public void setEnabled(boolean enabled) {
            Log.w(TAG, "setEnabled@DummyLockerManagerService");
        }

        @Override
        public boolean isEnabled() {
            Log.w(TAG, "setEnabled@DummyLockerManagerService");
            return false;
        }

        @Override
        public void addWatcher(ILockerWatcher w) {
            Log.w(TAG, "addWatcher@DummyLockerManagerService");
        }

        @Override
        public void deleteWatcher(ILockerWatcher w) {
            Log.w(TAG, "deleteWatcher@DummyLockerManagerService");
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

        @Override
        public void setVerifyResult(int request, int result, int reason) {
            Log.w(TAG, "setVerifyResult@DummyLockerManagerService");
        }
    }
}
