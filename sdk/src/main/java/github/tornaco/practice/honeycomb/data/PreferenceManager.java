package github.tornaco.practice.honeycomb.data;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

@AllArgsConstructor
public class PreferenceManager extends IPreferenceManager.Stub {

    private IPreferenceManager server;

    @Override
    @SneakyThrows
    public boolean putInt(String key, int value) {
        return server.putInt(key, value);
    }

    @Override
    @SneakyThrows
    public int getInt(String key, int def) {
        return server.getInt(key, def);
    }

    @Override
    @SneakyThrows
    public boolean putString(String key, String value) {
        return server.putString(key, value);
    }

    @Override
    @SneakyThrows
    public String getString(String key, String def) {
        return server.getString(key, def);
    }

    @Override
    @SneakyThrows
    public boolean putBoolean(String key, boolean value) {
        return server.putBoolean(key, value);
    }

    @Override
    @SneakyThrows
    public boolean getBoolean(String key, boolean def) {
        return server.getBoolean(key, def);
    }

    @Override
    @SneakyThrows
    public boolean putLong(String key, long value) {
        return server.putLong(key, value);
    }

    @Override
    @SneakyThrows
    public long getLong(String key, long def) {
        return server.getLong(key, def);
    }

    @Override
    @SneakyThrows
    public String getOwnerPackageName() {
        return server.getOwnerPackageName();
    }
}
