package github.tornaco.practice.honeycomb.modules;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableArrayList;
import androidx.lifecycle.AndroidViewModel;
import github.tornaco.practice.honeycomb.app.HoneyCombContext;
import github.tornaco.practice.honeycomb.data.Bee;
import github.tornaco.practice.honeycomb.data.BeeRepo;

public class ModulesViewModel extends AndroidViewModel {
    private BeeRepo beeRepo;
    private ObservableArrayList<Bee> bees = new ObservableArrayList<>();
    private HoneyCombContext honeyCombContext;

    public ModulesViewModel(@NonNull Application application, BeeRepo beeRepo) {
        super(application);
        this.beeRepo = beeRepo;
        this.honeyCombContext = HoneyCombContext.createContext();
    }

    public void start() {
        beeRepo.getAll(bees -> {
            ModulesViewModel.this.bees.clear();
            ModulesViewModel.this.bees.addAll(bees);
        });
    }

    public void setModuleActivated(Bee bee, boolean activated) {
        boolean isPresent = honeyCombContext.getHoneyCombManager().isPresent();
        if (isPresent) {
            honeyCombContext.getModuleManager().setModuleActive(bee.getPkgName(), activated);
        }
    }

    public ObservableArrayList<Bee> getBees() {
        return bees;
    }
}
