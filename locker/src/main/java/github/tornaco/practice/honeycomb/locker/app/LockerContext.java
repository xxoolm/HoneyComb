package github.tornaco.practice.honeycomb.locker.app;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public interface LockerContext {
    String LOCKER_SERVICE = "locker";

    @Nullable
    LockerManager getLockerManager();

    @NonNull
    static LockerContext createContext() {
        return new LockerContextImpl();
    }
}
