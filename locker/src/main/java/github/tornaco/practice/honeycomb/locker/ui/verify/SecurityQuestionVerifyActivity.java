package github.tornaco.practice.honeycomb.locker.ui.verify;

import android.os.Bundle;

import github.tornaco.honeycomb.common.ui.BaseDefaultMenuItemHandlingAppCompatActivity;
import github.tornaco.practice.honeycomb.locker.R;

public class SecurityQuestionVerifyActivity extends BaseDefaultMenuItemHandlingAppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.security_question_verify_activity);
        showHomeAsUpNavigator();
    }
}
