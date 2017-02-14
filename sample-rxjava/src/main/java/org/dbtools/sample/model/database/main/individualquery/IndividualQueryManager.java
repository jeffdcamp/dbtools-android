/*
 * IndividualQueryManager.java
 *
 * Generated on: 05/07/2016 08:50:53
 *
 */



package org.dbtools.sample.model.database.main.individualquery;

import org.dbtools.sample.model.database.DatabaseManager;
import org.dbtools.sample.model.database.main.individual.IndividualConst;


public class IndividualQueryManager extends IndividualQueryBaseManager {

    public static final String QUERY = "SELECT " +
            IndividualConst.FULL_C_ID + " AS " + IndividualQueryConst.C_ID + ", " +
            IndividualConst.FULL_C_FIRST_NAME + " AS " + IndividualQueryConst.C_NAME + ", " +
            " FROM " + IndividualConst.TABLE;


    public IndividualQueryManager(DatabaseManager databaseManager) {
        super(databaseManager);
    }

    @Override
    public String getQuery() {
        return QUERY;
    }


}