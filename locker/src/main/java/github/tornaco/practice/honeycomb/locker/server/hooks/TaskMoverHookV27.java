package github.tornaco.practice.honeycomb.locker.server.hooks;

import github.tornaco.practice.honeycomb.locker.server.verify.Verifier;

/**
 * Created by guohao4 on 2017/10/31.
 * Email: Tornaco@163.com
 */

// oid findTaskToMoveToFrontLocked(TaskRecord task, int flags, ActivityOptions options,
//            String reason, boolean forceNonResizeable) {
class TaskMoverHookV27 extends TaskMoverHookV26 {

    TaskMoverHookV27(Verifier verifier) {
        super(verifier);
    }
}
