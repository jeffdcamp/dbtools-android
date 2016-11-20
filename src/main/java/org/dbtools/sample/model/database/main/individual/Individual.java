/*
 * Individual.java
 *
 * Created: 11/20/2016 10:26:47
 */



package org.dbtools.sample.model.database.main.individual;

import android.database.Cursor;

import org.dbtools.android.domain.database.contentvalues.DBToolsContentValues;


public class Individual extends IndividualBaseRecord {


    public Individual(Individual record) {
        super(record);
    }

    public Individual(Cursor cursor) {
        setContent(cursor);
    }

    public Individual(DBToolsContentValues values) {
        setContent(values);
    }

    public Individual() {
    }


}