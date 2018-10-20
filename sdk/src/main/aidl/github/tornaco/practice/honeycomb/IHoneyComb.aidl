// IHoneyComb.aidl
package github.tornaco.practice.honeycomb;

import github.tornaco.practice.honeycomb.IPreferenceManager;

interface IHoneyComb {
    // Base info
    String getVersion();
    int getStatus();

    // Service hub
    void addService(String name, IBinder binder);
    void deleteService(String name);
    IBinder getService(String name);
    boolean hasService(String name);

    // Pref
    IPreferenceManager getPreferenceManager(String packageName);
}
