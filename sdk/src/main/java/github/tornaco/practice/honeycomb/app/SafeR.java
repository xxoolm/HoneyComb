package github.tornaco.practice.honeycomb.app;

import org.newstand.logger.Logger;

public abstract class SafeR implements Runnable {
    @Override
    public final void run() {
        try {
            runSafety();
        } catch (Throwable err) {
            Logger.e(err, "SafeR");
        }
    }

    public abstract void runSafety();
}
