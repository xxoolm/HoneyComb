package github.tornaco.practice.honeycomb.locker.server;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.RemoteCallbackList;
import android.os.RemoteException;

import org.newstand.logger.Logger;

import java.io.File;
import java.util.concurrent.atomic.AtomicBoolean;

import github.tornaco.practice.honeycomb.app.HoneyCombContext;
import github.tornaco.practice.honeycomb.data.PreferenceManager;
import github.tornaco.practice.honeycomb.data.RepoFactory;
import github.tornaco.practice.honeycomb.data.i.SetRepo;
import github.tornaco.practice.honeycomb.locker.ILocker;
import github.tornaco.practice.honeycomb.locker.ILockerWatcher;
import github.tornaco.practice.honeycomb.locker.server.verify.Verifier;
import github.tornaco.practice.honeycomb.locker.server.verify.VerifyCallback;
import github.tornaco.practice.honeycomb.locker.server.verify.VerifyResult;
import lombok.Getter;

import static github.tornaco.practice.honeycomb.locker.app.LockerKeys.KEY_LOCKER_ENABLED;

public class LockerServer extends ILocker.Stub implements Verifier {

    @Getter
    private Context systemContext;
    @Getter
    private HoneyCombContext honeyCombContext;
    private AtomicBoolean lockerEnabled = new AtomicBoolean(false);
    private SetRepo<String> appRepo;

    private final RemoteCallbackList<ILockerWatcher> watcherRemoteCallbackList
            = new RemoteCallbackList<>();

    LockerServer() {
    }

    void onStart(Context systemContext, HoneyCombContext honeyCombContext) {
        this.systemContext = systemContext;
        this.honeyCombContext = honeyCombContext;
        PreferenceManager preferenceManager = honeyCombContext.getPreferenceManager();
        this.lockerEnabled.set(preferenceManager.getBoolean(KEY_LOCKER_ENABLED, false));
        Logger.i("LockerServer start, lock enabled? %s", lockerEnabled.get());
        this.appRepo = RepoFactory.get().getOrCreateStringSetRepo(getAppRepoFile().getPath());
    }

    void systemReady() {
    }

    @Override
    public void setEnabled(boolean enabled) {
        if (lockerEnabled.compareAndSet(!enabled, enabled)) {
            PreferenceManager preferenceManager = honeyCombContext.getPreferenceManager();
            if (preferenceManager != null) {
                preferenceManager.putBoolean(KEY_LOCKER_ENABLED, enabled);
            }
            notifyWatcher();
        }
    }

    @Override
    public boolean isEnabled() {
        return lockerEnabled.get();
    }

    @Override
    public void addWatcher(ILockerWatcher w) {
        watcherRemoteCallbackList.register(w);
    }

    @Override
    public void deleteWatcher(ILockerWatcher w) {
        watcherRemoteCallbackList.unregister(w);
    }

    @Override
    public boolean isPackageLocked(String pkg) {
        return this.appRepo.has(pkg);
    }

    @Override
    public void setPackageLocked(String pkg, boolean locked) {
        Logger.v("setPackageLocked %s %s", pkg, locked);
        if (locked) {
            this.appRepo.add(pkg);
        } else {
            this.appRepo.remove(pkg);
        }
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

    private void notifyWatcher() {
        int N = watcherRemoteCallbackList.beginBroadcast();
        for (int i = 0; i < N; i++) {
            ILockerWatcher watcher = watcherRemoteCallbackList.getBroadcastItem(i);
            try {
                watcher.onEnableStateChanged(isEnabled());
            } catch (RemoteException e) {
                Logger.e(e, "notifyWatcher item err");
            }
        }
        watcherRemoteCallbackList.finishBroadcast();
    }

    private static File getAppRepoFile() {
        return new File(getBaseDataDir(), "lock_apps");
    }

    private static File getBaseDataDir() {
        File systemFile = new File(Environment.getDataDirectory(), "system");
        return new File(systemFile, "locker");
    }
}
