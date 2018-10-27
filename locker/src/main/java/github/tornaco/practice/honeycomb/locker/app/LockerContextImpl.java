package github.tornaco.practice.honeycomb.locker.app;

import org.newstand.logger.Logger;

import github.tornaco.practice.honeycomb.HoneyCombManager;
import github.tornaco.practice.honeycomb.app.HoneyCombContext;
import github.tornaco.practice.honeycomb.locker.ILocker;

class LockerContextImpl implements LockerContext {

    private LockerManager lockerManager;

    LockerContextImpl() {
        HoneyCombContext honeyCombContext = HoneyCombContext.createContext();
        HoneyCombManager honeyCombManager = honeyCombContext.getHoneyCombManager();
        if (honeyCombManager.isPresent() && honeyCombManager.hasService(LOCKER_SERVICE)) {
            this.lockerManager = new LockerManager(ILocker.Stub.asInterface(honeyCombManager.getService(LOCKER_SERVICE)));
        } else {
            Logger.w("Locker server is missing!");
        }
    }

    @Override
    public LockerManager getLockerManager() {
        return lockerManager;
    }
}
