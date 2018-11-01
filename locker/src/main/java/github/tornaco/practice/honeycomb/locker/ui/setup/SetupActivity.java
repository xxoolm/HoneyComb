package github.tornaco.practice.honeycomb.locker.ui.setup;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import github.tornaco.android.common.util.ApkUtil;
import github.tornaco.practice.honeycomb.locker.R;
import github.tornaco.practice.honeycomb.locker.ViewModelFactory;
import github.tornaco.practice.honeycomb.locker.app.LockerContext;
import github.tornaco.practice.honeycomb.util.PreconditionUtils;

public class SetupActivity extends AppCompatActivity {
    private int requestCode;
    private String pkg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verify_activity);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, PatternLockSetupFragment.newInstance())
                    .commitNow();
        }

        Intent intent = getIntent();
        PreconditionUtils.checkNotNull(intent, "Intent is null");
        PreconditionUtils.checkState(intent.hasExtra(LockerContext.LockerIntents.LOCKER_VERIFY_EXTRA_REQUEST_CODE), "No request code");
        PreconditionUtils.checkState(intent.hasExtra(LockerContext.LockerIntents.LOCKER_VERIFY_EXTRA_PACKAGE), "No pkg");
        requestCode = intent.getIntExtra(LockerContext.LockerIntents.LOCKER_VERIFY_EXTRA_REQUEST_CODE, Integer.MIN_VALUE);
        pkg = intent.getStringExtra(LockerContext.LockerIntents.LOCKER_VERIFY_EXTRA_PACKAGE);
        setTitle(ApkUtil.loadNameByPkgName(getApplicationContext(), pkg));
    }

    public static SetupViewModel obtainViewModel(FragmentActivity activity) {
        // Use a Factory to inject dependencies into the ViewModel
        ViewModelFactory factory = ViewModelFactory.getInstance(activity.getApplication());
        SetupViewModel setupViewModel = ViewModelProviders.of(activity, factory).get(SetupViewModel.class);
        injectVerifyArgs(activity, setupViewModel);
        return setupViewModel;
    }

    @VisibleForTesting
    static void injectVerifyArgs(FragmentActivity activity, SetupViewModel setupViewModel) {
        SetupActivity setupActivity = (SetupActivity) activity;
        setupViewModel.setPkg(setupActivity.pkg);
        setupViewModel.setRequestCode(setupActivity.requestCode);
    }
}
