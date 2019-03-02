package github.tornaco.practice.honeycomb.util;

import android.content.res.Resources;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoadedResources {
    private Resources resources;
    private String packageName;
    private ClassLoader classLoader;
}
