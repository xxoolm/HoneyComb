package github.tornaco.practice.honeycomb.data;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import org.newstand.logger.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import github.tornaco.practice.honeycomb.app.HoneyCombContext;
import github.tornaco.practice.honeycomb.pm.ModuleManager;
import github.tornaco.practice.honeycomb.util.ExecutorUtils;
import github.tornaco.practice.honeycomb.util.callback.Callback1;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class BeeRepo {

    private static final String ACTION_BEE_STARTER = "github.tornaco.practice.honeycomb.bee.action.START";

    private Context context;

    private List<Bee> get(boolean activated) {
        HoneyCombContext honeyCombContext = HoneyCombContext.createContext();
        ModuleManager moduleManager = honeyCombContext.getModuleManager();
        List<Bee> res = new ArrayList<>();
        final PackageManager pm = this.context.getPackageManager();
        List<ApplicationInfo> applicationInfos =
                pm.getInstalledApplications(PackageManager.GET_META_DATA);

        for (ApplicationInfo app : applicationInfos) {
            if (app.enabled && app.metaData != null && app.metaData.containsKey("comb_module")) {
                if (activated != (moduleManager != null && moduleManager.isModuleActivated(app.packageName))) {
                    continue;
                }
                String icon = app.metaData.getString("bee_icon");
                Bee bee = Bee.builder()
                        .isActivated(activated)
                        .name(String.valueOf(app.loadLabel(pm)))
                        .pkgName(app.packageName)
                        .icon(icon)
                        .starter(new Intent(ACTION_BEE_STARTER)
                                .setPackage(app.packageName)
                                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
                        .build();
                res.add(bee);
                Logger.i("Found a bee %s", bee);
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

    public void getActivated(@NonNull Callback1<List<Bee>> callback) {
        ExecutorUtils.io().execute(() -> callback.onResult(get(true)));
    }

    public void getNotActivated(@NonNull Callback1<List<Bee>> callback) {
        ExecutorUtils.io().execute(() -> callback.onResult(get(false)));
    }

}
