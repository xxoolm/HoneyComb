package github.tornaco.practice.honeycomb.locker.util;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class ExecutorUtils {

    private static final AtomicInteger TID = new AtomicInteger(0);
    private static volatile Executor io;
    private static volatile Executor worker;
    private static volatile Handler uiHandler;

    public synchronized static Executor io() {
        if (io == null) {
            io = new ThreadPoolExecutor(1, 1,
                    0L, TimeUnit.MILLISECONDS,
                    new LinkedBlockingQueue<>(),
                    r -> new Thread(r, "Io-" + TID.getAndIncrement()));
        }
        return io;
    }

    public synchronized static Executor worker() {
        if (worker == null) {
            int nThreads = Runtime.getRuntime().availableProcessors() / 4 + 1;
            worker = new ThreadPoolExecutor(nThreads, nThreads,
                    0L, TimeUnit.MILLISECONDS,
                    new LinkedBlockingQueue<>(),
                    r -> new Thread(r, "Worker-" + TID.getAndIncrement()));
        }
        return worker;
    }

    public synchronized static Handler uiHandler() {
        if (uiHandler == null) {
            uiHandler = new Handler(Looper.getMainLooper());
        }
        return uiHandler;
    }
}
