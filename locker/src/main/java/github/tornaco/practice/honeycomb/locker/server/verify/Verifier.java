package github.tornaco.practice.honeycomb.locker.server.verify;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;

public interface Verifier {

    boolean isActivityStartShouldBeInterrupted(ComponentName componentName);

    long wrapCallingUidForIntent(long ident, Intent intent);

    Intent getCheckedActivityIntent(Intent intent);

    boolean shouldVerify(ComponentName componentName, String source);

    void verify(Bundle options, String pkg, ComponentName componentName,
                int uid, int pid, VerifyCallback callback);
}
