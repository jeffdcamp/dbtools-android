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

    public abstract String getDropSql();

    public abstract String getCreateSql();

    public abstract T newRecord();

    public static SQLiteDatabase getDatabase(AndroidDatabase androidDatabase) {
        return androidDatabase.getSqLiteDatabase();
    }

    public static void openDatabase(AndroidDatabase androidDatabase) {
        androidDatabase.setSqLiteDatabase(SQLiteDatabase.openOrCreateDatabase(androidDatabase.getPath(), null));
    }

    public static boolean closeDatabase(AndroidDatabase androidDatabase) {
        SQLiteDatabase sqLiteDatabase = getDatabase(androidDatabase);
        if (sqLiteDatabase != null && sqLiteDatabase.isOpen() && !sqLiteDatabase.inTransaction()) {
            sqLiteDatabase.close();
            return true;
        }

        return false;
    }

    public static boolean isDatabaseAlreadyOpen(AndroidDatabase androidDatabase) {
        SQLiteDatabase database = getDatabase(androidDatabase);
        return database != null && database.isOpen();
    }

    public void createTable() {
        executeSql(getDatabaseName(), getCreateSql());
    }

    public void createTable(String databaseName) {
        executeSql(getWritableDatabase(databaseName), getCreateSql());
    }

    public void createTable(SQLiteDatabase db) {
        executeSql(db, getCreateSql());
    }

    public static void createTable(SQLiteDatabase db, String sql) {
        executeSql(db, sql);
    }

    public void dropTable() {
        executeSql(getDatabaseName(), getCreateSql());
    }

    public void dropTable(String databaseName) {
        executeSql(getWritableDatabase(databaseName), getCreateSql());
    }

    public void dropTable(SQLiteDatabase db) {
        executeSql(db, getCreateSql());
    }

    // for use with enum records
    public static void dropTable(SQLiteDatabase db, String sql) {
        executeSql(db, sql);
    }

    public void cleanTable() {
        cleanTable(getDatabaseName());
    }

    public void cleanTable(String databaseName) {
        cleanTable(getWritableDatabase(databaseName), getDropSql(), getCreateSql());
    }

    public void cleanTable(SQLiteDatabase db) {
        cleanTable(db, getDropSql(), getCreateSql());
    }

    public void cleanTable(String dropSql, String createSql) {
        cleanTable(getDatabaseName(), dropSql, createSql);
    }

    public void cleanTable(String databaseName, String dropSql, String createSql) {
        cleanTable(getWritableDatabase(databaseName), dropSql, createSql);
    }

    public static void cleanTable(SQLiteDatabase db, String dropSql, String createSql) {
        checkDB(db);
        executeSql(db, dropSql);
        executeSql(db, createSql);
    }

    public void executeSql(String sql) {
        executeSql(getDatabaseName(), sql);
    }

    public void executeSql(String databaseName, String sql) {
        executeSql(getWritableDatabase(databaseName), sql);
    }

    public static void executeSql(AndroidDatabase androidDatabase, String sql) {
        executeSql(getDatabase(androidDatabase), sql);
    }

    public static void executeSql(SQLiteDatabase db, String sql) {
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
            long newId = insert(databaseName, e);
            return newId != 0;
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
    public static boolean save(SQLiteDatabase db, AndroidBaseRecord e) {
        if (e.isNewRecord()) {
            long newId = insert(db, e);
            return newId != 0;
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
     * @param e  record to be inserted
     * @return long value of new id
     */
    public static long insert(SQLiteDatabase db, AndroidBaseRecord e) {
        checkDB(db);
        long rowId = db.insert(e.getTableName(), null, e.getContentValues());
        e.setPrimaryKeyId(rowId);
        return rowId;
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

        long rowId = statement.executeInsert();
        e.setPrimaryKeyId(rowId);
        return rowId;
    }

    public int update(ContentValues values, long rowId) {
        return update(getTableName(), values, getPrimaryKey(), rowId);
    }

    public int update(ContentValues values, String where, String[] whereArgs) {
        return update(getTableName(), values, where, whereArgs);
    }

    public int update(T e) {
        return update(getDatabaseName(), e);
    }

    public int update(String databaseName, T e) {
        return update(getWritableDatabase(databaseName), e);
    }

    public static int update(SQLiteDatabase db, AndroidBaseRecord e) {
        checkDB(db);
        long rowId = e.getPrimaryKeyId();
        if (rowId <= 0) {
            throw new IllegalArgumentException("Invalid rowId [" + rowId + "] be sure to call create(...) before update(...)");
        }

        return update(db, e.getTableName(), e.getContentValues(), e.getIdColumnName(), rowId);
    }

    public int update(String tableName, ContentValues contentValues, String rowKey, long rowId) {
        return update(getDatabaseName(), tableName, contentValues, rowKey, rowId);
    }

    public int update(String databaseName, String tableName, ContentValues contentValues, String rowKey, long rowId) {
        return update(getWritableDatabase(databaseName), tableName, contentValues, rowKey, rowId);
    }

    public static int update(SQLiteDatabase db, String tableName, ContentValues contentValues, String rowKey, long rowId) {
        checkDB(db);
        return db.update(tableName, contentValues, rowKey + "=?", new String[]{String.valueOf(rowId)});
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

    public long delete(long rowId) {
        return delete(getTableName(), getPrimaryKey(), rowId);
    }

    public long delete(String where, String[] whereArgs) {
        return delete(getTableName(), where, whereArgs);
    }

    public long delete(T e) {
        return delete(getDatabaseName(), e);
    }

    public long delete(String databaseName, T e) {
        return delete(getWritableDatabase(databaseName), e);
    }

    public static long delete(SQLiteDatabase db, AndroidBaseRecord e) {
        checkDB(db);
        long rowId = e.getPrimaryKeyId();
        if (rowId <= 0) {
            throw new IllegalArgumentException("Invalid rowId [" + rowId + "]");
        }

        return delete(db, e.getTableName(), e.getIdColumnName(), rowId);
    }

    public long delete(String tableName, String rowKey, long rowId) {
        return delete(getDatabaseName(), tableName, rowKey, rowId);
    }

    public long delete(String databaseName, String tableName, String rowKey, long rowId) {
        return delete(getWritableDatabase(databaseName), tableName, rowKey, rowId);
    }

    public static long delete(SQLiteDatabase db, String tableName, String rowKey, long rowId) {
        checkDB(db);
        return db.delete(tableName, rowKey + "=?", new String[]{String.valueOf(rowId)});
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
        return createSearchSuggestionIdColumn(idColumn) + ", " + createSearchSuggestionResult1Column(resultTextColumn);
    }

    public static String createSearchSuggestionIdColumn(String idColumn) {
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

    public Cursor findCursorByRowId(long rowId) {
        return findCursorBySelection(getPrimaryKey() + "=?", new String[]{String.valueOf(rowId)}, null);
    }

    public Cursor findCursorByRowId(String databaseName, long rowId) {
        return findCursorBySelection(databaseName, getPrimaryKey() + "=?", new String[]{String.valueOf(rowId)}, null);
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
     *
     * @param rawQuery Custom query
     * @return List of object T
     */
    public List<T> findAllByRawQuery(String rawQuery) {
        return getAllItemsFromCursor(findCursorByRawQuery(getDatabaseName(), rawQuery, null));
    }

    /**
     * Populate of List<T> from a rawQuery.  The raw query must contain all of the columns names for the object
     *
     * @param rawQuery Custom query
     * @return List of object T
     */
    public List<T> findAllByRawQuery(String rawQuery, String[] selectionArgs) {
        return getAllItemsFromCursor(findCursorByRawQuery(getDatabaseName(), rawQuery, selectionArgs));
    }

    /**
     * Populate of List<T> from a rawQuery.  The raw query must contain all of the columns names for the object
     *
     * @param databaseName  Name of database
     * @param rawQuery      Custom query
     * @param selectionArgs query arguments
     * @return List of object T
     */
    public List<T> findAllByRawQuery(String databaseName, String rawQuery, String[] selectionArgs) {
        return getAllItemsFromCursor(findCursorByRawQuery(databaseName, rawQuery, selectionArgs));
    }

    public List<T> getAllItemsFromCursor(Cursor cursor) {
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

    public T findByRowId(long rowId) {
        return findBySelection(getPrimaryKey() + "=?", new String[]{String.valueOf(rowId)}, null);
    }

    public T findByRowId(String databaseName, long rowId) {
        return findBySelection(databaseName, getPrimaryKey() + "=?", new String[]{String.valueOf(rowId)}, null);
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
        return createRecordFromCursor(cursor);
    }

    public T findByRawQuery(String rawQuery, String[] selectionArgs) {
        return findByRawQuery(getDatabaseName(), rawQuery, selectionArgs);
    }

    public T findByRawQuery(String databaseName, String rawQuery, String[] selectionArgs) {
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

    private T createRecordFromCursor(Cursor cursor) {
        if (cursor != null) {
            T record = newRecord();
            record.setContent(cursor);
            cursor.close();
            return record;
        } else {
            return null;
        }
    }

    public List<T> findAllByRowIds(long[] rowIds) {
        return findAllByRowIds(rowIds, null);
    }

    public List<T> findAllByRowIds(long[] rowIds, String orderBy) {
        return findAllByRowIds(getDatabaseName(), rowIds, orderBy);
    }

    public List<T> findAllByRowIds(String databaseName, long[] rowIds, String orderBy) {
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
     *
     * @param rawQuery Query
     * @return total count
     */
    public long findCountByRawQuery(String rawQuery) {
        return findCountByRawQuery(getDatabaseName(), rawQuery, null);
    }

    /**
     * Find count by raw query.  Raw query assumes first SELECT param is count(1).
     *
     * @param rawQuery Query
     * @return total count
     */
    public long findCountByRawQuery(String databaseName, String rawQuery) {
        return findCountByRawQuery(databaseName, rawQuery, null);
    }

    /**
     * Find count by raw query.  Raw query assumes first SELECT param is count(1).
     *
     * @param rawQuery      Query
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
     *
     * @param rawQuery      Query contain first column which is a long value
     * @param selectionArgs Query parameters
     * @return query results value or -1 if no data was returned
     */
    public long findLongByRawQuery(String rawQuery, String[] selectionArgs) {
        return findLongByRawQuery(getDatabaseName(), rawQuery, selectionArgs);
    }

    /**
     * Return the first column and first row value as a Long for given rawQuery and selectionArgs.
     *
     * @param databaseName  Name of database to query
     * @param rawQuery      Query contain first column which is a long value
     * @param selectionArgs Query parameters
     * @return query results value or -1 if no data was returned
     */
    public long findLongByRawQuery(String databaseName, String rawQuery, String[] selectionArgs) {
        return findLongByRawQuery(getReadableDatabase(databaseName), rawQuery, selectionArgs);
    }

    public static long findLongByRawQuery(SQLiteDatabase database, String rawQuery, String[] selectionArgs) {
        long value = -1;

        Cursor c = database.rawQuery(rawQuery, selectionArgs);
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
     *
     * @param rawQuery      Query contain first column which is a String value
     * @param selectionArgs Query parameters
     * @return query results value or null if no data was returned
     */
    public String findStringByRawQuery(String rawQuery, String[] selectionArgs) {
        return findStringByRawQuery(getDatabaseName(), rawQuery, selectionArgs);
    }

    /**
     * Return the first column and first row value as a String for given rawQuery and selectionArgs.
     *
     * @param databaseName  Name of database to query
     * @param rawQuery      Query contain first column which is a String value
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
     *
     * @param rawQuery      Query contain first column which is a Long value
     * @param selectionArgs Query parameters
     * @return query results List or empty List returned
     */
    public List<Long> findAllLongByRawQuery(String rawQuery, String[] selectionArgs) {
        return findAllLongByRawQuery(getDatabaseName(), rawQuery, selectionArgs);
    }

    /**
     * Return a list of all of the first column values as a List<Long> for given rawQuery and selectionArgs.
     *
     * @param databaseName  Name of database to query
     * @param rawQuery      Query contain first column which is a Long value
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
     *
     * @param rawQuery      Query contain first column which is a String value
     * @param selectionArgs Query parameters
     * @return query results List or empty List returned
     */
    public List<String> findAllStringByRawQuery(String rawQuery, String[] selectionArgs) {
        return findAllStringByRawQuery(getDatabaseName(), rawQuery, selectionArgs);
    }

    /**
     * Return a list of all of the first column values as a List<String> for given rawQuery and selectionArgs.
     *
     * @param databaseName  Name of database to query
     * @param rawQuery      Query contain first column which is a String value
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

    /**
     * Use a custom query to populate a custom class that extends CustomQueryRecord
     * CustomQueryRecord.setRowData(Object[] data) is called with each result (Object[] items contain and array of objects based on CustomQueryRecord.getColumnTypes())
     *
     * @param rawQuery      Query
     * @param selectionArgs Query parameters
     * @return List of CustomQueryRecord
     */
    public <T extends CustomQueryRecord> List<T> findAllCustomRecordByRawQuery(String rawQuery, String[] selectionArgs, Class<T> type) {
        return findAllCustomRecordByRawQuery(getDatabaseName(), rawQuery, selectionArgs, type);
    }

    /**
     * Use a custom query to populate a custom class that extends CustomQueryRecord
     * CustomQueryRecord.setRowData(Object[] data) is called with each result (Object[] items contain and array of objects based on CustomQueryRecord.getColumnTypes())
     *
     * @param databaseName  Name of database to query
     * @param rawQuery      Query
     * @param selectionArgs Query parameters
     * @return List of CustomQueryRecord
     */
    public <T extends CustomQueryRecord> List<T> findAllCustomRecordByRawQuery(String databaseName, String rawQuery, String[] selectionArgs, Class<T> type) {
        List<T> foundItems;

        Class[] colTypes = null;

        Cursor cursor = getWritableDatabase(databaseName).rawQuery(rawQuery, selectionArgs);
        if (cursor != null) {
            foundItems = new ArrayList<T>(cursor.getCount());
            if (cursor.moveToFirst()) {
                do {
                    T item;

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
            foundItems = new ArrayList<T>();
        }

        return foundItems;
    }

    private static final String TABLE_EXISTS = "SELECT COUNT(1) FROM sqlite_master WHERE type = 'table' AND name = ?";

    public boolean tableExists(String tableName) {
        return tableExists(getDatabaseName(), tableName);
    }

    public boolean tableExists(String databaseName, String tableName) {
        return tableExists(getReadableDatabase(databaseName), tableName);
    }

    public static boolean tableExists(AndroidDatabase androidDatabase, String tableName) {
        return tableExists(getDatabase(androidDatabase), tableName);
    }

    public static boolean tableExists(SQLiteDatabase db, String tableName) {
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
}
