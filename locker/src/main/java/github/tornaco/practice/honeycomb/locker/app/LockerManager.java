package github.tornaco.practice.honeycomb.locker.app;

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

    @SneakyThrows
    public void addWatcher(ILockerWatcher w) {
        requireLocker().or(DUMMY).addWatcher(w);
    }

    @SneakyThrows
    public void deleteWatcher(ILockerWatcher w) {
        requireLocker().or(DUMMY).deleteWatcher(w);
    }

    @SneakyThrows
    public void setLockerMethod(int method) {
        requireLocker().or(DUMMY).setLockerMethod(method);
    }

    @SneakyThrows
    public int getLockerMethod() {
        return requireLocker().or(DUMMY).getLockerMethod();
    }

    @SneakyThrows
    public void setLockerKey(int method, String key) {
        requireLocker().or(DUMMY).setLockerKey(method, key);
    }

    @SneakyThrows
    public boolean isLockerKeyValid(int method, String key) {
        return requireLocker().or(DUMMY).isLockerKeyValid(method, key);
    }

    @SneakyThrows
    public boolean isLockerKeySet(int method) {
        return requireLocker().or(DUMMY).isLockerKeySet(method);
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

        @Override
        public void setLockerMethod(int method) {
            Log.w(TAG, "setLockerMethod@DummyLockerManagerService");
        }

        @Override
        public int getLockerMethod() {
            Log.w(TAG, "getLockerMethod@DummyLockerManagerService");
            return 0;
        }

        @Override
        public void setLockerKey(int method, String key) {
            Log.w(TAG, "setLockerKey@DummyLockerManagerService");
        }

        @Override
        public boolean isLockerKeyValid(int method, String key) {
            Log.w(TAG, "isLockerKeyValid@DummyLockerManagerService");
            return false;
        }

        @Override
        public boolean isLockerKeySet(int method) {
            Log.w(TAG, "isLockerKeySet@DummyLockerManagerService");
            return false;
        }
    }
}
