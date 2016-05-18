package org.dbtools.sample.model.database;

import org.dbtools.android.domain.config.BuildEnv;
import org.dbtools.android.domain.config.TestDatabaseConfig;

import javax.annotation.Nonnull;

public class TestMainDatabaseConfig extends TestDatabaseConfig {
    public TestMainDatabaseConfig(@Nonnull String databaseFilename) {
        super(DatabaseManagerConst.MAIN_DATABASE_NAME, BuildEnv.GRADLE, databaseFilename, DatabaseManager.MAIN_TABLES_VERSION, DatabaseManager.MAIN_VIEWS_VERSION);
    }
}
