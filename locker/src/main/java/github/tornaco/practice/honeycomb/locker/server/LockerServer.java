package github.tornaco.practice.honeycomb.locker.server;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.RemoteCallbackList;
import android.os.RemoteException;

import org.newstand.logger.Logger;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import github.tornaco.practice.honeycomb.app.HoneyCombContext;
import github.tornaco.practice.honeycomb.data.PreferenceManager;
import github.tornaco.practice.honeycomb.data.RepoFactory;
import github.tornaco.practice.honeycomb.data.i.SetRepo;
import github.tornaco.practice.honeycomb.locker.ILocker;
import github.tornaco.practice.honeycomb.locker.ILockerWatcher;
import github.tornaco.practice.honeycomb.locker.app.LockerContext;
import github.tornaco.practice.honeycomb.locker.server.verify.Verifier;
import github.tornaco.practice.honeycomb.locker.server.verify.VerifyCallback;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import static github.tornaco.practice.honeycomb.locker.app.LockerKeys.KEY_LOCKER_ENABLED;

public class LockerServer extends ILocker.Stub implements Verifier {

    private static final AtomicInteger sReq = new AtomicInteger(0);
    @Getter
    private Context systemContext;
    @Getter
    private HoneyCombContext honeyCombContext;
    private AtomicBoolean lockerEnabled = new AtomicBoolean(false);
    private SetRepo<String> lockAppRepo;
    @SuppressLint("UseSparseArrays")
    private final Map<Integer, VerifyRecord> verifyRecords = new HashMap<>();
    private final Set<ComponentName> verifiedComponents = new HashSet<>();

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
        this.lockAppRepo = RepoFactory.get().getOrCreateStringSetRepo(getAppRepoFile().getPath());
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
        return this.lockAppRepo.has(pkg);
    }

    @Override
    public void setPackageLocked(String pkg, boolean locked) {
        Logger.v("setPackageLocked %s %s", pkg, locked);
        if (locked) {
            this.lockAppRepo.add(pkg);
        } else {
            this.lockAppRepo.remove(pkg);
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
    public boolean shouldVerify(ComponentName componentName, String source) {
        return !verifiedComponents.contains(componentName)
                && lockAppRepo.has(componentName.getPackageName());
    }

    @Override
    public void verify(Bundle options, String pkg, ComponentName componentName, int uid, int pid, VerifyCallback callback) {
        VerifyRecord record = VerifyRecord.builder()
                .pid(pid)
                .uid(uid)
                .pkg(pkg)
                .requestCode(allocateRequestCode())
                .verifyCallback(callback)
                .componentName(componentName)
                .build();
        Intent intent = new Intent(LockerContext.LOCKER_VERIFY_ACTION);
        intent.putExtra(LockerContext.LOCKER_VERIFY_EXTRA_PACKAGE, pkg);
        intent.putExtra(LockerContext.LOCKER_VERIFY_EXTRA_REQUEST_CODE, record.requestCode);
        verifyRecords.put(record.requestCode, record);
        systemContext.startActivity(intent);
    }

    @Override
    public void setVerifyResult(int request, int result, int reason) {
        if (verifyRecords.containsKey(request)) {
            VerifyRecord record = verifyRecords.remove(request);
            verifiedComponents.add(record.componentName);
            record.verifyCallback.onVerifyResult(result, reason);
            //noinspection UnusedAssignment
            record = null;
            Logger.v("setVerifyResult %s %s %s", request, result, reason);
        } else {
            Logger.e("No such request %s", request);
        }
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

    private static int allocateRequestCode() {
        return sReq.getAndIncrement();
    }

    @Builder
    @Getter
    @ToString
    public static class VerifyRecord {
        public VerifyCallback verifyCallback;
        public int uid;
        public int pid;
        public int requestCode;
        public String pkg;
        public ComponentName componentName;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            VerifyRecord that = (VerifyRecord) o;
            return requestCode == that.requestCode;
        }

        @Override
        public int hashCode() {
            return Objects.hash(requestCode);
        }
    }


}
