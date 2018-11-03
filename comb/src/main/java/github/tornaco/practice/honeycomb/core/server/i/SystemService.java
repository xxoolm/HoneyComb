package github.tornaco.practice.honeycomb.core.server.i;

import android.content.Context;

public interface SystemService {
    void onStart(Context context);

    void onSystemReady();

    void onShutDown();
}
