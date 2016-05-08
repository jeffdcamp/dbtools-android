/*
 * IndividualQuery.java
 *
 * Created: 05/07/2016 08:50:53
 */



package org.dbtools.sample.model.database.main.individualquery;

import android.database.Cursor;

import org.dbtools.android.domain.database.contentvalues.DBToolsContentValues;
import org.dbtools.sample.model.database.main.individual.IndividualConst;

public class IndividualQuery extends IndividualQueryBaseRecord {

    public static final String QUERY = "(" +
            "SELECT " +
            IndividualConst.FULL_C_ID + " AS " + IndividualQueryConst.C_ID + ", " +
            IndividualConst.FULL_C_FIRST_NAME + " AS " + IndividualQueryConst.C_NAME + ", " +
            " FROM SOME TABLE(S)" +
            ")";
    public static final String QUERY_RAW = "SELECT * FROM " + QUERY;

    public IndividualQuery(Cursor cursor) {
        setContent(cursor);
    }

    public IndividualQuery(DBToolsContentValues values) {
        setContent(values);
    }

    public IndividualQuery() {
    }


}