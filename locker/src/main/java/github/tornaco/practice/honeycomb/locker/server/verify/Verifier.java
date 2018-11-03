package github.tornaco.practice.honeycomb.locker.server.verify;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public interface Verifier {

    boolean isActivityStartShouldBeInterrupted(ComponentName componentName);

    long wrapCallingUidForIntent(long ident, Intent intent);

    Intent getCheckedActivityIntent(Intent intent);

    /**
     * 是否需要验证该组件，如果没有组件参数的话，需要传入包名
     */
    boolean shouldVerify(@Nullable ComponentName componentName, @NonNull String pkg, String source);

    void verify(Bundle options, String pkg, ComponentName componentName,
                int uid, int pid, VerifyCallback callback);
}
