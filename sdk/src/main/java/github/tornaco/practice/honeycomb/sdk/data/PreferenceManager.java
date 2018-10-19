package github.tornaco.practice.honeycomb.sdk.data;

import github.tornaco.practice.honeycomb.IPreferenceManager;
import lombok.AllArgsConstructor;
import lombok.experimental.Delegate;

@AllArgsConstructor
public class PreferenceManager extends IPreferenceManager.Stub {
    @Delegate
    private IPreferenceManager preferenceManager;

}
