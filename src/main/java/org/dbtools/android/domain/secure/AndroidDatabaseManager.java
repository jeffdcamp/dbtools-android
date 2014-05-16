package org.dbtools.android.domain.secure;


import net.sqlcipher.database.SQLiteDatabase;
import org.dbtools.android.domain.AndroidDatabase;
import org.dbtools.android.domain.AndroidDatabaseBaseManager;

import javax.annotation.Nonnull;

/**
 * This class helps open, create, and upgrade the database file.
 */
public abstract class AndroidDatabaseManager extends AndroidDatabaseBaseManager {
    @Nonnull
    public SQLiteDatabase getWritableDatabase(@Nonnull String databaseName) {
        connectDatabase(databaseName);

        AndroidDatabase db = getDatabase(databaseName);
        if (db != null) {
            return db.getSecureSqLiteDatabase();
        }

        throw new IllegalStateException("Unable to get SQLiteDatabase for database [" + databaseName + "]");
    }

    @Nonnull
    public SQLiteDatabase getReadableDatabase(@Nonnull String databaseName) {
        connectDatabase(databaseName);
        AndroidDatabase db = getDatabase(databaseName);
        if (db != null) {
            return db.getSecureSqLiteDatabase();
        }

        throw new IllegalStateException("Unable to get SQLiteDatabase for database [" + databaseName + "]");
    }
}
