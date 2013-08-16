package org.dbtools.android.domain;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AndroidDatabaseBaseManager {
    public static final String TAG = "AndroidDBTools";

    private List<AndroidDatabase> databases = new ArrayList<AndroidDatabase>();
    private Map<String, AndroidDatabase> databaseMap = new HashMap<String, AndroidDatabase>(); // <Database name, Database Path>

    public abstract void onCreate(AndroidDatabase database);
    public abstract void onUpgrade(AndroidDatabase database, int oldVersion, int newVersion);
    public abstract void onCleanDatabase(AndroidDatabase database);

    /**
     * Add a standard SQLite database
     * @param context Android Context
     * @param databaseName Name of the database
     * @param version Version of the database
     */
    public void addDatabase(Context context, String databaseName, int version) {
        String databasePath = getDatabaseFile(context, databaseName).getAbsolutePath();
        databases.add(new AndroidDatabase(databaseName, databasePath, version));
    }

    /**
     * Add a SQLCipher SQLite database.  If the password is null, then a standard SQLite database will be used
     * @param context Android Context
     * @param databaseName Name of the database
     * @param password Database password
     * @param version Version of the database
     */
    public void addDatabase(Context context, String databaseName, String password, int version) {
        String databasePath = getDatabaseFile(context, databaseName).getAbsolutePath();

        if (password != null) {
            databases.add(new AndroidDatabase(databaseName, password, databasePath, version));
        } else {
            databases.add(new AndroidDatabase(databaseName, databasePath, version));
        }
    }

    public void addDatabase(AndroidDatabase database) {
        databases.add(database);
    }

    public List<AndroidDatabase> getDatabases() {
        return databases;
    }

    private synchronized void initDatabases() {
        if (databaseMap == null || databaseMap.size() == 0) {
            List<AndroidDatabase> databases = getDatabases();
            for (AndroidDatabase database : databases) {
                databaseMap.put(database.getName(), database);
                connectDatabase(database.getName());
            }
        }
    }

    public String getDatabasePath(String databaseName) {
        return databaseMap.get(databaseName).getPath();
    }

    public AndroidDatabase getDatabase(String databaseName) {
        return databaseMap.get(databaseName);
    }

    private boolean isDatabaseAlreadyOpen(AndroidDatabase db) {
        if (db.isEncrypted()) {
            net.sqlcipher.database.SQLiteDatabase database = db.getSecureSqLiteDatabase();
            return database != null && database.isOpen();
        } else {
            SQLiteDatabase database = db.getSqLiteDatabase();
            return database != null && database.isOpen();
        }

    }

    /**
     * Load the SQLCipher Libraries
     * @param context Android Context
     */
    public void initSQLCipherLibs(Context context) {
        net.sqlcipher.database.SQLiteDatabase.loadLibs(context); // Initialize SQLCipher
    }

    /**
     * Load the SQLCipher Libraries
     * @param context Android Context
     * @param workingDir Directory to extract SQLCipher files (such as an external storage space)
     */
    public void initSQLCipherLibs(Context context, File workingDir) {
        net.sqlcipher.database.SQLiteDatabase.loadLibs(context, workingDir); // Initialize SQLCipher
    }

    private void openDatabase(AndroidDatabase db) {
        if (db.isEncrypted()) {
            try {
                db.setSecureSqLiteDatabase(net.sqlcipher.database.SQLiteDatabase.openOrCreateDatabase(db.getPath(), db.getPassword(), null));
            } catch (UnsatisfiedLinkError e) {
                throw new IllegalStateException("Could not find native libs (be sure to call initSQLCipherLibs(...))", e);
            }
        } else {
            db.setSqLiteDatabase(SQLiteDatabase.openOrCreateDatabase(db.getPath(), null));
        }
    }

    public void connectDatabase(String databaseName) {
        connectDatabase(databaseName, true);
    }

    public void connectDatabase(String databaseName, boolean checkForUpgrade) {
        initDatabases(); // (if needed)

        AndroidDatabase db = databaseMap.get(databaseName);
        boolean databaseAlreadyExists = new File(db.getPath()).exists();
        if (!databaseAlreadyExists || !isDatabaseAlreadyOpen(db)) {
            try {
                String databasePath = db.getPath();
                File databaseFile = new File(databasePath);
                boolean databaseExists = databaseFile.exists();
                Log.i(TAG, "Connecting to database");
                Log.i(TAG, "Database exists: " + databaseExists + "(path: " + databasePath + ")");

                try {
                    openDatabase(db);
                } catch (Exception e1) {
                    Log.e(TAG, "Failed to open database [" + databasePath + "] Reason: [" + e1.getMessage() + "]", e1);
                    Log.i(TAG, "Deleting existing database and trying to re-open database");
                    if (databaseExists) {
                        String message = databaseFile.delete() ? "Existing database deleted" : "FAILED to delete existing database";
                        Log.i(TAG, message);
                    }

                    Log.i(TAG, "Connecting to database (attempt 2)");
                    openDatabase(db);
                }

                // if the database did not already exist, just created it, otherwise perform upgrade path
                if (!databaseAlreadyExists) {
                    onCreate(db);
                } else if (checkForUpgrade) {
                    if (db.isEncrypted()) {
                        onUpgrade(db, db.getSecureSqLiteDatabase().getVersion(), db.getVersion());
                    } else {
                        onUpgrade(db, db.getSqLiteDatabase().getVersion(), db.getVersion());
                    }
                }

                // update database version
                if (db.isEncrypted()) {
                    db.getSecureSqLiteDatabase().setVersion(db.getVersion());
                } else {
                    db.getSqLiteDatabase().setVersion(db.getVersion());
                }
            } catch (Exception ex) {
                Log.e(TAG, "error -- " + ex.getMessage(), ex);
            }
        }
    }

    public void cleanDatabase(String databaseName) {
        onCleanDatabase(getDatabase(databaseName));
    }

    public void cleanAllDatabases() {
        for (AndroidDatabase androidDatabase : databaseMap.values()) {
            onCleanDatabase(androidDatabase);
        }
    }

    /**
     * Get Database File for an Internal Memory Database
     * @param databaseName Name of database file
     * @return Database File
     */
    public File getDatabaseFile(Context context, String databaseName) {
        File file =  context.getDatabasePath(databaseName);

        File directory = new File(file.getParent());

        // Make the necessary directories if needed.
        if (!directory.exists() && !directory.mkdirs()) {
            throw new IllegalStateException("Cannot write to database path: " + file.getAbsolutePath());
        }

        return file;
    }

    /**
     * Get Database File for an Internal Memory Database
     * @param context Android Context
     * @param databaseSubDir Name of directory to place database in
     * @param databaseName Name of database file
     * @return Database File
     */
    public File getDatabaseExternalFile(Context context, String databaseSubDir, String databaseName) {
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

    public boolean deleteDatabaseFiles(AndroidDatabase db) {
        return deleteDatabaseFiles(new File(db.getPath()));
    }

    /**
     * Deletes a database including its journal file and other auxiliary files
     * that may have been created by the database engine.
     *
     * @param file The database file path.
     * @return True if the database was successfully deleted.
     */
    public boolean deleteDatabaseFiles(File file) {
        if (file == null) {
            throw new IllegalArgumentException("file must not be null");
        }

        boolean deleted = false;
        deleted |= file.delete();
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

    public boolean renameDatabaseFiles(File srcFile, File targetFile) {
        if (srcFile == null) {
            throw new IllegalArgumentException("file must not be null");
        }

        boolean renamed = false;
        renamed |= srcFile.renameTo(new File(targetFile.getPath()));
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
}
