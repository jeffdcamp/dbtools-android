package org.dbtools.android.domain.database;

import android.content.ContentValues;
import android.database.Cursor;

public interface DatabaseWrapper<T> {

    T getDatabase();

    void attachDatabase(String toDbPath, String toDbName, String toDbPassword);

    void detachDatabase(String dbName);

    boolean isOpen();

    int getVersion();

    void setVersion(int version);

    long insert(String tableName, String o, ContentValues contentValues);

    int update(String tableName, ContentValues contentValues, String where, String[] whereArgs);

    int delete(String tableName, String where, String[] whereArgs);

    void execSQL(String sqlStatement);

    Cursor query(boolean distinct, String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit);

    Cursor query(String tableName, String[] strings, String selection, String[] selectionArgs, String groupBy, String having, String orderBy);

    Cursor rawQuery(String rawQuery, String[] selectionArgs);

    boolean inTransaction();

    void beginTransaction();

    void endTransaction();

    void setTransactionSuccessful();

    void close();
}
