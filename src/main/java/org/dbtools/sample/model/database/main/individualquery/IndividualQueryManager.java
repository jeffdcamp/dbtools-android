/*
 * IndividualQueryManager.java
 *
 * Generated on: 11/20/2016 10:26:47
 *
 */



package org.dbtools.sample.model.database.main.individualquery;

import org.dbtools.sample.model.database.DatabaseManager;


public class IndividualQueryManager extends IndividualQueryBaseManager {


    public IndividualQueryManager(DatabaseManager databaseManager) {
        super(databaseManager);
    }

    @Override
    public String getQuery() {
        return IndividualQuery.QUERY;
    }


}