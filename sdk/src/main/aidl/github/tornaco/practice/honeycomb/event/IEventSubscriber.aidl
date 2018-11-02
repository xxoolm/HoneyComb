package github.tornaco.practice.honeycomb.event;

import github.tornaco.practice.honeycomb.event.Event;

interface IEventSubscriber {
    void onEvent(in Event e);
}
