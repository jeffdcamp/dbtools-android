/*
 * IndividualView.java
 *
 * Created: 11/20/2016 10:26:47
 */



package org.dbtools.sample.model.database.main.individualview;

import android.database.Cursor;

import org.dbtools.android.domain.database.contentvalues.DBToolsContentValues;

// todo Replace the following the CREATE_VIEW sql (The following is a template suggestion for your view)
// todo SUGGESTION: Keep the " AS IndividualViewConst.<columnname>" portion of the sql


public class IndividualView extends IndividualViewBaseRecord {

    public static final String DROP_VIEW;
    public static final String CREATE_VIEW = "CREATE VIEW IF NOT EXISTS " + IndividualViewConst.TABLE + " AS SELECT " +
            IndividualViewConst.FULL_C_ID + " AS " + IndividualViewConst.C_ID + ", " +
            IndividualViewConst.FULL_C_NAME + " AS " + IndividualViewConst.C_NAME +
            " FROM " + IndividualViewConst.TABLE;

    static {
        DROP_VIEW = "DROP VIEW IF EXISTS " + IndividualViewConst.TABLE + ";";

    }

    public IndividualView(IndividualView record) {
        super(record);
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