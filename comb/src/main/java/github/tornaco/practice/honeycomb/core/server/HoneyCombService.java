package github.tornaco.practice.honeycomb.core.server;

import android.content.Context;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.ServiceManager;

import org.newstand.logger.Logger;

import github.tornaco.practice.honeycomb.IHoneyComb;

class HoneyCombService {

    void onStart(Context context) {
        publish();
    }

    private void publish() {
        try {
            ServiceManager.addService(Context.TV_INPUT_SERVICE, new ServiceStub());
        } catch (Throwable e) {
            Logger.e(e, "Fail publish HoneyCombService");
        }
    }

    public void shutDown() {

    }

    public void systemReady() {

    }

    private static class ServiceStub extends IHoneyComb.Stub {
        @Override
        public void addService(String name, IBinder binder) throws RemoteException {
            HoneyCombServiceManager.addService(name, binder);
        }

        @Override
        public void deleteService(String name) throws RemoteException {
            HoneyCombServiceManager.deleteService(name);
        }

        @Override
        public IBinder getService(String name) throws RemoteException {
            return HoneyCombServiceManager.getService(name);
        }

        @Override
        public boolean hasService(String name) throws RemoteException {
            return HoneyCombServiceManager.hasService(name);
        }
    }
}
