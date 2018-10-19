// IHoneyComb.aidl
package github.tornaco.practice.honeycomb;

interface IHoneyComb {
    // Service hub
    void addService(String name, IBinder binder);
    void deleteService(String name);
    IBinder getService(String name);
    boolean hasService(String name);
}
