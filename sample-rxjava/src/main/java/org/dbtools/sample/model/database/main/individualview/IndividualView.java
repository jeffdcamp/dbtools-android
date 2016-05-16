/*
 * IndividualView.java
 *
 * Created: 05/07/2016 08:50:53
 */



package org.dbtools.sample.model.database.main.individualview;

import android.database.Cursor;

import org.dbtools.sample.model.database.main.individual.IndividualConst;

import org.dbtools.android.domain.database.contentvalues.DBToolsContentValues;

public class IndividualView extends IndividualViewBaseRecord {

    public static final String DROP_VIEW;
    public static final String CREATE_VIEW = "CREATE VIEW IF NOT EXISTS " + IndividualViewConst.TABLE + " AS SELECT " +
            IndividualConst.FULL_C_ID + " AS " + IndividualViewConst.C_ID + ", " +
            IndividualConst.FULL_C_FIRST_NAME + " AS " + IndividualViewConst.C_NAME +
            " FROM " + IndividualConst.TABLE;

    static {
        DROP_VIEW = "DROP VIEW IF EXISTS " + IndividualViewConst.TABLE + ";";

    }

    public IndividualView(Cursor cursor) {
        setContent(cursor);
    }

    public IndividualView(DBToolsContentValues values) {
        setContent(values);
    }

    public IndividualView() {
    }

    public String getDropSql() {
        return DROP_VIEW;
    }

    public String getCreateSql() {
        return CREATE_VIEW;
    }


}