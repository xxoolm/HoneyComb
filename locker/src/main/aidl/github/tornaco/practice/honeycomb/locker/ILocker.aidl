// ILocker.aidl
package github.tornaco.practice.honeycomb.locker;

// Declare any non-default types here with import statements

interface ILocker {
   void setEnabled();
   boolean isEnabled();

   boolean isPackageLocked(String pkg);
   void setPackageLocked(String pkg, boolean locked);
}
