package org.dbtools.sample.model.database;

import org.dbtools.android.domain.AndroidDatabase;
import org.dbtools.android.domain.config.TestDatabaseConfig;

import java.util.Arrays;

import javax.annotation.Nonnull;

public class TestMainDatabaseConfig extends TestDatabaseConfig {
    public TestMainDatabaseConfig(@Nonnull String databaseName) {
        super(databaseName, Arrays.asList(new AndroidDatabase(DatabaseManagerConst.MAIN_DATABASE_NAME, TEST_DB_DIR + databaseName, DatabaseManager.MAIN_TABLES_VERSION, DatabaseManager.MAIN_VIEWS_VERSION)));
    }
}
