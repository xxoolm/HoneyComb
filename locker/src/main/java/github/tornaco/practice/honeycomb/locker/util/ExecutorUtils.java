package github.tornaco.practice.honeycomb.locker.util;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ExecutorUtils {

    private static final Executor IO = Executors.newSingleThreadExecutor();
    private static final Executor WORKER = Executors.newFixedThreadPool(
            Runtime.getRuntime().availableProcessors() / 4);

    public static Executor io() {
        return IO;
    }

    public static Executor worker() {
        return WORKER;
    }
}
