package github.tornaco.practice.honeycomb.locker.server;

import android.content.Context;
import android.os.RemoteException;

import org.newstand.logger.Logger;

import github.tornaco.practice.honeycomb.sdk.HoneyCombManager;

class Locker {

    private LockerServer lockerServer;

    void onStart(Context context) {
        this.lockerServer = new LockerServer(context);
    }

    void systemReady() {
        if (HoneyCombManager.global().isHoneyCombReady()) {
            try {
                HoneyCombManager.global().addService("locker", lockerServer);
                Logger.i("locker service published~");
            } catch (RemoteException e) {
                Logger.e(e, "Fail publish locker service");
            }
        }
    }
}
