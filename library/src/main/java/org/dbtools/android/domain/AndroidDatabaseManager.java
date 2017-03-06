package org.dbtools.android.domain;


import org.dbtools.android.domain.config.DatabaseConfig;
import org.dbtools.android.domain.database.DatabaseWrapper;
import org.dbtools.android.domain.database.contentvalues.DBToolsContentValues;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.annotation.Nonnull;

/**
 * This class helps open, create, and upgrade the database file.
 */
@SuppressWarnings("UnusedDeclaration")
public abstract class AndroidDatabaseManager extends AndroidDatabaseBaseManager {
    public AndroidDatabaseManager(DatabaseConfig databaseConfig) {
        super(databaseConfig);
    }

    private final Lock listenerLock = new ReentrantLock();
    private Map<String, Set<NotifiableManager>> writableManagerMap = new HashMap<>();

    @Nonnull
    public DatabaseWrapper<? super AndroidBaseRecord, ? super DBToolsContentValues<?>> getWritableDatabase(@Nonnull String databaseName) {
        connectDatabase(databaseName);

        AndroidDatabase db = getDatabase(databaseName);
        if (db != null) {
            return db.getDatabaseWrapper();
        }

        throw new IllegalStateException("Unable to get SQLiteDatabase for database [" + databaseName + "]");
    }

    @Nonnull
    public DatabaseWrapper<? super AndroidBaseRecord, ? super DBToolsContentValues<?>> getReadableDatabase(@Nonnull String databaseName) {
        connectDatabase(databaseName);
        AndroidDatabase db = getDatabase(databaseName);
        if (db != null) {
            return db.getDatabaseWrapper();
        }

        throw new IllegalStateException("Unable to get SQLiteDatabase for database [" + databaseName + "]");
    }

    public void addTransactionTableChange(String databaseName, NotifiableManager recordManager) {
        listenerLock.lock();
        try {
            Set<NotifiableManager> writableManagers = writableManagerMap.get(databaseName);
            if (writableManagers == null) {
                writableManagers = new HashSet<>();
                writableManagerMap.put(databaseName, writableManagers);
            }

            writableManagers.add(recordManager);
        } finally {
            listenerLock.unlock();
        }
    }

    public void notifyTransactionEnded(String databaseName) {
        listenerLock.lock();
        try {
            Set<NotifiableManager> writableManagers = writableManagerMap.remove(databaseName);
            if (writableManagers != null) {
                for (NotifiableManager writableManager : writableManagers) {
                    writableManager.notifyTableListeners(databaseName, true, getWritableDatabase(databaseName), new DatabaseTableChange(writableManager.getTableName()));
                }
            }

        } finally {
            listenerLock.unlock();
        }
    }

    public void clearTransactionTableChange(String databaseName) {
        listenerLock.lock();
        try {
            writableManagerMap.remove(databaseName);
        } finally {
            listenerLock.unlock();
        }
    }
}
