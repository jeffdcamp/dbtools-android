package org.dbtools.android.domain.database;

import android.content.ContentValues;
import android.database.Cursor;
import org.jetbrains.annotations.Nullable;

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

    Cursor query(boolean distinct, String table, String[] columns, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String groupBy, @Nullable String having, @Nullable String orderBy, @Nullable String limit);

    Cursor query(String tableName, String[] strings, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String groupBy, @Nullable String having, @Nullable String orderBy);

    Cursor rawQuery(String rawQuery, @Nullable String[] selectionArgs);

    boolean inTransaction();

    void beginTransaction();

    void endTransaction();

    void setTransactionSuccessful();

    void close();
}
