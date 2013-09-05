package org.dbtools.android.domain;

import android.app.SearchManager;
import android.content.ContentValues;
import android.database.Cursor;
import android.provider.BaseColumns;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jcampbell
 */
public abstract class AndroidBaseManager<T extends AndroidBaseRecord> {

    public static final String DEFAULT_COLLATE_LOCALIZED = " COLLATE LOCALIZED";

    public abstract SQLiteDatabase getReadableDatabase(String databaseName);

    public abstract SQLiteDatabase getWritableDatabase(String databaseName);

    public abstract String getDatabaseName();

    public abstract String getTableName();

    public abstract String getPrimaryKey();

    public abstract String[] getAllKeys();

    public abstract String getDropTableSQL();

    public abstract String getCreateTableSQL();

    public abstract T newRecord();

    public void createTable() {
        executeSQL(getDatabaseName(), getCreateTableSQL());
    }

    public void createTable(String databaseName) {
        executeSQL(getWritableDatabase(databaseName), getCreateTableSQL());
    }

    public void createTable(SQLiteDatabase db) {
        executeSQL(db, getCreateTableSQL());
    }

    public static void createTable(SQLiteDatabase db, String sql) {
        executeSQL(db, sql);
    }

    public void dropTable() {
        executeSQL(getDatabaseName(), getCreateTableSQL());
    }

    public void dropTable(String databaseName) {
        executeSQL(getWritableDatabase(databaseName), getCreateTableSQL());
    }

    public void dropTable(SQLiteDatabase db) {
        executeSQL(db, getCreateTableSQL());
    }

    // for use with enum records
    public static void dropTable(SQLiteDatabase db, String sql) {
        executeSQL(db, sql);
    }

    public void cleanTable() {
        cleanTable(getDatabaseName());
    }

    public void cleanTable(String databaseName) {
        cleanTable(getWritableDatabase(databaseName), getDropTableSQL(), getCreateTableSQL());
    }

    public void cleanTable(SQLiteDatabase db) {
        cleanTable(db, getDropTableSQL(), getCreateTableSQL());
    }

    public void cleanTable(String dropSQL, String createSQL) {
        cleanTable(getDatabaseName(), dropSQL, createSQL);
    }

    public void cleanTable(String databaseName, String dropSQL, String createSQL) {
        cleanTable(getWritableDatabase(databaseName), dropSQL, createSQL);
    }

    public static void cleanTable(SQLiteDatabase db, String dropSQL, String createSQL) {
        checkDB(db);
        executeSQL(db, dropSQL);
        executeSQL(db, createSQL);
    }

    public void executeSQL(String sql) {
        executeSQL(getDatabaseName(), sql);
    }

    public void executeSQL(String databaseName, String sql) {
        executeSQL(getWritableDatabase(databaseName), sql);
    }

    public static void executeSQL(SQLiteDatabase db, String sql) {
        checkDB(db);
        String[] sqlStatements = sql.split(";");

        for (String sqlStatement : sqlStatements) {
            if (sqlStatement.length() > 0) {
                db.execSQL(sqlStatement);
            }
        }
    }

    public void beginTransaction() {
        beginTransaction(getDatabaseName());
    }

    public void beginTransaction(String databaseName) {
        getWritableDatabase(databaseName).beginTransaction();
    }

    public void endTransaction(boolean success) {
        endTransaction(getDatabaseName(), success);
    }

    public void endTransaction(String databaseName, boolean success) {
        if (success) {
            getWritableDatabase(databaseName).setTransactionSuccessful();
        }
        getWritableDatabase(databaseName).endTransaction();
    }

    /**
     * Save Record.
     *
     * @param e Record to be saved
     * @return true if record was saved
     */
    public boolean save(T e) {
        return save(getDatabaseName(), e);
    }

    /**
     * Save Record.
     *
     * @param e Record to be saved
     * @return true if record was saved
     */
    public boolean save(String databaseName, T e) {
        if (e.isNewRecord()) {
            long newID = insert(databaseName, e);
            return newID != 0;
        } else {
            int count = update(databaseName, e);
            return count != 0;
        }
    }

    /**
     * Save Record.
     *
     * @param db database for the record to be saved to
     * @param e Record to be saved
     * @return true if record was saved
     */
    public static boolean save(SQLiteDatabase db, AndroidBaseRecord e) {
        if (e.isNewRecord()) {
            long newID = insert(db, e);
            return newID != 0;
        } else {
            int count = update(db, e);
            return count != 0;
        }
    }

    /**
     * Insert record into database.
     *
     * @param e record to be inserted
     * @return long value of new id
     */
    public long insert(T e) {
        return insert(getDatabaseName(), e);
    }

    /**
     * Insert record into database.
     *
     * @param e record to be inserted
     * @return long value of new id
     */
    public long insert(String databaseName, T e) {
        return insert(getWritableDatabase(databaseName), e);
    }

    /**
     * Insert record into database.
     *
     * @param db database for the record inserted into
     * @param e record to be inserted
     * @return long value of new id
     */
    public static long insert(SQLiteDatabase db, AndroidBaseRecord e) {
        checkDB(db);
        long rowID = db.insert(e.getTableName(), null, e.getContentValues());
        e.setID(rowID);
        return rowID;
    }

    public SQLiteStatement createCompiledInsert() {
        return createCompiledInsert(getDatabaseName());
    }

    public SQLiteStatement createCompiledInsert(String databaseName) {
        return createCompiledInsert(getWritableDatabase(databaseName));
    }

    public SQLiteStatement createCompiledInsert(SQLiteDatabase db) {
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO " + getTableName() + " (");

        int keyCount = 0;
        for (String key : getAllKeys()) {
            if (keyCount > 0) {
                sql.append(",");
            }
            sql.append(key);
            keyCount++;
        }

        sql.append(") VALUES (");

        for (int i = 0; i < keyCount; i++) {
            if (i > 0) {
                sql.append(",");
            }
            sql.append("?");
        }

        sql.append(")");

        return db.compileStatement(sql.toString());
    }

    public long insert(SQLiteStatement statement, AndroidBaseRecord e) {
        ContentValues contentValues = e.getContentValues();
        int bindItemCount = 1;
        for (String key : e.getAllKeys()) {
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

        long rowID = statement.executeInsert();
        e.setID(rowID);
        return rowID;
    }

    public int update(T e) {
        return update(getDatabaseName(), e);
    }

    public int update(String databaseName, T e) {
        return update(getWritableDatabase(databaseName), e);
    }

    public static int update(SQLiteDatabase db, AndroidBaseRecord e) {
        checkDB(db);
        long rowID = e.getID();
        if (rowID <= 0) {
            throw new IllegalArgumentException("Invalid rowID [" + rowID + "] be sure to call create(...) before update(...)");
        }

        return update(db, e.getTableName(), e.getContentValues(), e.getRowIDKey(), rowID);
    }

    public int update(String tableName, ContentValues contentValues, String rowKey, long rowID) {
        return update(getDatabaseName(), tableName, contentValues, rowKey, rowID);
    }

    public int update(String databaseName, String tableName, ContentValues contentValues, String rowKey, long rowID) {
        return update(getWritableDatabase(databaseName), tableName, contentValues, rowKey, rowID);
    }

    public static int update(SQLiteDatabase db, String tableName, ContentValues contentValues, String rowKey, long rowID) {
        checkDB(db);
        return db.update(tableName, contentValues, rowKey + "=" + rowID, null);
    }

    public int update(String tableName, ContentValues contentValues, String where, String[] whereArgs) {
        return update(getDatabaseName(), tableName, contentValues, where, whereArgs);
    }

    public int update(String databaseName, String tableName, ContentValues contentValues, String where, String[] whereArgs) {
        return update(getWritableDatabase(databaseName), tableName, contentValues, where, whereArgs);
    }

    public static int update(SQLiteDatabase db, String tableName, ContentValues contentValues, String where, String[] whereArgs) {
        checkDB(db);
        return db.update(tableName, contentValues, where, whereArgs);
    }

    public long delete(T e) {
        return delete(getDatabaseName(), e);
    }

    public long delete(String databaseName, T e) {
        return delete(getWritableDatabase(databaseName), e);
    }

    public static long delete(SQLiteDatabase db, AndroidBaseRecord e) {
        checkDB(db);
        long rowID = e.getID();
        if (rowID <= 0) {
            throw new IllegalArgumentException("Invalid rowID [" + rowID + "]");
        }

        return delete(db, e.getTableName(), e.getRowIDKey(), rowID);
    }

    public long delete(String tableName, String rowKey, long rowID) {
        return delete(getDatabaseName(), tableName, rowKey, rowID);
    }

    public long delete(String databaseName, String tableName, String rowKey, long rowID) {
        return delete(getWritableDatabase(databaseName), tableName, rowKey, rowID);
    }

    public static long delete(SQLiteDatabase db, String tableName, String rowKey, long rowID) {
        checkDB(db);
        return db.delete(tableName, rowKey + "=" + rowID, null);
    }

    public long delete(String tableName, String where, String[] whereArgs) {
        return delete(getDatabaseName(), tableName, where, whereArgs);
    }

    public long delete(String databaseName, String tableName, String where, String[] whereArgs) {
        return delete(getWritableDatabase(databaseName), tableName, where, whereArgs);
    }

    public static long delete(SQLiteDatabase db, String tableName, String where, String[] whereArgs) {
        checkDB(db);
        return db.delete(tableName, where, whereArgs);
    }

    public long deleteAll() {
        return deleteAll(getDatabaseName());
    }

    public long deleteAll(String databaseName) {
        return delete(getWritableDatabase(databaseName), getTableName(), null, null);
    }

    public long deleteAll(String databaseName, String tableName) {
        return delete(getWritableDatabase(databaseName), tableName, null, null);
    }

    public static long deleteAll(SQLiteDatabase db, String tableName) {
        return delete(db, tableName, null, null);
    }

    private static final String DEFAULT_SEARCH_SUG_INTENT = BaseColumns._ID + " AS " + SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID;

    public static String[] addSearchSuggestionIntentCol(String[] currentCols, String resultColName) {
        String sugCol1 = resultColName + " AS " + SearchManager.SUGGEST_COLUMN_TEXT_1;
        return addToArray(currentCols, new String[]{DEFAULT_SEARCH_SUG_INTENT, sugCol1});
    }

    public static String createSearchSuggestionColumnsSQL(String idColumn, String resultTextColumn) {
        return createSearchSuggestionIDColumn(idColumn) + ", " + createSearchSuggestionResult1Column(resultTextColumn);
    }

    public static String createSearchSuggestionIDColumn(String idColumn) {
        return idColumn + " AS " + SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID;
    }

    public static String createSearchSuggestionResult1Column(String resultTextColumn) {
        return resultTextColumn + " AS " + SearchManager.SUGGEST_COLUMN_TEXT_1;
    }

    public static String[] addToArray(String[] array, String[] strings) {
        String[] newArray = new String[array.length + strings.length];
        System.arraycopy(array, 0, newArray, 0, array.length);
        for (int i = array.length, j = 0; j < strings.length; i++, j++) { // NOPMD
            newArray[i] = strings[j];
        }
        return newArray;
    }

    private static void checkDB(SQLiteDatabase db) {
        if (db == null) {
            throw new IllegalArgumentException("db cannot be null");
        }
    }

    public int update(ContentValues values, long rowID) {
        return update(getTableName(), values, getPrimaryKey(), rowID);
    }

    public int update(ContentValues values, String where, String[] whereArgs) {
        return update(getTableName(), values, where, whereArgs);
    }

    public long delete(long rowID) {
        return delete(getTableName(), getPrimaryKey(), rowID);
    }

    public long delete(String where, String[] whereArgs) {
        return delete(getTableName(), where, whereArgs);
    }

    public Cursor findCursorByRawQuery(String rawQuery, String[] selectionArgs) {
        return findCursorByRawQuery(getDatabaseName(), rawQuery, selectionArgs);
    }

    public Cursor findCursorByRawQuery(String databaseName, String rawQuery, String[] selectionArgs) {
        return getReadableDatabase(databaseName).rawQuery(rawQuery, selectionArgs);
    }

    public Cursor findCursorBySelection(String selection, String orderBy) {
        return findCursorBySelection(selection, new String[]{}, orderBy);
    }

    public Cursor findCursorBySelection(String databaseName, String selection, String orderBy) {
        return findCursorBySelection(databaseName, selection, null, orderBy);
    }

    public Cursor findCursorBySelection(String selection, String[] selectionArgs, String orderBy) {
        return findCursorBySelection(getDatabaseName(), selection, selectionArgs, orderBy);
    }

    public Cursor findCursorBySelection(String databaseName, String selection, String[] selectionArgs, String orderBy) {
        Cursor cursor = getReadableDatabase(databaseName).query(true, getTableName(), getAllKeys(), selection, selectionArgs, null, null, orderBy, null);

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

    public Cursor findCursorByRowID(long rowID) {
        return findCursorBySelection(getPrimaryKey() + "=" + rowID, null);
    }

    public Cursor findCursorByRowID(String databaseName, long rowID) {
        return findCursorBySelection(databaseName, getPrimaryKey() + "=" + rowID, null);
    }

    public T findBySelection(String selection, String[] selectionArgs, String orderBy) {
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

    public T findBySelection(String databaseName, String selection, String[] selectionArgs, String orderBy) {
        Cursor cursor = findCursorBySelection(databaseName, selection, selectionArgs, orderBy);
        if (cursor != null) {
            T record = newRecord();
            record.setContent(cursor);
            cursor.close();
            return record;
        } else {
            return null;
        }
    }

    public List<T> findAll() {
        return findAllBySelection(null, null, null);
    }

    public List<T> findAll(String databaseName) {
        return findAllBySelection(databaseName, null, null, null);
    }

    public List<T> findAllOrderBy(String orderBy) {
        return findAllBySelection(null, null, orderBy);
    }

    public List<T> findAllOrderBy(String databaseName, String orderBy) {
        return findAllBySelection(databaseName, null, null, orderBy);
    }

    public List<T> findAllBySelection(String selection, String[] selectionArgs) {
        return findAllBySelection(selection, selectionArgs, null);
    }

    public List<T> findAllBySelection(String selection, String[] selectionArgs, String orderBy) {
        Cursor cursor = findCursorBySelection(selection, selectionArgs, orderBy);
        return getAllItemsFromCursor(cursor);
    }

    public List<T> findAllBySelection(String databaseName, String selection, String[] selectionArgs, String orderBy) {
        Cursor cursor = findCursorBySelection(databaseName, selection, selectionArgs, orderBy);
        return getAllItemsFromCursor(cursor);
    }

    /**
     * Populate of List<T> from a rawQuery.  The raw query must contain all of the columns names for the object
     * @param rawQuery Custom query
     * @return List of object T
     */
    public List<T> findAllByRawQuery(String rawQuery) {
        return getAllItemsFromCursor(findCursorByRawQuery(getDatabaseName(), rawQuery, null));
    }

    /**
     * Populate of List<T> from a rawQuery.  The raw query must contain all of the columns names for the object
     * @param rawQuery Custom query
     * @return List of object T
     */
    public List<T> findAllByRawQuery(String rawQuery, String[] selectionArgs) {
        return getAllItemsFromCursor(findCursorByRawQuery(getDatabaseName(), rawQuery, selectionArgs));
    }

    /**
     * Populate of List<T> from a rawQuery.  The raw query must contain all of the columns names for the object
     * @param databaseName Name of database
     * @param rawQuery Custom query
     * @param selectionArgs query arguments
     * @return List of object T
     */
    public List<T> findAllByRawQuery(String databaseName, String rawQuery, String[] selectionArgs) {
        return getAllItemsFromCursor(findCursorByRawQuery(databaseName, rawQuery, selectionArgs));
    }

    public List<T> getAllItemsFromCursor(Cursor cursor) {
        List<T> foundItems = null;
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

    public T findByRowID(long rowID) {
        return findBySelection(getPrimaryKey() + "=" + rowID, null, null);
    }

    public T findByRowID(String databaseName, long rowID) {
        return findBySelection(databaseName, getPrimaryKey() + "=" + rowID, null, null);
    }

    public List<T> findAllByRowIDs(long[] rowIDs) {
        return findAllByRowIDs(rowIDs, null);
    }

    public List<T> findAllByRowIDs(long[] rowIDs, String orderBy) {
        return findAllByRowIDs(getDatabaseName(), rowIDs, orderBy);
    }

    public List<T> findAllByRowIDs(String databaseName, long[] rowIDs, String orderBy) {
        if (rowIDs.length == 0) {
            return new ArrayList<T>();
        }

        StringBuilder sb = new StringBuilder();
        for (long rowID : rowIDs) {
            if (sb.length() > 0) {
                sb.append(" OR ");
            }
            sb.append(getPrimaryKey()).append(" = ").append(rowID);
        }

        return findAllBySelection(databaseName, sb.toString(), null, orderBy);
    }

    public long findCount() {
        return findCountBySelection(null, null);
    }

    public long findCount(String databaseName) {
        return findCountBySelection(databaseName, null, null);
    }

    public long findCountBySelection(String selection, String[] selectionArgs) {
        return findCountBySelection(getDatabaseName(), selection, selectionArgs);
    }

    public long findCountBySelection(String databaseName, String selection, String[] selectionArgs) {
        long count = -1;

        Cursor c = getReadableDatabase(databaseName).query(getTableName(), new String[]{"count(1)"}, selection, selectionArgs, null, null, null);
        if (c != null) {
            if (c.moveToFirst()) {
                count = c.getLong(0);
            }
            c.close();
        }
        return count;
    }

    /**
     * Find count by raw query.  Raw query assumes first SELECT param is count(1).
     * @param rawQuery Query
     * @return total count
     */
    public long findCountByRawQuery(String rawQuery) {
        return findCountByRawQuery(getDatabaseName(), rawQuery, null);
    }

    /**
     * Find count by raw query.  Raw query assumes first SELECT param is count(1).
     * @param rawQuery Query
     * @return total count
     */
    public long findCountByRawQuery(String databaseName, String rawQuery) {
        return findCountByRawQuery(databaseName, rawQuery, null);
    }

    /**
     * Find count by raw query.  Raw query assumes first SELECT param is count(1).
     * @param rawQuery Query
     * @param selectionArgs Selection args
     * @return total count
     */
    public long findCountByRawQuery(String rawQuery, String[] selectionArgs) {
        return findCountByRawQuery(getDatabaseName(), rawQuery, selectionArgs);
    }

    public long findCountByRawQuery(String databaseName, String rawQuery, String[] selectionArgs) {
        long count = 0;
        Cursor c = getReadableDatabase(databaseName).rawQuery(rawQuery, selectionArgs);
        if (c != null) {
            if (c.moveToFirst()) {
                count = c.getLong(0);
            }
            c.close();
        }

        return count;
    }

    /**
     * Return the first column and first row value as a Long for given rawQuery and selectionArgs.
     * @param rawQuery Query contain first column which is a long value
     * @param selectionArgs Query parameters
     * @return query results value or -1 if no data was returned
     */
    public long findLongByRawQuery(String rawQuery, String[] selectionArgs) {
        return findLongByRawQuery(getDatabaseName(), rawQuery, selectionArgs);
    }

    /**
     * Return the first column and first row value as a Long for given rawQuery and selectionArgs.
     * @param databaseName Name of database to query
     * @param rawQuery Query contain first column which is a long value
     * @param selectionArgs Query parameters
     * @return query results value or -1 if no data was returned
     */
    public long findLongByRawQuery(String databaseName, String rawQuery, String[] selectionArgs) {
        long value = -1;

        Cursor c = getReadableDatabase(databaseName).rawQuery(rawQuery, selectionArgs);
        if (c != null) {
            if (c.moveToFirst()) {
                value = c.getLong(0);
            }
            c.close();
        }

        return value;
    }

    /**
     * Return the first column and first row value as a String for given rawQuery and selectionArgs.
     * @param rawQuery Query contain first column which is a String value
     * @param selectionArgs Query parameters
     * @return query results value or null if no data was returned
     */
    public String findStringByRawQuery(String rawQuery, String[] selectionArgs) {
        return findStringByRawQuery(getDatabaseName(), rawQuery, selectionArgs);
    }

    /**
     * Return the first column and first row value as a String for given rawQuery and selectionArgs.
     * @param databaseName Name of database to query
     * @param rawQuery Query contain first column which is a String value
     * @param selectionArgs Query parameters
     * @return query results value or null if no data was returned
     */
    public String findStringByRawQuery(String databaseName, String rawQuery, String[] selectionArgs) {
        String value = null;

        Cursor c = getReadableDatabase(databaseName).rawQuery(rawQuery, selectionArgs);
        if (c != null) {
            if (c.moveToFirst()) {
                value = c.getString(0);
            }
            c.close();
        }

        return value;
    }

    /**
     * Return a list of all of the first column values as a List<Long> for given rawQuery and selectionArgs.
     * @param rawQuery Query contain first column which is a Long value
     * @param selectionArgs Query parameters
     * @return query results List or empty List returned
     */
    public List<Long> findAllLongByRawQuery(String rawQuery, String[] selectionArgs) {
        return findAllLongByRawQuery(getDatabaseName(), rawQuery, selectionArgs);
    }

    /**
     * Return a list of all of the first column values as a List<Long> for given rawQuery and selectionArgs.
     * @param databaseName Name of database to query
     * @param rawQuery Query contain first column which is a Long value
     * @param selectionArgs Query parameters
     * @return query results List or empty List returned
     */
    public List<Long> findAllLongByRawQuery(String databaseName, String rawQuery, String[] selectionArgs) {
        List<Long> foundItems;

        Cursor cursor = getWritableDatabase(databaseName).rawQuery(rawQuery, selectionArgs);
        if (cursor != null) {
            foundItems = new ArrayList<Long>(cursor.getCount());
            if (cursor.moveToFirst()) {
                do {
                    foundItems.add(cursor.getLong(0));
                } while (cursor.moveToNext());
            }
            cursor.close();
        } else {
            foundItems = new ArrayList<Long>();
        }

        return foundItems;
    }

    /**
     * Return a list of all of the first column values as a List<String> for given rawQuery and selectionArgs.
     * @param rawQuery Query contain first column which is a String value
     * @param selectionArgs Query parameters
     * @return query results List or empty List returned
     */
    public List<String> findAllStringByRawQuery(String rawQuery, String[] selectionArgs) {
        return findAllStringByRawQuery(getDatabaseName(), rawQuery, selectionArgs);
    }

    /**
     * Return a list of all of the first column values as a List<String> for given rawQuery and selectionArgs.
     * @param databaseName Name of database to query
     * @param rawQuery Query contain first column which is a String value
     * @param selectionArgs Query parameters
     * @return query results List or empty List returned
     */
    public List<String> findAllStringByRawQuery(String databaseName, String rawQuery, String[] selectionArgs) {
        List<String> foundItems;

        Cursor cursor = getWritableDatabase(databaseName).rawQuery(rawQuery, selectionArgs);
        if (cursor != null) {
            foundItems = new ArrayList<String>(cursor.getCount());
            if (cursor.moveToFirst()) {
                do {
                    foundItems.add(cursor.getString(0));
                } while (cursor.moveToNext());
            }
            cursor.close();
        } else {
            foundItems = new ArrayList<String>();
        }

        return foundItems;
    }

}
