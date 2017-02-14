
package org.dbtools.sample.kotlin.model.database

import org.dbtools.android.domain.AndroidDatabase
import org.dbtools.android.domain.config.DatabaseConfig


class DatabaseManager  : DatabaseBaseManager {

    companion object {
        const val mainTablesVersion = 1
        const val mainViewsVersion = 1
    }

    constructor(databaseConfig: DatabaseConfig) : super(databaseConfig) {
    }

    override fun onUpgrade(androidDatabase: AndroidDatabase, oldVersion: Int, newVersion: Int) {
        getLogger().i(TAG, "Upgrading database [${androidDatabase.name}] from version $oldVersion to $newVersion")
    }

    override fun onUpgradeViews(androidDatabase: AndroidDatabase, oldVersion: Int, newVersion: Int) {
        getLogger().i(TAG, "Upgrading database [${androidDatabase.name}] VIEWS from version $oldVersion to $newVersion")
        // automatically drop/create views
        super.onUpgradeViews(androidDatabase, oldVersion, newVersion)
    }


}