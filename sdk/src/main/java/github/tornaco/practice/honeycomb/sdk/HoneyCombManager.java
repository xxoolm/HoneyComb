package github.tornaco.practice.honeycomb.sdk;

import android.content.Context;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.Log;

import com.google.common.base.Optional;

import github.tornaco.practice.honeycomb.IHoneyComb;
import github.tornaco.practice.honeycomb.IPreferenceManager;
import github.tornaco.practice.honeycomb.sdk.data.PreferenceManager;

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

    public void addService(String name, IBinder binder) throws RemoteException {
        requireHoneyComb().or(DUMMY).addService(name, binder);
    }

    public void deleteService(String name) throws RemoteException {
        requireHoneyComb().or(DUMMY).deleteService(name);
    }

    public IBinder getService(String name) throws RemoteException {
        return requireHoneyComb().or(DUMMY).getService(name);
    }

    public boolean hasService(String name) throws RemoteException {
        return requireHoneyComb().or(DUMMY).hasService(name);
    }

    public PreferenceManager getPreferenceManager(String packageName) throws RemoteException {
        return new PreferenceManager(requireHoneyComb().or(DUMMY).getPreferenceManager(packageName));
    }

    private static class DummyHoneyComb extends IHoneyComb.Stub {

        private static final String TAG = "DummyHoneyComb";

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
        public IPreferenceManager getPreferenceManager(String packageName) throws RemoteException {
            Log.w(TAG, "getPreferenceManager@DummyHoneyComb");
            return null;
        }
    }
}
