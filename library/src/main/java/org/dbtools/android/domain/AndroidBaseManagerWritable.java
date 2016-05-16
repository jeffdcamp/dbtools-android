package org.dbtools.android.domain;

import org.dbtools.android.domain.database.DatabaseWrapper;
import org.dbtools.android.domain.database.contentvalues.DBToolsContentValues;
import org.dbtools.android.domain.database.statement.StatementWrapper;
import org.dbtools.android.domain.task.DeleteTask;
import org.dbtools.android.domain.task.DeleteWhereTask;
import org.dbtools.android.domain.task.InsertTask;
import org.dbtools.android.domain.task.UpdateTask;
import org.dbtools.android.domain.task.UpdateWhereTask;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@SuppressWarnings("UnusedDeclaration")
public abstract class AndroidBaseManagerWritable<T extends AndroidBaseRecord> extends AndroidBaseManager<T> implements AsyncManager<T> {

    private final AtomicBoolean transactionInsertOccurred = new AtomicBoolean(false);
    private final AtomicBoolean transactionUpdateOccurred = new AtomicBoolean(false);
    private final AtomicBoolean transactionDeleteOccurred = new AtomicBoolean(false);

    private final List<DBToolsTableChangeListener> tableChangeListeners = new ArrayList<DBToolsTableChangeListener>();

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
        DatabaseWrapper db = getWritableDatabase(databaseName);
        if (success) {
            db.setTransactionSuccessful();
        }

        // determine if there are changes
        DatabaseTableChange tableChange = new DatabaseTableChange(transactionInsertOccurred.get(), transactionUpdateOccurred.get(), transactionDeleteOccurred.get());
        transactionInsertOccurred.set(false);
        transactionUpdateOccurred.set(false);
        transactionDeleteOccurred.set(false);

        // end transaction
        db.endTransaction();

        // post end transaction event
        if (tableChange.hasChange()) {
            notifyTableListeners(db, tableChange);
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
     * @param e Record to be saved
     * @return true if record was saved
     */
    public boolean save(@Nonnull String databaseName, @Nullable T e) {
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
     * Save Record.
     *
     * @param db database for the record to be saved to
     * @param e  Record to be saved
     * @return true if record was saved
     */
    public boolean save(@Nonnull DatabaseWrapper db, @Nullable T e) {
        if (e == null) {
            return false;
        }

        if (e.isNewRecord()) {
            long newId = insert(db, e);
            return newId != 0;
        } else {
            int count = update(db, e);
            return count != 0;
        }
    }

    public void saveAsync(@Nullable T e) {
        saveAsync(getDatabaseName(), e);
    }

    public void saveAsync(@Nonnull String databaseName, @Nullable T e) {
        if (e == null) {
            return;
        }

        if (e.isNewRecord()) {
            insertAsync(databaseName, e);
        } else {
            updateAsync(databaseName, e);
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
        return insert(getWritableDatabase(databaseName), e);
    }

    /**
     * Insert record into database.
     *
     * @param db database for the record inserted into
     * @param e  record to be inserted
     * @return long value of new id
     */
    public long insert(@Nonnull DatabaseWrapper db, @Nullable T e) {
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

                notifyTableListeners(db, new DatabaseTableChange(true, false, false));

                success = true;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } // retry
        return rowId;
    }

    public void insertAsync(@Nullable T e) {
        insertAsync(getDatabaseName(), e);
    }

    public void insertAsync(@Nonnull String databaseName, @Nullable T e) {
        if (e == null) {
            return;
        }

        AndroidDatabase androidDatabase = getAndroidDatabase(databaseName);
        androidDatabase.getManagerExecutorServiceInstance().submit(new InsertTask<T>(databaseName, this, e));
    }

    public int update(@Nullable T e) {
        return update(getDatabaseName(), e);
    }

    public int update(@Nonnull String databaseName, @Nullable T e) {
        return update(getWritableDatabase(databaseName), e);
    }

    /**
     * Update a record
     * @param db database
     * @param e record
     * @return number of rows effected
     */
    public int update(@Nonnull DatabaseWrapper db, @Nullable T e) {
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
            notifyTableListeners(db, new DatabaseTableChange(false, true, false));
        }

        return rowsAffectedCount;
    }

    public int update(@Nonnull DBToolsContentValues values, long rowId) {
        return update(getDatabaseName(), values, getPrimaryKey() + " = ?", new String[]{String.valueOf(rowId)});
    }

    public int update(@Nonnull DBToolsContentValues values, @Nullable String where, @Nullable String[] whereArgs) {
        return update(getDatabaseName(), values, where, whereArgs);
    }

    public int update(@Nonnull String databaseName, @Nonnull DBToolsContentValues contentValues, @Nullable String where, @Nullable String[] whereArgs) {
        return update(getWritableDatabase(databaseName), contentValues, where, whereArgs);
    }

    public int update(@Nonnull DatabaseWrapper db, @Nonnull DBToolsContentValues contentValues, @Nullable String where, @Nullable String[] whereArgs) {
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
            notifyTableListeners(db, new DatabaseTableChange(false, true, false));
        }

        return rowsAffectedCount;
    }

    public void updateAsync(@Nullable T e) {
        updateAsync(getDatabaseName(), e);
    }

    public void updateAsync(@Nonnull String databaseName, @Nullable T e) {
        if (e == null) {
            return;
        }

        AndroidDatabase androidDatabase = getAndroidDatabase(databaseName);
        androidDatabase.getManagerExecutorServiceInstance().submit(new UpdateTask<T>(databaseName, this, e));
    }

    public void updateAsync(@Nonnull DBToolsContentValues contentValues, long rowId) {
        updateAsync(getDatabaseName(), contentValues, getPrimaryKey() + "= ?", new String[]{String.valueOf(rowId)});
    }

    public void updateAsync(@Nonnull DBToolsContentValues contentValues, @Nullable String where, @Nullable String[] whereArgs) {
        updateAsync(getDatabaseName(),  contentValues, where, whereArgs);
    }

    public void updateAsync(@Nonnull String databaseName, @Nonnull DBToolsContentValues contentValues, @Nullable String where, @Nullable String[] whereArgs) {
        AndroidDatabase androidDatabase = getAndroidDatabase(databaseName);
        androidDatabase.getManagerExecutorServiceInstance().submit(new UpdateWhereTask<T>(databaseName, this, contentValues, where, whereArgs));
    }

    public int delete(@Nullable T e) {
        return delete(getDatabaseName(), e);
    }

    public int delete(@Nonnull String databaseName, @Nullable T e) {
        return delete(getWritableDatabase(databaseName), e);
    }

    public int delete(@Nonnull DatabaseWrapper db, @Nullable T e) {
        if (e == null) {
            return 0;
        }

        checkDB(db);
        long rowId = e.getPrimaryKeyId();
        if (rowId <= 0) {
            throw new IllegalArgumentException("Invalid rowId [" + rowId + "]");
        }

        return delete(db, e.getIdColumnName() + " = ?", new String[]{String.valueOf(rowId)});
    }

    public int delete(long rowId) {
        return delete(getPrimaryKey() + " = ?", new String[]{String.valueOf(rowId)});
    }

    public int delete(@Nullable String where, @Nullable String[] whereArgs) {
        return delete(getDatabaseName(), where, whereArgs);
    }

    public int delete(@Nonnull String databaseName, @Nullable String where, @Nullable String[] whereArgs) {
        return delete(getWritableDatabase(databaseName), where, whereArgs);
    }

    public int delete(@Nonnull DatabaseWrapper db, @Nullable String where, @Nullable String[] whereArgs) {
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
            notifyTableListeners(db, new DatabaseTableChange(false, false, true));
        }

        return rowCountAffected;
    }

    public void deleteAsync(@Nullable T e) {
        deleteAsync(getDatabaseName(), e);
    }

    public void deleteAsync(@Nonnull String databaseName, @Nullable T e) {
        if (e == null) {
            return;
        }

        AndroidDatabase androidDatabase = getAndroidDatabase(databaseName);
        androidDatabase.getManagerExecutorServiceInstance().submit(new DeleteTask<T>(databaseName, this, e));
    }

    public void deleteAsync(@Nullable String where, @Nullable String[] whereArgs) {
        deleteAsync(getDatabaseName(), where, whereArgs);
    }

    public void deleteAsync(@Nonnull String databaseName, @Nullable String where, @Nullable String[] whereArgs) {
        AndroidDatabase androidDatabase = getAndroidDatabase(databaseName);
        androidDatabase.getManagerExecutorServiceInstance().submit(new DeleteWhereTask<T>(databaseName, this, where, whereArgs));
    }

    public long deleteAll() {
        return deleteAll(getDatabaseName());
    }

    public long deleteAll(@Nonnull String databaseName) {
        return delete(getWritableDatabase(databaseName), null, null);
    }

    public void deleteAllAsync() {
        deleteAllAsync(getDatabaseName());
    }

    public void deleteAllAsync(@Nonnull String databaseName) {
        AndroidDatabase androidDatabase = getAndroidDatabase(databaseName);
        androidDatabase.getManagerExecutorServiceInstance().submit(new DeleteWhereTask<T>(databaseName, this, null, null));
    }

    // ===== Listeners =====

    public void addTableChangeListener(DBToolsTableChangeListener listener) {
        tableChangeListeners.add(listener);
    }

    public void removeTableChangeListener(DBToolsTableChangeListener listener) {
        tableChangeListeners.remove(listener);
    }

    public void clearTableChangeListeners() {
        tableChangeListeners.clear();
    }

    private void notifyTableListeners(@Nullable DatabaseWrapper db, @Nonnull DatabaseTableChange changeType) {
        if (!(db != null && db.inTransaction())) {
            for (DBToolsTableChangeListener tableChangeListener : tableChangeListeners) {
                tableChangeListener.onTableChange(changeType);
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
}
