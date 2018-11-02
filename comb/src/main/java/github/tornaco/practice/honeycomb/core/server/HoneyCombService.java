package github.tornaco.practice.honeycomb.core.server;

import android.app.IApplicationThread;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.os.ServiceManager;

import org.newstand.logger.Logger;

import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import androidx.annotation.Nullable;
import github.tornaco.practice.honeycomb.BuildConfig;
import github.tornaco.practice.honeycomb.IHoneyComb;
import github.tornaco.practice.honeycomb.am.IActivityManager;
import github.tornaco.practice.honeycomb.annotations.SystemProcess;
import github.tornaco.practice.honeycomb.app.HoneyCombContext;
import github.tornaco.practice.honeycomb.core.server.am.ActivityManagerService;
import github.tornaco.practice.honeycomb.core.server.data.PreferenceManagerService;
import github.tornaco.practice.honeycomb.core.server.device.PowerManagerService;
import github.tornaco.practice.honeycomb.core.server.i.HoneyComb;
import github.tornaco.practice.honeycomb.core.server.pm.PackageManagerService;
import github.tornaco.practice.honeycomb.data.IPreferenceManager;
import github.tornaco.practice.honeycomb.device.IPowerManager;
import github.tornaco.practice.honeycomb.event.Event;
import github.tornaco.practice.honeycomb.event.IEventSubscriber;
import github.tornaco.practice.honeycomb.pm.IPackageManager;
import github.tornaco.practice.honeycomb.util.PreconditionUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Delegate;

import static github.tornaco.practice.honeycomb.core.server.util.PkgUtils.PACKAGE_NAME_ANDROID;

public class HoneyCombService implements HoneyComb {

    @Getter
    private Context systemContext;
    @Getter
    private IActivityManager activityManager;
    @Getter
    private IPowerManager powerManager;
    @Getter
    private IPackageManager packageManager;
    private IPreferenceManager preferenceManager;
    private final Executor eventPublishExecutor = Executors.newSingleThreadExecutor();
    private final RemoteCallbackList<EventSubscriberClient> eventSubscribers = new RemoteCallbackList<>();

    @SystemProcess
    public void onStart(Context context) {
        Logger.w("HoneyCombService start with context %s", context);
        this.systemContext = context;
        this.activityManager = new ActivityManagerService();
        this.powerManager = new PowerManagerService();
        this.packageManager = new PackageManagerService(context);
        // TODO Split for diff pkg.
        this.preferenceManager = new PreferenceManagerService(PACKAGE_NAME_ANDROID);
        publish();
        publishInternal();
    }

    @SystemProcess
    public void systemReady() {

    }

    @SystemProcess
    public void shutDown() {

    }

    @Override
    public boolean allowBroadcastIntentSending(IApplicationThread applicationThread, Intent intent) {
        Logger.v("allowBroadcastIntentSending? %s %s", applicationThread, intent);
        if (intent.getAction() == null) {
            return true;
        }
        publishEventToSubscribersAsync(new Event(intent.getAction(), intent.getExtras()));
        return true;
    }

    private void registerEventSubscriber(IntentFilter filter, IEventSubscriber subscriber) {
        PreconditionUtils.checkNotNull(subscriber, "subscriber is null");
        PreconditionUtils.checkNotNull(filter, "filter is null");

        eventSubscribers.register(new EventSubscriberClient(filter, subscriber));
    }

    private void unRegisterEventSubscriber(IEventSubscriber subscriber) {
        PreconditionUtils.checkNotNull(subscriber, "subscriber is null");
        eventSubscribers.unregister(new EventSubscriberClient(null, subscriber));
    }

    @SystemProcess
    private void publish() {
        try {
            ServiceManager.addService(Context.TV_INPUT_SERVICE, new ServiceStub());
            Logger.w("HoneyCombService publish success %s", this.toString());
        } catch (Throwable e) {
            Logger.e(e, "Fail publish HoneyCombService");
        }
    }

    @SystemProcess
    private void publishInternal() {
        HoneyCombServiceManager.addService(HoneyCombContext.PACKAGE_MANAGER_SERVICE, packageManager.asBinder());
        HoneyCombServiceManager.addService(HoneyCombContext.PREFERENCE_MANAGER_SERVICE, preferenceManager.asBinder());
        HoneyCombServiceManager.addService(HoneyCombContext.ACTIVITY_MANAGER_SERVICE, activityManager.asBinder());
    }

    private void publishEventToSubscribersAsync(Event event) {
        eventPublishExecutor.execute(() -> publishEventToSubscribers(event));
    }

    private void publishEventToSubscribers(Event event) {
        int itemCount = eventSubscribers.beginBroadcast();
        try {
            for (int i = 0; i < itemCount; ++i) {
                try {
                    EventSubscriberClient c = eventSubscribers.getBroadcastItem(i);
                    if (c.hasAction(event.getAction())) c.onEvent(event);
                } catch (RemoteException e) {
                    Logger.e(e, "publishEventToSubscriber %s", event);
                }
            }
        } finally {
            eventSubscribers.finishBroadcast();
        }
    }

    private class ServiceStub extends IHoneyComb.Stub {
        @Override
        public String getVersion() {
            return BuildConfig.VERSION_NAME;
        }

        @Override
        public int getStatus() {
            return 0;
        }

        @Override
        public void addService(String name, IBinder binder) {
            HoneyCombServiceManager.addService(name, binder);
        }

        @Override
        public void deleteService(String name) {
            HoneyCombServiceManager.deleteService(name);
        }

        @Override
        public IBinder getService(String name) {
            return HoneyCombServiceManager.getService(name);
        }

        @Override
        public boolean hasService(String name) {
            return HoneyCombServiceManager.hasService(name);
        }

        @Override
        public void registerEventSubscriber(IntentFilter filter, IEventSubscriber subscriber) {
            HoneyCombService.this.registerEventSubscriber(filter, subscriber);
        }

        @Override
        public void unRegisterEventSubscriber(IEventSubscriber subscriber) {
            HoneyCombService.this.unRegisterEventSubscriber(subscriber);
        }
    }

    @AllArgsConstructor
    private class EventSubscriberClient extends IEventSubscriber.Stub {
        @Nullable
        private IntentFilter intentFilter;
        @Delegate
        private IEventSubscriber subscriber;

        public boolean hasAction(String action) {
            return intentFilter != null && intentFilter.hasAction(action);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            EventSubscriberClient that = (EventSubscriberClient) o;
            return Objects.equals(subscriber, that.subscriber);
        }

        @Override
        public int hashCode() {

            return Objects.hash(subscriber);
        }
    }
}
