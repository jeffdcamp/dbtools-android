/*
 * IndividualViewBaseManager.kt
 *
 * GENERATED FILE - DO NOT EDIT
 * 
 */



package org.dbtools.sample.kotlin.model.database.main.individualview

import org.dbtools.sample.kotlin.model.database.DatabaseManager
import org.dbtools.android.domain.database.DatabaseWrapper
import org.dbtools.android.domain.KotlinAndroidBaseManagerReadOnly


@SuppressWarnings("all")
abstract class IndividualViewBaseManager : KotlinAndroidBaseManagerReadOnly<IndividualView> {

     var databaseManager: DatabaseManager

    constructor(databaseManager: DatabaseManager) {
        this.databaseManager = databaseManager
    }

    override fun getDatabaseName(): String {
        return IndividualViewConst.DATABASE
    }

    override fun newRecord(): IndividualView {
        return IndividualView()
    }

    override fun getTableName(): String {
        return IndividualViewConst.TABLE
    }

    override fun getAllColumns(): Array<String> {
        return IndividualViewConst.ALL_COLUMNS
    }

    override fun getReadableDatabase(@javax.annotation.Nonnull databaseName: String): DatabaseWrapper<*, *> {
        return databaseManager.getReadableDatabase(databaseName)
    }

    fun getReadableDatabase(): DatabaseWrapper<*, *> {
        return databaseManager.getReadableDatabase(databaseName)
    }

    override fun getWritableDatabase(@javax.annotation.Nonnull databaseName: String): DatabaseWrapper<*, *> {
        return databaseManager.getWritableDatabase(databaseName)
    }

    fun getWritableDatabase(): DatabaseWrapper<*, *> {
        return databaseManager.getWritableDatabase(databaseName)
    }

    override fun getAndroidDatabase(@javax.annotation.Nonnull databaseName: String): org.dbtools.android.domain.AndroidDatabase? {
        return databaseManager.getDatabase(databaseName)
    }

    override fun getDatabaseConfig(): org.dbtools.android.domain.config.DatabaseConfig {
        return databaseManager.getDatabaseConfig()
    }

    override fun getPrimaryKey(): String {
        return ""
    }

    override fun getDropSql(): String {
        return IndividualView.DROP_VIEW
    }

    override fun getCreateSql(): String {
        return IndividualView.CREATE_VIEW
    }

    override fun getInsertSql(): String {
        return ""
    }

    override fun getUpdateSql(): String {
        return ""
    }


}