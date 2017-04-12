package org.dbtools.android.domain;

import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.MergeCursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.dbtools.android.domain.database.DatabaseWrapper;
import org.dbtools.android.domain.database.contentvalues.DBToolsContentValues;
import org.dbtools.android.domain.database.statement.StatementWrapper;
import org.dbtools.android.domain.dbtype.DatabaseValue;
import org.dbtools.android.domain.dbtype.DatabaseValueUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("UnusedDeclaration")
public abstract class AndroidBaseManager<T extends AndroidBaseRecord> {

    public static final int MAX_TRY_COUNT = 3;
    public static final String DEFAULT_COLLATE_LOCALIZED = " COLLATE LOCALIZED";

    private final AndroidDatabaseManager androidDatabaseManager;

    protected AndroidBaseManager(AndroidDatabaseManager androidDatabaseManager) {
        this.androidDatabaseManager = androidDatabaseManager;
    }

    public AndroidDatabaseManager getAndroidDatabaseManager() {
        return androidDatabaseManager;
    }
    
    @NonNull
    public DatabaseWrapper<? super AndroidBaseRecord, ? super DBToolsContentValues<?>> getReadableDatabase(@NonNull String databaseName) {
        return androidDatabaseManager.getReadableDatabase(databaseName);
    }

    @NonNull
    public DatabaseWrapper<? super AndroidBaseRecord, ? super DBToolsContentValues<?>> getReadableDatabase() {
        return androidDatabaseManager.getReadableDatabase(getDatabaseName());
    }

    @NonNull
    public DatabaseWrapper<? super AndroidBaseRecord, ? super DBToolsContentValues<?>> getWritableDatabase(@NonNull String databaseName) {
        return androidDatabaseManager.getWritableDatabase(databaseName);
    }

    @NonNull
    public DatabaseWrapper<? super AndroidBaseRecord, ? super DBToolsContentValues<?>> getWritableDatabase() {
        return androidDatabaseManager.getWritableDatabase(getDatabaseName());
    }

    @Nullable
    public org.dbtools.android.domain.AndroidDatabase getAndroidDatabase(@NonNull String databaseName) {
        return androidDatabaseManager.getDatabase(databaseName);
    }

    public org.dbtools.android.domain.config.DatabaseConfig getDatabaseConfig() {
        return androidDatabaseManager.getDatabaseConfig();
    }

    public abstract String getDatabaseName();

    @NonNull
    public abstract String getTableName();

    public abstract String getPrimaryKey();

    public abstract String[] getAllColumns();

    public abstract String getDropSql();

    public abstract String getCreateSql();

    public abstract String getInsertSql();

    public abstract String getUpdateSql();

    public abstract T newRecord();

    /**
     * Return a database/platform specific version of DBToolsContentValues
     * Example:
     * - For Android platform, AndroidDBToolsContentValues
     * - For JDBC, JdbcDBToolsContentValues
     *
     * @return new instance of DBToolsContentValues
     */
    public DBToolsContentValues createNewDBToolsContentValues() {
        return getDatabaseConfig().createNewDBToolsContentValues();
    }

    public void createTable() {
        executeSql(getDatabaseName(), getCreateSql());
    }

    public void createTable(@NonNull String databaseName) {
        executeSql(databaseName, getCreateSql());
    }

    public static  void createTable(@NonNull DatabaseWrapper<? super AndroidBaseRecord, ? super DBToolsContentValues<?>> db, String sql) {
        executeSql(db, sql);
    }

    public void dropTable() {
        executeSql(getDatabaseName(), getDropSql());
    }

    public void dropTable(@NonNull String databaseName) {
        executeSql(databaseName, getDropSql());
    }

    // for use with enum records
    public static void dropTable(@NonNull DatabaseWrapper<? super AndroidBaseRecord, ? super DBToolsContentValues<?>> db, @NonNull String sql) {
        executeSql(db, sql);
    }

    public void cleanTable() {
        cleanTable(getDatabaseName());
    }

    public void cleanTable(@NonNull String databaseName) {
        cleanTable(databaseName, getDropSql(), getCreateSql());
    }

    public void cleanTable(@NonNull String dropSql, @NonNull String createSql) {
        cleanTable(getDatabaseName(), dropSql, createSql);
    }

    public void cleanTable(@NonNull String databaseName, @NonNull String dropSql, @NonNull String createSql) {
        DatabaseWrapper<? super AndroidBaseRecord, ? super DBToolsContentValues<?>> db = getWritableDatabase(databaseName);

        checkDB(db);
        executeSql(db, dropSql);
        executeSql(db, createSql);
    }

    public StatementWrapper getInsertStatement(@NonNull DatabaseWrapper<? super AndroidBaseRecord, ? super DBToolsContentValues<?>> db) {
        return db.getInsertStatement(getTableName(), getInsertSql());
    }

    public StatementWrapper getUpdateStatement(@NonNull DatabaseWrapper<? super AndroidBaseRecord, ? super DBToolsContentValues<?>> db) {
        return db.getUpdateStatement(getTableName(), getUpdateSql());
    }

    public void executeSql(@NonNull String sql) {
        executeSql(getDatabaseName(), sql);
    }

    public void executeSql(@NonNull String sql, @Nullable Object[] bindArgs) {
        executeSql(getDatabaseName(), sql, bindArgs);
    }

    public void executeSql(@NonNull String databaseName, @NonNull String sql, @Nullable Object[] bindArgs) {
        executeSql(getWritableDatabase(databaseName), sql, bindArgs);
    }

    public void executeSql(@NonNull String databaseName, @NonNull String sql) {
        executeSql(getWritableDatabase(databaseName), sql);
    }

    public static void executeSql(@NonNull AndroidDatabase androidDatabase, @NonNull String sql) {
        executeSql(androidDatabase.getDatabaseWrapper(), sql);
    }

    public static void executeSql(@Nullable DatabaseWrapper<? super AndroidBaseRecord, ? super DBToolsContentValues<?>> db, @NonNull String sql) {
        executeSql(db, sql, null);
    }

    public static void executeSql(@Nullable DatabaseWrapper<? super AndroidBaseRecord, ? super DBToolsContentValues<?>> db, @NonNull String sql, @Nullable Object[] bindArgs) {
        checkDB(db);

        String[] sqlStatements = sql.split(";");

        for (String sqlStatement : sqlStatements) {
            if (sqlStatement.length() > 0) {
                if (bindArgs != null) {
                    db.execSQL(sqlStatement, bindArgs);
                } else {
                    db.execSQL(sqlStatement);
                }
            }
        }
    }

    protected static void checkDB(@Nullable DatabaseWrapper db) {
        if (db == null) {
            throw new IllegalArgumentException("db cannot be null");
        }
    }

    @Nullable
    public Cursor findCursorAll() {
        return findCursorBySelection(null, null);
    }

    @Nullable
    public Cursor findCursorByRawQuery(@NonNull String rawQuery, @Nullable String[] selectionArgs) {
        return findCursorByRawQuery(getDatabaseName(), rawQuery, selectionArgs);
    }

    @Nullable
    public Cursor findCursorByRawQuery(@NonNull String databaseName, @NonNull String rawQuery, @Nullable String[] selectionArgs) {
        return getReadableDatabase(databaseName).rawQuery(rawQuery, selectionArgs);
    }

    @Nullable
    public Cursor findCursorBySelection(@Nullable String selection, @Nullable String orderBy) {
        return findCursorBySelection(selection, new String[]{}, orderBy);
    }

    @Nullable
    public Cursor findCursorBySelection(@NonNull String databaseName, @Nullable String selection, @Nullable String orderBy) {
        return findCursorBySelection(databaseName, selection, null, orderBy);
    }

    @Nullable
    public Cursor findCursorBySelection(@Nullable String selection, @Nullable String[] selectionArgs, @Nullable String orderBy) {
        return findCursorBySelection(getDatabaseName(), selection, selectionArgs, orderBy);
    }

    @Nullable
    public Cursor findCursorBySelection(@NonNull String databaseName, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String orderBy) {
        return findCursorBySelection(databaseName, true, getTableName(), getAllColumns(), selection, selectionArgs, null, null, orderBy, null);
    }

    @Nullable
    public Cursor findCursorBySelection(boolean distinct, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String groupBy, @Nullable String having, @Nullable String orderBy, @Nullable String limit) {
        return findCursorBySelection(getDatabaseName(), distinct, getTableName(), getAllColumns(), selection, selectionArgs, groupBy, having, orderBy, limit);
    }

    @Nullable
    public Cursor findCursorBySelection(@NonNull String databaseName, boolean distinct, @NonNull String table, @NonNull String[] columns, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String groupBy, @Nullable String having, @Nullable String orderBy, @Nullable String limit) {
        Cursor cursor = getReadableDatabase(databaseName).query(distinct, table, columns, selection, selectionArgs, groupBy, having, orderBy, limit);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                return cursor;
            } else {
                cursor.close();
                return null;
            }
        } else {
            return null;
        }
    }

    @Nullable
    public Cursor findCursorByRowId(long rowId) {
        return findCursorBySelection(getPrimaryKey() + "= ?", new String[]{String.valueOf(rowId)}, null);
    }

    @Nullable
    public Cursor findCursorByRowId(@NonNull String databaseName, long rowId) {
        return findCursorBySelection(databaseName, getPrimaryKey() + "= ?", new String[]{String.valueOf(rowId)}, null);
    }

    @NonNull
    public List<T> findAll() {
        return findAllBySelection(null, null, null);
    }

    @NonNull
    public List<T> findAll(@NonNull String databaseName) {
        return findAllBySelection(databaseName, null, null, null);
    }

    @NonNull
    public List<T> findAllOrderBy(@Nullable String orderBy) {
        return findAllBySelection(null, null, orderBy);
    }

    @NonNull
    public List<T> findAllOrderBy(@NonNull String databaseName, @Nullable String orderBy) {
        return findAllBySelection(databaseName, null, null, orderBy);
    }

    @NonNull
    public List<T> findAllBySelection(@Nullable String selection, @NonNull String[] selectionArgs) {
        return findAllBySelection(selection, selectionArgs, null);
    }

    @NonNull
    public List<T> findAllBySelection(@Nullable String selection, @Nullable String[] selectionArgs, @Nullable String orderBy) {
        return findAllBySelection(selection, selectionArgs, null, null, orderBy, null);
    }

    @NonNull
    public List<T> findAllBySelection(@Nullable String selection, @Nullable String[] selectionArgs, @Nullable String groupBy, @Nullable String having, @Nullable String orderBy, @Nullable String limit) {
        return findAllBySelection(getDatabaseName(), true, getTableName(), getAllColumns(), selection, selectionArgs, groupBy, having, orderBy, limit);
    }

    @NonNull
    public List<T> findAllBySelection(@NonNull String databaseName, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String orderBy) {
        return findAllBySelection(databaseName, true, getTableName(), getAllColumns(), selection, selectionArgs, null, null, orderBy, null);
    }

    @NonNull
    public List<T> findAllBySelection(@NonNull String databaseName, boolean distinct, @NonNull String table, @NonNull String[] columns, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String groupBy, @Nullable String having, @Nullable String orderBy, @Nullable String limit) {
        Cursor cursor = findCursorBySelection(databaseName, distinct, table, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
        return getAllItemsFromCursor(cursor);
    }

    /**
     * Populate of List from a rawQuery.  The raw query must contain all of the columns names for the object
     *
     * @param rawQuery Custom query
     * @return List of object T
     */
    @NonNull
    public List<T> findAllByRawQuery(@NonNull String rawQuery) {
        return getAllItemsFromCursor(findCursorByRawQuery(getDatabaseName(), rawQuery, null));
    }

    /**
     * Populate of List from a rawQuery.  The raw query must contain all of the columns names for the object
     *
     * @param rawQuery      Custom query
     * @param selectionArgs query arguments
     * @return List of object T
     */
    @NonNull
    public List<T> findAllByRawQuery(@NonNull String rawQuery, @Nullable String[] selectionArgs) {
        return getAllItemsFromCursor(findCursorByRawQuery(getDatabaseName(), rawQuery, selectionArgs));
    }

    /**
     * Populate of List from a rawQuery.  The raw query must contain all of the columns names for the object
     *
     * @param databaseName  Name of database
     * @param rawQuery      Custom query
     * @param selectionArgs query arguments
     * @return List of object T
     */
    @NonNull
    public List<T> findAllByRawQuery(@NonNull String databaseName, @NonNull String rawQuery, @Nullable String[] selectionArgs) {
        return getAllItemsFromCursor(findCursorByRawQuery(databaseName, rawQuery, selectionArgs));
    }

    @NonNull
    public List<T> getAllItemsFromCursor(@Nullable Cursor cursor) {
        List<T> foundItems;
        if (cursor != null) {
            foundItems = new ArrayList<>(cursor.getCount());
            if (cursor.moveToFirst()) {
                do {
                    T record = newRecord();
                    record.setContent(cursor);
                    foundItems.add(record);
                } while (cursor.moveToNext());
            }
            cursor.close();
        } else {
            foundItems = new ArrayList<>();
        }

        return foundItems;
    }

    @Nullable
    public T findByRowId(long rowId) {
        return findBySelection(getPrimaryKey() + " = ?", new String[]{String.valueOf(rowId)}, null);
    }

    @Nullable
    public T findByRowId(@NonNull String databaseName, long rowId) {
        return findBySelection(databaseName, getPrimaryKey() + " = ?", new String[]{String.valueOf(rowId)}, null);
    }

    @Nullable
    public T findBySelection(@Nullable String selection, @Nullable String[] selectionArgs, @Nullable String orderBy) {
        return findBySelection(selection, selectionArgs, null, null, orderBy);
    }

    @Nullable
    public T findBySelection(@NonNull String databaseName, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String orderBy) {
        return findBySelection(databaseName, true, getTableName(), getAllColumns(), selection, selectionArgs, null, null, orderBy);
    }

    @Nullable
    public T findBySelection(@Nullable String selection, @Nullable String[] selectionArgs, @Nullable String groupBy, @Nullable String having, @Nullable String orderBy) {
        return findBySelection(getDatabaseName(), true, getTableName(), getAllColumns(), selection, selectionArgs, groupBy, having, orderBy);
    }

    @Nullable
    public T findBySelection(@NonNull String databaseName, boolean distinct, @NonNull String table, @NonNull String[] columns, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String groupBy, @Nullable String having, @Nullable String orderBy) {
        Cursor cursor = findCursorBySelection(databaseName, distinct, table, columns, selection, selectionArgs, groupBy, having, orderBy, "1");
        return createRecordFromCursor(cursor);
    }

    @Nullable
    public T findByRawQuery(@NonNull String rawQuery, @Nullable String[] selectionArgs) {
        return findByRawQuery(getDatabaseName(), rawQuery, selectionArgs);
    }

    @Nullable
    public T findByRawQuery(@NonNull String databaseName, @NonNull String rawQuery, @Nullable String[] selectionArgs) {
        Cursor cursor = findCursorByRawQuery(databaseName, rawQuery, selectionArgs);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                return createRecordFromCursor(cursor);
            } else {
                cursor.close();
                return null;
            }
        } else {
            return null;
        }
    }

    @Nullable
    public T createRecordFromCursor(@Nullable Cursor cursor) {
        if (cursor != null) {
            T record = newRecord();
            record.setContent(cursor);
            cursor.close();
            return record;
        } else {
            return null;
        }
    }

    @NonNull
    public List<T> findAllByRowIds(long[] rowIds) {
        return findAllByRowIds(rowIds, null);
    }

    @NonNull
    public List<T> findAllByRowIds(long[] rowIds, @Nullable String orderBy) {
        return findAllByRowIds(getDatabaseName(), rowIds, orderBy);
    }

    @NonNull
    public List<T> findAllByRowIds(@NonNull String databaseName, long[] rowIds, @Nullable String orderBy) {
        if (rowIds.length == 0) {
            return new ArrayList<T>();
        }

        StringBuilder sb = new StringBuilder();
        for (long rowId : rowIds) {
            if (sb.length() > 0) {
                sb.append(" OR ");
            }
            sb.append(getPrimaryKey()).append(" = ").append(rowId);
        }

        return findAllBySelection(databaseName, sb.toString(), null, orderBy);
    }

    public long findCount() {
        return findCountBySelection(null, null);
    }

    public long findCount(@NonNull String databaseName) {
        return findCountBySelection(databaseName, null, null);
    }

    public long findCountBySelection(@Nullable String selection, @Nullable String[] selectionArgs) {
        return findCountBySelection(getDatabaseName(), selection, selectionArgs);
    }

    public long findCountBySelection(@NonNull String databaseName, @Nullable String selection, @Nullable String[] selectionArgs) {
        return findCountBySelection(getReadableDatabase(databaseName), getTableName(), selection, selectionArgs);
    }

    public static long findCountBySelection(@NonNull DatabaseWrapper<? super AndroidBaseRecord, ? super DBToolsContentValues<?>> database, @NonNull String tableName, @Nullable String selection, @Nullable String[] selectionArgs) {
        long count = -1;

        Cursor c = database.query(tableName, new String[]{"count(1)"}, selection, selectionArgs, null, null, null);
        if (c != null) {
            if (c.moveToFirst()) {
                count = c.getInt(0); // sqlite reads/writes this value as an int
            }
            c.close();
        }
        return count;
    }

    /**
     * Find count by raw query.  Raw query assumes first SELECT param is count(1).
     *
     * @param rawQuery Query
     * @return total count
     */
    public long findCountByRawQuery(@NonNull String rawQuery) {
        return findCountByRawQuery(getDatabaseName(), rawQuery, null);
    }

    /**
     * Find count by raw query.  Raw query assumes first SELECT param is count(1).
     *
     * @param databaseName name of database to use
     * @param rawQuery     Query
     * @return total count
     */
    public long findCountByRawQuery(@NonNull String databaseName, @NonNull String rawQuery) {
        return findCountByRawQuery(databaseName, rawQuery, null);
    }

    /**
     * Find count by raw query.  Raw query assumes first SELECT param is count(1).
     *
     * @param rawQuery      Query
     * @param selectionArgs Selection args
     * @return total count
     */
    public long findCountByRawQuery(@NonNull String rawQuery, @Nullable String[] selectionArgs) {
        return findCountByRawQuery(getDatabaseName(), rawQuery, selectionArgs);
    }

    public long findCountByRawQuery(@NonNull String databaseName, @NonNull String rawQuery, @Nullable String[] selectionArgs) {
        return findCountByRawQuery(getReadableDatabase(databaseName), rawQuery, selectionArgs);
    }

    public static long findCountByRawQuery(@NonNull DatabaseWrapper<? super AndroidBaseRecord, ? super DBToolsContentValues<?>> database, @NonNull String rawQuery, @Nullable String[] selectionArgs) {
        long count = 0;
        Cursor c = database.rawQuery(rawQuery, selectionArgs);
        if (c != null) {
            if (c.moveToFirst()) {
                count = c.getLong(0);
            }
            c.close();
        }

        return count;
    }

    /**
     * Return the first column and first row value as the value for given rawQuery and selectionArgs.
     *
     * @param valueType     Type to be used when getting data from database and what type is used on return (Integer.class, Boolean.class, etc)
     * @param rawQuery      Query contain first column which is the needed value
     * @param selectionArgs Query parameters
     * @param defaultValue  Value returned if nothing is found
     * @param <I>           Type of value
     * @return query results value or defaultValue if no data was returned
     */
    public <I> I findValueByRawQuery(@NonNull Class<I> valueType, @NonNull String rawQuery, @Nullable String[] selectionArgs, I defaultValue) {
        return findValueByRawQuery(getDatabaseName(), valueType, rawQuery, selectionArgs, defaultValue);
    }

    /**
     * Return the first column and first row value as the value for given rawQuery and selectionArgs.
     *
     * @param databaseName  Name of database to query
     * @param valueType     Type to be used when getting data from database and what type is used on return (Integer.class, Boolean.class, etc)
     * @param rawQuery      Query contain first column which is the needed value
     * @param selectionArgs Query parameters
     * @param defaultValue  Value returned if nothing is found
     * @param <I>           Type of value
     * @return query results value or defaultValue if no data was returned
     */
    public <I> I findValueByRawQuery(@NonNull String databaseName, @NonNull Class<I> valueType, @NonNull String rawQuery, @Nullable String[] selectionArgs, I defaultValue) {
        return findValueByRawQuery(getReadableDatabase(databaseName), valueType, rawQuery, selectionArgs, defaultValue);
    }

    /**
     * Return the first column and first row value as a Date for given rawQuery and selectionArgs.
     *
     * @param database      DatabaseWrapper of database to query
     * @param valueType     Type to be used when getting data from database and what type is used on return (Integer.class, Boolean.class, etc)
     * @param rawQuery      Query contain first column which is the needed value
     * @param selectionArgs Query parameters
     * @param defaultValue  Value returned if nothing is found
     * @param <I>           Type of value
     * @return query results value or defaultValue if no data was returned
     */
    public static <I> I findValueByRawQuery(@NonNull DatabaseWrapper<? super AndroidBaseRecord, ? super DBToolsContentValues<?>> database, @NonNull Class<I> valueType, @NonNull String rawQuery, @Nullable String[] selectionArgs, I defaultValue) {
        DatabaseValue<I> databaseValue = getDatabaseValue(valueType);
        I value = defaultValue;

        Cursor c = database.rawQuery(rawQuery, selectionArgs);
        if (c != null) {
            if (c.moveToFirst()) {
                value = databaseValue.getColumnValue(c, 0, defaultValue);
            }
            c.close();
        }

        return value;
    }

    /**
     * Return the value for the specified column and first row value as given type for given selection and selectionArgs.
     *
     * @param valueType     Type to be used when getting data from database and what type is used on return (Integer.class, Boolean.class, etc)
     * @param column        Column which contains value
     * @param rowId         Primary key value (where clause)
     * @param defaultValue  Value returned if nothing is found
     * @param <I>           Type of value
     * @return query results value or defaultValue if no data was returned
     */
    public <I> I findValueByRowId(@NonNull Class<I> valueType, @NonNull String column, long rowId, I defaultValue) {
        return findValueBySelection(getDatabaseName(), valueType, column, getPrimaryKey() + " = " + rowId, null, defaultValue);
    }

    /**
     * Return the value for the specified column and first row value as given type for given selection and selectionArgs.
     *
     * @param valueType     Type to be used when getting data from database and what type is used on return (Integer.class, Boolean.class, etc)
     * @param column        Column which contains value
     * @param selection     Query selection
     * @param selectionArgs Query parameters
     * @param defaultValue  Value returned if nothing is found
     * @param <I>           Type of value
     * @return query results value or defaultValue if no data was returned
     */
    public <I> I findValueBySelection(@NonNull Class<I> valueType, @NonNull String column, @Nullable String selection, @Nullable String[] selectionArgs, I defaultValue) {
        return findValueBySelection(getDatabaseName(), valueType, column, selection, selectionArgs, defaultValue);
    }

    /**
     * Return the value for the specified column and first row value as given type for given selection and selectionArgs.
     *
     * @param valueType     Type to be used when getting data from database and what type is used on return (Integer.class, Boolean.class, etc)
     * @param column        Column which contains value
     * @param selection     Query selection
     * @param selectionArgs Query parameters
     * @param orderBy       Order by value(s)
     * @param defaultValue  Value returned if nothing is found
     * @param <I>           Type of value
     * @return query results value or defaultValue if no data was returned
     */
    public <I> I findValueBySelection(@NonNull Class<I> valueType, @NonNull String column, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String orderBy, I defaultValue) {
        return findValueBySelection(getDatabaseName(), valueType, column, selection, selectionArgs, orderBy, defaultValue);
    }

    /**
     * Return the value for the specified column and first row value as given type for given selection and selectionArgs.
     *
     * @param valueType     Type to be used when getting data from database and what type is used on return (Integer.class, Boolean.class, etc)
     * @param column        Column which contains value
     * @param selection     Query selection
     * @param selectionArgs Query parameters
     * @param orderBy       Order by value(s)
     * @param defaultValue  Value returned if nothing is found
     * @param <I>           Type of value
     * @return query results value or defaultValue if no data was returned
     */
    public <I> I findValueBySelection(@NonNull Class<I> valueType, @NonNull String column, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String groupBy, @Nullable String having, @Nullable String orderBy, I defaultValue) {
        return findValueBySelection(getDatabaseName(), valueType, column, selection, selectionArgs, groupBy, having, orderBy, defaultValue);
    }

    /**
     * Return the value for the specified column and first row value as given type for given selection and selectionArgs.
     *
     * @param databaseName  Name of database to query
     * @param valueType     Type to be used when getting data from database and what type is used on return (Integer.class, Boolean.class, etc)
     * @param column        Column which contains value
     * @param rowId         Primary key value (where clause)
     * @param defaultValue  Value returned if nothing is found
     * @param <I>           Type of value
     * @return query results value or defaultValue if no data was returned
     */
    public <I> I findValueBySelection(@NonNull String databaseName, @NonNull Class<I> valueType, @NonNull String column, long rowId, I defaultValue) {
        return findValueBySelection(databaseName, valueType, column, getPrimaryKey() + " = " + rowId, null, null, defaultValue);
    }

    /**
     * Return the value for the specified column and first row value as given type for given selection and selectionArgs.
     *
     * @param databaseName  Name of database to query
     * @param valueType     Type to be used when getting data from database and what type is used on return (Integer.class, Boolean.class, etc)
     * @param column        Column which contains value
     * @param selection     Query selection
     * @param selectionArgs Query parameters
     * @param defaultValue  Value returned if nothing is found
     * @param <I>           Type of value
     * @return query results value or defaultValue if no data was returned
     */
    public <I> I findValueBySelection(@NonNull String databaseName, @NonNull Class<I> valueType, @NonNull String column, @Nullable String selection, @Nullable String[] selectionArgs, I defaultValue) {
        return findValueBySelection(databaseName, valueType, column, selection, selectionArgs, null, defaultValue);
    }

    /**
     * Return the value for the specified column and first row value as given type for given selection and selectionArgs.
     *
     * @param databaseName  Name of database to query
     * @param valueType     Type to be used when getting data from database and what type is used on return (Integer.class, Boolean.class, etc)
     * @param column        Column which contains value
     * @param selection     Query selection
     * @param selectionArgs Query parameters
     * @param orderBy       Order by value(s)
     * @param defaultValue  Value returned if nothing is found
     * @param <I>           Type of value
     * @return query results value or defaultValue if no data was returned
     */
    public <I> I findValueBySelection(@NonNull String databaseName, @NonNull Class<I> valueType, @NonNull String column, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String orderBy, I defaultValue) {
        return findValueBySelection(databaseName, valueType, column, selection, selectionArgs, null, null, orderBy, defaultValue);
    }

//    /**
//     * Return the value for the specified column and first row value as given type for given selection and selectionArgs.
//     *
//     * @param database      DatabaseWrapper of database to query
//     * @param tableName     Table to run query against
//     * @param valueType     Type to be used when getting data from database and what type is used on return (Integer.class, Boolean.class, etc)
//     * @param column        Column which contains value
//     * @param selection     Query selection
//     * @param selectionArgs Query parameters
//     * @param orderBy       Order by value(s)
//     * @param defaultValue  Value returned if nothing is found
//     * @param <I>           Type of value
//     * @return query results value or defaultValue if no data was returned
//     */
//    public static <I> I findValueBySelection(@NonNull DatabaseWrapper<? super AndroidBaseRecord, ? super DBToolsContentValues<?>> database, @NonNull String tableName, @NonNull Class<I> valueType, @NonNull String column, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String orderBy, I defaultValue) {
//        return findValueBySelection(database, tableName, valueType, column, selection, selectionArgs, null, null, orderBy, defaultValue);
//    }
//
//    /**
//     * Return the value for the specified column and first row value as given type for given selection and selectionArgs.
//     *
//     * @param database      DatabaseWrapper of database to query
//     * @param tableName     Table to run query against
//     * @param valueType     Type to be used when getting data from database and what type is used on return (Integer.class, Boolean.class, etc)
//     * @param column        Column which contains value
//     * @param selection     Query selection
//     * @param selectionArgs Query parameters
//     * @param orderBy       Order by value(s)
//     * @param defaultValue  Value returned if nothing is found
//     * @param <I>           Type of value
//     * @return query results value or defaultValue if no data was returned
//     */
//    public static <I> I findValueBySelection(@NonNull DatabaseWrapper<? super AndroidBaseRecord, ? super DBToolsContentValues<?>> database, @NonNull String tableName, @NonNull Class<I> valueType, @NonNull String column, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String groupBy, @Nullable String having, @Nullable String orderBy, I defaultValue) {

    /**
     * Return the value for the specified column and first row value as given type for given selection and selectionArgs.
     *
     * @param databaseName  Name of database to query
     * @param valueType     Type to be used when getting data from database and what type is used on return (Integer.class, Boolean.class, etc)
     * @param column        Column which contains value
     * @param selection     Query selection
     * @param selectionArgs Query parameters
     * @param orderBy       Order by value(s)
     * @param defaultValue  Value returned if nothing is found
     * @param <I>           Type of value
     * @return query results value or defaultValue if no data was returned
     */
    public <I> I findValueBySelection(@NonNull String databaseName, @NonNull Class<I> valueType, @NonNull String column, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String groupBy, @Nullable String having, @Nullable String orderBy, I defaultValue) {
        DatabaseWrapper<? super AndroidBaseRecord, ? super DBToolsContentValues<?>> db = getWritableDatabase(databaseName);
        String tableName = getTableName();
        return findValueBySelection(db, tableName, valueType, column, selection, selectionArgs, groupBy, having, orderBy, defaultValue);
    }

    /**
     * Return the value for the specified column and first row value as given type for given selection and selectionArgs.
     *
     * @param database      DatabaseWrapper of database to query
     * @param tableName     Table to run query against
     * @param valueType     Type to be used when getting data from database and what type is used on return (Integer.class, Boolean.class, etc)
     * @param column        Column which contains value
     * @param selection     Query selection
     * @param selectionArgs Query parameters
     * @param orderBy       Order by value(s)
     * @param defaultValue  Value returned if nothing is found
     * @param <I>           Type of value
     * @return query results value or defaultValue if no data was returned
     */
    public static <I> I findValueBySelection(@NonNull DatabaseWrapper<? super AndroidBaseRecord, ? super DBToolsContentValues<?>> database, @NonNull String tableName, @NonNull Class<I> valueType, @NonNull String column, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String groupBy, @Nullable String having, @Nullable String orderBy, I defaultValue) {
        DatabaseValue<I> databaseValue = getDatabaseValue(valueType);
        I value = defaultValue;

        Cursor c = database.query(false, tableName, new String[]{column}, selection, selectionArgs, groupBy, having, orderBy, "1");
        if (c != null) {
            if (c.moveToFirst()) {
                value = databaseValue.getColumnValue(c, 0, defaultValue);
            }
            c.close();
        }
        return value;
    }

    /**
     * Return a list of all of the first column values as a List for given rawQuery and selectionArgs.
     *
     * @param valueType     Type to be used when getting data from database and what type is used on return (Integer.class, Boolean.class, etc)
     * @param rawQuery      Query contain first column contains value
     * @param selectionArgs Query parameters
     * @param <I>           Type of value
     * @return query results List or empty List returned
     */
    @NonNull
    public <I> List<I> findAllValuesByRawQuery(@NonNull Class<I> valueType, @NonNull String rawQuery, @Nullable String[] selectionArgs) {
        return findAllValuesByRawQuery(getDatabaseName(), valueType, rawQuery, selectionArgs);
    }

    /**
     * Return a list of all of the first column values as a List for given rawQuery and selectionArgs.
     *
     * @param databaseName  Name of database to query
     * @param valueType     Type to be used when getting data from database and what type is used on return (Integer.class, Boolean.class, etc)
     * @param rawQuery      Query contain first column contains value
     * @param selectionArgs Query parameters
     * @param <I>           Type of value
     * @return query results List or empty List returned
     */
    @NonNull
    public <I> List<I> findAllValuesByRawQuery(@NonNull String databaseName, @NonNull Class<I> valueType, @NonNull String rawQuery, @Nullable String[] selectionArgs) {
        return findAllValuesByRawQuery(databaseName, valueType, 0, rawQuery, selectionArgs);
    }

    /**
     * Return a list of all of the first column values as a List for given rawQuery and selectionArgs.
     *
     * @param databaseName  Name of database to query
     * @param valueType     Type to be used when getting data from database and what type is used on return (Integer.class, Boolean.class, etc)
     * @param columnIndex   Index of column with value
     * @param rawQuery      Query contain first column contains value
     * @param selectionArgs Query parameters
     * @param <I>           Type of value
     * @return query results List or empty List returned
     */
    @NonNull
    public <I> List<I> findAllValuesByRawQuery(@NonNull String databaseName, @NonNull Class<I> valueType, int columnIndex, @NonNull String rawQuery, @Nullable String[] selectionArgs) {
        return findAllValuesByRawQuery(getReadableDatabase(databaseName), valueType, columnIndex, rawQuery, selectionArgs);
    }

    /**
     * Return a list of all of the first column values as a List for given rawQuery and selectionArgs.
     *
     * @param database      DatabaseWrapper of database to query
     * @param valueType     Type to be used when getting data from database and what type is used on return (Integer.class, Boolean.class, etc)
     * @param columnIndex   Index of column with value
     * @param rawQuery      Query contain first column contains value
     * @param selectionArgs Query parameters
     * @param <I>           Type of value
     * @return query results List or empty List returned
     */
    public static <I> List<I> findAllValuesByRawQuery(@NonNull DatabaseWrapper<? super AndroidBaseRecord, ? super DBToolsContentValues<?>> database, @NonNull Class<I> valueType, int columnIndex, @NonNull String rawQuery, @Nullable String[] selectionArgs) {
        List<I> foundItems;
        DatabaseValue<I> databaseValue = getDatabaseValue(valueType);

        Cursor cursor = database.rawQuery(rawQuery, selectionArgs);
        if (cursor != null) {
            foundItems = new ArrayList<I>(cursor.getCount());
            if (cursor.moveToFirst()) {
                do {
                    foundItems.add(databaseValue.getColumnValue(cursor, columnIndex, null));
                } while (cursor.moveToNext());
            }
            cursor.close();
        } else {
            foundItems = new ArrayList<I>();
        }

        return foundItems;
    }

    /**
     * Return a list of all values for the specified column for given selection and selectionArgs.
     *
     * @param valueType     Type to be used when getting data from database and what type is used on return (Integer.class, Boolean.class, etc)
     * @param column        Column which contains value
     * @param selection     Query selection
     * @param selectionArgs Query parameters
     * @param <I>           Type of value
     * @return query results List or empty List returned
     */
    @NonNull
    public <I> List<I> findAllValuesBySelection(@NonNull Class<I> valueType, @NonNull String column, @Nullable String selection, @Nullable String[] selectionArgs) {
        return findAllValuesBySelection(getDatabaseName(), valueType, column, selection, selectionArgs, null);
    }

    /**
     * Return a list of all values for the specified column for given selection and selectionArgs.
     *
     * @param valueType     Type to be used when getting data from database and what type is used on return (Integer.class, Boolean.class, etc)
     * @param column        Column which contains value
     * @param selection     Query selection
     * @param selectionArgs Query parameters
     * @param orderBy       Query order by
     * @param <I>           Type of value
     * @return query results List or empty List returned
     */
    @NonNull
    public <I> List<I> findAllValuesBySelection(@NonNull Class<I> valueType, @NonNull String column, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String orderBy) {
        return findAllValuesBySelection(getDatabaseName(), valueType, column, selection, selectionArgs, orderBy);
    }

    /**
     * Return a list of all values for the specified column for given selection and selectionArgs.
     *
     * @param valueType     Type to be used when getting data from database and what type is used on return (Integer.class, Boolean.class, etc)
     * @param column        Column which contains value
     * @param selection     Query selection
     * @param selectionArgs Query parameters
     * @param orderBy       Query order by
     * @param <I>           Type of value
     * @return query results List or empty List returned
     */
    @NonNull
    public <I> List<I> findAllValuesBySelection(@NonNull Class<I> valueType, @NonNull String column, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String groupBy, @Nullable String having, @Nullable String orderBy, @Nullable String limit) {
        return findAllValuesBySelection(getDatabaseName(), valueType, column, selection, selectionArgs, groupBy, having, orderBy, limit);
    }

    /**
     * Return a list of all values for the specified column for given selection and selectionArgs.
     *
     * @param databaseName  Name of database to query
     * @param valueType     Type to be used when getting data from database and what type is used on return (Integer.class, Boolean.class, etc)
     * @param column        Column which contains value
     * @param selection     Query selection
     * @param selectionArgs Query parameters
     * @param orderBy       Query order by
     * @param <I>           Type of value
     * @return query results List or empty List returned
     */
    @NonNull
    public <I> List<I> findAllValuesBySelection(@NonNull String databaseName, @NonNull Class<I> valueType, @NonNull String column, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String orderBy) {
        return findAllValuesBySelection(databaseName, valueType, column, selection, selectionArgs, null, null, orderBy, null);
    }

    /**
     * Return a list of all values for the specified column for given selection and selectionArgs.
     *
     * @param databaseName  Name of database to query
     * @param valueType     Type to be used when getting data from database and what type is used on return (Integer.class, Boolean.class, etc)
     * @param column        Column which contains value
     * @param selection     Query selection
     * @param selectionArgs Query parameters
     * @param orderBy       Query order by
     * @param <I>           Type of value
     * @return query results List or empty List returned
     */
    @NonNull
    public <I> List<I> findAllValuesBySelection(@NonNull String databaseName, @NonNull Class<I> valueType, @NonNull String column, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String groupBy, @Nullable String having, @Nullable String orderBy, @Nullable String limit) {
        return findAllValuesBySelection(getReadableDatabase(databaseName), getTableName(), valueType, column, selection, selectionArgs, groupBy, having, orderBy, limit);
    }

    /**
     * Return a list of all values for the specified column for given selection and selectionArgs.
     *
     * @param database      DatabaseWrapper of database to query
     * @param tableName     Table to run query against
     * @param valueType     Type to be used when getting data from database and what type is used on return (Integer.class, Boolean.class, etc)
     * @param column        Column which contains value
     * @param selection     Query selection
     * @param selectionArgs Query parameters
     * @param orderBy       Query order by
     * @param <I>           Type of value
     * @return query results List or empty List returned
     */
    @NonNull
    public static <I> List<I> findAllValuesBySelection(@NonNull DatabaseWrapper<? super AndroidBaseRecord, ? super DBToolsContentValues<?>> database, @NonNull String tableName, @NonNull Class<I> valueType, @NonNull String column, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String orderBy) {
        return findAllValuesBySelection(database, tableName, valueType, column, selection, selectionArgs, null, null, orderBy, null);
    }

    /**
     * Return a list of all values for the specified column for given selection and selectionArgs.
     *
     * @param database      DatabaseWrapper of database to query
     * @param tableName     Table to run query against
     * @param valueType     Type to be used when getting data from database and what type is used on return (Integer.class, Boolean.class, etc)
     * @param column        Column which contains value
     * @param selection     Query selection
     * @param selectionArgs Query parameters
     * @param orderBy       Query order by
     * @param <I>           Type of value
     * @return query results List or empty List returned
     */
    @NonNull
    public static <I> List<I> findAllValuesBySelection(@NonNull DatabaseWrapper<? super AndroidBaseRecord, ? super DBToolsContentValues<?>> database, @NonNull String tableName, @NonNull Class<I> valueType, @NonNull String column, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String groupBy, @Nullable String having, @Nullable String orderBy, @Nullable String limit) {
        List<I> foundItems;
        DatabaseValue<I> databaseValue = getDatabaseValue(valueType);

        Cursor c = database.query(true, tableName, new String[]{column}, selection, selectionArgs, groupBy, having, orderBy, limit);
        if (c != null) {
            foundItems = new ArrayList<I>(c.getCount());
            if (c.moveToFirst()) {
                do {
                    foundItems.add(databaseValue.getColumnValue(c, 0, null));
                } while (c.moveToNext());
            }
            c.close();
        } else {
            foundItems = new ArrayList<I>();
        }

        return foundItems;
    }

    public static <I> DatabaseValue getDatabaseValue(Class<? extends I> type) {
        return DatabaseValueUtil.getDatabaseValue(type);
    }

    private static final String TABLE_EXISTS = "SELECT COUNT(1) FROM sqlite_master WHERE type = 'table' AND name = ?";

    public boolean tableExists(@NonNull String tableName) {
        return tableExists(getDatabaseName(), tableName);
    }

    public boolean tableExists(@NonNull String databaseName, @NonNull String tableName) {
        return tableExists(getReadableDatabase(databaseName), tableName);
    }

    public static boolean tableExists(@NonNull AndroidDatabase androidDatabase, @NonNull String tableName) {
        return tableExists(androidDatabase.getDatabaseWrapper(), tableName);
    }

    public static boolean tableExists(@Nullable DatabaseWrapper<? super AndroidBaseRecord, ? super DBToolsContentValues<?>> db, @Nullable String tableName) {
        if (tableName == null || db == null || !db.isOpen()) {
            return false;
        }
        Cursor cursor = db.rawQuery(TABLE_EXISTS, new String[]{tableName});
        if (!cursor.moveToFirst()) {
            return false;
        }
        int count = cursor.getInt(0);
        cursor.close();
        return count > 0;
    }

    @Nullable
    public MatrixCursor toMatrixCursor(@NonNull T record) {
        List<T> list = new ArrayList<T>(1);
        list.add(record);
        return toMatrixCursor(record.getAllColumns(), list);
    }

    @Nullable
    public MatrixCursor toMatrixCursor(@NonNull T... records) {
        return toMatrixCursor(Arrays.asList(records));
    }

    @Nullable
    public MatrixCursor toMatrixCursor(@NonNull List<T> records) {
        if (records.isEmpty()) {
            return null;
        }

        T record = records.get(0);

        return toMatrixCursor(record.getAllColumns(), records);
    }

    public MatrixCursor toMatrixCursor(String[] columns, List<T> records) {
        MatrixCursor mx = new MatrixCursor(columns);
        for (T record : records) {
            mx.addRow(record.getValues());
        }
        return mx;
    }

    @NonNull
    public Cursor mergeCursors(@NonNull Cursor... cursors) {
        return new MergeCursor(cursors);
    }

    @NonNull
    public Cursor addAllToCursorTop(Cursor cursor, List<T> records) {
        return mergeCursors(toMatrixCursor(records), cursor);
    }

    @SafeVarargs
    public final Cursor addAllToCursorTop(Cursor cursor, T... records) {
        return mergeCursors(toMatrixCursor(records), cursor);
    }

    public Cursor addAllToCursorBottom(Cursor cursor, List<T> records) {
        return mergeCursors(cursor, toMatrixCursor(records));
    }

    @SafeVarargs
    public final Cursor addAllToCursorBottom(Cursor cursor, T... records) {
        return mergeCursors(cursor, toMatrixCursor(records));
    }
}
