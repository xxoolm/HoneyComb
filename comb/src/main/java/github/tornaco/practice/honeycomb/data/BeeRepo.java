package github.tornaco.practice.honeycomb.data;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import github.tornaco.practice.honeycomb.app.HoneyCombContext;
import github.tornaco.practice.honeycomb.pm.ModuleManager;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class BeeRepo {

    private static final String ACTION_BEE_STARTER = "github.tornaco.practice.honeycomb.bee.action.START";

    private Context context;

    public List<Bee> getAll() {
        HoneyCombContext honeyCombContext = HoneyCombContext.createContext();
        ModuleManager moduleManager = honeyCombContext.getModuleManager();
        List<Bee> res = new ArrayList<>();
        final PackageManager pm = this.context.getPackageManager();
        List<ApplicationInfo> applicationInfos =
                pm.getInstalledApplications(PackageManager.GET_META_DATA);

        for (ApplicationInfo app : applicationInfos) {
            if (app.enabled && app.metaData != null && app.metaData.containsKey("combmodule")) {
                res.add(Bee.builder()
                        .isActivated(moduleManager != null && moduleManager.isModuleActivated(app.packageName))
                        .name(String.valueOf(app.loadLabel(pm)))
                        .pkgName(app.packageName)
                        .starter(new Intent(ACTION_BEE_STARTER).setPackage(app.packageName).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
                        .build());
            }

        }
        Collections.sort(res, (bee1, bee2) -> {
            if (bee1.isActivated() == bee2.isActivated()) {
                return 0;
            }
            return bee1.isActivated() ? -1 : 1;
        });
        return res;
    }

}
