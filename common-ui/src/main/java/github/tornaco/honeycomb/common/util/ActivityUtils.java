package github.tornaco.honeycomb.common.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

public class ActivityUtils {

    public static void startActivity(Activity activity, Class<? extends Activity> clazz) {
        activity.startActivity(new Intent(activity, clazz));
    }

    public static void startActivity(Context context, Class<? extends Activity> clazz) {
        Intent intent = new Intent(context, clazz);
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

}
