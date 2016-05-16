/*
 * GENERATED FILE - DO NOT EDIT
 */



package org.dbtools.sample.model.database.main;

import org.dbtools.sample.model.database.main.individual.IndividualManager;
import org.dbtools.sample.model.database.main.individualview.IndividualViewManager;
import org.dbtools.sample.model.database.main.individualquery.IndividualQueryManager;
import org.dbtools.sample.model.database.DatabaseManager;


@SuppressWarnings("all")
public class MainDatabaseManagers {

    private static IndividualManager individualManager;
    private static IndividualViewManager individualViewManager;
    private static IndividualQueryManager individualQueryManager;

    public static void init(DatabaseManager databaseManager) {
        individualManager = new IndividualManager(databaseManager);
        individualViewManager = new IndividualViewManager(databaseManager);
        individualQueryManager = new IndividualQueryManager(databaseManager);
    }

    public static IndividualManager getIndividualManager() {
        return individualManager;
    }

    public static IndividualViewManager getIndividualViewManager() {
        return individualViewManager;
    }

    public static IndividualQueryManager getIndividualQueryManager() {
        return individualQueryManager;
    }


}