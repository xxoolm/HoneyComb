package github.tornaco.practice.honeycomb.core;

import android.content.Context;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.ServiceManager;

import com.google.common.base.Optional;

import org.newstand.logger.Logger;

import github.tornaco.practice.honeycomb.IHoneyComb;

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

    private static class DummyHoneyComb extends IHoneyComb.Stub {

        @Override
        public void addService(String name, IBinder binder) {
            Logger.w("addService@DummyHoneyComb");
        }

        @Override
        public void deleteService(String name) {
            Logger.w("deleteService@DummyHoneyComb");
        }

        @Override
        public IBinder getService(String name) {
            Logger.w("getService@DummyHoneyComb");
            return null;
        }

        @Override
        public boolean hasService(String name) {
            Logger.w("hasService@DummyHoneyComb");
            return false;
        }
    }
}
