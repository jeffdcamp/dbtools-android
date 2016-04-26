package org.dbtools.android.domain;


import org.dbtools.android.domain.config.DatabaseConfig;
import org.dbtools.android.domain.database.DatabaseWrapper;

import javax.annotation.Nonnull;

/**
 * This class helps open, create, and upgrade the database file.
 */
@SuppressWarnings("UnusedDeclaration")
public abstract class AndroidDatabaseManager extends AndroidDatabaseBaseManager {
    public AndroidDatabaseManager(DatabaseConfig databaseConfig) {
        super(databaseConfig);
    }

    @Nonnull
    public DatabaseWrapper getWritableDatabase(@Nonnull String databaseName) {
        connectDatabase(databaseName);

        AndroidDatabase db = getDatabase(databaseName);
        if (db != null) {
            return db.getDatabaseWrapper();
        }

        throw new IllegalStateException("Unable to get SQLiteDatabase for database [" + databaseName + "]");
    }

    @Nonnull
    public DatabaseWrapper getReadableDatabase(@Nonnull String databaseName) {
        connectDatabase(databaseName);
        AndroidDatabase db = getDatabase(databaseName);
        if (db != null) {
            return db.getDatabaseWrapper();
        }

        throw new IllegalStateException("Unable to get SQLiteDatabase for database [" + databaseName + "]");
    }
}
