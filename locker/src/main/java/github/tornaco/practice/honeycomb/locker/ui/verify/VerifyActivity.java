package github.tornaco.practice.honeycomb.locker.ui.verify;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import github.tornaco.practice.honeycomb.locker.R;
import github.tornaco.practice.honeycomb.locker.ViewModelFactory;
import github.tornaco.practice.honeycomb.locker.app.LockerContext;
import github.tornaco.practice.honeycomb.util.PreconditionUtils;

public class VerifyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verify_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, VerifyFragment.newInstance())
                    .commitNow();
        }
    }

    public static VerifyViewModel obtainViewModel(FragmentActivity activity) {
        // Use a Factory to inject dependencies into the ViewModel
        ViewModelFactory factory = ViewModelFactory.getInstance(activity.getApplication());
        VerifyViewModel verifyViewModel = ViewModelProviders.of(activity, factory).get(VerifyViewModel.class);
        injectVerifyArgs(activity, verifyViewModel);
        return verifyViewModel;
    }

    @VisibleForTesting
    static void injectVerifyArgs(FragmentActivity activity, VerifyViewModel verifyViewModel) {
        Intent intent = activity.getIntent();
        PreconditionUtils.checkNotNull(intent, "Intent is null");
        PreconditionUtils.checkState(intent.hasExtra(LockerContext.LOCKER_VERIFY_EXTRA_REQUEST_CODE), "No request code");
        PreconditionUtils.checkState(intent.hasExtra(LockerContext.LOCKER_VERIFY_EXTRA_PACKAGE), "No pkg");
        int requestCode = intent.getIntExtra(LockerContext.LOCKER_VERIFY_EXTRA_REQUEST_CODE, Integer.MIN_VALUE);
        String pkg = intent.getStringExtra(LockerContext.LOCKER_VERIFY_EXTRA_PACKAGE);
        verifyViewModel.setPkg(pkg);
        verifyViewModel.setRequestCode(requestCode);
    }
}