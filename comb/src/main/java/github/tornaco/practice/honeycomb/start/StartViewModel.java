package github.tornaco.practice.honeycomb.start;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableBoolean;
import androidx.lifecycle.AndroidViewModel;
import github.tornaco.practice.honeycomb.app.HoneyCombContext;
import github.tornaco.practice.honeycomb.data.Bee;
import github.tornaco.practice.honeycomb.data.BeeRepo;

public class StartViewModel extends AndroidViewModel {
    private BeeRepo beeRepo;
    private ObservableArrayList<Bee> activatedBees = new ObservableArrayList<>();
    private ObservableArrayList<Bee> notActivatedBees = new ObservableArrayList<>();
    private ObservableBoolean isCombActivated = new ObservableBoolean(false);
    private HoneyCombContext honeyCombContext;

    public StartViewModel(@NonNull Application application, BeeRepo beeRepo) {
        super(application);
        this.beeRepo = beeRepo;
        this.honeyCombContext = HoneyCombContext.createContext();
    }

    public void start() {
        beeRepo.getActivated(bees -> {
            StartViewModel.this.activatedBees.clear();
            StartViewModel.this.activatedBees.addAll(bees);
        });
        beeRepo.getNotActivated(bees -> {
            StartViewModel.this.notActivatedBees.clear();
            StartViewModel.this.notActivatedBees.addAll(bees);
        });
        isCombActivated.set(honeyCombContext.getHoneyCombManager().isPresent());
    }

    public void startBee(Bee bee) {
        getApplication().startActivity(bee.getStarter());
    }

    public ObservableArrayList<Bee> getActivatedBees() {
        return activatedBees;
    }

    public ObservableArrayList<Bee> getNotActivatedBees() {
        return notActivatedBees;
    }

    public ObservableBoolean getIsCombActivated() {
        return isCombActivated;
    }

    public String getCombVersionName() {
        if (honeyCombContext.getHoneyCombManager().isPresent()) {
            return String.valueOf(honeyCombContext.getHoneyCombManager().getVersion());
        }
        return null;
    }
}
