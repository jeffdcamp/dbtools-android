/*
 * IndividualQueryManager.kt
 *
 * Generated on: 05/09/2016 09:21:13
 *
 */



package org.dbtools.sample.kotlin.model.database.main.individualquery

import org.dbtools.sample.kotlin.model.database.DatabaseManager
import org.dbtools.sample.kotlin.model.database.main.individual.IndividualConst


class IndividualQueryManager(databaseManager: DatabaseManager) : IndividualQueryBaseManager(databaseManager) {

    companion object {
        val QUERY = "SELECT " +
                IndividualConst.FULL_C_ID + " AS " + IndividualQueryConst.C_ID + ", " +
                IndividualConst.FULL_C_FIRST_NAME + " AS " + IndividualQueryConst.C_NAME +
                " FROM " + IndividualConst.TABLE
    }

    override fun getQuery(): String {
        return QUERY
    }


}