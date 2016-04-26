package org.dbtools.android.domain.task;

import org.dbtools.android.domain.AndroidBaseRecord;
import org.dbtools.android.domain.AsyncManager;
import org.dbtools.android.domain.database.contentvalues.DBToolsContentValues;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class UpdateWhereTask<T extends AndroidBaseRecord> implements Runnable {

    @Nonnull
    private String databaseName;
    @Nonnull
    private AsyncManager<T> manager;
    @Nonnull
    DBToolsContentValues contentValues;
    @Nullable
    private final String where;
    @Nullable
    private final String[] whereArgs;

    public UpdateWhereTask(@Nonnull String databaseName, @Nonnull AsyncManager<T> manager, @Nonnull DBToolsContentValues contentValues, @Nullable String where, @Nullable String[] whereArgs) {
        this.databaseName = databaseName;
        this.manager = manager;
        this.contentValues = contentValues;
        this.where = where;
        this.whereArgs = whereArgs;
    }

    @Override
    public void run() {
        manager.update(databaseName, contentValues, where, whereArgs);
    }
}
