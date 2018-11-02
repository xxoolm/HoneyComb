package github.tornaco.practice.honeycomb.am;

interface IActivityManager {
    // @AppReporting
    void onActivityLaunching(in Intent intent, String reason);
    // @AppReporting
    void onTaskRemoving(int callingUid, int taskId);

    String getFrontUIAppPackageName();
    boolean isAppUIInFront(String pkg);
}
