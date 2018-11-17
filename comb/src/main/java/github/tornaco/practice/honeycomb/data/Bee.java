package github.tornaco.practice.honeycomb.data;

import android.content.Intent;

import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Bee {
    @StringRes
    private int name;
    private Intent starter;
    @DrawableRes
    private int icon;
}
