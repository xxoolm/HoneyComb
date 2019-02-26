package github.tornaco.practice.honeycomb.core.server.notification;

import android.app.Notification;
import android.os.Bundle;

import androidx.core.app.NotificationCompat;

public class NotificationHelper {

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
