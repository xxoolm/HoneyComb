package github.tornaco.practice.honeycomb.core.server.notification;

import java.util.concurrent.atomic.AtomicInteger;

public class NotificationIdFactory {

    private static final AtomicInteger sNotiId = new AtomicInteger(0);

    private NotificationIdFactory() {
    }

    public static int allocateNotificationId() {
        return sNotiId.getAndIncrement();
    }
}
