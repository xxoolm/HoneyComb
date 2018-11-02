package github.tornaco.practice.honeycomb;

import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

import com.google.common.base.Optional;

import github.tornaco.practice.honeycomb.event.IEventSubscriber;
import lombok.SneakyThrows;

@SuppressWarnings("Guava")
public class HoneyCombManager {

    private static final IHoneyComb DUMMY = new DummyHoneyComb();
    private IHoneyComb honeyComb;

    public HoneyCombManager(IHoneyComb honeyComb) {
        this.honeyComb = honeyComb;
    }

    private Optional<IHoneyComb> requireHoneyComb() {
        return Optional.fromNullable(honeyComb);
    }

    public boolean isPresent() {
        return requireHoneyComb().isPresent();
    }

    @SneakyThrows
    public String getVersion() {
        return requireHoneyComb().or(DUMMY).getVersion();
    }

    @SneakyThrows
    public int getStatus() {
        return requireHoneyComb().or(DUMMY).getStatus();
    }

    @SneakyThrows
    public void addService(String name, IBinder binder) {
        requireHoneyComb().or(DUMMY).addService(name, binder);
    }

    @SneakyThrows
    public void deleteService(String name) {
        requireHoneyComb().or(DUMMY).deleteService(name);
    }

    @SneakyThrows
    public IBinder getService(String name) {
        return requireHoneyComb().or(DUMMY).getService(name);
    }

    @SneakyThrows
    public boolean hasService(String name) {
        return requireHoneyComb().or(DUMMY).hasService(name);
    }

    @SneakyThrows
    public void registerEventSubscriber(IntentFilter filter, IEventSubscriber subscriber) {
        requireHoneyComb().or(DUMMY).registerEventSubscriber(filter, subscriber);
    }

    @SneakyThrows
    public void unRegisterEventSubscriber(IEventSubscriber subscriber) {
        requireHoneyComb().or(DUMMY).unRegisterEventSubscriber(subscriber);
    }

    private static class DummyHoneyComb extends IHoneyComb.Stub {

        private static final String TAG = "DummyHoneyComb";

        @Override
        public String getVersion() {
            Log.w(TAG, "getStatus@DummyHoneyComb");
            return null;
        }

        @Override
        public int getStatus() {
            Log.w(TAG, "getStatus@DummyHoneyComb");
            return 0;
        }

        @Override
        public void addService(String name, IBinder binder) {
            Log.w(TAG, "addService@DummyHoneyComb");
        }

        @Override
        public void deleteService(String name) {
            Log.w(TAG, "deleteService@DummyHoneyComb");
        }

        @Override
        public IBinder getService(String name) {
            Log.w(TAG, "getService@DummyHoneyComb");
            return null;
        }

        @Override
        public boolean hasService(String name) {
            Log.w(TAG, "hasService@DummyHoneyComb");
            return false;
        }

        @Override
        public void registerEventSubscriber(IntentFilter filter, IEventSubscriber subscriber) {
            Log.w(TAG, "registerEventSubscriber@DummyHoneyComb");
        }

        @Override
        public void unRegisterEventSubscriber(IEventSubscriber subscriber) {
            Log.w(TAG, "unRegisterEventSubscriber@DummyHoneyComb");
        }
    }
}
