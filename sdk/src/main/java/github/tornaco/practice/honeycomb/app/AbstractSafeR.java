package github.tornaco.practice.honeycomb.app;

import org.newstand.logger.Logger;

public abstract class AbstractSafeR implements Runnable {
    @Override
    public final void run() {
        try {
            runSafety();
        } catch (Throwable err) {
            Logger.e("AbstractSafeR err: %s", Logger.getStackTraceString(err));
        }
    }

    public abstract void runSafety();
}
