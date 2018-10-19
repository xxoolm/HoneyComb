package github.tornaco.practice.honeycomb.locker;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import github.tornaco.practice.honeycomb.locker.ui.start.StartFragment;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, StartFragment.newInstance())
                    .commitNow();
        }
    }
}
