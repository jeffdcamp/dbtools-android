/*
 * IndividualData.java
 *
 * Created: 11/20/2016 10:57:23
 */



package org.dbtools.sample.model.database.main.individualdata;

import android.database.Cursor;
import org.dbtools.android.domain.database.contentvalues.DBToolsContentValues;


public class IndividualData extends IndividualDataBaseRecord {


    public IndividualData(IndividualData record) {
        super(record);
    }

    public IndividualData(Cursor cursor) {
        setContent(cursor);
    }

    public IndividualData(DBToolsContentValues values) {
        setContent(values);
    }

    public IndividualData() {
    }


}