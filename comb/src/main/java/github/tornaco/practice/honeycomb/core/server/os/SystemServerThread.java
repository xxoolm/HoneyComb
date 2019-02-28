//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package github.tornaco.practice.honeycomb.core.server.os;

import android.os.Handler;
import android.os.HandlerThread;

public final class SystemServerThread extends HandlerThread {
    private static SystemServerThread sInstance;
    private static Handler sHandler;

    private SystemServerThread() {
        super("comb.system_server", Thread.MAX_PRIORITY);
    }

    private static void ensureThreadLocked() {
        if (sInstance == null) {
            sInstance = new SystemServerThread();
            sInstance.start();
            sHandler = new Handler(sInstance.getLooper());
        }

    }

    public static Handler getHandler() {
        synchronized (SystemServerThread.class) {
            ensureThreadLocked();
            return sHandler;
        }
    }
}
