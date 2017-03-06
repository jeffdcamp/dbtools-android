package org.dbtools.sample.model.database;

import org.dbtools.android.domain.AndroidDatabase;
import org.dbtools.android.domain.config.DatabaseConfig;

import javax.annotation.Nonnull;

public class MultiDatabaseManager extends DatabaseManager {
    public MultiDatabaseManager(DatabaseConfig databaseConfig) {
        super(databaseConfig);
    }

    @Override
    public void onCreate(@Nonnull AndroidDatabase androidDatabase) {
        createMainTables(androidDatabase);
    }

    @Override
    public void onCreateViews(@Nonnull AndroidDatabase androidDatabase) {
        createMainViews(androidDatabase);
    }

    @Override
    public void onDropViews(@Nonnull AndroidDatabase androidDatabase) {
        dropMainViews(androidDatabase);
    }
}
