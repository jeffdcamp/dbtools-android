package org.dbtools.android.domain.task;

import org.dbtools.android.domain.AndroidBaseRecord;
import org.dbtools.android.domain.AsyncManager;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class DeleteWhereTask<T extends AndroidBaseRecord> implements Runnable {

    @Nonnull
    private String databaseName;
    @Nonnull
    private AsyncManager<T> manager;
    @Nullable
    private final String where;
    @Nullable
    private final String[] whereArgs;

    public DeleteWhereTask(@Nonnull String databaseName, @Nonnull AsyncManager<T> manager, @Nullable String where, @Nullable String[] whereArgs) {
        this.databaseName = databaseName;
        this.manager = manager;
        this.where = where;
        this.whereArgs = whereArgs;
    }

    @Override
    public void run() {
        manager.delete(databaseName, where, whereArgs);
    }
}
