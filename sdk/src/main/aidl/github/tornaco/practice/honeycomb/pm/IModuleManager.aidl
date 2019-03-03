package github.tornaco.practice.honeycomb.pm;

interface IModuleManager {
    boolean isModuleActivated(String pkgName);
    void setModuleActive(String pkgName, boolean active);
}
