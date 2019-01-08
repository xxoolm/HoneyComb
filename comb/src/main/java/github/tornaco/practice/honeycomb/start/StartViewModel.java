package github.tornaco.practice.honeycomb.start;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableArrayList;
import androidx.lifecycle.AndroidViewModel;
import github.tornaco.practice.honeycomb.data.Bee;
import github.tornaco.practice.honeycomb.data.BeeRepo;

public class StartViewModel extends AndroidViewModel {
    private BeeRepo beeRepo;
    private ObservableArrayList<Bee> bees = new ObservableArrayList<>();

    public StartViewModel(@NonNull Application application, BeeRepo beeRepo) {
        super(application);
        this.beeRepo = beeRepo;
    }

    public void start() {
        bees.addAll(beeRepo.getAll());
    }

    public void startBee(Bee bee) {
        getApplication().startActivity(bee.getStarter());
    }

    public ObservableArrayList<Bee> getBees() {
        return bees;
    }
}
