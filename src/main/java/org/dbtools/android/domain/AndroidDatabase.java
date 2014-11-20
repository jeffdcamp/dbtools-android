package org.dbtools.android.domain;

import android.database.sqlite.SQLiteDatabase;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AndroidDatabase {
    private final boolean encrypted;
    private final boolean attached;
    private final String name;
    private final String path;
    private final int version;
    private final int viewsVersion;

    private final String password;

    private SQLiteDatabase sqLiteDatabase;
    private net.sqlcipher.database.SQLiteDatabase secureSqLiteDatabase;

    // attached Database info
    private final AndroidDatabase attachedMainDatabase;
    private final List<AndroidDatabase> attachedDatabases;

    private static ExecutorService managerExecutorService;

    public AndroidDatabase(String name, String path, int version, int viewsVersion) {
        this.name = name;
        this.path = path;
        this.version = version;
        this.viewsVersion = viewsVersion;
        this.password = null;
        this.encrypted = false;
        this.attached = false;

        this.attachedMainDatabase = null;
        this.attachedDatabases = null;
    }

    public AndroidDatabase(String name, String password, String path, int version, int viewsVersion) {
        this.name = name;
        this.password = password;
        this.encrypted = password != null;
        this.path = path;
        this.version = version;
        this.viewsVersion = viewsVersion;
        this.attached = false;

        this.attachedMainDatabase = null;
        this.attachedDatabases = null;
    }

    public AndroidDatabase(String name, AndroidDatabase attachMainDatabase, List<AndroidDatabase> attachedDatabases) {
        this.name = name;
        this.path = attachMainDatabase.getPath();
        this.version = attachMainDatabase.getVersion();
        this.viewsVersion = attachMainDatabase.getViewsVersion();
        this.password = null;
        this.encrypted = false;
        this.attached = true;

        this.attachedMainDatabase = attachMainDatabase;
        this.attachedDatabases = attachedDatabases;
    }

    public boolean isEncrypted() {
        return encrypted;
    }

    public boolean isAttached() {
        return attached;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    public int getVersion() {
        return version;
    }

    public int getViewsVersion() {
        return viewsVersion;
    }

    public String getPassword() {
        return password;
    }

    public SQLiteDatabase getSqLiteDatabase() {
        return sqLiteDatabase;
    }

    public void setSqLiteDatabase(SQLiteDatabase sqLiteDatabase) {
        this.sqLiteDatabase = sqLiteDatabase;
    }

    public net.sqlcipher.database.SQLiteDatabase getSecureSqLiteDatabase() {
        return secureSqLiteDatabase;
    }

    public void setSecureSqLiteDatabase(net.sqlcipher.database.SQLiteDatabase secureSqLiteDatabase) {
        this.secureSqLiteDatabase = secureSqLiteDatabase;
    }

    public AndroidDatabase getAttachMainDatabase() {
        return attachedMainDatabase;
    }

    public List<AndroidDatabase> getAttachedDatabases() {
        return attachedDatabases;
    }

    public boolean inTransaction() {
        if (encrypted) {
            if (secureSqLiteDatabase != null) {
                return secureSqLiteDatabase.inTransaction();
            }
        } else {
            if (sqLiteDatabase != null) {
                return sqLiteDatabase.inTransaction();
            }
        }

        return false;
    }

    public void close() {
        if (encrypted) {
            if (secureSqLiteDatabase != null) {
                secureSqLiteDatabase.close();
                secureSqLiteDatabase = null;
            }
        } else {
            if (sqLiteDatabase != null) {
                sqLiteDatabase.close();
                sqLiteDatabase = null;

            }
        }
    }

    public void beginTransaction() {
        if (encrypted) {
            if (secureSqLiteDatabase != null) {
                secureSqLiteDatabase.beginTransaction();
            }
        } else {
            if (sqLiteDatabase != null) {
                sqLiteDatabase.beginTransaction();
            }
        }
    }

    public void endTransaction(boolean success) {
        if (encrypted) {
            if (secureSqLiteDatabase != null) {
                if (success) {
                    secureSqLiteDatabase.setTransactionSuccessful();
                }
                secureSqLiteDatabase.endTransaction();
            }
        } else {
            if (sqLiteDatabase != null) {
                if (success) {
                    sqLiteDatabase.setTransactionSuccessful();
                }
                sqLiteDatabase.endTransaction();
            }
        }
    }

    @Nonnull
    public ExecutorService getManagerExecutorServiceInstance() {
        if (managerExecutorService == null) {
            managerExecutorService = Executors.newFixedThreadPool(1);
        }
        return managerExecutorService;
    }

    public void shutdownManagerExecutorService() {
        shutdownManagerExecutorService(false);
    }

    public void shutdownManagerExecutorService(boolean now) {
        if (managerExecutorService != null && !managerExecutorService.isShutdown()) {
            if (now) {
                managerExecutorService.shutdownNow();
            } else {
                managerExecutorService.shutdown();
            }
        }
    }
}
