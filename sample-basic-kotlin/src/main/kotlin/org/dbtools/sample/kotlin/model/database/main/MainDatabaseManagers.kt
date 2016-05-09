/*
 * GENERATED FILE - DO NOT EDIT
 */



package org.dbtools.sample.kotlin.model.database.main

import org.dbtools.sample.kotlin.model.database.main.individual.IndividualManager
import org.dbtools.sample.kotlin.model.database.main.individualview.IndividualViewManager
import org.dbtools.sample.kotlin.model.database.main.individualquery.IndividualQueryManager
import org.dbtools.sample.kotlin.model.database.DatabaseManager


@SuppressWarnings("all")
object MainDatabaseManagers {

     var individualManager: IndividualManager? = null
     var individualViewManager: IndividualViewManager? = null
     var individualQueryManager: IndividualQueryManager? = null

    fun init(databaseManager: DatabaseManager) {
        individualManager = IndividualManager(databaseManager);
        individualViewManager = IndividualViewManager(databaseManager);
        individualQueryManager = IndividualQueryManager(databaseManager);
    }


}