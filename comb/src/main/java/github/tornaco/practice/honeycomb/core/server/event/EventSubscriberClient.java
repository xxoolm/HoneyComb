package github.tornaco.practice.honeycomb.core.server.event;

import android.content.IntentFilter;

import java.util.Objects;

import androidx.annotation.Nullable;
import github.tornaco.practice.honeycomb.event.IEventSubscriber;
import lombok.AllArgsConstructor;
import lombok.experimental.Delegate;

@AllArgsConstructor
class EventSubscriberClient extends IEventSubscriber.Stub {
    @Nullable
    private IntentFilter intentFilter;
    @Delegate
    private IEventSubscriber subscriber;

    public boolean hasAction(String action) {
        return intentFilter != null && intentFilter.hasAction(action);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        EventSubscriberClient that = (EventSubscriberClient) o;
        return Objects.equals(subscriber, that.subscriber);
    }

    @Override
    public int hashCode() {

        return Objects.hash(subscriber);
    }
}