package org.dbtools.android.domain.config;

import org.dbtools.android.domain.AndroidDatabase;
import org.dbtools.android.domain.AndroidDatabaseBaseManager;
import org.dbtools.android.domain.database.DatabaseWrapper;
import org.dbtools.android.domain.database.JdbcSqliteDatabaseWrapper;
import org.dbtools.android.domain.log.DBToolsJavaLogger;
import org.dbtools.android.domain.log.DBToolsLogger;

import javax.annotation.Nonnull;
import java.io.File;
import java.util.List;

public class TestDatabaseConfig implements DatabaseConfig {
    public static final String TEST_DB_DIR = "target/test-db/";
    private String databaseName;
    private File databasePath;
    private List<AndroidDatabase> androidDatabaseList;

    public TestDatabaseConfig(@Nonnull String databaseName, @Nonnull List<AndroidDatabase> androidDatabaseList) {
        this.databaseName = databaseName;
        this.androidDatabaseList = androidDatabaseList;
        databasePath = new File(TEST_DB_DIR, databaseName);
    }

    @Override
    public DatabaseWrapper createNewDatabaseWrapper(AndroidDatabase androidDatabase) {
        File testDir = new File(TEST_DB_DIR);
        testDir.mkdirs();
        return new JdbcSqliteDatabaseWrapper("jdbc:sqlite:" + TEST_DB_DIR + databaseName);
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

    public void deleteDatabaseFile() {
        databasePath.delete();
    }
}
