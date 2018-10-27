package github.tornaco.practice.honeycomb.data;

import lombok.AllArgsConstructor;
import lombok.experimental.Delegate;

@AllArgsConstructor
public class PreferenceManager extends IPreferenceManager.Stub {
    @Delegate
    private IPreferenceManager preferenceManager;

}
