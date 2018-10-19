package github.tornaco.practice.honeycomb.core.server;

import android.content.Context;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.ServiceManager;

import org.newstand.logger.Logger;

import github.tornaco.practice.honeycomb.IHoneyComb;
import github.tornaco.practice.honeycomb.sdk.annotations.SystemProcess;
import lombok.Getter;

class HoneyCombService {

    @Getter
    private Context systemContext;

    @SystemProcess
    void onStart(Context context) {
        this.systemContext = context;
        publish();
    }

    @SystemProcess
    private void publish() {
        try {
            ServiceManager.addService(Context.TV_INPUT_SERVICE, new ServiceStub());
            Logger.w("HoneyCombService publish success %s", this.toString());
        } catch (Throwable e) {
            Logger.e(e, "Fail publish HoneyCombService");
        }
    }

    @SystemProcess
    void shutDown() {

    }

    @SystemProcess
    void systemReady() {

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
