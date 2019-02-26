package github.tornaco.practice.honeycomb.core.server.pm;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.RemoteException;

import org.newstand.logger.Logger;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import github.tornaco.practice.honeycomb.BuildConfig;
import github.tornaco.practice.honeycomb.app.AppResources;
import github.tornaco.practice.honeycomb.app.HoneyCombContext;
import github.tornaco.practice.honeycomb.core.server.build.Drawables;
import github.tornaco.practice.honeycomb.core.server.build.FWStrings;
import github.tornaco.practice.honeycomb.core.server.data.PreferenceManagerService;
import github.tornaco.practice.honeycomb.core.server.event.EventBus;
import github.tornaco.practice.honeycomb.core.server.i.SystemService;
import github.tornaco.practice.honeycomb.core.server.notification.NotificationHelper;
import github.tornaco.practice.honeycomb.core.server.notification.NotificationIdFactory;
import github.tornaco.practice.honeycomb.pm.IModuleManager;
import github.tornaco.practice.honeycomb.util.OSUtils;

public class ModuleManagerService extends IModuleManager.Stub
        implements SystemService, PackageChangeListener.OnModuleInstalledListener {

    private static final String NOTIFICATION_ID_MODULE_INSTALLED = "comb.notification.module.installed";

    private Context context;
    private PreferenceManagerService preferenceManagerService;

    @Override
    public void onStart(Context context) {
        this.context = context;
        this.preferenceManagerService = new PreferenceManagerService(HoneyCombContext.HoneyCombConfigs.ENABLED_BEE_MODULES_PREF_NAME);
    }

    @Override
    public void onSystemReady() {
        registerReceivers();
    }

    @Override
    public void onShutDown() {

    }

    @Override
    public boolean isModuleActivated(String pkgName) throws RemoteException {
        return this.preferenceManagerService.getString(pkgName, null) != null;
    }

    @Override
    public void setModuleActive(String pkgName) throws RemoteException {
        enforceCallingPermissions();
        PackageManager pm = context.getPackageManager();
        try {
            ApplicationInfo applicationInfo = pm.getApplicationInfo(pkgName, PackageManager.GET_META_DATA);
            preferenceManagerService.putString(pkgName, applicationInfo.publicSourceDir);
        } catch (PackageManager.NameNotFoundException e) {
            Logger.e("setModuleActive NameNotFoundException %s", e);
        }
    }

    private void enforceCallingPermissions() {
        // TODO: 2019/2/24 Check perm.
    }

    private void registerReceivers() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_PACKAGE_ADDED);
        intentFilter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        intentFilter.addAction(Intent.ACTION_PACKAGE_CHANGED);
        intentFilter.addDataScheme("package");
        EventBus.getInstance().registerEventSubscriber(intentFilter, new PackageChangeListener(context, this));
    }

    @Override
    public void onNewModuleInstalled(String pkgName, String path) {
        showNewModuleInstalledNotification(pkgName, path);
    }

    private void showNewModuleInstalledNotification(String pkgName, String path) {
        Logger.d("showNewModuleInstalledNotification %s %s", pkgName, path);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, NOTIFICATION_ID_MODULE_INSTALLED);
        AppResources appResources = new AppResources(context, BuildConfig.APPLICATION_ID);
        NotificationHelper.overrideNotificationAppName(builder, appResources.getString(FWStrings.NOTIFICATION_OVERRIDE_MODULE_MANAGER));

        Intent disableBroadcastIntent = new Intent("");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                NotificationIdFactory.allocateNotificationId(),
                disableBroadcastIntent,
                0);

        Notification n = builder
                .addAction(0, "立即重启", pendingIntent)
                .setContentTitle("需要重启")
                .setContentText("你现在需要重启你的设备已完成更新。")
                .setSmallIcon(android.R.drawable.stat_sys_warning)
                .build();

        if (OSUtils.isMOrAbove()) {
            n.setSmallIcon(appResources.getIcon(Drawables.IC_INFO_WHITE_24DP));
        }

        NotificationManagerCompat.from(context).notify(NotificationIdFactory.allocateNotificationId(), n);
    }
}
