package github.tornaco.practice.honeycomb.core.server.am;

import android.content.ComponentName;

import github.tornaco.practice.honeycomb.app.AbstractSafeR;
import lombok.Setter;

public class ShowCurrentComponentViewR extends AbstractSafeR {

    @Setter
    private ComponentName name;
    @Setter
    private CurrentComponentView view;

    @Override
    public void runSafety() {
        if (name != null) {
            view.attach();
            view.show();
            view.setText(name.flattenToString());
        }
    }
}
