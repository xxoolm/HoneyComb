package github.tornaco.practice.honeycomb.locker.server.hooks;

import android.os.Build;

import github.tornaco.practice.honeycomb.locker.server.verify.Verifier;
import lombok.experimental.Delegate;

/**
 * Created by guohao4 on 2017/10/31.
 * Email: Tornaco@163.com
 */

public class TaskMoverHookDelegate extends TaskMoverHook {
    @Delegate
    private TaskMoverHook taskMoverHook;

    public TaskMoverHookDelegate(Verifier verifier) {
        super(verifier);
        int sdkVersion = Build.VERSION.SDK_INT;
        switch (sdkVersion) {
            case 21:
                taskMoverHook = new TaskMoverHookV21(verifier);
                break;
            case 22:
                taskMoverHook = new TaskMoverHookV22(verifier);
                break;
            case 23:
                taskMoverHook = new TaskMoverHookV23(verifier);
                break;
            case 24:
                taskMoverHook = new TaskMoverHookV24(verifier);
                break;
            case 25:
                taskMoverHook = new TaskMoverHookV25(verifier);
                break;
            case 26:
                taskMoverHook = new TaskMoverHookV26(verifier);
                break;
            case 27:
                taskMoverHook = new TaskMoverHookV27(verifier);
                break;
            default:
                taskMoverHook = new TaskMoverHookEmpty(verifier);
                break;

        }
    }
}
