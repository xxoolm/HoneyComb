package github.tornaco.practice.honeycomb.core.server.data;

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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;

import androidx.annotation.NonNull;
import github.tornaco.practice.honeycomb.core.server.data.i.MapRepo;
import github.tornaco.practice.honeycomb.util.CloserUtils;
import github.tornaco.practice.honeycomb.util.FileUtils;
import github.tornaco.practice.honeycomb.util.XmlUtils;

/**
 * Created by guohao4 on 2017/12/28.
 * Email: Tornaco@163.com
 */

public class StringMapRepo implements MapRepo<String, String> {

    private static final String NULL_INDICATOR = "NULL";

    private final Map<String, String> mStorage = new HashMap<>();

    // Flush data too many times, may drain battery.
    private static final int FLUSH_DELAY = 5000;
    private static final int FLUSH_DELAY_FAST = 100;

    private Handler mHandler;
    private ExecutorService mExe;

    private AtomicFile mFile;

    private final Object sync = new Object();

    StringMapRepo(File file, Handler handler, ExecutorService service) {
        this.mFile = new AtomicFile(file);
        this.mExe = service;
        this.mHandler = handler;

        if (!this.mFile.getBaseFile().exists()) {
            try {
                Files.createParentDirs(file);
            } catch (IOException e) {
                Logger.wtf("Fail createParentDirs for: " + file + "\n" + Log.getStackTraceString(e));
            }
        }

        Logger.d("StringMapRepo: " + name() + ", comes up");

        reload();
    }

    @Override
    public void reload() {
        synchronized (sync) {
            com.google.common.io.Closer closer = com.google.common.io.Closer.create();
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


                InputStream is = closer.register(mFile.openRead());
                @SuppressWarnings("unchecked") Map<String, String> m = (Map<String, String>) XmlUtils.readMapXml(is);
                mStorage.putAll(m);

            } catch (Throwable e) {
                Logger.wtf("Fail reload@IOException: " + mFile + "\n" + Log.getStackTraceString(e));
            } finally {
                CloserUtils.closeQuietly(closer);
            }
        }
    }

    @Override
    public void reloadAsync() {
        Runnable r = this::reload;
        if (mExe == null) {
            new Thread(r).start();
        } else {
            mExe.execute(r);
        }
    }

    @Override
    public void flush() {
        Logger.i("flush");
        com.google.common.io.Closer closer = com.google.common.io.Closer.create();
        synchronized (sync) {
            try {

                Map<String, String> m = new HashMap<>(mStorage);

                FileOutputStream fos = closer.register(mFile.startWrite());
                XmlUtils.writeMapXml(m, fos);
                mFile.finishWrite(fos);

            } catch (Throwable e) {
                Logger.wtf("Fail flush@IOException: " + mFile + "\n" + Log.getStackTraceString(e));
            } finally {
                CloserUtils.closeQuietly(closer);
            }
        }
    }

    private Runnable mFlusher = this::flush;

    private Runnable mFlushCaller = this::flushAsync;

    @Override
    public void flushAsync() {
        Logger.i("flush async");
        if (mExe == null) {
            new Thread(mFlusher).start();
        } else {
            mExe.execute(mFlusher);
        }
    }

    @Override
    public String name() {
        return Files.getNameWithoutExtension(mFile.getBaseFile().getPath());
    }

    @Override
    public Map<String, String> dup() {
        return new HashMap<>(mStorage);
    }

    @Override
    public boolean hasNoneNullValue(String s) {
        String v = mStorage.get(s);
        return v != null && (!NULL_INDICATOR.equals(v));
    }

    @Override
    public int size() {
        return mStorage.size();
    }

    @Override
    public boolean isEmpty() {
        return mStorage.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return mStorage.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return mStorage.containsValue(value);
    }

    @Override
    public String get(Object key) {
        return mStorage.get(key);
    }

    @Override
    public String put(String key, String value) {
        String res = mStorage.put(key, value == null ? NULL_INDICATOR : value);
        mHandler.removeCallbacks(mFlushCaller);
        mHandler.postDelayed(mFlushCaller, FLUSH_DELAY);
        return res;
    }

    @Override
    public String remove(Object key) {
        String res = mStorage.remove(key);
        mHandler.removeCallbacks(mFlushCaller);
        mHandler.postDelayed(mFlushCaller, FLUSH_DELAY);
        return res;
    }

    @Override
    public void putAll(@NonNull Map<? extends String, ? extends String> m) {
        mStorage.putAll(m);
        mHandler.removeCallbacks(mFlushCaller);
        mHandler.postDelayed(mFlushCaller, FLUSH_DELAY);
    }

    @Override
    public void clear() {
        mStorage.clear();
        mHandler.removeCallbacks(mFlushCaller);
        mHandler.postDelayed(mFlushCaller, FLUSH_DELAY_FAST);
    }

    @NonNull
    @Override
    public Set<String> keySet() {
        return new HashSet<>(mStorage.keySet());
    }

    @NonNull
    @Override
    public Collection<String> values() {
        return mStorage.values();
    }

    @NonNull
    @Override
    public Set<Entry<String, String>> entrySet() {
        return new HashSet<>(mStorage.entrySet());
    }
}
