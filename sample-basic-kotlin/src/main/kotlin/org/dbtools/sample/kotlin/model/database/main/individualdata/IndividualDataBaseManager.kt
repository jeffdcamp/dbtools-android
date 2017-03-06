/*
 * IndividualDataBaseManager.kt
 *
 * GENERATED FILE - DO NOT EDIT
 * 
 */



package org.dbtools.sample.kotlin.model.database.main.individualdata

import org.dbtools.sample.kotlin.model.database.DatabaseManager
import org.dbtools.android.domain.KotlinAndroidBaseManagerWritable


@Suppress("unused")
@SuppressWarnings("all")
abstract class IndividualDataBaseManager (databaseManager: DatabaseManager) : KotlinAndroidBaseManagerWritable<IndividualData>(databaseManager) {

     override val allColumns: Array<String> = IndividualDataConst.ALL_COLUMNS
     override val primaryKey = "NO_PRIMARY_KEY"
     override val dropSql = IndividualDataConst.DROP_TABLE
     override val createSql = IndividualDataConst.CREATE_TABLE
     override val insertSql = IndividualDataConst.INSERT_STATEMENT
     override val updateSql = IndividualDataConst.UPDATE_STATEMENT

    override fun getDatabaseName() : String {
        return IndividualDataConst.DATABASE
    }

    override fun newRecord() : IndividualData {
        return IndividualData()
    }

    override fun getTableName() : String {
        return IndividualDataConst.TABLE
    }


}