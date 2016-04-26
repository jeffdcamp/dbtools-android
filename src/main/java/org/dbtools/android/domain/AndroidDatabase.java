package org.dbtools.android.domain;

import org.dbtools.android.domain.database.DatabaseWrapper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SuppressWarnings("UnusedDeclaration")
public class AndroidDatabase {
    private final boolean attached;
    private final String name;
    private final String path;
    private final int version;
    private final int viewsVersion;

    private final String password;

    private DatabaseWrapper databaseWrapper;

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
        this.attached = false;

        this.attachedMainDatabase = null;
        this.attachedDatabases = null;
    }

    public AndroidDatabase(String name, String password, String path, int version, int viewsVersion) {
        this.name = name;
        this.password = password;
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
        this.attached = true;

        this.attachedMainDatabase = attachMainDatabase;
        this.attachedDatabases = attachedDatabases;
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

    public DatabaseWrapper getDatabaseWrapper() {
        return databaseWrapper;
    }

    public void setDatabaseWrapper(@Nullable DatabaseWrapper databaseWrapper) {
        this.databaseWrapper = databaseWrapper;
    }

    public AndroidDatabase getAttachMainDatabase() {
        return attachedMainDatabase;
    }

    public List<AndroidDatabase> getAttachedDatabases() {
        return attachedDatabases;
    }

    public boolean inTransaction() {
        if (databaseWrapper != null) {
            return databaseWrapper.inTransaction();
        }

        return false;
    }

    public void close() {
        if (databaseWrapper != null) {
            databaseWrapper.close();
            databaseWrapper = null;
        }
    }

    public void beginTransaction() {
        if (databaseWrapper != null) {
            databaseWrapper.beginTransaction();
        }
    }

    public void endTransaction(boolean success) {
        if (databaseWrapper != null) {
            if (success) {
                databaseWrapper.setTransactionSuccessful();
            }
            databaseWrapper.endTransaction();
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
