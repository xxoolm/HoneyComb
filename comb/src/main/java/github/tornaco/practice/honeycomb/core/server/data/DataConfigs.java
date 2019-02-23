package github.tornaco.practice.honeycomb.core.server.data;

import android.os.Environment;

import java.io.File;

public class DataConfigs {

    public static File getBaseDataDir() {
        File systemFile = new File(Environment.getDataDirectory(), "system");
        return new File(systemFile, "comb/core");
    }
}
