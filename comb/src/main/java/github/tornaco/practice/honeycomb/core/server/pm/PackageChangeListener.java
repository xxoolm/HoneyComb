package github.tornaco.practice.honeycomb.core.server.pm;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.RemoteException;

import org.newstand.logger.Logger;

import github.tornaco.practice.honeycomb.event.Event;
import github.tornaco.practice.honeycomb.event.IEventSubscriber;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class PackageChangeListener extends IEventSubscriber.Stub {

    private Context context;
    private OnModuleInstalledListener listener;

    private static String getPackageName(Intent intent) {
        Uri uri = intent.getData();
        return (uri != null) ? uri.getSchemeSpecificPart() : null;
    }

    @Override
    public void onEvent(Event e) throws RemoteException {
        Logger.d("PackageChangeListener onEvent %s", e);
        if (e.getIntent().getAction() == null) {
            return;
        }
        String pkgName = getPackageName(e.getIntent());
        PackageManager pm = context.getPackageManager();
        String apkPath = null;
        boolean isCombModule = false;
        try {
            ApplicationInfo app = pm.getApplicationInfo(pkgName, PackageManager.GET_META_DATA);
            if (app.enabled && app.metaData != null && app.metaData.containsKey("combmodule")) {
                isCombModule = true;
                apkPath = app.publicSourceDir;
            }
        } catch (PackageManager.NameNotFoundException e1) {
            return;
        }

        if (!isCombModule) {
            return;
        }

        if (e.getIntent().getAction().equals(Intent.ACTION_PACKAGE_ADDED)) {
            listener.onNewModuleInstalled(pkgName, apkPath);
        } else if (e.getIntent().getAction().equals(Intent.ACTION_PACKAGE_CHANGED)) {
            listener.onNewModuleUpdate(pkgName, apkPath);
        }
    }

    interface OnModuleInstalledListener {
        void onNewModuleInstalled(String pkgName, String path);

        void onNewModuleUpdate(String pkgName, String path);
    }
}
