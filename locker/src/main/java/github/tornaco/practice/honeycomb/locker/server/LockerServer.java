package github.tornaco.practice.honeycomb.locker.server;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.os.UserHandle;
import android.text.TextUtils;

import org.newstand.logger.Logger;

import java.io.File;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import androidx.annotation.GuardedBy;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import github.tornaco.practice.honeycomb.app.AbstractSafeR;
import github.tornaco.practice.honeycomb.app.HoneyCombContext;
import github.tornaco.practice.honeycomb.data.PreferenceManager;
import github.tornaco.practice.honeycomb.data.RepoFactory;
import github.tornaco.practice.honeycomb.data.i.SetRepo;
import github.tornaco.practice.honeycomb.event.Event;
import github.tornaco.practice.honeycomb.event.IEventSubscriber;
import github.tornaco.practice.honeycomb.locker.ILocker;
import github.tornaco.practice.honeycomb.locker.ILockerWatcher;
import github.tornaco.practice.honeycomb.locker.app.LockerContext;
import github.tornaco.practice.honeycomb.locker.server.verify.Verifier;
import github.tornaco.practice.honeycomb.locker.server.verify.VerifyCallback;
import github.tornaco.practice.honeycomb.locker.server.verify.VerifyResult;
import github.tornaco.practice.honeycomb.locker.util.KeyStoreUtils;
import github.tornaco.practice.honeycomb.util.HandlerUtils;
import github.tornaco.practice.honeycomb.util.PreconditionUtils;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import static github.tornaco.practice.honeycomb.locker.app.LockerContext.LockerKeys.KEY_LOCKER_ENABLED;
import static github.tornaco.practice.honeycomb.locker.app.LockerContext.LockerMethod.PIN;

public class LockerServer extends ILocker.Stub implements Verifier {

    private static final AtomicInteger S_REQ = new AtomicInteger(0);
    @Getter
    private Context systemContext;
    @Getter
    private HoneyCombContext honeyCombContext;
    private Handler h;
    private AtomicBoolean lockerEnabled = new AtomicBoolean(false);
    private SetRepo<String> lockAppRepo;
    @SuppressLint("UseSparseArrays")
    @GuardedBy("ConcurrentHashMap")
    private final Map<Integer, VerifyRecord> verifyRecords = new ConcurrentHashMap<>();
    private final Set<String> verifiedPackages = new HashSet<>();
    private final RemoteCallbackList<ILockerWatcher> watcherRemoteCallbackList
            = new RemoteCallbackList<>();

    private final IEventSubscriber systemEventSubscriber = new IEventSubscriber.Stub() {
        @Override
        public void onEvent(Event e) {
            Logger.i("onEvent: %s @%s", e, Thread.currentThread().getName());
            if (Intent.ACTION_SCREEN_OFF.equals(e.getIntent().getAction())) {
                h.post(new AbstractSafeR() {
                    @Override
                    public void runSafety() {
                        onScreenStateChange(false);
                    }
                });
            }
            if (Intent.ACTION_SCREEN_ON.equals(e.getIntent().getAction())) {
                h.post(new AbstractSafeR() {
                    @Override
                    public void runSafety() {
                        onScreenStateChange(true);
                    }
                });
            }
            if (Event.ACTION_TASK_REMOVED.equals(e.getIntent().getAction())) {
                h.post(new AbstractSafeR() {
                    @Override
                    public void runSafety() {
                        Bundle extra = e.getIntent().getExtras();
                        if (extra != null) {
                            String pkgName = extra.getString("pkg");
                            if (pkgName != null) {
                                onTaskRemoved(pkgName);
                            }
                        }
                    }
                });
            }
            if (Event.ACTION_FRONT_UI_APP_CHANGED.equals(e.getIntent().getAction())) {
                h.post(new AbstractSafeR() {
                    @Override
                    public void runSafety() {
                        Bundle extra = e.getIntent().getExtras();
                        if (extra != null) {
                            String from = extra.getString("from");
                            String to = extra.getString("to");
                            onFrontUIAppChanged(from, to);
                        }
                    }
                });
            }
        }
    };

    LockerServer() {
        h = HandlerUtils.newHandlerOfNewThread("LockerServer");
    }

    void onStart(Context systemContext, HoneyCombContext honeyCombContext) {
        this.systemContext = systemContext;
        this.honeyCombContext = honeyCombContext;
        PreferenceManager preferenceManager = honeyCombContext.getPreferenceManager();
        this.lockerEnabled.set(preferenceManager.getBoolean(KEY_LOCKER_ENABLED,
                LockerContext.LockerConfigs.DEF_LOCKER_ENABLED));
        Logger.i("LockerServer start, lock enabled? %s", lockerEnabled.get());
        this.lockAppRepo = RepoFactory.get().getOrCreateStringSetRepo(getAppRepoFile().getPath());
    }

    void systemReady() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
        intentFilter.addAction(Intent.ACTION_SCREEN_ON);
        intentFilter.addAction(Event.ACTION_TASK_REMOVED);
        intentFilter.addAction(Event.ACTION_FRONT_UI_APP_CHANGED);
        honeyCombContext.registerEventSubscriber(intentFilter, systemEventSubscriber);
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
    public boolean shouldVerify(@Nullable ComponentName componentName, @NonNull String pkg, String source) {
        return isEnabled()
                && isLockerKeySet(getLockerMethod())
                && !isLockVerifyActivity(componentName)
                && !verifiedPackages.contains(pkg)
                && lockAppRepo.has(pkg);
    }

    @Override
    public void verify(Bundle options, String pkg, ComponentName componentName,
                       int uid, int pid, VerifyCallback callback) {
        VerifyRecord record = VerifyRecord.builder()
                .pid(pid)
                .uid(uid)
                .pkg(pkg)
                .requestCode(allocateRequestCode())
                .verifyCallback(callback)
                .componentName(componentName)
                .build();
        Intent intent = new Intent(LockerContext.LockerIntents.LOCKER_VERIFY_ACTION);
        intent.putExtra(LockerContext.LockerIntents.LOCKER_VERIFY_EXTRA_PACKAGE, pkg);
        intent.putExtra(LockerContext.LockerIntents.LOCKER_VERIFY_EXTRA_REQUEST_CODE, record.requestCode);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        verifyRecords.put(record.requestCode, record);
        systemContext.startActivityAsUser(intent, options, UserHandle.of(UserHandle.getCallingUserId()));
    }

    @Override
    public void setVerifyResult(int request, int result, int reason) {
        if (verifyRecords.containsKey(request)) {
            VerifyRecord record = verifyRecords.remove(request);
            if (record == null) {
                return;
            }
            if (result == VerifyResult.PASS) {
                verifiedPackages.add(record.pkg);
            }
            notifyVerifyCallback(record, result, reason);
            Logger.v("setVerifyResult %s %s %s", request, result, reason);
        } else {
            Logger.e("No such request %s", request);
        }
    }

    @Override
    public void setLockerMethod(int method) {
        PreconditionUtils.checkState(method == LockerContext.LockerMethod.PATTERN
                || method == PIN, "Invalid method");
        PreferenceManager preferenceManager = honeyCombContext.getPreferenceManager();
        preferenceManager.putInt(LockerContext.LockerKeys.KEY_LOCKER_METHOD, method);
    }

    @Override
    public int getLockerMethod() {
        PreferenceManager preferenceManager = honeyCombContext.getPreferenceManager();
        return preferenceManager.getInt(LockerContext.LockerKeys.KEY_LOCKER_METHOD, LockerContext.LockerConfigs.DEF_LOCKER_METHOD);
    }

    @Override
    public void setLockerKey(int method, String key) {
        PreferenceManager preferenceManager = honeyCombContext.getPreferenceManager();
        preferenceManager.putString(LockerContext.LockerKeys.KEY_LOCKER_KEY_PREFIX + method, KeyStoreUtils.encryptString(key));
    }

    @Override
    public boolean isLockerKeyValid(int method, String key) {
        PreferenceManager preferenceManager = honeyCombContext.getPreferenceManager();
        return key.equals(KeyStoreUtils.decryptString(
                preferenceManager.getString(
                        LockerContext.LockerKeys.KEY_LOCKER_KEY_PREFIX + method, null)));
    }

    @Override
    public boolean isLockerKeySet(int method) {
        PreferenceManager preferenceManager = honeyCombContext.getPreferenceManager();
        return !TextUtils.isEmpty(preferenceManager.getString(
                LockerContext.LockerKeys.KEY_LOCKER_KEY_PREFIX + method, null));
    }

    private boolean isLockVerifyActivity(ComponentName name) {
        return name != null
                && LockerContext.LockerIntents.LOCKER_VERIFY_CLASS_NAME
                .equals(name.getClassName());
    }

    private void notifyVerifyCallback(VerifyRecord record, int result, int reason) {
        PreferenceManager preferenceManager = honeyCombContext.getPreferenceManager();
        boolean workaround = preferenceManager.getBoolean(
                LockerContext.LockerKeys.KEY_VERIFY_RES_WORKAROUND_ENABLED,
                LockerContext.LockerConfigs.DEF_VERIFY_RES_WORKAROUND_ENABLED);
        long delay = workaround ? preferenceManager.getLong(
                LockerContext.LockerKeys.KEY_VERIFY_RES_WORKAROUND_DELAY,
                LockerContext.LockerConfigs.DEF_VERIFY_RES_WORKAROUND_DELAY)
                : 0;
        Logger.v("notifyVerifyCallback with delay %s", delay);
        h.postDelayed(new AbstractSafeR() {
            @Override
            public void runSafety() {
                record.verifyCallback.onVerifyResult(result, reason);
            }
        }, delay);
    }

    private void notifyWatcher() {
        int n = watcherRemoteCallbackList.beginBroadcast();
        for (int i = 0; i < n; i++) {
            ILockerWatcher watcher = watcherRemoteCallbackList.getBroadcastItem(i);
            try {
                watcher.onEnableStateChanged(isEnabled());
            } catch (RemoteException e) {
                Logger.e(e, "notifyWatcher item err");
            }
        }
        watcherRemoteCallbackList.finishBroadcast();
    }

    private void onScreenStateChange(boolean on) {
        Logger.v("onScreenStateChange %s", on);
        boolean verifyOnScreenOff = honeyCombContext.getPreferenceManager()
                .getBoolean(LockerContext.LockerKeys.KEY_RE_VERIFY_ON_SCREEN_OFF,
                        LockerContext.LockerConfigs.DEF_RE_VERIFY_ON_SCREEN_OFF);
        if (verifyOnScreenOff) {
            Logger.v("clear verifiedPackages %s", "SCREEN OFF");
            verifiedPackages.clear();
        }
    }

    private void onTaskRemoved(String pkgName) {
        Logger.v("onTaskRemoved %s", pkgName);
        boolean verifyOnTaskRemoved = honeyCombContext.getPreferenceManager()
                .getBoolean(LockerContext.LockerKeys.KEY_RE_VERIFY_ON_TASK_REMOVED,
                        LockerContext.LockerConfigs.DEF_RE_VERIFY_ON_TASK_REMOVED);
        if (verifyOnTaskRemoved) {
            Logger.v("clear verifiedPackages %s", "TASK REMOVAL");
            verifiedPackages.remove(pkgName);
        }
    }

    private void onFrontUIAppChanged(String from, String to) {
        Logger.v("onFrontUIAppChanged %s %s", from, to);
        boolean verifyOnAppSwitch = honeyCombContext.getPreferenceManager()
                .getBoolean(LockerContext.LockerKeys.KEY_RE_VERIFY_ON_APP_SWITCH,
                        LockerContext.LockerConfigs.DEF_RE_VERIFY_ON_APP_SWITCH);
        if (verifyOnAppSwitch) {
            Logger.v("clear verifiedPackages %s", "APP SWITCH");
            verifiedPackages.remove(from);
        }
    }

    private static File getAppRepoFile() {
        return new File(getBaseDataDir(), "lock_apps");
    }

    private static File getBaseDataDir() {
        File systemFile = new File(Environment.getDataDirectory(), "system");
        return new File(systemFile, "locker");
    }

    private static int allocateRequestCode() {
        return S_REQ.getAndIncrement();
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
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            VerifyRecord that = (VerifyRecord) o;
            return requestCode == that.requestCode;
        }

        @Override
        public int hashCode() {
            return Objects.hash(requestCode);
        }
    }


}
