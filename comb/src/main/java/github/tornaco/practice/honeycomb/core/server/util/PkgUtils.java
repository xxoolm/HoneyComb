package github.tornaco.practice.honeycomb.core.server.util;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.provider.Telephony;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PkgUtils {
    public static final String PACKAGE_NAME_ANDROID = "android";

    public static boolean isMainIntent(Intent intent) {
        return intent != null
                && Intent.ACTION_MAIN.equals(intent.getAction())
                && intent.hasCategory(Intent.CATEGORY_LAUNCHER);
    }

    public static String getPackageNameFromIntent(Intent intent) {
        if (intent == null) {
            return null;
        }
        String packageName = intent.getPackage();
        if (packageName != null) {
            return packageName;
        }
        if (intent.getComponent() == null) {
            return null;
        }
        return intent.getComponent().getPackageName();
    }

    public static boolean isLauncherApp(Context context, String packageName) {
        PackageManager pkgManager = context.getPackageManager();
        Intent mainIntent = new Intent("android.intent.action.MAIN", null);
        mainIntent.addCategory("android.intent.category.LAUNCHER");
        mainIntent.setPackage(packageName);
        ResolveInfo ri = pkgManager.resolveActivity(mainIntent, 0);
        return !(ri == null || ri.activityInfo == null);
    }

    public static boolean isHomeApp(Context context, String packageName) {
        PackageManager pkgManager = context.getPackageManager();
        Intent homeIntent = new Intent("android.intent.action.MAIN");
        homeIntent.addCategory("android.intent.category.HOME");
        homeIntent.setPackage(packageName);
        ResolveInfo ri = pkgManager.resolveActivity(homeIntent, 0);
        return !(ri == null || ri.activityInfo == null);
    }

    public static boolean isInputMethodApp(Context context, String pkgName) {
        PackageManager pm = context.getPackageManager();
        boolean isIme = false;
        PackageInfo pkgInfo;
        try {
            pkgInfo = pm.getPackageInfo(pkgName, PackageManager.GET_SERVICES);
            if (pkgInfo != null) {
                ServiceInfo[] servicesInfos = pkgInfo.services;
                if (null != servicesInfos) {
                    for (ServiceInfo sInfo : servicesInfos) {
                        if (null != sInfo.permission && sInfo.permission.equals(Manifest.permission.BIND_INPUT_METHOD)) {
                            isIme = true;
                            break;
                        }
                    }
                }
            }
        } catch (Exception ignored) {
        }
        return isIme;
    }

    /**
     * Fix dead lock issue when call this along with xxx framework patch.
     */
    @Deprecated
    public static boolean isDefaultSmsApp(Context context, String packageName) {
        String def = Telephony.Sms.getDefaultSmsPackage(context);
        return def != null && def.equals(packageName);
    }

    public static Set<String> getRunningProcessPackages(Context context) {
        @SuppressWarnings("ConstantConditions") List<ActivityManager.RunningAppProcessInfo> processes =
                ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE))
                        .getRunningAppProcesses();
        HashSet<String> h = new HashSet<>();
        if (processes == null || processes.size() == 0) {
            return h;
        }
        for (ActivityManager.RunningAppProcessInfo info : processes) {
            if (info != null && info.pkgList != null && info.pkgList.length > 0) {
                Collections.addAll(h, info.pkgList);
            }
        }
        return h;
    }
}
