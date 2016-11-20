/*
 * IndividualDataBaseRecord.java
 *
 * GENERATED FILE - DO NOT EDIT
 * CHECKSTYLE:OFF
 * 
 */



package org.dbtools.sample.model.database.main.individualdata;

import android.database.Cursor;


@SuppressWarnings("all")
public class IndividualDataConst {

    public static final String DATABASE = "main";
    public static final String TABLE = "INDIVIDUAL_DATA";
    public static final String FULL_TABLE = "main.INDIVIDUAL_DATA";
    public static final String C_EXTERNAL_ID = "EXTERNAL_ID";
    public static final String FULL_C_EXTERNAL_ID = "INDIVIDUAL_DATA.EXTERNAL_ID";
    public static final String C_TYPE_ID = "TYPE_ID";
    public static final String FULL_C_TYPE_ID = "INDIVIDUAL_DATA.TYPE_ID";
    public static final String C_NAME = "NAME";
    public static final String FULL_C_NAME = "INDIVIDUAL_DATA.NAME";
    public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS INDIVIDUAL_DATA (" + 
        "EXTERNAL_ID INTEGER," + 
        "TYPE_ID INTEGER," + 
        "NAME TEXT," + 
        "UNIQUE(EXTERNAL_ID, TYPE_ID) ON CONFLICT REPLACE" + 
        ");" + 
        "" + 
        "CREATE INDEX IF NOT EXISTS INDIVIDUAL_DATAEXTERNAL_ID_IDX ON INDIVIDUAL_DATA (EXTERNAL_ID);" + 
        "" + 
        "CREATE INDEX IF NOT EXISTS INDIVIDUAL_DATATYPE_ID_IDX ON INDIVIDUAL_DATA (TYPE_ID);" + 
        "" + 
        "";
    public static final String DROP_TABLE = "DROP TABLE IF EXISTS INDIVIDUAL_DATA;";
    public static final String INSERT_STATEMENT = "INSERT INTO INDIVIDUAL_DATA (EXTERNAL_ID,TYPE_ID,NAME) VALUES (?,?,?)";
    public static final String UPDATE_STATEMENT = "UPDATE INDIVIDUAL_DATA SET EXTERNAL_ID=?, TYPE_ID=?, NAME=? WHERE  = ?";
    public static final String[] ALL_COLUMNS = new String[] {
        C_EXTERNAL_ID,
        C_TYPE_ID,
        C_NAME};
    public static final String[] ALL_COLUMNS_FULL = new String[] {
        FULL_C_EXTERNAL_ID,
        FULL_C_TYPE_ID,
        FULL_C_NAME};

    public IndividualDataConst() {
    }

    public static Long getExternalId(Cursor cursor) {
        return !cursor.isNull(cursor.getColumnIndexOrThrow(C_EXTERNAL_ID)) ? cursor.getLong(cursor.getColumnIndexOrThrow(C_EXTERNAL_ID)) : null;
    }

    public static Integer getTypeId(Cursor cursor) {
        return !cursor.isNull(cursor.getColumnIndexOrThrow(C_TYPE_ID)) ? cursor.getInt(cursor.getColumnIndexOrThrow(C_TYPE_ID)) : null;
    }

    public static String getName(Cursor cursor) {
        return !cursor.isNull(cursor.getColumnIndexOrThrow(C_NAME)) ? cursor.getString(cursor.getColumnIndexOrThrow(C_NAME)) : null;
    }


}