package github.tornaco.practice.honeycomb;

import github.tornaco.practice.honeycomb.data.IPreferenceManager;
import github.tornaco.practice.honeycomb.am.IActivityManager;
import github.tornaco.practice.honeycomb.device.IPowerManager;
import github.tornaco.practice.honeycomb.pm.IPackageManager;

interface IHoneyComb {
    // Base info
    String getVersion();
    int getStatus();

    // Service hub
    void addService(String name, IBinder binder);
    void deleteService(String name);
    IBinder getService(String name);
    boolean hasService(String name);

    IPreferenceManager getPreferenceManager(String packageName);
    IActivityManager getActivityManager();
    IPowerManager getPowerManager();
    IPackageManager getPackageManager();
}
