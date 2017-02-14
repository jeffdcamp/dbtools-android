package org.dbtools.android.domain;

import org.dbtools.android.domain.database.DatabaseWrapper;
import org.dbtools.android.domain.database.contentvalues.DBToolsContentValues;
import org.dbtools.android.domain.database.statement.StatementWrapper;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import rx.Observable;
import rx.subjects.PublishSubject;

@SuppressWarnings("UnusedDeclaration")
public abstract class RxAndroidBaseManagerWritable<T extends AndroidBaseRecord> extends RxAndroidBaseManager<T> {

    private final AtomicBoolean transactionInsertOccurred = new AtomicBoolean(false);
    private final AtomicBoolean transactionUpdateOccurred = new AtomicBoolean(false);
    private final AtomicBoolean transactionDeleteOccurred = new AtomicBoolean(false);
    private long lastTableModifiedTs = -1L;

    private final Lock listenerLock = new ReentrantLock();
    private final Set<DBToolsTableChangeListener> tableChangeListeners = new HashSet<>();
    private final PublishSubject<DatabaseTableChange> tableChanges = PublishSubject.create();

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
        getWritableDatabase(databaseName).beginTransaction();
    }

    public void endTransaction(boolean success) {
        endTransaction(getDatabaseName(), success);
    }

    public void endTransaction(@Nonnull String databaseName, boolean success) {
        DatabaseWrapper<? super AndroidBaseRecord, ? super DBToolsContentValues<?>> db = getWritableDatabase(databaseName);
        if (success) {
            db.setTransactionSuccessful();
        }

        // determine if there are changes
        DatabaseTableChange tableChange = new DatabaseTableChange(getTableName(), transactionInsertOccurred.get(), transactionUpdateOccurred.get(), transactionDeleteOccurred.get());
        transactionInsertOccurred.set(false);
        transactionUpdateOccurred.set(false);
        transactionDeleteOccurred.set(false);

        // end transaction
        db.endTransaction();

        // post end transaction event
        if (tableChange.hasChange()) {
            notifyTableListeners(true, db, tableChange);
        }
    }

    /**
     * Save Record.
     *
     * @param e Record to be saved
     * @return true if record was saved
     */
    public boolean save(@Nullable T e) {
        return save(getDatabaseName(), e);
    }

    /**
     * Save Record.
     *
     * @param databaseName database name to use
     * @param e            Record to be saved
     * @return true if record was saved
     */
    public boolean save(@Nonnull String databaseName, @Nullable T e) {
//        return save(getWritableDatabase(databaseName), e);
//    }
//
//    /**
//     * Save Record.
//     *
//     * @param db database for the record to be saved to
//     * @param e  Record to be saved
//     * @return true if record was saved
//     */
//    public boolean save(@Nonnull DatabaseWrapper<? super AndroidBaseRecord, ? super DBToolsContentValues<?>> db, @Nullable T e) {
        if (e == null) {
            return false;
        }

        if (e.isNewRecord()) {
            long newId = insert(databaseName, e);
            return newId > 0;
        } else {
            int count = update(databaseName, e);
            return count != 0;
        }
    }

    /**
     * Insert record into database.
     *
     * @param e record to be inserted
     * @return long value of new id
     */
    public long insert(@Nullable T e) {
        return insert(getDatabaseName(), e);
    }

    /**
     * Insert record into database.
     *
     * @param e record to be inserted
     * @return long value of new id
     */
    public long insert(@Nonnull String databaseName, @Nullable T e) {
//        return insert(getWritableDatabase(databaseName), e);
//    }
//
//    /**
//     * Insert record into database.
//     *
//     * @param db database for the record inserted into
//     * @param e  record to be inserted
//     * @return long value of new id
//     */
//    public long insert(@Nonnull DatabaseWrapper<? super AndroidBaseRecord, ? super DBToolsContentValues<?>> db, @Nullable T e) {
        DatabaseWrapper<? super AndroidBaseRecord, ? super DBToolsContentValues<?>> db = getWritableDatabase(databaseName);

        if (e == null) {
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
                e.bindInsertStatement(statement);
                rowId = statement.executeInsert();

                e.setPrimaryKeyId(rowId);

                notifyTableListeners(false, db, new DatabaseTableChange(getTableName(), rowId, true, false, false));

                success = true;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } // retry
        return rowId;
    }

    public int update(@Nullable T e) {
        return update(getDatabaseName(), e);
    }

    public int update(@Nonnull String databaseName, @Nullable T e) {
//        return update(getWritableDatabase(databaseName), e);
//    }
//
//    /**
//     * Update a record
//     *
//     * @param db database
//     * @param e  record
//     * @return number of rows effected
//     */
//    public int update(@Nonnull DatabaseWrapper<? super AndroidBaseRecord, ? super DBToolsContentValues<?>> db, @Nullable T e) {
        DatabaseWrapper<? super AndroidBaseRecord, ? super DBToolsContentValues<?>> db = getWritableDatabase(databaseName);

        if (e == null) {
            return 0;
        }

        checkDB(db);
        long rowId = e.getPrimaryKeyId();
        if (rowId <= 0) {
            throw new IllegalArgumentException("Invalid rowId [" + rowId + "] be sure to call create(...) before update(...)");
        }

        // Statement
        StatementWrapper statement = getUpdateStatement(db);
        e.bindUpdateStatement(statement);
        int rowsAffectedCount = statement.executeUpdateDelete();

        if (rowsAffectedCount > 0) {
            notifyTableListeners(false, db, new DatabaseTableChange(getTableName(), rowId, false, true, false));
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
//        return update(getWritableDatabase(databaseName), contentValues, where, whereArgs);
//    }
//
//    public int update(@Nonnull DatabaseWrapper<? super AndroidBaseRecord, ? super DBToolsContentValues<?>> db, @Nonnull DBToolsContentValues<?> contentValues, @Nullable String where, @Nullable String[] whereArgs) {
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
            notifyTableListeners(false, db, new DatabaseTableChange(getTableName(), false, true, false));
        }

        return rowsAffectedCount;
    }

    public int delete(@Nullable T e) {
        return delete(getDatabaseName(), e);
    }

    public int delete(@Nonnull String databaseName, @Nullable T e) {
//        return delete(getWritableDatabase(databaseName), e);
//    }
//
//    public int delete(@Nonnull DatabaseWrapper<? super AndroidBaseRecord, ? super DBToolsContentValues<?>> db, @Nullable T e) {

        if (e == null) {
            return 0;
        }

        long rowId = e.getPrimaryKeyId();
        if (rowId <= 0) {
            throw new IllegalArgumentException("Invalid rowId [" + rowId + "]");
        }

        return delete(databaseName, e.getIdColumnName() + " = ?", new String[]{String.valueOf(rowId)});
    }

    public int delete(long rowId) {
        return delete(getPrimaryKey() + " = ?", new String[]{String.valueOf(rowId)});
    }

    public int delete(@Nullable String where, @Nullable String[] whereArgs) {
        return delete(getDatabaseName(), where, whereArgs);
    }

    public int delete(@Nonnull String databaseName, @Nullable String where, @Nullable String[] whereArgs) {
//        return delete(getWritableDatabase(databaseName), where, whereArgs);
//    }
//
//    public int delete(@Nonnull DatabaseWrapper<? super AndroidBaseRecord, ? super DBToolsContentValues<?>> db, @Nullable String where, @Nullable String[] whereArgs) {
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
            notifyTableListeners(false, db, new DatabaseTableChange(getTableName(), false, false, true));
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
        listenerLock.lock();
        try {
            tableChangeListeners.add(listener);
        } finally {
            listenerLock.unlock();
        }
    }

    public void removeTableChangeListener(DBToolsTableChangeListener listener) {
        listenerLock.lock();
        try {
            tableChangeListeners.remove(listener);
        } finally {
            listenerLock.unlock();
        }
    }

    public void clearTableChangeListeners() {
        listenerLock.lock();
        try {
            tableChangeListeners.clear();
        } finally {
            listenerLock.unlock();
        }
    }

    private void notifyTableListeners(boolean forceNotify, @Nullable DatabaseWrapper<? super AndroidBaseRecord, ? super DBToolsContentValues<?>> db, @Nonnull DatabaseTableChange changeType) {
        updateLastTableModifiedTs();

        if (forceNotify || !(db != null && db.inTransaction())) {
            tableChanges.onNext(changeType);

            listenerLock.lock();
            try {
                for (DBToolsTableChangeListener tableChangeListener : tableChangeListeners) {
                    tableChangeListener.onTableChange(changeType);
                }
            } finally {
                listenerLock.unlock();
            }
        } else {
            if (changeType.isInsert()) {
                transactionInsertOccurred.set(true);
            } else if (changeType.isUpdate()) {
                transactionUpdateOccurred.set(true);
            } else if (changeType.isDelete()) {
                transactionDeleteOccurred.set(true);
            }
        }
    }

    // ===== Observables =====

    public Observable<DatabaseTableChange> tableChanges() {
        return tableChanges.asObservable();
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
