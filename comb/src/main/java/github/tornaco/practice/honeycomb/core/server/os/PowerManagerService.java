package github.tornaco.practice.honeycomb.core.server.os;

import android.content.Context;
import android.os.PowerManager;

import github.tornaco.practice.honeycomb.core.server.i.SystemService;
import github.tornaco.practice.honeycomb.os.IPowerManager;

public class PowerManagerService extends IPowerManager.Stub implements SystemService {
    private Context context;

    @Override
    public void onStart(Context context) {
        this.context = context;
    }

    @Override
    public void onSystemReady() {

    }

    @Override
    public void onShutDown() {

    }

    @Override
    public void reboot() {
        SystemServerThread.getHandler().post(() -> {
            PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            powerManager.reboot(null);
        });
    }

    @Override
    public void softReboot() {

    }
}
