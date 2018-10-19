package github.tornaco.practice.honeycomb.core.server;


import java.io.File;

import github.tornaco.practice.honeycomb.IPreferenceManager;
import github.tornaco.practice.honeycomb.core.server.data.DataConfigs;
import github.tornaco.practice.honeycomb.core.server.data.SettingsProvider;

public class PreferenceManagerStub extends IPreferenceManager.Stub {

    private String ownerPackageName;
    private SettingsProvider settingsProvider;

    PreferenceManagerStub(String ownerPackageName) {
        this.ownerPackageName = ownerPackageName;
        String path = new File(DataConfigs.getBaseDataDir(), ownerPackageName + ".xml").getPath();
        this.settingsProvider = SettingsProvider.getOrCreate(path);
    }

    @Override
    public boolean putInt(String key, int value) {
        return settingsProvider.putInt(key, value);
    }

    @Override
    public int getInt(String key, int def) {
        return settingsProvider.getInt(key, def);
    }

    @Override
    public boolean putString(String key, String value) {
        return settingsProvider.putString(key, value);
    }

    @Override
    public String getString(String key, String def) {
        return settingsProvider.getString(key, def);
    }

    @Override
    public boolean putBoolean(String key, boolean value) {
        return settingsProvider.putBoolean(key, value);
    }

    @Override
    public boolean getBoolean(String key, boolean def) {
        return settingsProvider.getBoolean(key, def);
    }

    @Override
    public boolean putLong(String key, long value) {
        return settingsProvider.putLong(key, value);
    }

    @Override
    public long getLong(String key, long def) {
        return settingsProvider.getLong(key, def);
    }

    @Override
    public String getOwnerPackageName() {
        return ownerPackageName;
    }
}
