/*
 * IndividualQueryManager.kt
 *
 * Generated on: 05/09/2016 09:21:13
 *
 */



package org.dbtools.sample.kotlin.model.database.main.individualquery

import org.dbtools.sample.kotlin.model.database.DatabaseManager


class IndividualQueryManager : IndividualQueryBaseManager {


    constructor(databaseManager: DatabaseManager): super(databaseManager) {
    }

    override fun getQuery(): String {
        return IndividualQuery.QUERY
    }


}