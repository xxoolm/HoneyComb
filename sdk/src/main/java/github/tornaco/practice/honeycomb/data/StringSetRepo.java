package github.tornaco.practice.honeycomb.data;

import android.os.Handler;
import android.util.AtomicFile;
import android.util.Log;

import com.google.common.io.Files;

import org.newstand.logger.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import github.tornaco.practice.honeycomb.data.i.SetRepo;
import github.tornaco.practice.honeycomb.util.FileUtils;
import github.tornaco.practice.honeycomb.util.XmlUtils;
import lombok.Cleanup;

/**
 * Created by guohao4 on 2017/12/11.
 * Email: Tornaco@163.com
 */

public class StringSetRepo implements SetRepo<String> {

    private static final int FLUSH_DELAY = 5000;
    private static final int FLUSH_DELAY_FAST = 100;

    private static final ExecutorService IO = new ThreadPoolExecutor(1, 1,
            0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>(),
            r -> new Thread(r, "Io-MapRepo"));

    private Handler mHandler;
    private ExecutorService mExe;

    private AtomicFile mFile;

    public StringSetRepo(File file, Handler handler, ExecutorService service) {
        this.mFile = new AtomicFile(file);
        this.mExe = service;
        this.mHandler = handler;

        try {
            if (!this.mFile.getBaseFile().exists()) {
                Files.createParentDirs(file);
            }
        } catch (IOException e) {
            Logger.wtf("Fail createParentDirs for: " + file + "\n" + Log.getStackTraceString(e));
        }

        Logger.d("StringSetRepo: " + name() + ", comes up @%s", file);

        reload();
    }

    private final Set<String> mStorage = new HashSet<>();

    private final Object sync = new Object();

    @Override
    public Set<String> getAll() {
        return new HashSet<>(mStorage);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void reload() {
        synchronized (sync) {
            try {

                if (!mFile.getBaseFile().exists()) {
                    Logger.wtf("getBaseFile not exists, skip load: " + name());
                    return;
                }

                if (mFile.getBaseFile().isDirectory()) {
                    Logger.wtf("getBaseFile isDirectory, clean up: " + name());
                    FileUtils.deleteDirQuiet(mFile.getBaseFile());
                    mFile.delete();
                }

                @Cleanup
                InputStream inputStream = mFile.openRead();
                Set h = new HashSet(XmlUtils.readSetXml(inputStream));
                mStorage.addAll(h);

            } catch (Throwable e) {
                Logger.wtf("Fail reload@IOException: " + mFile + "\n" + Log.getStackTraceString(e));
            }
        }
    }


    @Override
    public void reloadAsync() {
        Runnable r = this::reload;
        if (mExe == null) {
            IO.execute(r);
        } else {
            mExe.execute(r);
        }
    }

    @Override
    public void flush() {
        Logger.i("flush");
        synchronized (sync) {
            try {
                Set<String> out = new HashSet<>(mStorage);
                @Cleanup
                FileOutputStream fos = mFile.startWrite();
                XmlUtils.writeSetXml(out, fos);
                mFile.finishWrite(fos);
            } catch (Throwable e) {
                Logger.wtf("Fail flush@IOException: " + mFile + "\n" + Log.getStackTraceString(e));
            }
        }
    }

    private Runnable mFlusher = this::flush;

    private Runnable mFlushCaller = this::flushAsync;

    @Override
    public void flushAsync() {
        Logger.i("flush async");
        if (mExe == null) {
            IO.execute(mFlusher);
        } else {
            mExe.execute(mFlusher);
        }
    }

    @Override
    public boolean add(String s) {
        if (s == null) {
            return false;
        }
        boolean added = mStorage.add(s);
        if (added && mHandler != null) {
            mHandler.removeCallbacks(mFlushCaller);
            mHandler.postDelayed(mFlushCaller, FLUSH_DELAY);
        }
        return added;
    }

    @Override
    public boolean addAll(Collection<? extends String> c) {
        if (c == null) {
            return false;
        }
        boolean added = mStorage.addAll(c);
        if (added && mHandler != null) {
            mHandler.removeCallbacks(mFlushCaller);
            mHandler.postDelayed(mFlushCaller, FLUSH_DELAY);
        }
        return added;
    }

    @Override
    public boolean remove(String s) {
        if (s == null) {
            return false;
        }
        boolean removed = mStorage.remove(s);
        if (removed && mHandler != null) {
            mHandler.removeCallbacks(mFlushCaller);
            mHandler.postDelayed(mFlushCaller, FLUSH_DELAY);
        }
        return removed;
    }

    @Override
    public void removeAll() {
        mStorage.clear();
        if (mHandler != null) {
            mHandler.removeCallbacks(mFlushCaller);
            mHandler.postDelayed(mFlushCaller, FLUSH_DELAY_FAST);
        }
    }

    @Override
    public boolean has(String s) {
        return s != null && mStorage.contains(s);
    }

    @Override
    public boolean has(String[] t) {
        if (t != null) {
            for (String tt : t) {
                if (has(tt)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public String name() {
        return Files.getNameWithoutExtension(mFile.getBaseFile().getPath());
    }

    @Override
    public int size() {
        return mStorage.size();
    }

}
