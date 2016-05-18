package org.dbtools.android.domain.database;

import android.annotation.TargetApi;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteTransactionListener;
import android.os.Build;
import android.os.CancellationSignal;
import android.util.Pair;
import org.dbtools.android.domain.database.contentvalues.AndroidDBToolsContentValues;
import org.dbtools.android.domain.database.statement.AndroidStatementWrapper;
import org.dbtools.android.domain.database.statement.StatementWrapper;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class AndroidDatabaseWrapper implements DatabaseWrapper<SQLiteDatabase, AndroidDBToolsContentValues> {
    private SQLiteDatabase database;

    private Map<String, StatementWrapper> insertStatementMap = new HashMap<>();
    private Map<String, StatementWrapper> updateStatementMap = new HashMap<>();

    public AndroidDatabaseWrapper(String path) {
        database = SQLiteDatabase.openOrCreateDatabase(path, null);
    }

    @Override
    public SQLiteDatabase getDatabase() {
        return database;
    }

    @Override
    public AndroidDBToolsContentValues newContentValues() {
        return new AndroidDBToolsContentValues();
    }

    @Override
    public void attachDatabase(String toDbPath, String toDbName, String toDbPassword) {
        String sql = "ATTACH DATABASE '" + toDbPath + "' AS " + toDbName;
        database.execSQL(sql);
    }

    @Override
    public void detachDatabase(String dbName) {
        String sql = "DETACH DATABASE '" + dbName + "'";
        database.execSQL(sql);
    }

    // **** following are from SQLiteDatabase.java ****

    @Override
    public void close() {
        database.close();

        // cleanup statements
        DatabaseWrapperUtil.closeStatements(insertStatementMap);
        DatabaseWrapperUtil.closeStatements(updateStatementMap);
    }

    @Override
    public StatementWrapper getInsertStatement(String tableName, String sql) {
        return DatabaseWrapperUtil.createStatement(this, tableName, sql, insertStatementMap);
    }

    @Override
    public StatementWrapper getUpdateStatement(String tableName, String sql) {
        return DatabaseWrapperUtil.createStatement(this, tableName, sql, updateStatementMap);
    }

    public static int releaseMemory() {
        return SQLiteDatabase.releaseMemory();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void beginTransactionWithListenerNonExclusive(SQLiteTransactionListener transactionListener) {
        database.beginTransactionWithListenerNonExclusive(transactionListener);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void disableWriteAheadLogging() {
        database.disableWriteAheadLogging();
    }

    @Override
    public void setTransactionSuccessful() {
        database.setTransactionSuccessful();
    }

    public void beginTransactionWithListener(SQLiteTransactionListener transactionListener) {
        database.beginTransactionWithListener(transactionListener);
    }

    public StatementWrapper compileStatement(String sql) throws SQLException {
        return new AndroidStatementWrapper(database.compileStatement(sql));
    }

    public static SQLiteDatabase openDatabase(String path, SQLiteDatabase.CursorFactory factory, int flags) {
        return SQLiteDatabase.openDatabase(path, factory, flags);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static SQLiteDatabase openDatabase(String path, SQLiteDatabase.CursorFactory factory, int flags, DatabaseErrorHandler errorHandler) {
        return SQLiteDatabase.openDatabase(path, factory, flags, errorHandler);
    }

    public long getPageSize() {
        return database.getPageSize();
    }

    @Override
    public Cursor query(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy) {
        return database.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public boolean enableWriteAheadLogging() {
        return database.enableWriteAheadLogging();
    }

    @Override
    public int delete(String table, String whereClause, String[] whereArgs) {
        return database.delete(table, whereClause, whereArgs);
    }

    public long getMaximumSize() {
        return database.getMaximumSize();
    }


    @Override
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

    @Override
    public void execSQL(String sql, Object[] bindArgs) throws SQLException {
        database.execSQL(sql, bindArgs);
    }

    @Override
    public void beginTransaction() {
        database.beginTransaction();
    }

    public void setPageSize(long numBytes) {
        database.setPageSize(numBytes);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
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

    @Override
    public Cursor query(boolean distinct, String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit) {
        return database.query(distinct, table, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
    }

    @Deprecated
    public void markTableSyncable(String table, String deletedTable) {
        database.markTableSyncable(table, deletedTable);
    }

    @Override
    public Cursor rawQuery(String sql, String[] selectionArgs) {
        return database.rawQuery(sql, selectionArgs);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void beginTransactionNonExclusive() {
        database.beginTransactionNonExclusive();
    }

    public static SQLiteDatabase openOrCreateDatabase(String path, SQLiteDatabase.CursorFactory factory) {
        return SQLiteDatabase.openOrCreateDatabase(path, factory);
    }

    public boolean yieldIfContendedSafely(long sleepAfterYieldDelay) {
        return database.yieldIfContendedSafely(sleepAfterYieldDelay);
    }

    @Override
    public int getVersion() {
        return database.getVersion();
    }

    public long insertWithOnConflict(String table, String nullColumnHack, ContentValues initialValues, int conflictAlgorithm) {
        return database.insertWithOnConflict(table, nullColumnHack, initialValues, conflictAlgorithm);
    }

    @Override
    public void setVersion(int version) {
        database.setVersion(version);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
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

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public boolean isDatabaseIntegrityOk() {
        return database.isDatabaseIntegrityOk();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void setMaxSqlCacheSize(int cacheSize) {
        database.setMaxSqlCacheSize(cacheSize);
    }

    @Override
    public long insert(String table, String nullColumnHack, AndroidDBToolsContentValues values) {
        return database.insert(table, nullColumnHack, values.getContentValues());
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

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public boolean isWriteAheadLoggingEnabled() {
        return database.isWriteAheadLoggingEnabled();
    }

    @Override
    public boolean inTransaction() {
        return database.inTransaction();
    }

    public Cursor queryWithFactory(SQLiteDatabase.CursorFactory cursorFactory, boolean distinct, String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit) {
        return database.queryWithFactory(cursorFactory, distinct, table, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
    }

    @Override
    public int update(String table, AndroidDBToolsContentValues values, String whereClause, String[] whereArgs) {
        return database.update(table, values.getContentValues(), whereClause, whereArgs);
    }

    @Override
    public void endTransaction() {
        database.endTransaction();
    }

    public static String findEditTable(String tables) {
        return SQLiteDatabase.findEditTable(tables);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static SQLiteDatabase openOrCreateDatabase(String path, SQLiteDatabase.CursorFactory factory, DatabaseErrorHandler errorHandler) {
        return SQLiteDatabase.openOrCreateDatabase(path, factory, errorHandler);
    }

    public long setMaximumSize(long numBytes) {
        return database.setMaximumSize(numBytes);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
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

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
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

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public Cursor query(boolean distinct, String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit, CancellationSignal cancellationSignal) {
        return database.query(distinct, table, columns, selection, selectionArgs, groupBy, having, orderBy, limit, cancellationSignal);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public List<Pair<String, String>> getAttachedDbs() {
        return database.getAttachedDbs();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
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

    @Deprecated
    public void setLockingEnabled(boolean lockingEnabled) {
        database.setLockingEnabled(lockingEnabled);
    }

    @Override
    public void execSQL(String sql) throws SQLException {
        database.execSQL(sql);
    }
}
