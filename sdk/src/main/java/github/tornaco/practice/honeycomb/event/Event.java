package github.tornaco.practice.honeycomb.event;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public final class Event implements Parcelable {

    private String action;
    private Bundle extra;

    private Event(Parcel in) {
        extra = in.readBundle(getClass().getClassLoader());
        action = in.readString();
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
        dest.writeBundle(extra);
        dest.writeString(action);
    }
}
