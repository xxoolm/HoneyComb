package github.tornaco.practice.honeycomb.core.server;

import android.os.IBinder;

import java.util.HashMap;

class HoneyCombServiceManager {

    private static HashMap<String, IBinder> sCache = new HashMap<>();

    static IBinder getService(String name) {
        return sCache.get(name);
    }

    static boolean hasService(String name) {
        return sCache.containsKey(name);
    }

    static void addService(String name, IBinder service) {
        if (hasService(name)) {
            throw new SecurityException("Service not allowed to be replace " + name);
        }
        sCache.put(name, service);
    }

    static void deleteService(String name) {
        sCache.remove(name);
    }

}
