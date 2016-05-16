package org.dbtools.sample.kotlin.model.database

import org.dbtools.android.domain.AndroidDatabase
import org.dbtools.android.domain.config.BuildEnv
import org.dbtools.android.domain.config.TestDatabaseConfig
import java.util.*

class TestMainDatabaseConfig(databaseName: String) : TestDatabaseConfig(
        databaseName,
        Arrays.asList(AndroidDatabase(DatabaseManagerConst.MAIN_DATABASE_NAME, BuildEnv.GRADLE.testDbDir + databaseName, DatabaseManager.mainTablesVersion, DatabaseManager.mainViewsVersion)), BuildEnv.GRADLE)
