package org.dbtools.android.domain.task;

import org.dbtools.android.domain.AndroidBaseRecord;
import org.dbtools.android.domain.AsyncManager;

import javax.annotation.Nonnull;

public class InsertTask<T extends AndroidBaseRecord> implements Runnable {

    private String databaseName;
    private AsyncManager<T> manager;
    private T record;

    public InsertTask(@Nonnull String databaseName, @Nonnull AsyncManager<T> manager, @Nonnull T record) {
        this.databaseName = databaseName;
        this.manager = manager;
        this.record = record;
    }

    @Override
    public void run() {
        manager.insert(databaseName, record);
    }
}
