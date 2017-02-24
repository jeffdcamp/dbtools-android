/*
 * GENERATED FILE - DO NOT EDIT
 */



package org.dbtools.sample.kotlin.model.database.main

import org.dbtools.sample.kotlin.model.database.DatabaseManager
import org.dbtools.sample.kotlin.model.database.main.individual.IndividualManager
import org.dbtools.sample.kotlin.model.database.main.individualdata.IndividualDataManager
import org.dbtools.sample.kotlin.model.database.main.individualquery.IndividualQueryManager
import org.dbtools.sample.kotlin.model.database.main.individualview.IndividualViewManager


@SuppressWarnings("all")
object MainDatabaseManagers  {

     lateinit var individualManager: IndividualManager private set
     lateinit var individualDataManager: IndividualDataManager private set
     lateinit var individualViewManager: IndividualViewManager private set
     lateinit var individualQueryManager: IndividualQueryManager private set

    fun init(databaseManager: DatabaseManager) {
        individualManager = IndividualManager(databaseManager);
        individualDataManager = IndividualDataManager(databaseManager);
        individualViewManager = IndividualViewManager(databaseManager);
        individualQueryManager = IndividualQueryManager(databaseManager);
    }
}