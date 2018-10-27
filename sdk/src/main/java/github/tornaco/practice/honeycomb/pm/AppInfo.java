package github.tornaco.practice.honeycomb.pm;

import android.os.Parcel;
import android.os.Parcelable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AppInfo implements Parcelable {

    private String pkgName;
    private String appLabel;
    private int versionCode;
    private String versionName;
    private int flags;

    protected AppInfo(Parcel in) {
        pkgName = in.readString();
        appLabel = in.readString();
        versionCode = in.readInt();
        versionName = in.readString();
        flags = in.readInt();
    }

    public static final Creator<AppInfo> CREATOR = new Creator<AppInfo>() {
        @Override
        public AppInfo createFromParcel(Parcel in) {
            return new AppInfo(in);
        }

        @Override
        public AppInfo[] newArray(int size) {
            return new AppInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(pkgName);
        dest.writeString(appLabel);
        dest.writeInt(versionCode);
        dest.writeString(versionName);
        dest.writeInt(flags);
    }
}
