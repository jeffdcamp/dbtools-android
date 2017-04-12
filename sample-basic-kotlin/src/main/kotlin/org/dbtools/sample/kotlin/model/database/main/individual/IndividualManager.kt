/*
 * IndividualManager.kt
 *
 * Generated on: 05/09/2016 09:21:13
 *
 */



package org.dbtools.sample.kotlin.model.database.main.individual

import org.dbtools.query.sql.SQLQueryBuilder
import org.dbtools.sample.kotlin.model.database.DatabaseManager


class IndividualManager(databaseManager: DatabaseManager) : IndividualBaseManager(databaseManager) {

    fun findLastIndividualId(): Long {
        return findValueByRawQuery(valueType = Long::class.java,
                rawQuery = "SELECT MAX(${IndividualConst.C_ID}) FROM ${IndividualConst.TABLE}",
                defaultValue = 0L)
    }

    fun findLastIndividual(): Individual? {
        return findByRowId(findLastIndividualId())
    }

    fun findFirstNameById(id: Long): String {
        return findValueBySelection(String::class.java, IndividualConst.C_FIRST_NAME, "", IndividualConst.C_ID + " = ?", SQLQueryBuilder.toSelectionArgs(id), "")
    }
}