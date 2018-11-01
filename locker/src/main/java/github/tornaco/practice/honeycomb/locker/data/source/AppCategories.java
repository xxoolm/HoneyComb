package github.tornaco.practice.honeycomb.locker.data.source;

// Align with R.array.app_list_filter_categories
public enum AppCategories {
    User, System;

    public static AppCategories belong(int ord) {
        for (AppCategories c : AppCategories.values()) {
            if (c.ordinal() == ord) {
                return c;
            }
        }
        return null;
    }
}
