package github.tornaco.practice.honeycomb.locker.server;

import android.content.Context;

import org.newstand.logger.Logger;

import github.tornaco.practice.honeycomb.HoneyCombManager;
import github.tornaco.practice.honeycomb.app.HoneyCombContext;
import github.tornaco.practice.honeycomb.locker.app.LockerContext;
import github.tornaco.practice.honeycomb.locker.server.verify.Verifier;

public class Locker {

    private LockerServer lockerServer = new LockerServer();

    public void onStart(Context context) {
        this.lockerServer.onStart(context);
    }

    public void systemReady() {
        HoneyCombContext honeyCombContext = HoneyCombContext.createContext();
        HoneyCombManager honeyCombManager = honeyCombContext.getHoneyCombManager();
        if (honeyCombManager.isPresent()) {
            try {
                honeyCombManager.addService(LockerContext.LOCKER_SERVICE, lockerServer);
                Logger.i("locker service published~");
            } catch (Exception e) {
                Logger.e(e, "Fail publish locker service");
            }
        }
    }

    Verifier getVerifier() {
        return lockerServer;
    }
}
