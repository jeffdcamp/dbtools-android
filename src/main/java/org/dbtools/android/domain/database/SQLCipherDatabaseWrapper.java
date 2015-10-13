package org.dbtools.android.domain.database;

import android.content.ContentValues;
import android.content.Context;
import net.sqlcipher.Cursor;
import net.sqlcipher.SQLException;
import net.sqlcipher.database.*;

import javax.annotation.Nonnull;
import java.io.File;
import java.util.Locale;
import java.util.Map;

public class SQLCipherDatabaseWrapper implements DatabaseWrapper<SQLiteDatabase> {

    private SQLiteDatabase database;

    public SQLCipherDatabaseWrapper(Context context, String path, String password) {
        try {
            initSQLCipherLibs(context);
            database = SQLiteDatabase.openOrCreateDatabase(path, password, null);
        } catch (UnsatisfiedLinkError e) {
            throw new IllegalStateException("Could not find native libs (be sure to call initSQLCipherLibs(...))", e);
        }
    }

    public SQLiteDatabase getDatabase() {
        return database;
    }
    
    public void attachDatabase(String toDbPath, String toDbName, String toDbPassword) {
        String sql = "ATTACH DATABASE '" + toDbPath + "' AS " + toDbName + " KEY '" + toDbPassword + "'";
        database.execSQL(sql);
    }

    public void detachDatabase(String dbName) {
        String sql = "DETACH DATABASE '" + dbName + "'";
        database.execSQL(sql);
    }

    /**
     * Helper method to load the SQLCipher Libraries
     * @param context Android Context
     */
    public void initSQLCipherLibs(@Nonnull Context context) {
        net.sqlcipher.database.SQLiteDatabase.loadLibs(context); // Initialize SQLCipher
    }

    // **** following are from SQLiteDatabase.java ****

    @Override
    public void close() {
        database.close();
    }

    public int status(int operation, boolean reset) {
        return database.status(operation, reset);
    }

    public String getPath() {
        return database.getPath();
    }

    public boolean yieldIfContendedSafely() {
        return database.yieldIfContendedSafely();
    }

    public void releaseReference() {
        database.releaseReference();
    }

    public static SQLiteDatabase openDatabase(String path, char[] password, SQLiteDatabase.CursorFactory factory, int flags) {
        return SQLiteDatabase.openDatabase(path, password, factory, flags);
    }

    public long replace(String table, String nullColumnHack, ContentValues initialValues) {
        return database.replace(table, nullColumnHack, initialValues);
    }

    public long getMaximumSize() {
        return database.getMaximumSize();
    }

    public static SQLiteDatabase openDatabase(String path, String password, SQLiteDatabase.CursorFactory factory, int flags) {
        return SQLiteDatabase.openDatabase(path, password, factory, flags);
    }

    public void setPageSize(long numBytes) {
        database.setPageSize(numBytes);
    }

    public Cursor rawQueryWithFactory(SQLiteDatabase.CursorFactory cursorFactory, String sql, String[] selectionArgs, String editTable) {
        return database.rawQueryWithFactory(cursorFactory, sql, selectionArgs, editTable);
    }

    public void changePassword(String password) throws SQLiteException {
        database.changePassword(password);
    }

    public static SQLiteDatabase openOrCreateDatabase(String path, String password, SQLiteDatabase.CursorFactory factory) {
        return SQLiteDatabase.openOrCreateDatabase(path, password, factory);
    }

    public void changePassword(char[] password) throws SQLiteException {
        database.changePassword(password);
    }

    public int updateWithOnConflict(String table, ContentValues values, String whereClause, String[] whereArgs, int conflictAlgorithm) {
        return database.updateWithOnConflict(table, values, whereClause, whereArgs, conflictAlgorithm);
    }

    public static SQLiteDatabase openOrCreateDatabase(String path, char[] password, SQLiteDatabase.CursorFactory factory) {
        return SQLiteDatabase.openOrCreateDatabase(path, password, factory);
    }

    public long insertOrThrow(String table, String nullColumnHack, ContentValues values) throws SQLException {
        return database.insertOrThrow(table, nullColumnHack, values);
    }

    public Cursor rawQuery(String sql, String[] selectionArgs, int initialRead, int maxRead) {
        return database.rawQuery(sql, selectionArgs, initialRead, maxRead);
    }

    public static SQLiteDatabase create(SQLiteDatabase.CursorFactory factory, char[] password) {
        return SQLiteDatabase.create(factory, password);
    }

    public void setMaxSqlCacheSize(int cacheSize) {
        database.setMaxSqlCacheSize(cacheSize);
    }

    public Map<String, String> getSyncedTables() {
        return database.getSyncedTables();
    }

    public static void setICURoot(String s) {
        SQLiteDatabase.setICURoot(s);
    }

    public Cursor query(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy) {
        return database.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);
    }

    public void setLockingEnabled(boolean lockingEnabled) {
        database.setLockingEnabled(lockingEnabled);
    }

    public void purgeFromCompiledSqlCache(String sql) {
        database.purgeFromCompiledSqlCache(sql);
    }

    public boolean isDbLockedByCurrentThread() {
        return database.isDbLockedByCurrentThread();
    }

    @Deprecated
    public boolean yieldIfContended() {
        return database.yieldIfContended();
    }

    public static int releaseMemory() {
        return SQLiteDatabase.releaseMemory();
    }

    public boolean needUpgrade(int newVersion) {
        return database.needUpgrade(newVersion);
    }

    public static SQLiteDatabase openOrCreateDatabase(File file, String password, SQLiteDatabase.CursorFactory factory, SQLiteDatabaseHook databaseHook) {
        return SQLiteDatabase.openOrCreateDatabase(file, password, factory, databaseHook);
    }

    public boolean isDbLockedByOtherThreads() {
        return database.isDbLockedByOtherThreads();
    }

    public static void loadLibs(Context context, File workingDir) {
        SQLiteDatabase.loadLibs(context, workingDir);
    }

    public void resetCompiledSqlCache() {
        database.resetCompiledSqlCache();
    }

    public boolean isInCompiledSqlCache(String sql) {
        return database.isInCompiledSqlCache(sql);
    }

    public void markTableSyncable(String table, String foreignKey, String updateTable) {
        database.markTableSyncable(table, foreignKey, updateTable);
    }

    public static SQLiteDatabase openOrCreateDatabase(File file, String password, SQLiteDatabase.CursorFactory factory) {
        return SQLiteDatabase.openOrCreateDatabase(file, password, factory);
    }

    public void beginTransactionWithListener(SQLiteTransactionListener transactionListener) {
        database.beginTransactionWithListener(transactionListener);
    }

    public static SQLiteDatabase openDatabase(String path, char[] password, SQLiteDatabase.CursorFactory factory, int flags, SQLiteDatabaseHook databaseHook) {
        return SQLiteDatabase.openDatabase(path, password, factory, flags, databaseHook);
    }

    public void execSQL(String sql, Object[] bindArgs) throws SQLException {
        database.execSQL(sql, bindArgs);
    }

    public int getMaxSqlCacheSize() {
        return database.getMaxSqlCacheSize();
    }

    public void releaseReferenceFromContainer() {
        database.releaseReferenceFromContainer();
    }

    public static SQLiteDatabase openOrCreateDatabase(String path, char[] password, SQLiteDatabase.CursorFactory factory, SQLiteDatabaseHook databaseHook) {
        return SQLiteDatabase.openOrCreateDatabase(path, password, factory, databaseHook);
    }

    public static void loadLibs(Context context) {
        SQLiteDatabase.loadLibs(context);
    }

    public void setLocale(Locale locale) {
        database.setLocale(locale);
    }

    public static SQLiteDatabase openOrCreateDatabase(String path, String password, SQLiteDatabase.CursorFactory factory, SQLiteDatabaseHook databaseHook) {
        return SQLiteDatabase.openOrCreateDatabase(path, password, factory, databaseHook);
    }

    public Cursor query(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit) {
        return database.query(table, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
    }

    public void markTableSyncable(String table, String deletedTable) {
        database.markTableSyncable(table, deletedTable);
    }

    public long insertWithOnConflict(String table, String nullColumnHack, ContentValues initialValues, int conflictAlgorithm) {
        return database.insertWithOnConflict(table, nullColumnHack, initialValues, conflictAlgorithm);
    }

    public void setVersion(int version) {
        database.setVersion(version);
    }

    public int getVersion() {
        return database.getVersion();
    }

    public boolean yieldIfContendedSafely(long sleepAfterYieldDelay) {
        return database.yieldIfContendedSafely(sleepAfterYieldDelay);
    }

    public static String findEditTable(String tables) {
        return SQLiteDatabase.findEditTable(tables);
    }

    public void acquireReference() {
        database.acquireReference();
    }

    public static SQLiteDatabase create(SQLiteDatabase.CursorFactory factory, String password) {
        return SQLiteDatabase.create(factory, password);
    }

    public static SQLiteDatabase openDatabase(String path, String password, SQLiteDatabase.CursorFactory factory, int flags, SQLiteDatabaseHook databaseHook) {
        return SQLiteDatabase.openDatabase(path, password, factory, flags, databaseHook);
    }

    public boolean isReadOnly() {
        return database.isReadOnly();
    }

    public long getPageSize() {
        return database.getPageSize();
    }

    public long setMaximumSize(long numBytes) {
        return database.setMaximumSize(numBytes);
    }

    public long replaceOrThrow(String table, String nullColumnHack, ContentValues initialValues) throws SQLException {
        return database.replaceOrThrow(table, nullColumnHack, initialValues);
    }

    public Cursor queryWithFactory(SQLiteDatabase.CursorFactory cursorFactory, boolean distinct, String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit) {
        return database.queryWithFactory(cursorFactory, distinct, table, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
    }

    public void rawExecSQL(String sql) {
        database.rawExecSQL(sql);
    }

    @Override
    public void beginTransaction() {
        database.beginTransaction();
    }

    @Override
    public void endTransaction() {
        database.endTransaction();
    }

    @Override
    public void setTransactionSuccessful() {
        database.setTransactionSuccessful();
    }

    @Override
    public boolean inTransaction() {
        return database.inTransaction();
    }

    @Override
    public Cursor query(boolean distinct, String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit) {
        return database.query(distinct, table, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
    }

    @Override
    public Cursor rawQuery(String sql, String[] selectionArgs) {
        return database.rawQuery(sql, selectionArgs);
    }

    @Override
    public long insert(String table, String nullColumnHack, ContentValues values) {
        return database.insert(table, nullColumnHack, values);
    }

    @Override
    public int delete(String table, String whereClause, String[] whereArgs) {
        return database.delete(table, whereClause, whereArgs);
    }

    @Override
    public int update(String table, ContentValues values, String whereClause, String[] whereArgs) {
        return database.update(table, values, whereClause, whereArgs);
    }

    @Override
    public void execSQL(String sql) throws SQLException {
        database.execSQL(sql);
    }

    @Override
    public boolean isOpen() {
        return database.isOpen();
    }
}
