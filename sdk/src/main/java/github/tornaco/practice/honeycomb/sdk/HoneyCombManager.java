package github.tornaco.practice.honeycomb.sdk;

import android.content.Context;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.Log;

import com.google.common.base.Optional;

import github.tornaco.practice.honeycomb.IHoneyComb;
import github.tornaco.practice.honeycomb.am.IActivityManager;
import github.tornaco.practice.honeycomb.data.IPreferenceManager;
import github.tornaco.practice.honeycomb.device.IPowerManager;
import github.tornaco.practice.honeycomb.pm.IPackageManager;
import github.tornaco.practice.honeycomb.sdk.data.PreferenceManager;
import lombok.SneakyThrows;

@SuppressWarnings("Guava")
public class HoneyCombManager {

    private volatile static HoneyCombManager sMe;
    private static final IHoneyComb DUMMY = new DummyHoneyComb();
    private IHoneyComb honeyComb;

    public synchronized static HoneyCombManager global() {
        if (sMe == null) sMe = new HoneyCombManager();
        return sMe;
    }

    private Optional<IHoneyComb> requireHoneyComb() {
        synchronized (this) {
            if (honeyComb == null || !honeyComb.asBinder().isBinderAlive()) {
                honeyComb = IHoneyComb.Stub.asInterface(
                        ServiceManager.getService(Context.TV_INPUT_SERVICE));
            }
            return Optional.fromNullable(honeyComb);
        }
    }

    public boolean isHoneyCombReady() {
        return requireHoneyComb().isPresent();
    }

    @SneakyThrows
    public String getVersion() {
        return requireHoneyComb().or(DUMMY).getVersion();
    }

    @SneakyThrows
    public int getStatus() {
        return requireHoneyComb().or(DUMMY).getStatus();
    }

    @SneakyThrows
    public void addService(String name, IBinder binder) {
        requireHoneyComb().or(DUMMY).addService(name, binder);
    }

    @SneakyThrows
    public void deleteService(String name) {
        requireHoneyComb().or(DUMMY).deleteService(name);
    }

    @SneakyThrows
    public IBinder getService(String name) {
        return requireHoneyComb().or(DUMMY).getService(name);
    }

    @SneakyThrows
    public boolean hasService(String name) {
        return requireHoneyComb().or(DUMMY).hasService(name);
    }

    @SneakyThrows
    public PreferenceManager getPreferenceManager(String packageName) {
        return new PreferenceManager(requireHoneyComb().or(DUMMY).getPreferenceManager(packageName));
    }

    @SneakyThrows
    public IActivityManager getActivityManager() {
        return honeyComb.getActivityManager();
    }

    @SneakyThrows
    public IPowerManager getPowerManager() {
        return honeyComb.getPowerManager();
    }

    @SneakyThrows
    public IPackageManager getPackageManager() {
        return honeyComb.getPackageManager();
    }

    private static class DummyHoneyComb extends IHoneyComb.Stub {

        private static final String TAG = "DummyHoneyComb";

        @Override
        public String getVersion() {
            Log.w(TAG, "getStatus@DummyHoneyComb");
            return null;
        }

        @Override
        public int getStatus() {
            Log.w(TAG, "getStatus@DummyHoneyComb");
            return 0;
        }

        @Override
        public void addService(String name, IBinder binder) {
            Log.w(TAG, "addService@DummyHoneyComb");
        }

        @Override
        public void deleteService(String name) {
            Log.w(TAG, "deleteService@DummyHoneyComb");
        }

        @Override
        public IBinder getService(String name) {
            Log.w(TAG, "getService@DummyHoneyComb");
            return null;
        }

        @Override
        public boolean hasService(String name) {
            Log.w(TAG, "hasService@DummyHoneyComb");
            return false;
        }

        @Override
        public IPreferenceManager getPreferenceManager(String packageName) {
            Log.w(TAG, "getPreferenceManager@DummyHoneyComb");
            return null;
        }

        @Override
        public IActivityManager getActivityManager() {
            Log.w(TAG, "getActivityManager@DummyHoneyComb");
            return null;
        }

        @Override
        public IPowerManager getPowerManager() {
            Log.w(TAG, "getPowerManager@DummyHoneyComb");
            return null;
        }

        @Override
        public IPackageManager getPackageManager() {
            Log.w(TAG, "getPackageManager@DummyHoneyComb");
            return null;
        }
    }
}
