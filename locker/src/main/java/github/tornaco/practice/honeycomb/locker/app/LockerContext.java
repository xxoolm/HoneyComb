package github.tornaco.practice.honeycomb.locker.app;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public interface LockerContext {
    String LOCKER_SERVICE = "locker";
    String LOCKER_VERIFY_ACTION = "github.tornaco.practice.honeycomb.locker.action.VERIFY";
    String LOCKER_VERIFY_EXTRA_PACKAGE = "pkg";
    String LOCKER_VERIFY_EXTRA_REQUEST_CODE = "request_code";

    @Nullable
    LockerManager getLockerManager();

    @NonNull
    static LockerContext createContext() {
        return new LockerContextImpl();
    }
}
