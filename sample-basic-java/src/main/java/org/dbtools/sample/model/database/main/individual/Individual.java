/*
 * Individual.java
 *
 * Created: 05/07/2016 08:50:53
 */



package org.dbtools.sample.model.database.main.individual;

import android.database.Cursor;
import org.dbtools.android.domain.database.contentvalues.DBToolsContentValues;


public class Individual extends IndividualBaseRecord {


    public Individual(Cursor cursor) {
        setContent(cursor);
    }

    public Individual(DBToolsContentValues values) {
        setContent(values);
    }

    public Individual() {
    }


}