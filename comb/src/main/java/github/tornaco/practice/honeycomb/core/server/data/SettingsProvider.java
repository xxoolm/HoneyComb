package github.tornaco.practice.honeycomb.core.server.data;

import android.os.HandlerThread;
import android.os.Looper;
import android.util.Log;

import java.io.File;

import github.tornaco.practice.honeycomb.BuildConfig;
import github.tornaco.practice.honeycomb.util.Singleton1;

import static github.tornaco.practice.honeycomb.core.server.data.DataConfigs.getBaseDataDir;


/**
 * Created by guohao4 on 2017/12/19.
 * Email: Tornaco@163.com
 */

public class SettingsProvider {

    private static final String TAG = "SettingsProvider";

    private static final Singleton1<SettingsProvider, String> sProvider =
            new Singleton1<SettingsProvider, String>() {
                @Override
                protected SettingsProvider create(String name) {
                    return new SettingsProvider(name, name.hashCode());
                }
            };

    public static SettingsProvider getOrCreate(String path) {
        return sProvider.get(path);
    }

    private SettingsState settingsState;
    private Looper stateLooper;
    private String name;
    private int key;
    private final Object lock = new Object();

    private SettingsProvider(String path, int key) {

        HandlerThread stateThread = new HandlerThread("SettingsProvider#" + path);
        stateThread.start();
        stateLooper = stateThread.getLooper();

        initSettingsState(stateLooper, path, key);
    }

    private void initSettingsState(Looper looper, String path, int key) {
        this.name = path;
        this.key = key;
        settingsState = new SettingsState(lock,
                new File(path),
                key,
                -1, // No limit.
                looper);
    }

    private boolean insertSettingLocked(String name, String value) {
        synchronized (lock) {
            if (BuildConfig.DEBUG) {
                Log.d(TAG, "insertSettingLocked: " + name + " " + value);
            }
            return settingsState.insertSettingLocked(name, value, "tornaco", true, "android");
        }
    }

    private String getSettingLocked(String name) {
        synchronized (lock) {
            SettingsState.Setting setting = settingsState.getSettingLocked(name);
            if (setting.isNull()) {
                return null;
            }
            return setting.getValue();
        }
    }

    public boolean putString(String name, String value) {
        try {
            return insertSettingLocked(name, value);
        } catch (Throwable e) {
            Log.e(TAG, "putString" + Log.getStackTraceString(e));
            return false;
        }
    }

    public String getString(String name, String def) {
        try {
            return getSettingLocked(name);
        } catch (Throwable e) {
            Log.e(TAG, "getString" + Log.getStackTraceString(e));
            return def;
        }
    }

    public boolean putInt(String name, int value) {
        return putString(name, String.valueOf(value));
    }

    public int getInt(String name, int def) {
        try {
            String s = getString(name, String.valueOf(def));
            if (s == null) return def;
            return Integer.parseInt(s);
        } catch (Throwable e) {
            Log.e(TAG, "getInt" + Log.getStackTraceString(e));
            return def;
        }
    }

    public boolean getBoolean(String name, boolean def) {
        String v = getSettingLocked(name);
        if (v == null) return def;
        try {
            return Boolean.parseBoolean(v);
        } catch (Throwable e) {
            Log.e(TAG, "getBoolean" + Log.getStackTraceString(e));
            return def;
        }
    }

    public boolean putBoolean(String name, boolean value) {
        try {
            return insertSettingLocked(name, String.valueOf(value));
        } catch (Throwable e) {
            Log.e(TAG, "putBoolean" + Log.getStackTraceString(e));
            return false;
        }
    }

    public long getLong(String name, long def) {
        String v = getSettingLocked(name);
        if (v == null) return def;
        try {
            return Long.parseLong(v);
        } catch (Throwable e) {
            Log.e(TAG, "getLong" + Log.getStackTraceString(e));
            return def;
        }
    }

    public boolean putLong(String name, long value) {
        try {
            return insertSettingLocked(name, String.valueOf(value));
        } catch (Throwable e) {
            Log.e(TAG, "putLong" + Log.getStackTraceString(e));
            return false;
        }
    }

    public void reset() {
        try {
            synchronized (lock) {
                settingsState.reset();
                File dir = getBaseDataDir();
                //noinspection ResultOfMethodCallIgnored
                new File(dir, this.name).delete();
                initSettingsState(stateLooper, this.name, this.key);
                Log.d(TAG, "Settings state has been reset!");
            }
        } catch (Throwable e) {
            Log.e(TAG, "Fail reset settings state: " + Log.getStackTraceString(e));
        }
    }
}
