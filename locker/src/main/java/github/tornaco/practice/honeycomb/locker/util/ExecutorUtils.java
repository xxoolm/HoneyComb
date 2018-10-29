package github.tornaco.practice.honeycomb.locker.util;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ExecutorUtils {

    private static volatile Executor io;
    private static volatile Executor worker;

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
}
