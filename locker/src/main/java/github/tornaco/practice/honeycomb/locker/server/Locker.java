package github.tornaco.practice.honeycomb.locker.server;

import android.content.Context;

import org.newstand.logger.Logger;

import github.tornaco.practice.honeycomb.locker.server.verify.Verifier;
import github.tornaco.practice.honeycomb.sdk.HoneyCombManager;

public class Locker {

    private LockerServer lockerServer = new LockerServer();

    public void onStart(Context context) {
        this.lockerServer.onStart(context);
    }

    public void systemReady() {
        if (HoneyCombManager.global().isHoneyCombReady()) {
            try {
                HoneyCombManager.global().addService("locker", lockerServer);
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
