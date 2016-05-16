/*
 * IndividualViewBaseRecord.java
 *
 * GENERATED FILE - DO NOT EDIT
 * CHECKSTYLE:OFF
 * 
 */



package org.dbtools.sample.model.database.main.individualview;

import android.database.Cursor;


@SuppressWarnings("all")
public class IndividualViewConst {

    public static final String DATABASE = "main";
    public static final String TABLE = "INDIVIDUAL_VIEW";
    public static final String FULL_TABLE = "main.INDIVIDUAL_VIEW";
    public static final String C_ID = "id";
    public static final String FULL_C_ID = "INDIVIDUAL_VIEW.id";
    public static final String C_NAME = "NAME";
    public static final String FULL_C_NAME = "INDIVIDUAL_VIEW.NAME";
    public static final String[] ALL_COLUMNS = new String[] {
        C_ID,
        C_NAME};
    public static final String[] ALL_COLUMNS_FULL = new String[] {
        FULL_C_ID,
        FULL_C_NAME};

    public IndividualViewConst() {
    }

    public static Long getId(Cursor cursor) {
        return !cursor.isNull(cursor.getColumnIndexOrThrow(C_ID)) ? cursor.getLong(cursor.getColumnIndexOrThrow(C_ID)) : null;
    }

    public static String getName(Cursor cursor) {
        return cursor.getString(cursor.getColumnIndexOrThrow(C_NAME));
    }


}