/*
 * IndividualQueryManager.java
 *
 * Generated on: 05/07/2016 08:50:53
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