package org.dbtools.sample.kotlin.model.database

import org.dbtools.android.domain.AndroidDatabase
import org.dbtools.android.domain.config.DatabaseConfig

class MultiDatabaseManager(databaseConfig: DatabaseConfig) : DatabaseManager(databaseConfig) {

    override fun onCreate(androidDatabase: AndroidDatabase) {
        createMainTables(androidDatabase)
    }

    override fun onCreateViews(androidDatabase: AndroidDatabase) {
        createMainViews(androidDatabase)
    }

    override fun onDropViews(androidDatabase: AndroidDatabase) {
        dropMainViews(androidDatabase)
    }
}
