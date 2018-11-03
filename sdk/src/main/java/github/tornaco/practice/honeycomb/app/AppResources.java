package github.tornaco.practice.honeycomb.app;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.RequiresApi;
import github.tornaco.practice.honeycomb.sdk.BuildConfig;
import github.tornaco.practice.honeycomb.util.PkgUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static android.content.Context.CONTEXT_IGNORE_SECURITY;

/**
 * Created by Tornaco on 2018/4/4 10:56.
 * God bless no bug!
 */

@AllArgsConstructor
@Getter
public class AppResources {

    private static final String LOG_TAG = "AppResources";

    private static final Map<Object, String> sStringResCache = new HashMap<>();
    private static final Map<Object, String[]> sStringArrayResCache = new HashMap<>();

    private Context context;
    private String appPackageName;

    public Bitmap getBitmap(String resName) {
        if (!PkgUtils.isPkgInstalled(this.context, appPackageName)) {
            return BitmapFactory.decodeResource(this.context.getResources(), android.R.drawable.stat_sys_warning);
        }
        if (BuildConfig.DEBUG) {
            Log.d(LOG_TAG, "getBitmap, resName: " + resName);
        }
        try {
            Context appContext = getAppContext();
            if (appContext != null) {
                Resources res = appContext.getResources();
                if (BuildConfig.DEBUG) {
                    Log.d(LOG_TAG, "getBitmap, res: " + res);
                }
                if (res != null) {
                    int id = res.getIdentifier(resName, "drawable", BuildConfig.APPLICATION_ID);
                    if (BuildConfig.DEBUG) {
                        Log.d(LOG_TAG, "getBitmap, id: " + id);
                    }
                    if (id > 0) {
                        Bitmap bitmap = BitmapFactory.decodeResource(res, id);
                        if (BuildConfig.DEBUG) {
                            Log.d(LOG_TAG, "getBitmap, bitmap: " + bitmap);
                        }
                        if (bitmap != null) {
                            return bitmap;
                        } else {
                            return BitmapFactory.decodeResource(this.context.getResources(), android.R.drawable.stat_sys_warning);
                        }
                    }
                }
            }
        } catch (Throwable e) {
            Log.e(LOG_TAG, "Fail getBitmap: " + Log.getStackTraceString(e));
        }
        return null;
    }

    @RequiresApi(Build.VERSION_CODES.M)
    public Icon getIcon(String resName) {
        return getIcon(resName, null);
    }

    @RequiresApi(Build.VERSION_CODES.M)
    public Icon getIcon(String resName, Transform<Bitmap> bitmapTransform) {
        if (!PkgUtils.isPkgInstalled(this.context, BuildConfig.APPLICATION_ID)) {
            return Icon.createWithResource(this.context.getResources(), android.R.drawable.stat_sys_warning);
        }
        if (BuildConfig.DEBUG) {
            Log.d(LOG_TAG, "getIcon, resName: " + resName);
        }
        try {
            Context appContext = getAppContext();
            if (appContext != null) {
                Resources res = appContext.getResources();
                if (BuildConfig.DEBUG) {
                    Log.d(LOG_TAG, "getIcon, res: " + res);
                }
                if (res != null) {
                    int id = res.getIdentifier(resName, "drawable", BuildConfig.APPLICATION_ID);
                    if (BuildConfig.DEBUG) {
                        Log.d(LOG_TAG, "getIcon, id: " + id);
                    }
                    if (id > 0) {
                        Icon ic = null;
                        // Create with res directly.
                        if (bitmapTransform == null) {
                            ic = Icon.createWithResource(res, id);
                        } else {
                            // Create bitmap and transform.
                            try {
                                Bitmap icb = BitmapFactory.decodeResource(res, id);
                                if (icb != null) {
                                    icb = bitmapTransform.onTransform(icb);
                                    if (icb != null) {
                                        ic = Icon.createWithBitmap(icb);
                                    }
                                }
                            } catch (Exception e) {
                                Log.e(LOG_TAG, "getIcon, bitmap transform err: " + e);
                            } finally {
                                // Back to res.
                                if (ic == null) {
                                    ic = Icon.createWithResource(res, id);
                                }
                            }
                        }
                        if (BuildConfig.DEBUG) {
                            Log.d(LOG_TAG, "getIcon, ic: " + ic);
                        }
                        if (ic != null) {
                            return ic;
                        }
                    }
                }
            }
        } catch (Throwable e) {
            Log.e(LOG_TAG, "Fail getIcon: " + Log.getStackTraceString(e));
        }
        return Icon.createWithResource(getContext(), android.R.drawable.stat_sys_warning);
    }

    private Context getAppContext() {
        Context context = getContext();
        if (context == null) {
            Log.e(LOG_TAG, "Context is null!!!");
            return null;
        }
        try {
            return context.createPackageContext(BuildConfig.APPLICATION_ID, CONTEXT_IGNORE_SECURITY);
        } catch (Throwable e) {
            Log.e(LOG_TAG, "Fail createPackageContext: " + Log.getStackTraceString(e));
        }
        return null;
    }

    String[] getStringArray(String resName) {
        if (!PkgUtils.isPkgInstalled(this.context, BuildConfig.APPLICATION_ID)) {
            if (sStringArrayResCache.containsKey(resName)) {
                String[] cached = sStringArrayResCache.get(resName);
                if (cached != null) {
                    return cached;
                }
            }
            return new String[0];
        }
        Context context = getContext();
        if (context == null) {
            Log.e(LOG_TAG, "Context is null!!!");
            return new String[0];
        }
        try {
            Context appContext =
                    context.createPackageContext(BuildConfig.APPLICATION_ID, CONTEXT_IGNORE_SECURITY);
            Resources res = appContext.getResources();
            int id = res.getIdentifier(resName, "array", BuildConfig.APPLICATION_ID);
            Log.d(LOG_TAG, "getStringArray get id: " + id + ", for res: " + resName);
            if (id != 0) {
                String[] stringArr = res.getStringArray(id);
                sStringArrayResCache.put(resName, stringArr);
                return stringArr;
            }
        } catch (Throwable e) {
            Log.e(LOG_TAG, "Fail createPackageContext: " + Log.getStackTraceString(e));
        }
        return new String[0];
    }

    public String getString(String resName, Object... args) {
        if (!PkgUtils.isPkgInstalled(this.context, BuildConfig.APPLICATION_ID)) {
            // Return cache.
            String cachedString = sStringResCache.get(resName);
            if (cachedString != null) {
                return String.format(cachedString, args);
            }
            return resName;
        }
        Context context = getContext();
        if (context == null) {
            Log.e(LOG_TAG, "Context is null!!!");
            return null;
        }
        try {
            Context appContext =
                    context.createPackageContext(BuildConfig.APPLICATION_ID, CONTEXT_IGNORE_SECURITY);
            Resources res = appContext.getResources();
            int id = res.getIdentifier(resName, "string", BuildConfig.APPLICATION_ID);
            Log.d(LOG_TAG, "getString get id: " + id + ", for res: " + resName);
            if (id != 0) {
                String string = res.getString(id, args);
                sStringResCache.put(resName, string);
                return string;
            }
        } catch (Throwable e) {
            Log.e(LOG_TAG, "Fail createPackageContext: " + Log.getStackTraceString(e));
        }
        return null;
    }

    public interface Transform<T> {
        T onTransform(T in) throws Exception;
    }
}
