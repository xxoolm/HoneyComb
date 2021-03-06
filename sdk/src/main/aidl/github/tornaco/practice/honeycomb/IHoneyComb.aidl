package github.tornaco.practice.honeycomb;

import github.tornaco.practice.honeycomb.event.IEventSubscriber;

interface IHoneyComb {
    // Base info
    String getVersion();
    int getStatus();

    // Service hub
    void addService(String name, IBinder binder);
    void deleteService(String name);
    IBinder getService(String name);
    boolean hasService(String name);

    void registerEventSubscriber(in IntentFilter filter, in IEventSubscriber subscriber);
    void unRegisterEventSubscriber(in IEventSubscriber subscriber);
}
