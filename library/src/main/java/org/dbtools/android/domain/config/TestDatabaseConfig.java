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
import java.util.List;

import javax.annotation.Nonnull;

public class TestDatabaseConfig implements DatabaseConfig {
    private String databaseName;
    private File databasePath;
    private List<AndroidDatabase> androidDatabaseList;
    private BuildEnv buildEnv;

    public TestDatabaseConfig(@Nonnull String databaseName, @Nonnull List<AndroidDatabase> androidDatabaseList, BuildEnv buildEnv) {
        this.databaseName = databaseName;
        this.androidDatabaseList = androidDatabaseList;
        databasePath = new File(buildEnv.getTestDbDir(), databaseName);
        this.buildEnv = buildEnv;
    }

    @Override
    public DatabaseWrapper createNewDatabaseWrapper(AndroidDatabase androidDatabase) {
        File testDir = new File(buildEnv.getTestDbDir());
        testDir.mkdirs();
        return new JdbcSqliteDatabaseWrapper("jdbc:sqlite:" + buildEnv.getTestDbDir() + databaseName);
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

    public void deleteDatabaseFile() {
        databasePath.delete();
    }

    public BuildEnv getBuildEnv() {
        return buildEnv;
    }
}
