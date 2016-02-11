package org.dbtools.android.domain;

import android.content.ContentValues;
import android.database.sqlite.SQLiteStatement;
import org.dbtools.android.domain.database.DatabaseWrapper;
import org.dbtools.android.domain.event.DatabaseDeleteEvent;
import org.dbtools.android.domain.event.DatabaseEndTransactionEvent;
import org.dbtools.android.domain.event.DatabaseInsertEvent;
import org.dbtools.android.domain.event.DatabaseUpdateEvent;
import org.dbtools.android.domain.task.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("UnusedDeclaration")
public abstract class AndroidBaseManagerWritable<T extends AndroidBaseRecord> extends AndroidBaseManager<T> implements AsyncManager<T> {

    // use static to get ALL tables across ALL managers... by Database <DatabaseName, Set of table names>
    private static final Map<String, Set<String>> transactionChangesTableNamesMap = new HashMap<String, Set<String>>();


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
        if (success) {
            getWritableDatabase(databaseName).setTransactionSuccessful();
        }

        // get list of changed table names
        Set<String> tableNameChanges = transactionChangesTableNamesMap.get(databaseName);
        transactionChangesTableNamesMap.remove(databaseName);

        // end transaction
        getWritableDatabase(databaseName).endTransaction();

        // post end transaction event
        postEndTransactionEvent(success, getDatabaseName(), tableNameChanges);
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
                rowId = db.insert(getTableName(), null, e.getContentValues());
                e.setPrimaryKeyId(rowId);
                postInsertEvent(db, getTableName(), rowId);
                success = true;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

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

    public long insert(@Nonnull SQLiteStatement statement, @Nullable T e) {
        if (e == null) {
            return 0;
        }

        ContentValues contentValues = e.getContentValues();
        int bindItemCount = 1;
        for (String key : e.getAllColumns()) {
            Object objKey = contentValues.get(key);
            if (objKey instanceof Long) {
                statement.bindLong(bindItemCount, (Long) objKey);
            } else if (objKey instanceof Integer) {
                statement.bindLong(bindItemCount, (Integer) objKey);
            } else if (objKey instanceof String) {
                statement.bindString(bindItemCount, (String) objKey);
            } else if (objKey instanceof Double) {
                statement.bindDouble(bindItemCount, (Double) objKey);
            } else if (objKey instanceof Float) {
                statement.bindDouble(bindItemCount, (Float) objKey);
            } else if (objKey == null) {
                statement.bindNull(bindItemCount);
            } else {
                throw new IllegalStateException("Cannot bind [" + key + "] to statement");
            }

            bindItemCount++;
        }

        long rowId = statement.executeInsert();
        e.setPrimaryKeyId(rowId);
        postInsertEvent(null, getTableName(), rowId);
        return rowId;
    }

    public int update(@Nullable T e) {
        return update(getDatabaseName(), e);
    }

    public int update(@Nonnull String databaseName, @Nullable T e) {
        return update(getWritableDatabase(databaseName), e);
    }

    public int update(@Nonnull DatabaseWrapper db, @Nullable T e) {
        if (e == null) {
            return 0;
        }

        checkDB(db);
        long rowId = e.getPrimaryKeyId();
        if (rowId <= 0) {
            throw new IllegalArgumentException("Invalid rowId [" + rowId + "] be sure to call create(...) before update(...)");
        }

        return update(db, e.getContentValues(), e.getIdColumnName() + " = ?", new String[]{String.valueOf(rowId)});
    }

    public int update(@Nonnull ContentValues values, long rowId) {
        return update(getDatabaseName(), values, getPrimaryKey() + " = ?", new String[]{String.valueOf(rowId)});
    }

    public int update(@Nonnull ContentValues values, @Nullable String where, @Nullable String[] whereArgs) {
        return update(getDatabaseName(), values, where, whereArgs);
    }

    public int update(@Nonnull String databaseName, @Nonnull ContentValues contentValues, @Nullable String where, @Nullable String[] whereArgs) {
        return update(getWritableDatabase(databaseName), contentValues, where, whereArgs);
    }

    public int update(@Nonnull DatabaseWrapper db, @Nonnull ContentValues contentValues, @Nullable String where, @Nullable String[] whereArgs) {
        int rowCountAffected = 0;

        checkDB(db);
        // Make sure that if there is an error (LockedException), that we try again.
        boolean success = false;
        for (int tryCount = 0; tryCount < MAX_TRY_COUNT && !success; tryCount++) {
            try {
                rowCountAffected = db.update(getTableName(), contentValues, where, whereArgs);
                postUpdateEvent(db, getTableName(), rowCountAffected);
                success = true;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        return rowCountAffected;
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

    public void updateAsync(@Nonnull ContentValues contentValues, long rowId) {
        updateAsync(getDatabaseName(), contentValues, getPrimaryKey() + "= ?", new String[]{String.valueOf(rowId)});
    }

    public void updateAsync(@Nonnull ContentValues contentValues, @Nullable String where, @Nullable String[] whereArgs) {
        updateAsync(getDatabaseName(),  contentValues, where, whereArgs);
    }

    public void updateAsync(@Nonnull String databaseName, @Nonnull ContentValues contentValues, @Nullable String where, @Nullable String[] whereArgs) {
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
                postDeleteEvent(db, getTableName(), rowCountAffected);
                success = true;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
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

    private void postInsertEvent(@Nullable DatabaseWrapper db, @Nonnull String tableName, long rowId) {
        DBToolsEventBus bus = getBus();
        if (bus != null) {
            if (!(db != null && db.inTransaction())) {
                bus.post(new DatabaseInsertEvent(tableName, rowId));
            } else {
                addTransactionTableNameChange(tableName);
            }
        }
    }

    private void postUpdateEvent(@Nonnull DatabaseWrapper db, @Nonnull String tableName, int rowCountAffected) {
        DBToolsEventBus bus = getBus();
        if (bus != null) {
            if (!db.inTransaction()) {
                bus.post(new DatabaseUpdateEvent(tableName, rowCountAffected));
            } else {
                addTransactionTableNameChange(tableName);
            }
        }
    }

    private void postDeleteEvent(@Nonnull DatabaseWrapper db, @Nonnull String tableName, int rowCountAffected) {
        DBToolsEventBus bus = getBus();
        if (bus != null) {
            if (!db.inTransaction()) {
                bus.post(new DatabaseDeleteEvent(tableName, rowCountAffected));
            } else {
                addTransactionTableNameChange(tableName);
            }
        }
    }

    private void postEndTransactionEvent(boolean success, String databaseName, Set<String> tableNameChanges) {
        DBToolsEventBus bus = getBus();
        if (bus != null && tableNameChanges != null) {
            bus.post(new DatabaseEndTransactionEvent(success, databaseName, tableNameChanges));
        }
    }

    private void addTransactionTableNameChange(String tableName) {
        String databaseName = getDatabaseName();

        Set<String> transactionChangesTableNames;
        synchronized (transactionChangesTableNamesMap) {
            transactionChangesTableNames = transactionChangesTableNamesMap.get(databaseName);

            // if transactionChangesTableNames does not exist... create!
            if (transactionChangesTableNames == null) {
                transactionChangesTableNames = new HashSet<String>();
                transactionChangesTableNamesMap.put(databaseName, transactionChangesTableNames);
            }
        }

        transactionChangesTableNames.add(tableName);
    }
}
