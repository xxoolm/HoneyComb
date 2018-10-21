package github.tornaco.practice.honeycomb.am;

interface IActivityManager {
    void onActivityLaunching(in Intent intent, String reason);

    String getFrontUIAppPackageName();
    boolean isAppUIInFront(String pkg);
}
