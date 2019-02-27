package github.tornaco.practice.honeycomb.core.server.notification;

import android.app.Notification;
import android.os.Bundle;

import androidx.annotation.DrawableRes;
import androidx.core.app.NotificationCompat;

public class NotificationHelper {
    @DrawableRes
    public static final int DEFAULE_NOTIFICATION_ICON = android.R.drawable.stat_sys_warning;

    public static void overrideNotificationAppName(Notification.Builder n, String name) {
        final Bundle extras = new Bundle();
        extras.putString(Notification.EXTRA_SUBSTITUTE_APP_NAME, name);
        n.addExtras(extras);
    }

    public static void overrideNotificationAppName(NotificationCompat.Builder n, String name) {
        final Bundle extras = new Bundle();
        extras.putString(Notification.EXTRA_SUBSTITUTE_APP_NAME, name);
        n.addExtras(extras);
    }
}
