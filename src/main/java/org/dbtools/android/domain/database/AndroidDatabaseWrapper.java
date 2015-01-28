package org.dbtools.android.domain.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.database.sqlite.SQLiteTransactionListener;
import android.os.CancellationSignal;
import android.util.Pair;

import java.io.File;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class AndroidDatabaseWrapper implements DatabaseWrapper<SQLiteDatabase> {
    private SQLiteDatabase database;

    public AndroidDatabaseWrapper(String path) {
        database = SQLiteDatabase.openOrCreateDatabase(path, null);
    }

    public SQLiteDatabase getDatabase() {
        return database;
    }

    public void attachDatabase(String toDbPath, String toDbName, String toDbPassword) {
        String sql = "ATTACH DATABASE '" + toDbPath + "' AS " + toDbName;
        database.execSQL(sql);
    }

    // **** following are from SQLiteDatabase.java ****

    public void close() {
        database.close();
    }

    public static int releaseMemory() {
        return SQLiteDatabase.releaseMemory();
    }

    public void beginTransactionWithListenerNonExclusive(SQLiteTransactionListener transactionListener) {
        database.beginTransactionWithListenerNonExclusive(transactionListener);
    }

    public void disableWriteAheadLogging() {
        database.disableWriteAheadLogging();
    }

    public void setTransactionSuccessful() {
        database.setTransactionSuccessful();
    }

    public void beginTransactionWithListener(SQLiteTransactionListener transactionListener) {
        database.beginTransactionWithListener(transactionListener);
    }

    public SQLiteStatement compileStatement(String sql) throws SQLException {
        return database.compileStatement(sql);
    }

    public static SQLiteDatabase openDatabase(String path, SQLiteDatabase.CursorFactory factory, int flags) {
        return SQLiteDatabase.openDatabase(path, factory, flags);
    }

    public long getPageSize() {
        return database.getPageSize();
    }

    public Cursor query(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy) {
        return database.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);
    }

    public boolean enableWriteAheadLogging() {
        return database.enableWriteAheadLogging();
    }

    public int delete(String table, String whereClause, String[] whereArgs) {
        return database.delete(table, whereClause, whereArgs);
    }

    public long getMaximumSize() {
        return database.getMaximumSize();
    }


    public boolean isOpen() {
        return database.isOpen();
    }

    public static SQLiteDatabase openOrCreateDatabase(File file, SQLiteDatabase.CursorFactory factory) {
        return SQLiteDatabase.openOrCreateDatabase(file, factory);
    }

    @Deprecated
    public Map<String, String> getSyncedTables() {
        return database.getSyncedTables();
    }

    public boolean isDbLockedByCurrentThread() {
        return database.isDbLockedByCurrentThread();
    }

    public void execSQL(String sql, Object[] bindArgs) throws SQLException {
        database.execSQL(sql, bindArgs);
    }

    public void beginTransaction() {
        database.beginTransaction();
    }

    public void setPageSize(long numBytes) {
        database.setPageSize(numBytes);
    }

    public Cursor queryWithFactory(SQLiteDatabase.CursorFactory cursorFactory, boolean distinct, String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit, CancellationSignal cancellationSignal) {
        return database.queryWithFactory(cursorFactory, distinct, table, columns, selection, selectionArgs, groupBy, having, orderBy, limit, cancellationSignal);
    }

    public static SQLiteDatabase create(SQLiteDatabase.CursorFactory factory) {
        return SQLiteDatabase.create(factory);
    }

    public void acquireReference() {
        database.acquireReference();
    }

    public void releaseReference() {
        database.releaseReference();
    }

    public Cursor query(boolean distinct, String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit) {
        return database.query(distinct, table, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
    }

    @Deprecated
    public void markTableSyncable(String table, String deletedTable) {
        database.markTableSyncable(table, deletedTable);
    }

    public Cursor rawQuery(String sql, String[] selectionArgs) {
        return database.rawQuery(sql, selectionArgs);
    }

    public void beginTransactionNonExclusive() {
        database.beginTransactionNonExclusive();
    }

    public static SQLiteDatabase openOrCreateDatabase(String path, SQLiteDatabase.CursorFactory factory) {
        return SQLiteDatabase.openOrCreateDatabase(path, factory);
    }

    public boolean yieldIfContendedSafely(long sleepAfterYieldDelay) {
        return database.yieldIfContendedSafely(sleepAfterYieldDelay);
    }

    public int getVersion() {
        return database.getVersion();
    }

    public long insertWithOnConflict(String table, String nullColumnHack, ContentValues initialValues, int conflictAlgorithm) {
        return database.insertWithOnConflict(table, nullColumnHack, initialValues, conflictAlgorithm);
    }

    public void setVersion(int version) {
        database.setVersion(version);
    }

    public Cursor rawQuery(String sql, String[] selectionArgs, CancellationSignal cancellationSignal) {
        return database.rawQuery(sql, selectionArgs, cancellationSignal);
    }

    public long replaceOrThrow(String table, String nullColumnHack, ContentValues initialValues) throws SQLException {
        return database.replaceOrThrow(table, nullColumnHack, initialValues);
    }

    @Deprecated
    public boolean isDbLockedByOtherThreads() {
        return database.isDbLockedByOtherThreads();
    }

    public boolean isDatabaseIntegrityOk() {
        return database.isDatabaseIntegrityOk();
    }

    public void setMaxSqlCacheSize(int cacheSize) {
        database.setMaxSqlCacheSize(cacheSize);
    }

    public long insert(String table, String nullColumnHack, ContentValues values) {
        return database.insert(table, nullColumnHack, values);
    }

    public Cursor query(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit) {
        return database.query(table, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
    }

    public int updateWithOnConflict(String table, ContentValues values, String whereClause, String[] whereArgs, int conflictAlgorithm) {
        return database.updateWithOnConflict(table, values, whereClause, whereArgs, conflictAlgorithm);
    }

    public long replace(String table, String nullColumnHack, ContentValues initialValues) {
        return database.replace(table, nullColumnHack, initialValues);
    }

    @Deprecated
    public void markTableSyncable(String table, String foreignKey, String updateTable) {
        database.markTableSyncable(table, foreignKey, updateTable);
    }

    public boolean isWriteAheadLoggingEnabled() {
        return database.isWriteAheadLoggingEnabled();
    }

    public boolean inTransaction() {
        return database.inTransaction();
    }

    public Cursor queryWithFactory(SQLiteDatabase.CursorFactory cursorFactory, boolean distinct, String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit) {
        return database.queryWithFactory(cursorFactory, distinct, table, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
    }

    public int update(String table, ContentValues values, String whereClause, String[] whereArgs) {
        return database.update(table, values, whereClause, whereArgs);
    }

    public void endTransaction() {
        database.endTransaction();
    }

    public static String findEditTable(String tables) {
        return SQLiteDatabase.findEditTable(tables);
    }

    public static SQLiteDatabase openOrCreateDatabase(String path, SQLiteDatabase.CursorFactory factory, DatabaseErrorHandler errorHandler) {
        return SQLiteDatabase.openOrCreateDatabase(path, factory, errorHandler);
    }

    public long setMaximumSize(long numBytes) {
        return database.setMaximumSize(numBytes);
    }

    public void setForeignKeyConstraintsEnabled(boolean enable) {
        database.setForeignKeyConstraintsEnabled(enable);
    }

    public String getPath() {
        return database.getPath();
    }

    public Cursor rawQueryWithFactory(SQLiteDatabase.CursorFactory cursorFactory, String sql, String[] selectionArgs, String editTable) {
        return database.rawQueryWithFactory(cursorFactory, sql, selectionArgs, editTable);
    }

    public long insertOrThrow(String table, String nullColumnHack, ContentValues values) throws SQLException {
        return database.insertOrThrow(table, nullColumnHack, values);
    }

    public Cursor rawQueryWithFactory(SQLiteDatabase.CursorFactory cursorFactory, String sql, String[] selectionArgs, String editTable, CancellationSignal cancellationSignal) {
        return database.rawQueryWithFactory(cursorFactory, sql, selectionArgs, editTable, cancellationSignal);
    }

    public boolean isReadOnly() {
        return database.isReadOnly();
    }

    public boolean yieldIfContendedSafely() {
        return database.yieldIfContendedSafely();
    }

    public boolean needUpgrade(int newVersion) {
        return database.needUpgrade(newVersion);
    }

    public Cursor query(boolean distinct, String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit, CancellationSignal cancellationSignal) {
        return database.query(distinct, table, columns, selection, selectionArgs, groupBy, having, orderBy, limit, cancellationSignal);
    }

    public List<Pair<String, String>> getAttachedDbs() {
        return database.getAttachedDbs();
    }

    public static boolean deleteDatabase(File file) {
        return SQLiteDatabase.deleteDatabase(file);
    }

    @Deprecated
    public boolean yieldIfContended() {
        return database.yieldIfContended();
    }

    public void setLocale(Locale locale) {
        database.setLocale(locale);
    }

    @Deprecated
    public void releaseReferenceFromContainer() {
        database.releaseReferenceFromContainer();
    }

    public static SQLiteDatabase openDatabase(String path, SQLiteDatabase.CursorFactory factory, int flags, DatabaseErrorHandler errorHandler) {
        return SQLiteDatabase.openDatabase(path, factory, flags, errorHandler);
    }

    @Deprecated
    public void setLockingEnabled(boolean lockingEnabled) {
        database.setLockingEnabled(lockingEnabled);
    }

    public void execSQL(String sql) throws SQLException {
        database.execSQL(sql);
    }
}
