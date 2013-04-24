package org.dbtools.android.domain.secure;


import net.sqlcipher.database.SQLiteDatabase;
import org.dbtools.android.domain.AndroidDatabaseBaseManager;

/**
 * This class helps open, create, and upgrade the database file.
 */
public abstract class AndroidDatabaseManager extends AndroidDatabaseBaseManager {
    public SQLiteDatabase getWritableDatabase(String databaseName) {
        connectDatabase(databaseName);
        return getDatabase(databaseName).getSecureSqLiteDatabase();
    }

    public SQLiteDatabase getReadableDatabase(String databaseName) {
        connectDatabase(databaseName);
        return getDatabase(databaseName).getSecureSqLiteDatabase();
    }
}
