package github.tornaco.practice.honeycomb.core.server.event;

import android.content.IntentFilter;
import android.os.RemoteCallbackList;
import android.os.RemoteException;

import org.newstand.logger.Logger;

import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import github.tornaco.practice.honeycomb.event.Event;
import github.tornaco.practice.honeycomb.event.IEventSubscriber;
import github.tornaco.practice.honeycomb.util.PreconditionUtils;
import github.tornaco.practice.honeycomb.util.Singleton;

public class EventBus {
    private final Executor eventPublishExecutor = new ThreadPoolExecutor(1, 1,
            0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>(),
            r -> new Thread(r, "Comb-publish"));
    private final RemoteCallbackList<EventSubscriberClient> eventSubscribers = new RemoteCallbackList<>();
    private static final Singleton<EventBus> BUS = new Singleton<EventBus>() {
        @Override
        protected EventBus create() {
            return new EventBus();
        }
    };

    public static EventBus getInstance() {
        return BUS.get();
    }

    public void publishEventToSubscribersAsync(Event event) {
        eventPublishExecutor.execute(() -> publishEventToSubscribers(event));
    }

    public void publishEventToSubscribers(Event event) {
        int itemCount = eventSubscribers.beginBroadcast();
        try {
            for (int i = 0; i < itemCount; ++i) {
                try {
                    EventSubscriberClient c = eventSubscribers.getBroadcastItem(i);
                    if (c.hasAction(event.getAction())) {
                        c.onEvent(event);
                    }
                } catch (RemoteException e) {
                    Logger.e(e, "publishEventToSubscriber %s", event);
                }
            }
        } finally {
            eventSubscribers.finishBroadcast();
        }
    }

    public void registerEventSubscriber(IntentFilter filter, IEventSubscriber subscriber) {
        PreconditionUtils.checkNotNull(subscriber, "subscriber is null");
        PreconditionUtils.checkNotNull(filter, "filter is null");
        eventSubscribers.register(new EventSubscriberClient(filter, subscriber));
    }

    public void unRegisterEventSubscriber(IEventSubscriber subscriber) {
        PreconditionUtils.checkNotNull(subscriber, "subscriber is null");
        eventSubscribers.unregister(new EventSubscriberClient(null, subscriber));
    }
}
