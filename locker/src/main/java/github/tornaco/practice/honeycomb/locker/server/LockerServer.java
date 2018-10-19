package github.tornaco.practice.honeycomb.locker.server;

import android.content.Context;
import android.os.RemoteException;

import github.tornaco.practice.honeycomb.locker.ILocker;
import lombok.Getter;

public class LockerServer extends ILocker.Stub {

    @Getter
    private Context systemContext;

    LockerServer(Context systemContext) {
        this.systemContext = systemContext;
    }

    @Override
    public void setEnabled() throws RemoteException {

    }

    @Override
    public boolean isEnabled() throws RemoteException {
        return false;
    }

    @Override
    public boolean isPackageLocked(String pkg) throws RemoteException {
        return false;
    }

    @Override
    public void setPackageLocked(String pkg, boolean locked) throws RemoteException {

    }
}
