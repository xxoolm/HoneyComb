package github.tornaco.practice.honeycomb.locker;

import github.tornaco.practice.honeycomb.locker.ILockerWatcher;

interface ILocker {
   void setEnabled(boolean enabled);
   boolean isEnabled();

   void addWatcher(in ILockerWatcher w);
   void deleteWatcher(in ILockerWatcher w);

   boolean isPackageLocked(String pkg);
   void setPackageLocked(String pkg, boolean locked);

   void setVerifyResult(int request, int result, int reason);
}
