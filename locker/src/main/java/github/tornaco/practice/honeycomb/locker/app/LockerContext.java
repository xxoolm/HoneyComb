package github.tornaco.practice.honeycomb.locker.app;

public interface LockerContext {
    String LOCKER_SERVICE = "locker";

    LockerManager getLockerManager();

    static LockerContext createContext() {
        return new LockerContextImpl();
    }
}
