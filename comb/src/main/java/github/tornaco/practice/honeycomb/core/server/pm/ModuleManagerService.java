package github.tornaco.practice.honeycomb.core.server.pm;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.PowerManager;
import android.os.RemoteException;

import org.newstand.logger.Logger;

import java.util.Objects;

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
import github.tornaco.practice.honeycomb.core.server.os.SystemServerThread;
import github.tornaco.practice.honeycomb.event.Event;
import github.tornaco.practice.honeycomb.event.IEventSubscriber;
import github.tornaco.practice.honeycomb.pm.IModuleManager;
import github.tornaco.practice.honeycomb.util.OSUtils;

public class ModuleManagerService extends IModuleManager.Stub
        implements SystemService, PackageChangeListener.OnModuleInstalledListener {

    private static final String NOTIFICATION_ID_MODULE_INSTALLED = "comb.notification.module.installed";
    private static final String ACTION_ACTIVATE_MODULE = "comb.action.activate.module";
    private static final String ACTION_ACTIVATE_MODULE_AND_REBOOT = "comb.action.activate.module.reboot";

    private Context context;
    private PreferenceManagerService preferenceManagerService;

    @Override
    public void onStart(Context context) {
        this.context = context;
        this.preferenceManagerService = new PreferenceManagerService(HoneyCombContext.HoneyCombConfigs.ENABLED_BEE_MODULES_PREF_NAME);
    }

    @Override
    public void onSystemReady() {
        registerPackageReceivers();
        registerActionReceivers();
    }

    @Override
    public void onShutDown() {

    }

    @Override
    public boolean isModuleActivated(String pkgName) throws RemoteException {
        return this.preferenceManagerService.getString(pkgName, null) != null;
    }

    @Override
    public void setModuleActive(String pkgName, boolean active) throws RemoteException {
        enforceCallingPermissions();
        PackageManager pm = context.getPackageManager();
        try {
            ApplicationInfo applicationInfo = pm.getApplicationInfo(pkgName, PackageManager.GET_META_DATA);
            preferenceManagerService.putString(pkgName, active ? applicationInfo.publicSourceDir : null);
        } catch (PackageManager.NameNotFoundException e) {
            Logger.e("setModuleActive NameNotFoundException %s", e);
        }
    }

    private void enforceCallingPermissions() {
        // TODO: 2019/2/24 Check perm.
    }

    private void registerPackageReceivers() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_PACKAGE_ADDED);
        intentFilter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        intentFilter.addAction(Intent.ACTION_PACKAGE_CHANGED);
        intentFilter.addDataScheme("package");
        EventBus.getInstance().registerEventSubscriber(intentFilter, new PackageChangeListener(context, this));
    }

    private void registerActionReceivers() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_ACTIVATE_MODULE);
        intentFilter.addAction(ACTION_ACTIVATE_MODULE_AND_REBOOT);
        EventBus.getInstance().registerEventSubscriber(intentFilter, new IEventSubscriber.Stub() {
            @Override
            public void onEvent(Event e) throws RemoteException {
                Logger.d("registerActionReceivers onEvent %s", e);
                if (ACTION_ACTIVATE_MODULE.equals(e.getIntent().getAction())) {
                    Intent intent = e.getIntent();
                    String pkgName = intent.getStringExtra("package_name");
                    String path = intent.getStringExtra("path");
                    setModuleActive(pkgName, true);
                } else if (ACTION_ACTIVATE_MODULE_AND_REBOOT.equals(e.getIntent().getAction())) {
                    SystemServerThread.getHandler().post(new Runnable() {
                        @Override
                        public void run() {
                            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
                            Objects.requireNonNull(pm).reboot(null);
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onNewModuleInstalled(String pkgName, String path) {
        SystemServerThread.getHandler().post(new Runnable() {
            @Override
            public void run() {
                showNewModuleInstalledNotification(pkgName, path);
            }
        });
    }

    @Override
    public void onNewModuleUpdate(String pkgName, String path) {
        SystemServerThread.getHandler().post(new Runnable() {
            @Override
            public void run() {
                showNewModuleInstalledNotification(pkgName, path);
            }
        });
    }

    private void showNewModuleInstalledNotification(String pkgName, String path) {
        Logger.d("showNewModuleInstalledNotification %s %s", pkgName, path);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, NOTIFICATION_ID_MODULE_INSTALLED);
        AppResources appResources = new AppResources(context, BuildConfig.APPLICATION_ID);
        NotificationHelper.overrideNotificationAppName(builder, appResources.getString(FWStrings.NOTIFICATION_OVERRIDE_MODULE_MANAGER));
        Intent combAppIntent = context.getPackageManager().getLaunchIntentForPackage(BuildConfig.APPLICATION_ID);
        Notification n = builder
                .setContentTitle(appResources.getString(FWStrings.NOTIFICATION_TITLE_MODULE_INSTALLED))
                .setContentText(appResources.getString(FWStrings.NOTIFICATION_CONTENT_MODULE_INSTALLED))
                .setSmallIcon(NotificationHelper.DEFAULE_NOTIFICATION_ICON)
                .setContentIntent(PendingIntent.getActivity(context, NotificationIdFactory.allocateNotificationId(), combAppIntent, 0))
                .setAutoCancel(true)
                .build();

        if (OSUtils.isMOrAbove()) {
            n.setSmallIcon(appResources.getIcon(Drawables.IC_INFO_WHITE_24DP));
        }
        NotificationManagerCompat.from(context).notify(NotificationIdFactory.allocateNotificationId(), n);
    }
}
