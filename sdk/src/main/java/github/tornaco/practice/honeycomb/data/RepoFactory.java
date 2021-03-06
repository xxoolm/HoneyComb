package github.tornaco.practice.honeycomb.data;

import android.os.Handler;
import android.os.HandlerThread;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import github.tornaco.practice.honeycomb.util.BaseSingleton;

public class RepoFactory {

    private static final ExecutorService IO = new ThreadPoolExecutor(1, 1,
            0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>(),
            r -> new Thread(r, "Io-Repo"));
    private static final BaseSingleton<RepoFactory> ME = new BaseSingleton<RepoFactory>() {
        @Override
        protected RepoFactory create() {
            return new RepoFactory();
        }
    };

    private final Map<String, StringMapRepo> stringMapRepoCache = new ConcurrentHashMap<>();
    private final Map<String, StringSetRepo> stringSetRepoCache = new ConcurrentHashMap<>();
    private final Handler repoHandler;

    private RepoFactory() {
        // Sync in a new handler thread.
        HandlerThread hr = new HandlerThread("RepoFactory");
        hr.start();
        this.repoHandler = new Handler(hr.getLooper());
    }

    public static RepoFactory get() {
        return ME.get();
    }

    public StringMapRepo getOrCreateStringMapRepo(String path) {
        if (stringMapRepoCache.containsKey(path)) {
            return stringMapRepoCache.get(path);
        }
        StringMapRepo repo = new StringMapRepo(new File(path), this.repoHandler, IO);
        stringMapRepoCache.put(path, repo);
        return repo;
    }

    public StringSetRepo getOrCreateStringSetRepo(String path) {
        if (stringSetRepoCache.containsKey(path)) {
            return stringSetRepoCache.get(path);
        }
        StringSetRepo repo = new StringSetRepo(new File(path), this.repoHandler, IO);
        stringSetRepoCache.put(path, repo);
        return repo;
    }
}
