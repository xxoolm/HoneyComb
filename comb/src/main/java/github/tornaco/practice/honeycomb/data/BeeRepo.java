package github.tornaco.practice.honeycomb.data;

import android.content.Intent;

import com.google.common.collect.Lists;

import java.util.List;

import github.tornaco.practice.honeycomb.R;

public class BeeRepo {

    private static final String ACTION_LOCKER = "github.tornaco.practice.honeycomb.locker.action.START";

    public List<Bee> getAll() {
        return Lists.newArrayList(
                Bee.builder()
                        .icon(R.drawable.ic_lock_white_24dp)
                        .name(R.string.app_name_locker)
                        .starter(new Intent(ACTION_LOCKER).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
                        .build(),
                Bee.builder()
                        .icon(R.drawable.ic_lock_white_24dp)
                        .name(R.string.app_name_locker)
                        .starter(new Intent(ACTION_LOCKER).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
                        .build(),
                Bee.builder()
                        .icon(R.drawable.ic_lock_white_24dp)
                        .name(R.string.app_name_locker)
                        .starter(new Intent(ACTION_LOCKER).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
                        .build()

        );
    }
}
