package github.tornaco.practice.honeycomb.event;

import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public final class Event implements Parcelable {
    public static final String ACTION_BASE = "github.tornaco.practice.honeycomb.event.action_";
    public static final String ACTION_TASK_REMOVED = ACTION_BASE + "task_removed";
    public static final String ACTION_FRONT_UI_APP_CHANGED = ACTION_BASE + "front_ui_app_changed";

    private Intent intent;

    private Event(Parcel in) {
        intent = in.readParcelable(Intent.class.getClassLoader());
    }

    public static final Creator<Event> CREATOR = new Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel in) {
            return new Event(in);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(intent, flags);
    }
}
