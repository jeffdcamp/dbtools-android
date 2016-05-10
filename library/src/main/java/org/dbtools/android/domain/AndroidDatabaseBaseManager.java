package org.dbtools.android.domain;

import android.content.Context;
import android.database.Cursor;
import android.os.Environment;

import org.dbtools.android.domain.config.DatabaseConfig;
import org.dbtools.android.domain.database.DatabaseWrapper;
import org.dbtools.android.domain.database.contentvalues.DBToolsContentValues;
import org.dbtools.android.domain.log.DBToolsLogger;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@SuppressWarnings("UnusedDeclaration")
public abstract class AndroidDatabaseBaseManager {
    public static final String TAG = "AndroidDBTools";

    private static final String MERGE_SOURCE_DATABASE_NAME = "dbtools_merge_source";
    private static final String TABLE_NAMES_QUERY = "SELECT name FROM %1$ssqlite_master WHERE type='table' AND name NOT IN ('metadata', 'sqlite_sequence')";
    private static final String MERGE_INSERT_QUERY = "INSERT OR IGNORE INTO %1$s SELECT * FROM %2$s";

    private Map<String, AndroidDatabase> databaseMap = new HashMap<String, AndroidDatabase>(); // <Database name, Database Path>
    private DatabaseConfig databaseConfig;
    private DBToolsLogger log;


    public abstract void onCreate(AndroidDatabase androidDatabase);

    public abstract void onCreateViews(AndroidDatabase androidDatabase);

    public abstract void onDropViews(AndroidDatabase androidDatabase);

    public abstract void onUpgrade(AndroidDatabase androidDatabase, int oldVersion, int newVersion);

    public DatabaseWrapper createNewDatabaseWrapper(AndroidDatabase androidDatabase) {
        return databaseConfig.createNewDatabaseWrapper(androidDatabase);
    }

    public AndroidDatabaseBaseManager(DatabaseConfig databaseConfig) {
        if (databaseConfig == null) {
            throw new IllegalArgumentException("databaseConfig cannot be null");
        }
        this.databaseConfig = databaseConfig;
        this.log = databaseConfig.createNewDBToolsLogger();
    }

    public Map<String, AndroidDatabase> getDatabaseMap() {
        if (databaseMap == null || databaseMap.isEmpty()) {
            // add databases to databaseMap
            databaseConfig.identifyDatabases(this);

            if (databaseMap.isEmpty()) {
                throw new IllegalStateException("Cannot get database map... DatabaseConfig.identifyDatabase(...) does not add any databases");
            }
        }
        return databaseMap;
    }

    /**
     * Return a database/platform specific version of DBToolsContentValues
     * Example:
     * - For Android platform, AndroidDBToolsContentValues
     * - For JDBC, JdbcDBToolsContentValues
     *
     * @return new instance of DBToolsContentValues
     */
    public DBToolsContentValues createNewDBToolsContentValues() {
        return databaseConfig.createNewDBToolsContentValues();
    }

    /**
     * Add a standard SQLite database
     *
     * @param context      Android Context (used to get database from context.getDatabasePath(databaseName))
     * @param databaseName Name of the database (DBTools reference name)
     * @param version      Version of the database
     * @param viewsVersion Version of the database view
     */
    public void addDatabase(@Nonnull Context context, @Nonnull String databaseName, int version, int viewsVersion) {
        String databasePath = getDatabaseFile(context, databaseName).getAbsolutePath();
        addDatabase(databaseName, databasePath, null, version, viewsVersion);
    }

    /**
     * Add a SQLCipher SQLite database.  If the password is null, then a standard SQLite database will be used
     *
     * @param context      Android Context (used to get database from context.getDatabasePath(databaseName))
     * @param databaseName Name of the database (DBTools reference name)
     * @param password     Database password
     * @param version      Version of the database
     * @param viewsVersion Version of the database view
     */
    public void addDatabase(@Nonnull Context context, @Nonnull String databaseName, @Nullable String password, int version, int viewsVersion) {
        String databasePath = getDatabaseFile(context, databaseName).getAbsolutePath();
        addDatabase(databaseName, databasePath, password, version, viewsVersion);
    }

    /**
     * Add a SQLCipher SQLite database.
     *
     * @param databaseName Name of the database (DBTools reference name)
     * @param databasePath Absolute path to database
     * @param version      Version of the database
     * @param viewsVersion Version of the database view
     */
    public void addDatabase(@Nonnull String databaseName, @Nonnull String databasePath, int version, int viewsVersion) {
        addDatabase(databaseName, databasePath, null, version, viewsVersion);
    }

    /**
     * Add a SQLCipher SQLite database.  If the password is null, then a standard SQLite database will be used
     *
     * @param databaseName Name of the database (DBTools reference name)
     * @param databasePath Absolute path to database
     * @param password     Database password
     * @param version      Version of the database
     * @param viewsVersion Version of the database view
     */
    public void addDatabase(@Nonnull String databaseName, @Nonnull String databasePath, @Nullable String password, int version, int viewsVersion) {
        if (password != null) {
            addDatabase(new AndroidDatabase(databaseName, password, databasePath, version, viewsVersion));
        } else {
            addDatabase(new AndroidDatabase(databaseName, databasePath, version, viewsVersion));
        }
    }

    public void addDatabase(@Nonnull AndroidDatabase database) {
        databaseMap.put(database.getName(), database);
    }

    /**
     * Close and Remove an identified database from AndroidDatabaseManager
     * @param databaseName Database to be closed and removed
     */
    public void removeDatabase(@Nonnull String databaseName) {
        AndroidDatabase database = getDatabaseMap().get(databaseName);
        if (database != null) {
            closeDatabase(database);
            getDatabaseMap().remove(databaseName);
        }
    }

    /**
     * Add a attached standard SQLite database
     *
     * @param databaseName          Name of the attached database (DBTools reference name)
     * @param primaryDatabaseName   Primary Database name
     * @param attachedDatabaseNames Database names to be attached to Primary Database
     */
    public void addAttachedDatabase(@Nonnull String databaseName, @Nonnull String primaryDatabaseName, @Nonnull List<String> attachedDatabaseNames) {
        AndroidDatabase primaryDatabase = getDatabase(primaryDatabaseName);
        if (primaryDatabase == null) {
            throw new IllegalStateException("Database [" + primaryDatabaseName + "] does not exist");
        }

        List<AndroidDatabase> attachedDatabases = new ArrayList<AndroidDatabase>();
        for (String databaseNameToAttach : attachedDatabaseNames) {
            AndroidDatabase databaseToAttach = getDatabase(databaseNameToAttach);
            if (databaseToAttach == null) {
                throw new IllegalStateException("Database to attach [" + databaseNameToAttach + "] does not exist");
            }

            attachedDatabases.add(databaseToAttach);
        }

        getDatabaseMap().put(databaseName, new AndroidDatabase(databaseName, primaryDatabase, attachedDatabases));
    }

    /**
     * Add a attached standard SQLite database
     *
     * @param databaseName      Name of the attached database
     * @param primaryDatabase   Primary Database
     * @param attachedDatabases Databases to be attached to Primary Database
     */
    public void addAttachedDatabase(@Nonnull String databaseName, @Nonnull AndroidDatabase primaryDatabase, @Nonnull List<AndroidDatabase> attachedDatabases) {
        getDatabaseMap().put(databaseName, new AndroidDatabase(databaseName, primaryDatabase, attachedDatabases));
    }

    /**
     * Close and Remove and attached database from AndroidDatabaseManager
     * @param databaseName Database to be closed and removed
     */
    public void removeAttachedDatabase(@Nonnull String databaseName) {
        removeDatabase(databaseName);
    }

    /**
     * Reset/Removes any references to any database that was added
     */
    public void reset() {
        databaseMap = new HashMap<String, AndroidDatabase>();
    }

    @Nonnull
    public Collection<AndroidDatabase> getDatabases() {
        return getDatabaseMap().values();
    }

    @Nonnull
    public String getDatabasePath(@Nonnull String databaseName) {
        return getDatabaseMap().get(databaseName).getPath();
    }

    @Nullable
    public AndroidDatabase getDatabase(@Nonnull String databaseName) {
        return getDatabaseMap().get(databaseName);
    }

    public boolean containsDatabase(@Nonnull String databaseName) {
        return getDatabaseMap().containsKey(databaseName);
    }

    private boolean isDatabaseAlreadyOpen(@Nonnull AndroidDatabase androidDatabase) {
        DatabaseWrapper database = androidDatabase.getDatabaseWrapper();
        return database != null && database.getDatabase() != null && database.isOpen();
    }

    public boolean openDatabase(@Nonnull String databaseName) {
        AndroidDatabase androidDatabase = getDatabase(databaseName);
        return androidDatabase != null && openDatabase(androidDatabase);
    }

    public boolean openDatabase(@Nonnull AndroidDatabase androidDatabase) {
        DatabaseWrapper wrapper = androidDatabase.getDatabaseWrapper();

        if (wrapper == null) {
            wrapper = createNewDatabaseWrapper(androidDatabase);
            androidDatabase.setDatabaseWrapper(wrapper);
        }

        return wrapper.isOpen();
    }

    public void closeDatabase(@Nonnull String databaseName) {
        AndroidDatabase database = getDatabase(databaseName);
        if (database != null) {
            closeDatabase(database);
        }
    }

    public boolean closeDatabase(@Nonnull AndroidDatabase androidDatabase) {
        DatabaseWrapper wrapper = androidDatabase.getDatabaseWrapper();
        if (wrapper != null && wrapper.isOpen() && !wrapper.inTransaction()) {
            wrapper.close();
            androidDatabase.setDatabaseWrapper(null);
            return true;
        }

        return false;
    }

    public void connectAllDatabases() {
        for (AndroidDatabase database : getDatabases()) {
            connectDatabase(database.getName());
        }
    }

    public void connectDatabase(@Nonnull String databaseName) {
        connectDatabase(databaseName, true);
    }

    public synchronized void connectDatabase(@Nonnull String databaseName, boolean checkForUpgrade) {
        AndroidDatabase androidDatabase = getDatabaseMap().get(databaseName);

        if (androidDatabase == null) {
            throw new IllegalArgumentException("Cannot connect to database (Cannot find database [" + databaseName + "] in databaseMap (databaseMap size: " + databaseMap.size() + ")).  Was this database added in the identifyDatabases() method?");
        }

        boolean databaseAlreadyExists = new File(androidDatabase.getPath()).exists();
        if (!databaseAlreadyExists || !isDatabaseAlreadyOpen(androidDatabase)) {
            String databasePath = androidDatabase.getPath();
            File databaseFile = new File(databasePath);
            boolean databaseExists = databaseFile.exists();
            log.i(TAG, "Connecting to database");
            log.i(TAG, "Database exists: " + databaseExists + "(path: " + databasePath + ")");

            // if this is an attached database, make sure the main database is open first
            AndroidDatabase attachMainDatabase = androidDatabase.getAttachMainDatabase();
            if (attachMainDatabase != null) {
                connectDatabase(attachMainDatabase.getName());
            }

            openDatabase(androidDatabase);

            if (!androidDatabase.isAttached()) {
                // if the database did not already exist, just created it, otherwise perform upgrade path
                if (!databaseAlreadyExists) {
                    createMetaTableIfNotExists(androidDatabase);
                    onCreate(androidDatabase);
                    onCreateViews(androidDatabase);
                } else if (checkForUpgrade) {
                    onUpgrade(androidDatabase, androidDatabase.getDatabaseWrapper().getVersion(), androidDatabase.getVersion());
                    onUpgradeViews(androidDatabase, findViewVersion(androidDatabase), androidDatabase.getViewsVersion());
                }

                // update database and views versions
                androidDatabase.getDatabaseWrapper().setVersion(androidDatabase.getVersion());
                updateDatabaseMetaViewVersion(androidDatabase, androidDatabase.getViewsVersion());
            } else {
                // make sure database being attached are connected/opened
                for (AndroidDatabase otherDb : androidDatabase.getAttachedDatabases()) {
                    if (otherDb.isAttached()) {
                        throw new IllegalStateException("Attached databases cannot be attach type databases (for database [" + otherDb.getName() + "]");
                    }

                    connectDatabase(otherDb.getName());
                }

                // attached database
                attachDatabases(androidDatabase);
            }
        }
    }

    public void attachDatabases(@Nonnull AndroidDatabase db) {
        for (AndroidDatabase toDb : db.getAttachedDatabases()) {
            db.getDatabaseWrapper().attachDatabase(toDb.getPath(), toDb.getName(), toDb.getPassword());
        }
    }

    public void detachDatabases(@Nonnull String databaseName) {
        AndroidDatabase androidDatabase = getDatabaseMap().get(databaseName);
        if (androidDatabase == null) {
            throw new IllegalArgumentException("Database [" + databaseName + "] does not exist");
        }

        detachDatabases(androidDatabase);
    }

    public void detachDatabases(@Nonnull AndroidDatabase db) {
        for (AndroidDatabase toDb : db.getAttachedDatabases()) {
            detachDatabase(db, toDb.getName());
        }
    }

    public void detachDatabase(@Nonnull String databaseName, @Nonnull String databaseToDetach) {
        AndroidDatabase androidDatabase = getDatabaseMap().get(databaseName);
        if (androidDatabase == null) {
            throw new IllegalArgumentException("Database [" + databaseName + "] does not exist");
        }

        detachDatabase(androidDatabase, databaseToDetach);
    }

    public void detachDatabase(@Nonnull AndroidDatabase db, @Nonnull String databaseToDetach) {
        db.getDatabaseWrapper().detachDatabase(databaseToDetach);
    }

    public void cleanDatabase(@Nonnull String databaseName) {
        AndroidDatabase database = getDatabase(databaseName);
        if (database != null) {
            onCleanDatabase(database);
        }
    }

    public void cleanAllDatabases() {
        for (AndroidDatabase androidDatabase : getDatabaseMap().values()) {
            onCleanDatabase(androidDatabase);
        }
    }

    /**
     * Get Database File for an Internal Memory Database
     *
     * @param context      Android Context
     * @param databaseName Name of database file
     * @return Database File
     */
    @Nonnull
    public File getDatabaseFile(@Nonnull Context context, @Nonnull String databaseName) {
        File file = context.getDatabasePath(databaseName);

        File directory = new File(file.getParent());

        // Make the necessary directories if needed.
        if (!directory.exists() && !directory.mkdirs()) {
            throw new IllegalStateException("Cannot write to database path: " + file.getAbsolutePath());
        }

        return file;
    }

    /**
     * Get Database File for an Internal Memory Database
     *
     * @param context        Android Context
     * @param databaseSubDir Name of directory to place database in
     * @param databaseName   Name of database file
     * @return Database File
     */
    @Nonnull
    public File getDatabaseExternalFile(@Nonnull Context context, @Nonnull String databaseSubDir, @Nonnull String databaseName) {
        boolean mediaMounted = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        File cacheDir = context.getExternalFilesDir(databaseSubDir);

        File file;
        File directory;
        if (mediaMounted) {
            directory = cacheDir;
            file = new File(directory, databaseName);
        } else {
            directory = context.getFilesDir();
            file = new File(directory.getAbsolutePath() + File.separator + databaseName);
        }

        directory = new File(file.getParent());

        // Make the necessary directories if needed.
        if (!directory.exists() && !directory.mkdirs()) {
            throw new IllegalStateException("Cannot write to SDCard.  Be sure the application has permissions.");
        }

        return file;
    }

    /**
     * Delete and then recreate the database
     *
     * @param androidDatabase database to clean
     */
    public void onCleanDatabase(@Nonnull AndroidDatabase androidDatabase) {
        log.i(TAG, "Cleaning Database");
        deleteDatabase(androidDatabase);
        connectDatabase(androidDatabase.getName(), false);  // do not update here, because it will cause a recursive call
    }

    /**
     * Delete databases and reset references from AndroidDatabaseManager
     */
    public void wipeDatabases() {
        log.e(TAG, "Wiping databases");
        for (AndroidDatabase database : getDatabases()) {
            deleteDatabase(database);
        }

        // remove existing references
        // this should be done after clearing the preferences (to be sure not to use the old password)
        reset();
        databaseConfig.identifyDatabases(this);
    }

    public void deleteDatabase(@Nonnull String databaseName) {
        AndroidDatabase database = getDatabase(databaseName);
        if (database != null) {
            deleteDatabase(database);
        } else {
            log.e(TAG, "FAILED to delete database named [" + databaseName + "]. This database is not added to DatabaseManager");
        }
    }

    public void deleteDatabase(@Nonnull AndroidDatabase androidDatabase) {
        String databasePath = androidDatabase.getPath();

        try {
            if (!androidDatabase.inTransaction()) {
                androidDatabase.close(); // this will hang if there is an open transaction
            }
        } catch (Exception e) {
            // inTransaction can throw "IllegalStateException: attempt to re-open an already-closed object"
            // This should not keep LDS Tools from performing a SYNC
            log.w(TAG, "deleteDatabase().inTransaction() Error: [" + e.getMessage() + "]");
        }

        log.i(TAG, "Deleting database: [" + databasePath + "]");
        File databaseFile = new File(databasePath);
        if (databaseFile.exists() && !deleteDatabaseFiles(databaseFile)) {
            String errorMessage = "FAILED to delete database: [" + databasePath + "]";
            log.e(TAG, errorMessage);
            throw new IllegalStateException(errorMessage);
        }
    }

    /**
     * Deletes a database including its journal file and other auxiliary files
     * that may have been created by the database engine.
     *
     * @param file The database file path.
     * @return True if the database was successfully deleted.
     */
    public boolean deleteDatabaseFiles(@Nullable File file) {
        if (file == null) {
            throw new IllegalArgumentException("file must not be null");
        }

        boolean deleted;
        deleted = file.delete();
        deleted |= new File(file.getPath() + "-journal").delete();
        deleted |= new File(file.getPath() + "-shm").delete();
        deleted |= new File(file.getPath() + "-wal").delete();

        File dir = file.getParentFile();
        if (dir != null) {
            final String prefix = file.getName() + "-mj";
            final FileFilter filter = new FileFilter() {
                @Override
                public boolean accept(File candidate) {
                    return candidate.getName().startsWith(prefix);
                }
            };
            for (File masterJournal : dir.listFiles(filter)) {
                deleted |= masterJournal.delete();
            }
        }
        return deleted;
    }

    public boolean renameDatabaseFiles(@Nullable File srcFile, @Nonnull File targetFile) {
        if (srcFile == null) {
            throw new IllegalArgumentException("file must not be null");
        }

        boolean renamed;
        renamed = srcFile.renameTo(new File(targetFile.getPath()));
        renamed |= new File(srcFile.getPath() + "-journal").renameTo(new File(targetFile.getPath() + "-journal"));
        renamed |= new File(srcFile.getPath() + "-shm").renameTo(new File(targetFile.getPath() + "-shm"));
        renamed |= new File(srcFile.getPath() + "-wal").renameTo(new File(targetFile.getPath() + "-wal"));

        // delete srcFile -mj files
        File dir = srcFile.getParentFile();
        if (dir != null) {
            final String prefix = srcFile.getName() + "-mj";
            final FileFilter filter = new FileFilter() {
                @Override
                public boolean accept(File candidate) {
                    return candidate.getName().startsWith(prefix);
                }
            };
            for (File masterJournal : dir.listFiles(filter)) {
                renamed |= masterJournal.delete();
            }
        }
        return renamed;
    }

    public void beginTransaction(@Nonnull String databaseName) {
        AndroidDatabase database = getDatabase(databaseName);
        if (database != null) {
            database.beginTransaction();
        }
    }

    public void endTransaction(@Nonnull String databaseName, boolean success) {
        AndroidDatabase database = getDatabase(databaseName);
        if (database != null) {
            database.endTransaction(success);
        }
    }

    public void onUpgradeViews(@Nonnull AndroidDatabase androidDatabase, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            log.i(TAG, "Upgrading database VIEWS [" + androidDatabase.getName() + "] from version " + oldVersion + " to " + newVersion);
            onDropViews(androidDatabase);
            onCreateViews(androidDatabase);
        }
    }

    public void createMetaTableIfNotExists(@Nonnull AndroidDatabase androidDatabase) {
        if (!AndroidBaseManager.tableExists(androidDatabase.getDatabaseWrapper(), DBToolsMetaData.TABLE)) {
            AndroidBaseManager.executeSql(androidDatabase.getDatabaseWrapper(), DBToolsMetaData.CREATE_TABLE);
        }
    }

    private String getViewVersionKey(@Nonnull AndroidDatabase androidDatabase) {
        return androidDatabase.getName() + "_view_version";
    }

    private void updateDatabaseMetaViewVersion(@Nonnull AndroidDatabase androidDatabase, int version) {
        createMetaTableIfNotExists(androidDatabase);

        int currentVersion = findViewVersion(androidDatabase);
        String keyName = getViewVersionKey(androidDatabase);

        String table = DBToolsMetaData.TABLE;

        DBToolsContentValues contentValues = androidDatabase.getDatabaseWrapper().newContentValues();
        contentValues.put(DBToolsMetaData.C_KEY, keyName);
        contentValues.put(DBToolsMetaData.C_VALUE, version);

        if (currentVersion != -1) {
            String selection = DBToolsMetaData.KEY_SELECTION;
            String[] whereArgs = new String[]{keyName};
            androidDatabase.getDatabaseWrapper().update(table, contentValues, selection, whereArgs);
        } else {
            androidDatabase.getDatabaseWrapper().insert(table, null, contentValues);
        }
    }

    private static final String FIND_VERSION = "SELECT " + DBToolsMetaData.C_VALUE + " FROM " + DBToolsMetaData.TABLE +
            " WHERE " + DBToolsMetaData.KEY_SELECTION;

    public int findViewVersion(@Nonnull AndroidDatabase androidDatabase) {
        createMetaTableIfNotExists(androidDatabase);

        String[] selectionArgs = new String[]{getViewVersionKey(androidDatabase)};
        String textValue = AndroidBaseManager.findValueByRawQuery(androidDatabase.getDatabaseWrapper(), String.class, FIND_VERSION, selectionArgs, "-1");

        int value = -1;
        try {
            value = Integer.parseInt(textValue);
        } catch (Exception e) {
            getLogger().e(TAG, "Cannot parse database view version", e);
        }

        return value;
    }

    public void shutdownAllManagerExecutorServices() {
        for (AndroidDatabase androidDatabase : getDatabases()) {
            shutdownManagerExecutorService(androidDatabase);
        }
    }

    public void shutdownManagerExecutorService(String databaseName) {
        AndroidDatabase androidDatabase = getDatabase(databaseName);

        if (androidDatabase != null) {
            shutdownManagerExecutorService(androidDatabase);
        }
    }

    public void shutdownManagerExecutorService(@Nonnull AndroidDatabase androidDatabase) {
        ExecutorService service = androidDatabase.getManagerExecutorServiceInstance();
        if (!service.isShutdown()) {
            service.shutdown();
        }
    }

    public boolean mergeDatabase(@Nullable File sourceDatabaseFile, @Nullable AndroidDatabase targetDatabase) {
        return mergeDatabase(sourceDatabaseFile, null, targetDatabase);
    }

    public boolean mergeDatabase(@Nullable File sourceDatabaseFile, @Nullable String sourceDatabasePassword, @Nullable AndroidDatabase targetDatabase) {
        if (targetDatabase == null) {
            log.e(TAG, "Failed to merged :: targetDatabase is null");
            return false;
        }

        if (sourceDatabaseFile == null) {
            log.e(TAG, "Failed to merged :: sourceDatabaseFile is null");
            return false;
        }

        if (!sourceDatabaseFile.exists()) {
            log.e(TAG, "Failed to merged [" + sourceDatabaseFile.getAbsolutePath() + "] into [" + targetDatabase.getName() + "] :: Source database does not exist");
            return false;
        }

        // Attach sourceDatabase with primary
        targetDatabase.getDatabaseWrapper().attachDatabase(sourceDatabaseFile.getAbsolutePath(), MERGE_SOURCE_DATABASE_NAME, sourceDatabasePassword);



        // Get a list of tables to merge
        List<String> sourceTableNames = findTableNames(targetDatabase, MERGE_SOURCE_DATABASE_NAME);
        List<String> targetTableNames = findTableNames(targetDatabase);

        // Merge table content (insert content from source database)
        targetDatabase.beginTransaction();

        for (String tableName : sourceTableNames) {
            if (targetTableNames.contains(tableName)) {
                copyTableData(targetDatabase, MERGE_SOURCE_DATABASE_NAME + "." + tableName, tableName);
            }
        }

        targetDatabase.endTransaction(true);

        // Detach databases
        targetDatabase.getDatabaseWrapper().detachDatabase(MERGE_SOURCE_DATABASE_NAME);

        return true;
    }

    public void copyTableData(@Nonnull AndroidDatabase targetDatabase, String sourceTableName, String targetTableName) {
        targetDatabase.getDatabaseWrapper().execSQL(String.format(MERGE_INSERT_QUERY, targetTableName, sourceTableName));
    }

    @Nonnull
    private List<String> findTableNames(@Nonnull String databaseName) {
        return findTableNames(getDatabase(databaseName));
    }

    @Nonnull
    private List<String> findTableNames(@Nullable AndroidDatabase database) {
        return findTableNames(database, null);
    }

    @Nonnull
    private List<String> findTableNames(@Nullable AndroidDatabase database, @Nullable String attachedDatabaseName) {
        if (database == null) {
            return new ArrayList<String>();
        }

        List<String> tableNames;

        // prepare query
        String attachedDbPrefix;
        if (attachedDatabaseName != null && !attachedDatabaseName.trim().isEmpty()) {
            attachedDbPrefix = attachedDatabaseName + ".";
        } else {
            attachedDbPrefix = "";
        }
        String query = String.format(TABLE_NAMES_QUERY, attachedDbPrefix);

        Cursor tableNamesCursor = database.getDatabaseWrapper().rawQuery(query, null);
        if (tableNamesCursor != null) {
            tableNames = new ArrayList<String>(tableNamesCursor.getCount());
            if (tableNamesCursor.moveToFirst()) {
                do {
                    tableNames.add(tableNamesCursor.getString(0));
                } while (tableNamesCursor.moveToNext());
            }
            tableNamesCursor.close();
        } else {
            tableNames = new ArrayList<String>();
        }

        return tableNames;
    }

    public DBToolsLogger getLogger() {
        return log;
    }

    public DatabaseConfig getDatabaseConfig() {
        return databaseConfig;
    }
}
