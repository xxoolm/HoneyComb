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
    private ObservableArrayList<Bee> bees = new ObservableArrayList<>();
    private ObservableBoolean isCombActivated = new ObservableBoolean(false);

    public StartViewModel(@NonNull Application application, BeeRepo beeRepo) {
        super(application);
        this.beeRepo = beeRepo;
    }

    public void start() {
        bees.addAll(beeRepo.getAll());
        isCombActivated.set(HoneyCombContext.createContext().getHoneyCombManager().isPresent());
    }

    public void startBee(Bee bee) {
        getApplication().startActivity(bee.getStarter());
    }

    public ObservableArrayList<Bee> getBees() {
        return bees;
    }

    public ObservableBoolean getIsCombActivated() {
        return isCombActivated;
    }
}
