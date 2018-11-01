package github.tornaco.practice.honeycomb.locker.util;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ExecutorUtils {

    private static volatile Executor io;
    private static volatile Executor worker;
    private static volatile Handler uiHandler;

    public synchronized static Executor io() {
        if (io == null) {
            io = Executors.newSingleThreadExecutor();
        }
        return io;
    }

    public synchronized static Executor worker() {
        if (worker == null) {
            int nThreads = Runtime.getRuntime().availableProcessors() / 4 + 1;
            worker = new ThreadPoolExecutor(nThreads, nThreads,
                    0L, TimeUnit.MILLISECONDS,
                    new LinkedBlockingQueue<Runnable>());
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
