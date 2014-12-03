package org.dbtools.android.domain;


import android.database.sqlite.SQLiteDatabase;

import javax.annotation.Nonnull;

/**
 * This class helps open, create, and upgrade the database file.
 */
@SuppressWarnings("UnusedDeclaration")
public abstract class AndroidDatabaseManager extends AndroidDatabaseBaseManager {
    @Nonnull
    public SQLiteDatabase getWritableDatabase(@Nonnull String databaseName) {
        connectDatabase(databaseName);

        AndroidDatabase db = getDatabase(databaseName);
        if (db != null) {
            return db.getSqLiteDatabase();
        }

        throw new IllegalStateException("Unable to get SQLiteDatabase for database [" + databaseName + "]");
    }

    @Nonnull
    public SQLiteDatabase getReadableDatabase(@Nonnull String databaseName) {
        connectDatabase(databaseName);
        AndroidDatabase db = getDatabase(databaseName);
        if (db != null) {
            return db.getSqLiteDatabase();
        }

        throw new IllegalStateException("Unable to get SQLiteDatabase for database [" + databaseName + "]");
    }
}
