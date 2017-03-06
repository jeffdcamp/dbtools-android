/*
 * IndividualViewBaseManager.kt
 *
 * GENERATED FILE - DO NOT EDIT
 * 
 */



package org.dbtools.sample.kotlin.model.database.main.individualview

import org.dbtools.sample.kotlin.model.database.DatabaseManager
import org.dbtools.android.domain.KotlinAndroidBaseManagerReadOnly


@Suppress("unused")
@SuppressWarnings("all")
abstract class IndividualViewBaseManager (databaseManager: DatabaseManager) : KotlinAndroidBaseManagerReadOnly<IndividualView>(databaseManager) {

     override val allColumns: Array<String> = IndividualViewConst.ALL_COLUMNS
     override val primaryKey = "<NO_PRIMARY_KEY_ON_VIEWS>"
     override val dropSql = IndividualViewManager.DROP_VIEW
     override val createSql = IndividualViewManager.CREATE_VIEW
     override val insertSql = ""
     override val updateSql = ""

    override fun getDatabaseName() : String {
        return IndividualViewConst.DATABASE
    }

    override fun newRecord() : IndividualView {
        return IndividualView()
    }

    override fun getTableName() : String {
        return IndividualViewConst.TABLE
    }


}