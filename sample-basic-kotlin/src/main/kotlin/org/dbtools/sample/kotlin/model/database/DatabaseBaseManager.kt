/*
 * DatabaseBaseManager.kt
 *
 * GENERATED FILE - DO NOT EDIT
 * CHECKSTYLE:OFF
 * 
 */



package org.dbtools.sample.kotlin.model.database

import org.dbtools.android.domain.AndroidDatabase
import org.dbtools.android.domain.AndroidBaseManager
import org.dbtools.android.domain.AndroidDatabaseManager
import org.dbtools.android.domain.database.DatabaseWrapper
import org.dbtools.android.domain.config.DatabaseConfig


@SuppressWarnings("all")
abstract class DatabaseBaseManager : AndroidDatabaseManager {


    constructor(databaseConfig: DatabaseConfig): super(databaseConfig) {
    }

    fun createMainTables(androidDatabase: AndroidDatabase) {
        val database = androidDatabase.databaseWrapper
        database.beginTransaction()
        
        // Enum Tables
        AndroidBaseManager.createTable(database, org.dbtools.sample.kotlin.model.database.main.individualtype.IndividualTypeConst.CREATE_TABLE)
        
        // Tables
        AndroidBaseManager.createTable(database, org.dbtools.sample.kotlin.model.database.main.individual.IndividualConst.CREATE_TABLE)
        
        database.setTransactionSuccessful()
        database.endTransaction()
    }

    override fun onCreate(androidDatabase: AndroidDatabase) {
        getLogger().i(TAG, "Creating database: $androidDatabase.name")
        if (androidDatabase.name.equals(DatabaseManagerConst.MAIN_DATABASE_NAME)) {
            createMainTables(androidDatabase)
        }
    }

    fun createMainViews(androidDatabase: AndroidDatabase) {
        val database = androidDatabase.databaseWrapper
        database.beginTransaction()
        
        // Views
        AndroidBaseManager.createTable(database, org.dbtools.sample.kotlin.model.database.main.individualview.IndividualView.CREATE_VIEW)
        
        database.setTransactionSuccessful()
        database.endTransaction()
    }

    fun dropMainViews(androidDatabase: AndroidDatabase) {
        val database = androidDatabase.databaseWrapper
        database.beginTransaction()
        
        // Views
        AndroidBaseManager.dropTable(database, org.dbtools.sample.kotlin.model.database.main.individualview.IndividualView.DROP_VIEW)
        
        database.setTransactionSuccessful()
        database.endTransaction()
    }

    override fun onCreateViews(androidDatabase: AndroidDatabase) {
        getLogger().i(TAG, "Creating database views: $androidDatabase.name")
        if (androidDatabase.name.equals(DatabaseManagerConst.MAIN_DATABASE_NAME)) {
            createMainViews(androidDatabase)
        }
    }

    override fun onDropViews(androidDatabase: AndroidDatabase) {
        getLogger().i(TAG, "Dropping database views: $androidDatabase.name")
        if (androidDatabase.name.equals(DatabaseManagerConst.MAIN_DATABASE_NAME)) {
            dropMainViews(androidDatabase)
        }
    }


}