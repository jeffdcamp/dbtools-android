package org.dbtools.android.domain.config;

import org.dbtools.android.domain.AndroidDatabase;
import org.dbtools.android.domain.AndroidDatabaseBaseManager;
import org.dbtools.android.domain.database.DatabaseWrapper;
import org.dbtools.android.domain.database.JdbcSqliteDatabaseWrapper;
import org.dbtools.android.domain.database.contentvalues.DBToolsContentValues;
import org.dbtools.android.domain.database.contentvalues.JdbcDBToolsContentValues;
import org.dbtools.android.domain.log.DBToolsJavaLogger;
import org.dbtools.android.domain.log.DBToolsLogger;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

public class TestDatabaseConfig implements DatabaseConfig {
    private List<AndroidDatabase> androidDatabaseList;

    public TestDatabaseConfig(String databaseName, BuildEnv buildEnv, String databaseFilename, int tableVersion, int viewVersion) {
        this(databaseName, buildEnv.getTestDbDir() + databaseFilename, tableVersion, viewVersion);
    }

    public TestDatabaseConfig(String databaseName, String databasePath, int tableVersion, int viewVersion) {
        androidDatabaseList = new ArrayList<>();
        androidDatabaseList.add(new AndroidDatabase(databaseName, databasePath, tableVersion, viewVersion));
    }

    public TestDatabaseConfig(@Nonnull List<AndroidDatabase> androidDatabaseList) {
        this.androidDatabaseList = androidDatabaseList;
    }

    @Override
    public DatabaseWrapper createNewDatabaseWrapper(AndroidDatabase androidDatabase) {
        File testDir = new File(androidDatabase.getPath());
        testDir.getParentFile().mkdirs();

        return new JdbcSqliteDatabaseWrapper("jdbc:sqlite:" + androidDatabase.getPath());
    }

    @Override
    public void identifyDatabases(AndroidDatabaseBaseManager databaseManager) {
        for (AndroidDatabase androidDatabase : androidDatabaseList) {
            databaseManager.addDatabase(androidDatabase);
        }
    }

    @Override
    public DBToolsLogger createNewDBToolsLogger() {
        return new DBToolsJavaLogger();
    }

    @Override
    public DBToolsContentValues createNewDBToolsContentValues() {
        return new JdbcDBToolsContentValues();
    }

    /**
     * Delete single database file, based on database name
     * @param databaseName name of database to delete
     */
    public void deleteDatabaseFile(String databaseName) {
        for (AndroidDatabase androidDatabase : androidDatabaseList) {
            if (databaseName.equals(androidDatabase.getName())) {
                File file = new File(androidDatabase.getPath());
                file.delete();
            }
        }
    }

    /**
     * Delete all database files
     */
    public void deleteAllDatabaseFiles() {
        for (AndroidDatabase androidDatabase : androidDatabaseList) {
            File file = new File(androidDatabase.getPath());
            file.delete();
        }
    }
}
