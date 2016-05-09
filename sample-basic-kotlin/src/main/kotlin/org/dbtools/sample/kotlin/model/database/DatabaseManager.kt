
package org.dbtools.sample.kotlin.model.database

import org.dbtools.android.domain.config.DatabaseConfig
import org.dbtools.android.domain.AndroidDatabase


class DatabaseManager : DatabaseBaseManager {

    companion object {
        val mainTablesVersion = 1
        val mainViewsVersion = 1
    }

    constructor(databaseConfig: DatabaseConfig): super(databaseConfig) {
    }

    override fun onUpgrade(androidDatabase: AndroidDatabase, oldVersion: Int, newVersion: Int) {
        getLogger().i(TAG, "Upgrading database [$androidDatabase.name] from version $oldVersion to $newVersion")
    }

    override fun onUpgradeViews(androidDatabase: AndroidDatabase, oldVersion: Int, newVersion: Int) {
        getLogger().i(TAG, "Upgrading database [$androidDatabase.name] VIEWS from version $oldVersion to $newVersion")
        // automatically drop/create views
        super.onUpgradeViews(androidDatabase, oldVersion, newVersion)
    }


}