package org.dbtools.android.domain;

import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.MergeCursor;

import org.dbtools.android.domain.config.DatabaseConfig;
import org.dbtools.android.domain.database.DatabaseWrapper;
import org.dbtools.android.domain.database.contentvalues.DBToolsContentValues;
import org.dbtools.android.domain.database.statement.StatementWrapper;
import org.dbtools.android.domain.dbtype.DatabaseValue;
import org.dbtools.android.domain.dbtype.DatabaseValueUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@SuppressWarnings("UnusedDeclaration")
public abstract class AndroidBaseManager<T extends AndroidBaseRecord> {

    public static final int MAX_TRY_COUNT = 3;
    public static final String DEFAULT_COLLATE_LOCALIZED = " COLLATE LOCALIZED";

    public abstract DatabaseWrapper getReadableDatabase(String databaseName);

    public abstract DatabaseWrapper getWritableDatabase(String databaseName);

    public abstract AndroidDatabase getAndroidDatabase(String databaseName);

    public abstract String getDatabaseName();

    public abstract String getTableName();

    public abstract String getPrimaryKey();

    public abstract String[] getAllColumns();

    public abstract String getDropSql();

    public abstract String getCreateSql();

    public abstract String getInsertSql();

    public abstract String getUpdateSql();

    public abstract T newRecord();

    public abstract DatabaseConfig getDatabaseConfig();

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

    public void createTable(@Nonnull String databaseName) {
        executeSql(getWritableDatabase(databaseName), getCreateSql());
    }

    public void createTable(@Nonnull DatabaseWrapper db) {
        executeSql(db, getCreateSql());
    }

    public static void createTable(@Nonnull DatabaseWrapper db, String sql) {
        executeSql(db, sql);
    }

    public void dropTable() {
        executeSql(getDatabaseName(), getDropSql());
    }

    public void dropTable(@Nonnull String databaseName) {
        executeSql(getWritableDatabase(databaseName), getDropSql());
    }

    public void dropTable(@Nonnull DatabaseWrapper db) {
        executeSql(db, getDropSql());
    }

    // for use with enum records
    public static void dropTable(@Nonnull DatabaseWrapper db, @Nonnull String sql) {
        executeSql(db, sql);
    }

    public void cleanTable() {
        cleanTable(getDatabaseName());
    }

    public void cleanTable(@Nonnull String databaseName) {
        cleanTable(getWritableDatabase(databaseName), getDropSql(), getCreateSql());
    }

    public void cleanTable(@Nonnull DatabaseWrapper db) {
        cleanTable(db, getDropSql(), getCreateSql());
    }

    public void cleanTable(@Nonnull String dropSql, @Nonnull String createSql) {
        cleanTable(getDatabaseName(), dropSql, createSql);
    }

    public void cleanTable(@Nonnull String databaseName, @Nonnull String dropSql, @Nonnull String createSql) {
        cleanTable(getWritableDatabase(databaseName), dropSql, createSql);
    }

    public void cleanTable(@Nonnull DatabaseWrapper db, @Nonnull String dropSql, @Nonnull String createSql) {
        checkDB(db);
        executeSql(db, dropSql);
        executeSql(db, createSql);
    }

    public StatementWrapper getInsertStatement(@Nonnull DatabaseWrapper db) {
        return db.getInsertStatement(getTableName(), getInsertSql());
    }

    public StatementWrapper getUpdateStatement(@Nonnull DatabaseWrapper db) {
        return db.getUpdateStatement(getTableName(), getUpdateSql());
    }

    public void executeSql(@Nonnull String sql) {
        executeSql(getDatabaseName(), sql);
    }

    public void executeSql(@Nonnull String databaseName, @Nonnull String sql) {
        executeSql(getWritableDatabase(databaseName), sql);
    }

    public static void executeSql(@Nonnull AndroidDatabase androidDatabase, @Nonnull String sql) {
        executeSql(androidDatabase.getDatabaseWrapper(), sql);
    }

    public static void executeSql(@Nullable DatabaseWrapper db, @Nonnull String sql) {
        checkDB(db);

        String[] sqlStatements = sql.split(";");

        for (String sqlStatement : sqlStatements) {
            if (sqlStatement.length() > 0) {
                db.execSQL(sqlStatement);
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
    public Cursor findCursorByRawQuery(@Nonnull String rawQuery, @Nullable String[] selectionArgs) {
        return findCursorByRawQuery(getDatabaseName(), rawQuery, selectionArgs);
    }

    @Nullable
    public Cursor findCursorByRawQuery(@Nonnull String databaseName, @Nonnull String rawQuery, @Nullable String[] selectionArgs) {
        return getReadableDatabase(databaseName).rawQuery(rawQuery, selectionArgs);
    }

    @Nullable
    public Cursor findCursorBySelection(@Nullable String selection, @Nullable String orderBy) {
        return findCursorBySelection(selection, new String[]{}, orderBy);
    }

    @Nullable
    public Cursor findCursorBySelection(@Nonnull String databaseName, @Nullable String selection, @Nullable String orderBy) {
        return findCursorBySelection(databaseName, selection, null, orderBy);
    }

    @Nullable
    public Cursor findCursorBySelection(@Nullable String selection, @Nullable String[] selectionArgs, @Nullable String orderBy) {
        return findCursorBySelection(getDatabaseName(), selection, selectionArgs, orderBy);
    }

    @Nullable
    public Cursor findCursorBySelection(@Nonnull String databaseName, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String orderBy) {
        return findCursorBySelection(databaseName, true, getTableName(), getAllColumns(), selection, selectionArgs, null, null, orderBy, null);
    }

    @Nullable
    public Cursor findCursorBySelection(boolean distinct, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String groupBy, @Nullable String having, @Nullable String orderBy, @Nullable String limit) {
        return findCursorBySelection(getDatabaseName(), distinct, getTableName(), getAllColumns(), selection, selectionArgs, groupBy, having, orderBy, limit);
    }

    @Nullable
    public Cursor findCursorBySelection(@Nonnull String databaseName, boolean distinct, @Nonnull String table, @Nonnull String[] columns, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String groupBy, @Nullable String having, @Nullable String orderBy, @Nullable String limit) {
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
    public Cursor findCursorByRowId(@Nonnull String databaseName, long rowId) {
        return findCursorBySelection(databaseName, getPrimaryKey() + "= ?", new String[]{String.valueOf(rowId)}, null);
    }

    @Nonnull
    public List<T> findAll() {
        return findAllBySelection(null, null, null);
    }

    @Nonnull
    public List<T> findAll(@Nonnull String databaseName) {
        return findAllBySelection(databaseName, null, null, null);
    }

    @Nonnull
    public List<T> findAllOrderBy(@Nullable String orderBy) {
        return findAllBySelection(null, null, orderBy);
    }

    @Nonnull
    public List<T> findAllOrderBy(@Nonnull String databaseName, @Nullable String orderBy) {
        return findAllBySelection(databaseName, null, null, orderBy);
    }

    @Nonnull
    public List<T> findAllBySelection(@Nullable String selection, @Nonnull String[] selectionArgs) {
        return findAllBySelection(selection, selectionArgs, null);
    }

    @Nonnull
    public List<T> findAllBySelection(@Nullable String selection, @Nullable String[] selectionArgs, @Nullable String orderBy) {
        Cursor cursor = findCursorBySelection(selection, selectionArgs, orderBy);
        return getAllItemsFromCursor(cursor);
    }

    @Nonnull
    public List<T> findAllBySelection(@Nonnull String databaseName, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String orderBy) {
        Cursor cursor = findCursorBySelection(databaseName, selection, selectionArgs, orderBy);
        return getAllItemsFromCursor(cursor);
    }

    /**
     * Populate of List from a rawQuery.  The raw query must contain all of the columns names for the object
     *
     * @param rawQuery Custom query
     * @return List of object T
     */
    @Nonnull
    public List<T> findAllByRawQuery(@Nonnull String rawQuery) {
        return getAllItemsFromCursor(findCursorByRawQuery(getDatabaseName(), rawQuery, null));
    }

    /**
     * Populate of List from a rawQuery.  The raw query must contain all of the columns names for the object
     *
     * @param rawQuery      Custom query
     * @param selectionArgs query arguments
     * @return List of object T
     */
    @Nonnull
    public List<T> findAllByRawQuery(@Nonnull String rawQuery, @Nullable String[] selectionArgs) {
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
    @Nonnull
    public List<T> findAllByRawQuery(@Nonnull String databaseName, @Nonnull String rawQuery, @Nullable String[] selectionArgs) {
        return getAllItemsFromCursor(findCursorByRawQuery(databaseName, rawQuery, selectionArgs));
    }

    @Nonnull
    public List<T> getAllItemsFromCursor(@Nullable Cursor cursor) {
        List<T> foundItems;
        if (cursor != null) {
            foundItems = new ArrayList<T>(cursor.getCount());
            if (cursor.moveToFirst()) {
                do {
                    T record = newRecord();
                    record.setContent(cursor);
                    foundItems.add(record);
                } while (cursor.moveToNext());
            }
            cursor.close();
        } else {
            foundItems = new ArrayList<T>();
        }

        return foundItems;
    }

    @Nullable
    public T findByRowId(long rowId) {
        return findBySelection(getPrimaryKey() + "= ?", new String[]{String.valueOf(rowId)}, null);
    }

    @Nullable
    public T findByRowId(@Nonnull String databaseName, long rowId) {
        return findBySelection(databaseName, getPrimaryKey() + "= ?", new String[]{String.valueOf(rowId)}, null);
    }

    @Nullable
    public T findBySelection(@Nullable String selection, @Nullable String[] selectionArgs, @Nullable String orderBy) {
        Cursor cursor = findCursorBySelection(selection, selectionArgs, orderBy);
        if (cursor != null) {
            T record = newRecord();
            record.setContent(cursor);
            cursor.close();
            return record;
        } else {
            return null;
        }
    }

    @Nullable
    public T findBySelection(@Nonnull String databaseName, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String orderBy) {
        Cursor cursor = findCursorBySelection(databaseName, selection, selectionArgs, orderBy);
        return createRecordFromCursor(cursor);
    }

    @Nullable
    public T findByRawQuery(@Nonnull String rawQuery, @Nullable String[] selectionArgs) {
        return findByRawQuery(getDatabaseName(), rawQuery, selectionArgs);
    }

    @Nullable
    public T findByRawQuery(@Nonnull String databaseName, @Nonnull String rawQuery, @Nullable String[] selectionArgs) {
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

    @Nonnull
    public List<T> findAllByRowIds(long[] rowIds) {
        return findAllByRowIds(rowIds, null);
    }

    @Nonnull
    public List<T> findAllByRowIds(long[] rowIds, @Nullable String orderBy) {
        return findAllByRowIds(getDatabaseName(), rowIds, orderBy);
    }

    @Nonnull
    public List<T> findAllByRowIds(@Nonnull String databaseName, long[] rowIds, @Nullable String orderBy) {
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

    public long findCount(@Nonnull String databaseName) {
        return findCountBySelection(databaseName, null, null);
    }

    public long findCountBySelection(@Nullable String selection, @Nullable String[] selectionArgs) {
        return findCountBySelection(getDatabaseName(), selection, selectionArgs);
    }

    public long findCountBySelection(@Nonnull String databaseName, @Nullable String selection, @Nullable String[] selectionArgs) {
        return findCountBySelection(getReadableDatabase(databaseName), getTableName(), selection, selectionArgs);
    }

    public static long findCountBySelection(@Nonnull DatabaseWrapper database, @Nonnull String tableName, @Nullable String selection, @Nullable String[] selectionArgs) {
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
    public long findCountByRawQuery(@Nonnull String rawQuery) {
        return findCountByRawQuery(getDatabaseName(), rawQuery, null);
    }

    /**
     * Find count by raw query.  Raw query assumes first SELECT param is count(1).
     *
     * @param databaseName name of database to use
     * @param rawQuery     Query
     * @return total count
     */
    public long findCountByRawQuery(@Nonnull String databaseName, @Nonnull String rawQuery) {
        return findCountByRawQuery(databaseName, rawQuery, null);
    }

    /**
     * Find count by raw query.  Raw query assumes first SELECT param is count(1).
     *
     * @param rawQuery      Query
     * @param selectionArgs Selection args
     * @return total count
     */
    public long findCountByRawQuery(@Nonnull String rawQuery, @Nullable String[] selectionArgs) {
        return findCountByRawQuery(getDatabaseName(), rawQuery, selectionArgs);
    }

    public long findCountByRawQuery(@Nonnull String databaseName, @Nonnull String rawQuery, @Nullable String[] selectionArgs) {
        return findCountByRawQuery(getReadableDatabase(databaseName), rawQuery, selectionArgs);
    }

    public static long findCountByRawQuery(@Nonnull DatabaseWrapper database, @Nonnull String rawQuery, @Nullable String[] selectionArgs) {
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
    public <I> I findValueByRawQuery(@Nonnull Class<I> valueType, @Nonnull String rawQuery, @Nullable String[] selectionArgs, I defaultValue) {
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
    public <I> I findValueByRawQuery(@Nonnull String databaseName, @Nonnull Class<I> valueType, @Nonnull String rawQuery, @Nullable String[] selectionArgs, I defaultValue) {
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
    public static <I> I findValueByRawQuery(@Nonnull DatabaseWrapper database, @Nonnull Class<I> valueType, @Nonnull String rawQuery, @Nullable String[] selectionArgs, I defaultValue) {
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
     * @param selection     Query selection
     * @param selectionArgs Query parameters
     * @param defaultValue  Value returned if nothing is found
     * @param <I>           Type of value
     * @return query results value or defaultValue if no data was returned
     */
    public <I> I findValueBySelection(@Nonnull Class<I> valueType, @Nonnull String column, @Nullable String selection, @Nullable String[] selectionArgs, I defaultValue) {
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
    public <I> I findValueBySelection(@Nonnull Class<I> valueType, @Nonnull String column, @Nullable String selection, @Nullable String[] selectionArgs, String orderBy, I defaultValue) {
        return findValueBySelection(getDatabaseName(), valueType, column, selection, selectionArgs, orderBy, defaultValue);
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
    public <I> I findValueBySelection(@Nonnull String databaseName, @Nonnull Class<I> valueType, @Nonnull String column, @Nullable String selection, @Nullable String[] selectionArgs, I defaultValue) {
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
    public <I> I findValueBySelection(@Nonnull String databaseName, @Nonnull Class<I> valueType, @Nonnull String column, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String orderBy, I defaultValue) {
        return findValueBySelection(getReadableDatabase(databaseName), getTableName(), valueType, column, selection, selectionArgs, orderBy, defaultValue);
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
    public static <I> I findValueBySelection(@Nonnull DatabaseWrapper database, @Nonnull String tableName, @Nonnull Class<I> valueType, @Nonnull String column, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String orderBy, I defaultValue) {
        DatabaseValue<I> databaseValue = getDatabaseValue(valueType);
        I value = defaultValue;

        Cursor c = database.query(false, tableName, new String[]{column}, selection, selectionArgs, null, null, orderBy, "1");
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
    @Nonnull
    public <I> List<I> findAllValuesByRawQuery(@Nonnull Class<I> valueType, @Nonnull String rawQuery, @Nullable String[] selectionArgs) {
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
    @Nonnull
    public <I> List<I> findAllValuesByRawQuery(@Nonnull String databaseName, @Nonnull Class<I> valueType, @Nonnull String rawQuery, @Nullable String[] selectionArgs) {
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
    @Nonnull
    public <I> List<I> findAllValuesByRawQuery(@Nonnull String databaseName, @Nonnull Class<I> valueType, int columnIndex, @Nonnull String rawQuery, @Nullable String[] selectionArgs) {
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
    public static <I> List<I> findAllValuesByRawQuery(@Nonnull DatabaseWrapper database, @Nonnull Class<I> valueType, int columnIndex, @Nonnull String rawQuery, @Nullable String[] selectionArgs) {
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
    @Nonnull
    public <I> List<I> findAllValuesBySelection(@Nonnull Class<I> valueType, @Nonnull String column, @Nullable String selection, @Nullable String[] selectionArgs) {
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
    @Nonnull
    public <I> List<I> findAllValuesBySelection(@Nonnull Class<I> valueType, @Nonnull String column, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String orderBy) {
        return findAllValuesBySelection(getDatabaseName(), valueType, column, selection, selectionArgs, orderBy);
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
    @Nonnull
    public <I> List<I> findAllValuesBySelection(@Nonnull String databaseName, @Nonnull Class<I> valueType, @Nonnull String column, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String orderBy) {
        return findAllValuesBySelection(getReadableDatabase(databaseName), getTableName(), valueType, column, selection, selectionArgs, orderBy);
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
    @Nonnull
    public static <I> List<I> findAllValuesBySelection(@Nonnull DatabaseWrapper database, @Nonnull String tableName, @Nonnull Class<I> valueType, @Nonnull String column, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String orderBy) {
        List<I> foundItems;
        DatabaseValue<I> databaseValue = getDatabaseValue(valueType);

        Cursor c = database.query(tableName, new String[]{column}, selection, selectionArgs, null, null, orderBy);
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

    protected static <I> DatabaseValue<I> getDatabaseValue(Class<I> type) {
        return DatabaseValueUtil.getDatabaseValue(type);
    }

    /*
     * Use a custom query to populate a custom class that extends CustomQueryRecord
     * CustomQueryRecord.setRowData(Object[] data) is called with each result (Object[] items contain and array of objects based on CustomQueryRecord.getColumnTypes())
     *
     * @param rawQuery      Query
     * @param selectionArgs Query parameters
     * @param type Type of Class
     * @return List of CustomQueryRecord
     * @deprecated Use Query tables instead
     */
    @Nonnull
    @Deprecated
    public <S extends CustomQueryRecord> List<S> findAllCustomRecordByRawQuery(@Nonnull String rawQuery, @Nullable String[] selectionArgs, @Nonnull Class<S> type) {
        return findAllCustomRecordByRawQuery(getDatabaseName(), rawQuery, selectionArgs, type);
    }

    /*
     * Use a custom query to populate a custom class that extends CustomQueryRecord
     * CustomQueryRecord.setRowData(Object[] data) is called with each result (Object[] items contain and array of objects based on CustomQueryRecord.getColumnTypes())
     *
     * @param databaseName  Name of database to query
     * @param rawQuery      Query
     * @param selectionArgs Query parameters
     * @param type Type of Class
     * @return List of CustomQueryRecord
     * @deprecated Use Query tables instead
     */
    @Nonnull
    @Deprecated
    public <S extends CustomQueryRecord> List<S> findAllCustomRecordByRawQuery(@Nonnull String databaseName, @Nonnull String rawQuery, @Nullable String[] selectionArgs, @Nonnull Class<S> type) {
        List<S> foundItems;

        Class[] colTypes = null;

        Cursor cursor = getWritableDatabase(databaseName).rawQuery(rawQuery, selectionArgs);
        if (cursor != null) {
            foundItems = new ArrayList<S>(cursor.getCount());
            if (cursor.moveToFirst()) {
                do {
                    S item;

                    try {
                        item = type.newInstance();
                    } catch (InstantiationException e) {
                        throw new IllegalStateException("Failed to create CustomQueryRecord", e);
                    } catch (IllegalAccessException e) {
                        throw new IllegalStateException("Failed to create CustomQueryRecord", e);
                    }

                    if (colTypes == null) {
                        colTypes = item.getColumnTypes();
                    }

                    int cursorColCount = cursor.getColumnCount();
                    if (colTypes.length != cursorColCount) {
                        throw new IllegalStateException("getColumnTypes().length != cursor.getColumnCount() for query [" + rawQuery + "]");
                    }

                    Object[] rowData = new Object[cursorColCount];
                    for (int col = 0; col < cursorColCount; col++) {
                        Class colType = colTypes[col];
                        if (colType == Integer.class || colType == int.class) {
                            rowData[col] = cursor.getInt(col);
                        } else if (colType == Long.class || colType == long.class) {
                            rowData[col] = cursor.getLong(col);
                        } else if (colType == String.class) {
                            rowData[col] = cursor.getString(col);
                        } else if (colType == Boolean.class || colType == boolean.class) {
                            rowData[col] = cursor.getInt(col) != 0;
                        } else if (colType == Float.class || colType == float.class) {
                            rowData[col] = cursor.getFloat(col);
                        } else if (colType == Double.class || colType == double.class) {
                            rowData[col] = cursor.getDouble(col);
                        } else if (colType == byte[].class) {
                            rowData[col] = cursor.getBlob(col);
                        } else {
                            throw new IllegalStateException("Unknown/Supported Column Type [" + colType + "]");
                        }
                    }

                    item.setRowData(rowData);
                    foundItems.add(item);
                } while (cursor.moveToNext());
            }
            cursor.close();
        } else {
            foundItems = new ArrayList<S>();
        }

        return foundItems;
    }

    private static final String TABLE_EXISTS = "SELECT COUNT(1) FROM sqlite_master WHERE type = 'table' AND name = ?";

    public boolean tableExists(@Nonnull String tableName) {
        return tableExists(getDatabaseName(), tableName);
    }

    public boolean tableExists(@Nonnull String databaseName, @Nonnull String tableName) {
        return tableExists(getReadableDatabase(databaseName), tableName);
    }

    public static boolean tableExists(@Nonnull AndroidDatabase androidDatabase, @Nonnull String tableName) {
        return tableExists(androidDatabase.getDatabaseWrapper(), tableName);
    }

    public static boolean tableExists(@Nullable DatabaseWrapper db, @Nullable String tableName) {
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
    public MatrixCursor toMatrixCursor(@Nonnull T record) {
        List<T> list = new ArrayList<T>(1);
        list.add(record);
        return toMatrixCursor(record.getAllColumns(), list);
    }

    @Nullable
    public MatrixCursor toMatrixCursor(@Nonnull T... records) {
        return toMatrixCursor(Arrays.asList(records));
    }

    @Nullable
    public MatrixCursor toMatrixCursor(@Nonnull List<T> records) {
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

    public Cursor mergeCursors(Cursor... cursors) {
        return new MergeCursor(cursors);
    }

    public Cursor addAllToCursorTop(Cursor cursor, List<T> records) {
        return mergeCursors(toMatrixCursor(records), cursor);
    }

    public Cursor addAllToCursorTop(Cursor cursor, T... records) {
        return mergeCursors(toMatrixCursor(records), cursor);
    }

    public Cursor addAllToCursorBottom(Cursor cursor, List<T> records) {
        return mergeCursors(cursor, toMatrixCursor(records));
    }

    public Cursor addAllToCursorBottom(Cursor cursor, T... records) {
        return mergeCursors(cursor, toMatrixCursor(records));
    }


}
