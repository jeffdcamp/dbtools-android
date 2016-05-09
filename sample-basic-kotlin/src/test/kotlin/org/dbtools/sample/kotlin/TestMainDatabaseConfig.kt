package org.dbtools.sample.kotlin

import org.dbtools.android.domain.AndroidDatabase
import org.dbtools.android.domain.config.TestDatabaseConfig
import org.dbtools.sample.kotlin.model.database.DatabaseManager
import org.dbtools.sample.kotlin.model.database.DatabaseManagerConst
import java.util.*

class TestMainDatabaseConfig(databaseName: String) : TestDatabaseConfig(
        databaseName,
        Arrays.asList(AndroidDatabase(DatabaseManagerConst.MAIN_DATABASE_NAME, TEST_DB_DIR + databaseName, DatabaseManager.mainTablesVersion, DatabaseManager.mainViewsVersion)))
