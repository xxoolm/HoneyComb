package github.tornaco.practice.honeycomb.core.server.am;

import android.content.Context;

import androidx.annotation.NonNull;
import github.tornaco.practice.honeycomb.util.ClipboardUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CurrentComponentViewCallback implements CurrentComponentView.Callback {

    private Context context;

    @Override
    public void onSingleTap(String text) {
        ClipboardUtils.copyToClipboard(getContext(), "current", text);
    }

    @Override
    public void onDoubleTap() {

    }

    @Override
    public void onSwipeDirection(@NonNull CurrentComponentView.SwipeDirection direction) {

    }

    @Override
    public void onSwipeDirectionLargeDistance(@NonNull CurrentComponentView.SwipeDirection direction) {

    }

    @Override
    public void onLongPress() {

    }
}
