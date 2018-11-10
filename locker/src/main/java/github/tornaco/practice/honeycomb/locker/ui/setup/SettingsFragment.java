package github.tornaco.practice.honeycomb.locker.ui.setup;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;

import github.tornaco.practice.honeycomb.app.HoneyCombContext;
import github.tornaco.practice.honeycomb.data.PreferenceManager;
import github.tornaco.practice.honeycomb.locker.R;
import github.tornaco.practice.honeycomb.locker.app.LockerContext;
import github.tornaco.practice.honeycomb.locker.util.fingerprint.FingerprintManagerCompat;

public class SettingsFragment extends PreferenceFragment {

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings_fragment);
        bindPreferences();
    }

    private void bindPreferences() {
        HoneyCombContext honeyCombContext = HoneyCombContext.createContext();
        PreferenceManager preferenceManager = honeyCombContext.getPreferenceManager();
        if (preferenceManager == null) {
            return;
        }

        SwitchPreference reVerifyScreenOff = (SwitchPreference) findPreference(getString(R.string.key_re_verify_on_screen_off));
        SwitchPreference reVerifyAppSwitch = (SwitchPreference) findPreference(getString(R.string.key_re_verify_on_app_switch));
        SwitchPreference reVerifyTaskRemoved = (SwitchPreference) findPreference(getString(R.string.key_re_verify_on_task_removed));
        SwitchPreference enableWorkaround = (SwitchPreference) findPreference(getString(R.string.key_verify_workaround_enabled));
        SwitchPreference enableFP = (SwitchPreference) findPreference(getString(R.string.key_fp));

        reVerifyScreenOff.setChecked(preferenceManager.getBoolean(
                LockerContext.LockerKeys.KEY_RE_VERIFY_ON_SCREEN_OFF,
                LockerContext.LockerConfigs.DEF_RE_VERIFY_ON_SCREEN_OFF));
        reVerifyAppSwitch.setChecked(preferenceManager.getBoolean(
                LockerContext.LockerKeys.KEY_RE_VERIFY_ON_APP_SWITCH,
                LockerContext.LockerConfigs.DEF_RE_VERIFY_ON_APP_SWITCH));
        reVerifyTaskRemoved.setChecked(preferenceManager.getBoolean(
                LockerContext.LockerKeys.KEY_RE_VERIFY_ON_TASK_REMOVED,
                LockerContext.LockerConfigs.DEF_RE_VERIFY_ON_TASK_REMOVED));
        enableWorkaround.setChecked(preferenceManager.getBoolean(
                LockerContext.LockerKeys.KEY_VERIFY_RES_WORKAROUND_ENABLED,
                LockerContext.LockerConfigs.DEF_VERIFY_RES_WORKAROUND_ENABLED));

        boolean isFingerPrintSupported = FingerprintManagerCompat.from(getActivity())
                .isHardwareDetected();
        enableFP.setEnabled(isFingerPrintSupported);
        enableFP.setChecked(isFingerPrintSupported && preferenceManager.getBoolean(
                LockerContext.LockerKeys.KEY_FP_ENABLED,
                LockerContext.LockerConfigs.DEF_FP_ENABLED));

        reVerifyScreenOff.setOnPreferenceClickListener(preference -> {
            preferenceManager.putBoolean(
                    LockerContext.LockerKeys.KEY_RE_VERIFY_ON_SCREEN_OFF, reVerifyScreenOff.isChecked());
            return true;
        });
        reVerifyAppSwitch.setOnPreferenceClickListener(preference -> {
            preferenceManager.putBoolean(
                    LockerContext.LockerKeys.KEY_RE_VERIFY_ON_APP_SWITCH, reVerifyAppSwitch.isChecked());
            return true;
        });
        reVerifyTaskRemoved.setOnPreferenceClickListener(preference -> {
            preferenceManager.putBoolean(
                    LockerContext.LockerKeys.KEY_RE_VERIFY_ON_TASK_REMOVED, reVerifyTaskRemoved.isChecked());
            return true;
        });
        enableWorkaround.setOnPreferenceClickListener(preference -> {
            preferenceManager.putBoolean(
                    LockerContext.LockerKeys.KEY_VERIFY_RES_WORKAROUND_ENABLED, enableWorkaround.isChecked());
            return true;
        });
        if (isFingerPrintSupported) {
            enableFP.setOnPreferenceClickListener(preference -> {
                preferenceManager.putBoolean(
                        LockerContext.LockerKeys.KEY_FP_ENABLED, enableFP.isChecked());
                return true;
            });
        }
    }
}
