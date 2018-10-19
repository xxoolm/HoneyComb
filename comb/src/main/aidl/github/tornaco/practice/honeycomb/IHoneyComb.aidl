// IHoneyComb.aidl
package github.tornaco.practice.honeycomb;

interface IHoneyComb {
    void addService(String name, IBinder binder);
    void deleteService(String name);
    IBinder getService(String name);
    boolean hasService(String name);
}
