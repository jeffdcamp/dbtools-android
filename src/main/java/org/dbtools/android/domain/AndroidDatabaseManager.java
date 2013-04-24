package org.dbtools.android.domain;


import android.database.sqlite.SQLiteDatabase;

/**
 * This class helps open, create, and upgrade the database file.
 */
public abstract class AndroidDatabaseManager extends AndroidDatabaseBaseManager {
    public SQLiteDatabase getWritableDatabase(String databaseName) {
        connectDatabase(databaseName);
        return getDatabase(databaseName).getSqLiteDatabase();
    }

    public SQLiteDatabase getReadableDatabase(String databaseName) {
        connectDatabase(databaseName);
        return getDatabase(databaseName).getSqLiteDatabase();
    }
}
