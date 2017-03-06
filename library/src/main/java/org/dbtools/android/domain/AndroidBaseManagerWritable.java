package org.dbtools.android.domain;

import android.support.annotation.NonNull;

import org.dbtools.android.domain.database.DatabaseWrapper;
import org.dbtools.android.domain.database.contentvalues.DBToolsContentValues;
import org.dbtools.android.domain.database.statement.StatementWrapper;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@SuppressWarnings("UnusedDeclaration")
public abstract class AndroidBaseManagerWritable<T extends AndroidBaseRecord> extends AndroidBaseManager<T> implements NotifiableManager {

    private long lastTableModifiedTs = -1L;

    private final Lock listenerLock = new ReentrantLock();
    private final Map<String, Set<DBToolsTableChangeListener>> tableChangeListenersMap = new HashMap<>();

    public AndroidBaseManagerWritable(AndroidDatabaseManager androidDatabaseManager) {
        super(androidDatabaseManager);
    }

    public boolean inTransaction() {
        return inTransaction(getDatabaseName());
    }

    public boolean inTransaction(@Nonnull String databaseName) {
        return getWritableDatabase(databaseName).inTransaction();
    }

    public void beginTransaction() {
        beginTransaction(getDatabaseName());
    }

    public void beginTransaction(@Nonnull String databaseName) {
        // log warning
        DatabaseWrapper<? super AndroidBaseRecord, ? super DBToolsContentValues<?>> writableDatabase = getWritableDatabase(databaseName);
        if (writableDatabase.inTransaction()) {
            getAndroidDatabaseManager().getLogger().w(AndroidDatabaseBaseManager.TAG, "WARNING: Already in transaction.");
        }

        // clear table changes and start transaction
        getAndroidDatabaseManager().clearTransactionTableChange(databaseName);
        writableDatabase.beginTransaction();
    }

    public void endTransaction(boolean success) {
        endTransaction(getDatabaseName(), success);
    }

    public void endTransaction(@Nonnull String databaseName, boolean success) {
        DatabaseWrapper<? super AndroidBaseRecord, ? super DBToolsContentValues<?>> db = getWritableDatabase(databaseName);
        if (success) {
            db.setTransactionSuccessful();
        }

        // end transaction
        db.endTransaction();

        // post end transaction event
        if (success) {
            getAndroidDatabaseManager().notifyTransactionEnded(databaseName);
        }
    }

    /**
     * Save Record.
     *
     * @param record Record to be saved
     * @return true if record was saved
     */
    public boolean save(@Nullable T record) {
        return save(getDatabaseName(), record);
    }

    /**
     * Save Record.
     *
     * @param databaseName database name to use
     * @param record            Record to be saved
     * @return true if record was saved
     */
    public boolean save(@Nonnull String databaseName, @Nullable T record) {
        if (record == null) {
            return false;
        }

        if (record.isNewRecord()) {
            long newId = insert(databaseName, record);
            return newId > 0;
        } else {
            int count = update(databaseName, record);
            return count != 0;
        }
    }

    /**
     * Insert record into database.
     *
     * @param record record to be inserted
     * @return long value of new id
     */
    public long insert(@Nullable T record) {
        return insert(getDatabaseName(), record);
    }

    /**
     * Insert record into database.
     *
     * @param record record to be inserted
     * @return long value of new id
     */
    public long insert(@Nonnull String databaseName, @Nullable T record) {
        DatabaseWrapper<? super AndroidBaseRecord, ? super DBToolsContentValues<?>> db = getWritableDatabase(databaseName);

        if (record == null) {
            return -1;
        }

        checkDB(db);
        long rowId = -1;

        // Make sure that if there is an error (LockedException), that we try again.
        boolean success = false;
        for (int tryCount = 0; tryCount < MAX_TRY_COUNT && !success; tryCount++) {
            try {
                // statement
                StatementWrapper statement = getInsertStatement(db);
                statement.clearBindings();
                record.bindInsertStatement(statement);
                rowId = statement.executeInsert();

                record.setPrimaryKeyId(rowId);

                notifyTableListeners(databaseName, false, db, new DatabaseTableChange(getTableName(), rowId, true, false, false));

                success = true;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } // retry
        return rowId;
    }

    public int update(@Nullable T record) {
        return update(getDatabaseName(), record);
    }

    public int update(@Nonnull String databaseName, @Nullable T record) {
        DatabaseWrapper<? super AndroidBaseRecord, ? super DBToolsContentValues<?>> db = getWritableDatabase(databaseName);

        if (record == null) {
            return 0;
        }

        checkDB(db);
        long rowId = record.getPrimaryKeyId();
        if (rowId <= 0) {
            throw new IllegalArgumentException("Invalid rowId [" + rowId + "] be sure to call create(...) before update(...)");
        }

        // Statement
        StatementWrapper statement = getUpdateStatement(db);
        statement.clearBindings();
        record.bindUpdateStatement(statement);
        int rowsAffectedCount = statement.executeUpdateDelete();

        if (rowsAffectedCount > 0) {
            notifyTableListeners(databaseName, false, db, new DatabaseTableChange(getTableName(), rowId, false, true, false));
        }

        return rowsAffectedCount;
    }

    public int update(@Nonnull DBToolsContentValues<?> values, long rowId) {
        return update(getDatabaseName(), values, getPrimaryKey() + " = ?", new String[]{String.valueOf(rowId)});
    }

    public int update(@Nonnull DBToolsContentValues<?> values, @Nullable String where, @Nullable String[] whereArgs) {
        return update(getDatabaseName(), values, where, whereArgs);
    }

    public int update(@Nonnull String databaseName, @Nonnull DBToolsContentValues<?> contentValues, @Nullable String where, @Nullable String[] whereArgs) {
        DatabaseWrapper<? super AndroidBaseRecord, ? super DBToolsContentValues<?>> db = getWritableDatabase(databaseName);

        int rowsAffectedCount = 0;

        checkDB(db);
        // Make sure that if there is an error (LockedException), that we try again.
        boolean success = false;
        for (int tryCount = 0; tryCount < MAX_TRY_COUNT && !success; tryCount++) {
            try {
                rowsAffectedCount = db.update(getTableName(), contentValues, where, whereArgs);
                success = true;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        if (success && rowsAffectedCount > 0) {
            notifyTableListeners(databaseName, false, db, new DatabaseTableChange(getTableName(), false, true, false));
        }

        return rowsAffectedCount;
    }

    public int delete(@Nullable T record) {
        return delete(getDatabaseName(), record);
    }

    public int delete(@Nonnull String databaseName, @Nullable T record) {
        if (record == null) {
            return 0;
        }

        long rowId = record.getPrimaryKeyId();
        if (rowId <= 0) {
            throw new IllegalArgumentException("Invalid rowId [" + rowId + "]");
        }

        return delete(databaseName, record.getIdColumnName() + " = ?", new String[]{String.valueOf(rowId)});
    }

    public int delete(long rowId) {
        return delete(getPrimaryKey() + " = ?", new String[]{String.valueOf(rowId)});
    }

    public int delete(@Nullable String where, @Nullable String[] whereArgs) {
        return delete(getDatabaseName(), where, whereArgs);
    }

    public int delete(@Nonnull String databaseName, @Nullable String where, @Nullable String[] whereArgs) {
        DatabaseWrapper<? super AndroidBaseRecord, ? super DBToolsContentValues<?>> db = getWritableDatabase(databaseName);

        checkDB(db);
        int rowCountAffected = 0;

        // Make sure that if there is an error (LockedException), that we try again.
        boolean success = false;
        for (int tryCount = 0; tryCount < MAX_TRY_COUNT && !success; tryCount++) {
            try {
                rowCountAffected = db.delete(getTableName(), where, whereArgs);
                success = true;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        if (success && rowCountAffected > 0) {
            notifyTableListeners(databaseName, false, db, new DatabaseTableChange(getTableName(), false, false, true));
        }

        return rowCountAffected;
    }

    public long deleteAll() {
        return deleteAll(getDatabaseName());
    }

    public long deleteAll(@Nonnull String databaseName) {
        return delete(databaseName, null, null);
    }

    // ===== Listeners =====

    public void addTableChangeListener(DBToolsTableChangeListener listener) {
        addTableChangeListener(getDatabaseName(), listener);
    }

    public void addTableChangeListener(String databaseName, DBToolsTableChangeListener listener) {
        listenerLock.lock();
        try {
            Set<DBToolsTableChangeListener> tableChangeListeners = tableChangeListenersMap.get(databaseName);
            if (tableChangeListeners == null) {
                tableChangeListeners = new HashSet<>();
                tableChangeListenersMap.put(databaseName, tableChangeListeners);
            }

            tableChangeListeners.add(listener);
        } finally {
            listenerLock.unlock();
        }
    }

    public void removeTableChangeListener(DBToolsTableChangeListener listener) {
        removeTableChangeListener(getDatabaseName(), listener);
    }

    public void removeTableChangeListener(String databaseName, DBToolsTableChangeListener listener) {
        listenerLock.lock();
        try {
            Set<DBToolsTableChangeListener> tableChangeListeners = tableChangeListenersMap.get(databaseName);
            if (tableChangeListeners != null) {
                tableChangeListeners.remove(listener);
            }
        } finally {
            listenerLock.unlock();
        }
    }

    public void clearTableChangeListeners() {
        clearTableChangeListeners(getDatabaseName());
    }

    public void clearTableChangeListeners(String databaseName) {
        listenerLock.lock();
        try {
            Set<DBToolsTableChangeListener> tableChangeListeners = tableChangeListenersMap.get(databaseName);
            if (tableChangeListeners != null) {
                tableChangeListeners.clear();
            }
        } finally {
            listenerLock.unlock();
        }
    }

    @Override
    public void notifyTableListeners(@NonNull String databaseName, boolean forceNotify, @Nullable DatabaseWrapper<? super AndroidBaseRecord, ? super DBToolsContentValues<?>> databaseWrapper, @Nonnull DatabaseTableChange changeType) {
        updateLastTableModifiedTs();

        if (forceNotify || !(databaseWrapper != null && databaseWrapper.inTransaction())) {
            listenerLock.lock();
            try {
                Set<DBToolsTableChangeListener> tableChangeListeners = tableChangeListenersMap.get(databaseName);

                if (tableChangeListeners != null) {
                    for (DBToolsTableChangeListener tableChangeListener : tableChangeListeners) {
                        tableChangeListener.onTableChange(changeType);
                    }
                }
            } finally {
                listenerLock.unlock();
            }
        } else {
            getAndroidDatabaseManager().addTransactionTableChange(databaseName, this);
        }
    }

    // ===== Table Change =====

    /**
     * Returns the last modification long ts
     *
     * @return long ts value of the last modification to this table using this manager, or -1 if no modifications have occurred since app launch
     */
    public long getLastTableModifiedTs() {
        return lastTableModifiedTs;
    }

    private void updateLastTableModifiedTs() {
        this.lastTableModifiedTs = System.currentTimeMillis();
    }
}
