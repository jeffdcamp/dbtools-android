/*
 * IndividualQuery.java
 *
 * Created: 11/20/2016 10:26:47
 */



package org.dbtools.sample.model.database.main.individualquery;

import android.database.Cursor;

import org.dbtools.android.domain.database.contentvalues.DBToolsContentValues;

// todo Replace the following the QUERY sql (The following is a template suggestion for your query)
// todo BE SURE TO KEEP THE OPENING AND CLOSING PARENTHESES (so queries can be run as sub-select: select * from (select a, b from t) )
// todo SUGGESTION: Keep the " AS IndividualQueryConst.<columnname>" portion of the sql


public class IndividualQuery extends IndividualQueryBaseRecord {

    public static final String QUERY = "(" +
            "SELECT " +
            IndividualQueryConst.FULL_C_ID + " AS " + IndividualQueryConst.C_ID + ", " +
            IndividualQueryConst.FULL_C_NAME + " AS " + IndividualQueryConst.C_NAME +
            " FROM SOME TABLE(S)" +
            ")";
    public static final String QUERY_RAW = "SELECT * FROM " + QUERY;

    public IndividualQuery(IndividualQuery record) {
        super(record);
    }

    public IndividualQuery(Cursor cursor) {
        setContent(cursor);
    }

    public IndividualQuery(DBToolsContentValues values) {
        setContent(values);
    }

    public IndividualQuery() {
    }


}