package github.tornaco.practice.honeycomb.locker.server;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.RemoteException;

import github.tornaco.practice.honeycomb.locker.ILocker;
import github.tornaco.practice.honeycomb.locker.server.verify.Verifier;
import github.tornaco.practice.honeycomb.locker.server.verify.VerifyCallback;
import github.tornaco.practice.honeycomb.locker.server.verify.VerifyResult;
import lombok.Getter;

public class LockerServer extends ILocker.Stub implements Verifier {

    @Getter
    private Context systemContext;

    LockerServer() {
    }

    void onStart(Context systemContext) {
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

    @Override
    public boolean isActivityStartShouldBeInterrupted(ComponentName componentName) {
        return false;
    }

    @Override
    public long wrapCallingUidForIntent(long ident, Intent intent) {
        return ident;
    }

    @Override
    public Intent getCheckedActivityIntent(Intent intent) {
        return intent;
    }

    @Override
    public boolean shouldVerify(String pkg, String source) {
        return true;
    }

    @Override
    public void verify(Bundle options, String pkg, ComponentName componentName, int uid, int pid, VerifyCallback callback) {
        callback.onVerifyResult(VerifyResult.PASS, "default");
    }
}
