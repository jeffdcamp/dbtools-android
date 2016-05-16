/*
 * IndividualManager.java
 *
 * Generated on: 05/07/2016 08:50:53
 *
 */



package org.dbtools.sample.model.database.main.individual;

import org.dbtools.query.sql.SQLQueryBuilder;
import org.dbtools.sample.model.database.DatabaseManager;


public class IndividualManager extends IndividualBaseManager {


    public IndividualManager(DatabaseManager databaseManager) {
        super(databaseManager);
    }


    public long findLastIndividualId() {
        return findValueByRawQuery(Long.class, "SELECT MAX(" + IndividualConst.C_ID + ") FROM " + IndividualConst.TABLE, null, 0L);
    }

    public Individual findLastIndividual() {
        return findByRowId(findLastIndividualId());
    }

    public String findFirstNameById(long id) {
        return findValueBySelection(String.class, IndividualConst.C_FIRST_NAME, IndividualConst.C_ID + " = ?", SQLQueryBuilder.toSelectionArgs(id), "");
    }
}