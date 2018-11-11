package github.tornaco.honeycomb.common.ui;

import android.view.MenuItem;

import java.util.Objects;

import androidx.appcompat.app.AppCompatActivity;

public class BaseDefaultMenuItemHandlingAppCompatActivity extends AppCompatActivity {

    protected void showHomeAsUpNavigator() {
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    protected boolean onHomeMenuSelected() {
        finish();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (android.R.id.home == item.getItemId()) {
            return onHomeMenuSelected();
        }
        return super.onOptionsItemSelected(item);
    }
}
