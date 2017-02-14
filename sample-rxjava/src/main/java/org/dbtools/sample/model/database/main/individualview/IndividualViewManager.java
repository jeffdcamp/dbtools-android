/*
 * IndividualViewManager.java
 *
 * Generated on: 05/07/2016 08:50:53
 *
 */



package org.dbtools.sample.model.database.main.individualview;

import org.dbtools.sample.model.database.DatabaseManager;
import org.dbtools.sample.model.database.main.individual.IndividualConst;


public class IndividualViewManager extends IndividualViewBaseManager {
    public static final String DROP_VIEW = "DROP VIEW IF EXISTS " + IndividualViewConst.TABLE;
    public static final String CREATE_VIEW = "CREATE VIEW IF NOT EXISTS " + IndividualViewConst.TABLE + " AS SELECT " +
            IndividualConst.FULL_C_ID + " AS " + IndividualViewConst.C_ID + ", " +
            IndividualConst.FULL_C_FIRST_NAME + " AS " + IndividualViewConst.C_NAME +
            " FROM " + IndividualConst.TABLE;

    public IndividualViewManager(DatabaseManager databaseManager) {
        super(databaseManager);
    }


}