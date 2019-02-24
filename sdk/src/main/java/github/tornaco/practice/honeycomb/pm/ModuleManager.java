package github.tornaco.practice.honeycomb.pm;

import android.os.RemoteException;

import com.google.common.base.Optional;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

@SuppressWarnings("Guava")
@AllArgsConstructor
public class ModuleManager extends IModuleManager.Stub {

    private static IModuleManager DUMMY = new DummyModuleManager();
    private IModuleManager service;

    private Optional<IModuleManager> requireModuleManager() {
        return Optional.fromNullable(service);
    }

    public boolean isPresent() {
        return requireModuleManager().isPresent();
    }

    @Override
    @SneakyThrows
    public boolean isModuleActivated(String pkgName) {
        return requireModuleManager().or(DUMMY).isModuleActivated(pkgName);
    }

    @Override
    @SneakyThrows
    public void setModuleActive(String pkgName) {
        requireModuleManager().or(DUMMY).setModuleActive(pkgName);
    }

    private static class DummyModuleManager extends IModuleManager.Stub {
        @Override
        public boolean isModuleActivated(String pkgName) throws RemoteException {
            return false;
        }

        @Override
        public void setModuleActive(String pkgName) throws RemoteException {

        }
    }
}
