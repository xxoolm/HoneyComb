package github.tornaco.practice.honeycomb.data;

import android.content.Intent;

import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Bee {
    private String name;
    private Intent starter;
    private String pkgName;
    private boolean isActivated;
}
