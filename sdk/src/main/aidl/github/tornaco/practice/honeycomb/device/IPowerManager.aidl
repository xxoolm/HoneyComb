package github.tornaco.practice.honeycomb.device;

interface IPowerManager {
    void sleep();
    void reboot(long delay);
    void shutdown(long delay);
}
