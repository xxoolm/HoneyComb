package github.tornaco.practice.honeycomb.start;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableBoolean;
import androidx.lifecycle.AndroidViewModel;
import github.tornaco.practice.honeycomb.app.HoneyCombContext;
import github.tornaco.practice.honeycomb.data.Bee;
import github.tornaco.practice.honeycomb.data.BeeRepo;
import lombok.Getter;

public class StartViewModel extends AndroidViewModel {
    private BeeRepo beeRepo;
    @Getter
    private ObservableBoolean isActivatedEmpty = new ObservableBoolean(false);
    @Getter
    private ObservableArrayList<Bee> activatedBees = new ObservableArrayList<>();
    @Getter
    private ObservableArrayList<Bee> notActivatedBees = new ObservableArrayList<>();
    @Getter
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
            isActivatedEmpty.set(activatedBees.isEmpty());
        });
        isCombActivated.set(honeyCombContext.getHoneyCombManager().isPresent());
    }

    public String getCombVersionName() {
        if (honeyCombContext.getHoneyCombManager().isPresent()) {
            return String.valueOf(honeyCombContext.getHoneyCombManager().getVersion());
        }
        return null;
    }
}
