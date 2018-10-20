package github.tornaco.practice.honeycomb.locker.server.hooks;

import android.os.Build;

import de.robv.android.xposed.IXposedHookLoadPackage;
import github.tornaco.practice.honeycomb.locker.server.verify.Verifier;
import lombok.experimental.Delegate;

/**
 * Created by guohao4 on 2017/10/31.
 * Email: Tornaco@163.com
 */

public class ActivityStartHookDelegate implements IXposedHookLoadPackage {

    public ActivityStartHookDelegate(Verifier verifier) {
        int sdkVersion = Build.VERSION.SDK_INT;
        switch (sdkVersion) {
            case 21:
                activityStartHookImpl = new ActivityStartHookV21(verifier);
                break;
            case 22:
                activityStartHookImpl = new ActivityStartHookV22(verifier);
                break;
            case 23:
                activityStartHookImpl = new ActivityStartHookV23(verifier);
                break;
            case 24:
                activityStartHookImpl = new ActivityStartHookV24(verifier);
                break;
            case 25:
                activityStartHookImpl = new ActivityStartHookV25(verifier);
                break;
            case 26:
                activityStartHookImpl = new ActivityStartHookV26(verifier);
                break;
            case 27:
                activityStartHookImpl = new ActivityStartHookV27(verifier);
                break;
            default:
                activityStartHookImpl = new ActivityStartHookEmpty();
                break;
        }
    }

    @Delegate
    private IXposedHookLoadPackage activityStartHookImpl;

}
