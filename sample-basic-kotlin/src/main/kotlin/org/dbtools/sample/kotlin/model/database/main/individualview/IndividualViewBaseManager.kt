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
import org.dbtools.android.domain.database.contentvalues.DBToolsContentValues
import org.dbtools.android.domain.AndroidBaseRecord


@Suppress("unused", "ConvertSecondaryConstructorToPrimary")
@SuppressWarnings("all")
abstract class IndividualViewBaseManager  : KotlinAndroidBaseManagerReadOnly<IndividualView> {

     override val allColumns: Array<String> = IndividualViewConst.ALL_COLUMNS
     override val primaryKey = "<NO_PRIMARY_KEY_ON_VIEWS>"
     override val dropSql = IndividualViewManager.DROP_VIEW
     override val createSql = IndividualViewManager.CREATE_VIEW
     override val insertSql = ""
     override val updateSql = ""
     var databaseManager: DatabaseManager

    constructor(databaseManager: DatabaseManager) {
        this.databaseManager = databaseManager
    }

    override fun getDatabaseName() : String {
        return IndividualViewConst.DATABASE
    }

    override fun newRecord() : IndividualView {
        return IndividualView()
    }

    override fun getTableName() : String {
        return IndividualViewConst.TABLE
    }

    override fun getReadableDatabase(@javax.annotation.Nonnull databaseName: String) : DatabaseWrapper<in AndroidBaseRecord, in DBToolsContentValues<*>> {
        return databaseManager.getReadableDatabase(databaseName)
    }

    override fun getWritableDatabase(@javax.annotation.Nonnull databaseName: String) : DatabaseWrapper<in AndroidBaseRecord, in DBToolsContentValues<*>> {
        return databaseManager.getWritableDatabase(databaseName)
    }

    override fun getAndroidDatabase(@javax.annotation.Nonnull databaseName: String) : org.dbtools.android.domain.AndroidDatabase? {
        return databaseManager.getDatabase(databaseName)
    }

    override fun getDatabaseConfig() : org.dbtools.android.domain.config.DatabaseConfig {
        return databaseManager.databaseConfig
    }


}