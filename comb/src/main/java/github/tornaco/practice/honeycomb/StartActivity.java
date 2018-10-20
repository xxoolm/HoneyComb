package github.tornaco.practice.honeycomb;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import github.tornaco.practice.honeycomb.sdk.HoneyCombManager;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        TextView statusText = findViewById(R.id.status);
        statusText.setText(HoneyCombManager.global()
                .isHoneyCombReady()
                ? R.string.status_active :
                R.string.status_not_active);
    }
}
