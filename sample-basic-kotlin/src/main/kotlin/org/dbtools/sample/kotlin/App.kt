package org.dbtools.sample.kotlin

import android.app.Application

import org.dbtools.sample.kotlin.model.database.AppDatabaseConfig
import org.dbtools.sample.kotlin.model.database.DatabaseManager
import org.dbtools.sample.kotlin.model.database.main.MainDatabaseManagers

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        databaseManager = DatabaseManager(AppDatabaseConfig(this))
        MainDatabaseManagers.init(databaseManager as DatabaseManager)
    }

    companion object {
        private var databaseManager: DatabaseManager? = null
    }
}
