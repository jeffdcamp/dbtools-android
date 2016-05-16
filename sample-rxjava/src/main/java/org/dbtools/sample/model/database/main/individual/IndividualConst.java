/*
 * IndividualBaseRecord.java
 *
 * GENERATED FILE - DO NOT EDIT
 * CHECKSTYLE:OFF
 * 
 */



package org.dbtools.sample.model.database.main.individual;

import android.database.Cursor;
import org.dbtools.sample.model.database.main.individualtype.IndividualType;


@SuppressWarnings("all")
public class IndividualConst {

    public static final String DATABASE = "main";
    public static final String TABLE = "INDIVIDUAL";
    public static final String FULL_TABLE = "main.INDIVIDUAL";
    public static final String PRIMARY_KEY_COLUMN = "_id";
    public static final String C_ID = "_id";
    public static final String FULL_C_ID = "INDIVIDUAL._id";
    public static final String C_INDIVIDUAL_TYPE = "INDIVIDUAL_TYPE_ID";
    public static final String FULL_C_INDIVIDUAL_TYPE = "INDIVIDUAL.INDIVIDUAL_TYPE_ID";
    public static final String C_FIRST_NAME = "FIRST_NAME";
    public static final String FULL_C_FIRST_NAME = "INDIVIDUAL.FIRST_NAME";
    public static final String C_LAST_NAME = "LAST_NAME";
    public static final String FULL_C_LAST_NAME = "INDIVIDUAL.LAST_NAME";
    public static final String C_SAMPLE_DATE_TIME = "SAMPLE_DATE_TIME";
    public static final String FULL_C_SAMPLE_DATE_TIME = "INDIVIDUAL.SAMPLE_DATE_TIME";
    public static final String C_BIRTH_DATE = "BIRTH_DATE";
    public static final String FULL_C_BIRTH_DATE = "INDIVIDUAL.BIRTH_DATE";
    public static final String C_LAST_MODIFIED = "LAST_MODIFIED";
    public static final String FULL_C_LAST_MODIFIED = "INDIVIDUAL.LAST_MODIFIED";
    public static final String C_NUMBER = "NUMBER";
    public static final String FULL_C_NUMBER = "INDIVIDUAL.NUMBER";
    public static final String C_PHONE = "PHONE";
    public static final String FULL_C_PHONE = "INDIVIDUAL.PHONE";
    public static final String C_EMAIL = "EMAIL";
    public static final String FULL_C_EMAIL = "INDIVIDUAL.EMAIL";
    public static final String C_DATA = "DATA";
    public static final String FULL_C_DATA = "INDIVIDUAL.DATA";
    public static final String C_AMOUNT1 = "AMOUNT1";
    public static final String FULL_C_AMOUNT1 = "INDIVIDUAL.AMOUNT1";
    public static final String C_AMOUNT2 = "AMOUNT2";
    public static final String FULL_C_AMOUNT2 = "INDIVIDUAL.AMOUNT2";
    public static final String C_ENABLED = "ENABLED";
    public static final String FULL_C_ENABLED = "INDIVIDUAL.ENABLED";
    public static final String C_SPOUSE_INDIVIDUAL_ID = "SPOUSE_INDIVIDUAL_ID";
    public static final String FULL_C_SPOUSE_INDIVIDUAL_ID = "INDIVIDUAL.SPOUSE_INDIVIDUAL_ID";
    public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS INDIVIDUAL (" + 
        "_id INTEGER PRIMARY KEY  AUTOINCREMENT," + 
        "INDIVIDUAL_TYPE_ID INTEGER NOT NULL," + 
        "FIRST_NAME TEXT NOT NULL," + 
        "LAST_NAME TEXT NOT NULL," + 
        "SAMPLE_DATE_TIME TEXT," + 
        "BIRTH_DATE TEXT," + 
        "LAST_MODIFIED INTEGER," + 
        "NUMBER INTEGER," + 
        "PHONE TEXT," + 
        "EMAIL TEXT," + 
        "DATA BLOB," + 
        "AMOUNT1 REAL," + 
        "AMOUNT2 REAL," + 
        "ENABLED INTEGER," + 
        "SPOUSE_INDIVIDUAL_ID INTEGER," + 
        "FOREIGN KEY (INDIVIDUAL_TYPE_ID) REFERENCES INDIVIDUAL_TYPE (_id)" + 
        ");" + 
        "" + 
        "";
    public static final String DROP_TABLE = "DROP TABLE IF EXISTS INDIVIDUAL;";
    public static final String INSERT_STATEMENT = "INSERT INTO INDIVIDUAL (INDIVIDUAL_TYPE_ID,FIRST_NAME,LAST_NAME,SAMPLE_DATE_TIME,BIRTH_DATE,LAST_MODIFIED,NUMBER,PHONE,EMAIL,DATA,AMOUNT1,AMOUNT2,ENABLED,SPOUSE_INDIVIDUAL_ID) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    public static final String UPDATE_STATEMENT = "UPDATE INDIVIDUAL SET INDIVIDUAL_TYPE_ID=?, FIRST_NAME=?, LAST_NAME=?, SAMPLE_DATE_TIME=?, BIRTH_DATE=?, LAST_MODIFIED=?, NUMBER=?, PHONE=?, EMAIL=?, DATA=?, AMOUNT1=?, AMOUNT2=?, ENABLED=?, SPOUSE_INDIVIDUAL_ID=? WHERE _id = ?";
    public static final String[] ALL_COLUMNS = new String[] {
        C_ID,
        C_INDIVIDUAL_TYPE,
        C_FIRST_NAME,
        C_LAST_NAME,
        C_SAMPLE_DATE_TIME,
        C_BIRTH_DATE,
        C_LAST_MODIFIED,
        C_NUMBER,
        C_PHONE,
        C_EMAIL,
        C_DATA,
        C_AMOUNT1,
        C_AMOUNT2,
        C_ENABLED,
        C_SPOUSE_INDIVIDUAL_ID};
    public static final String[] ALL_COLUMNS_FULL = new String[] {
        FULL_C_ID,
        FULL_C_INDIVIDUAL_TYPE,
        FULL_C_FIRST_NAME,
        FULL_C_LAST_NAME,
        FULL_C_SAMPLE_DATE_TIME,
        FULL_C_BIRTH_DATE,
        FULL_C_LAST_MODIFIED,
        FULL_C_NUMBER,
        FULL_C_PHONE,
        FULL_C_EMAIL,
        FULL_C_DATA,
        FULL_C_AMOUNT1,
        FULL_C_AMOUNT2,
        FULL_C_ENABLED,
        FULL_C_SPOUSE_INDIVIDUAL_ID};

    public IndividualConst() {
    }

    public static long getId(Cursor cursor) {
        return cursor.getLong(cursor.getColumnIndexOrThrow(C_ID));
    }

    public static IndividualType getIndividualType(Cursor cursor) {
        return IndividualType.values()[cursor.getInt(cursor.getColumnIndexOrThrow(C_INDIVIDUAL_TYPE))];
    }

    public static String getFirstName(Cursor cursor) {
        return cursor.getString(cursor.getColumnIndexOrThrow(C_FIRST_NAME));
    }

    public static String getLastName(Cursor cursor) {
        return cursor.getString(cursor.getColumnIndexOrThrow(C_LAST_NAME));
    }

    public static java.util.Date getSampleDateTime(Cursor cursor) {
        return org.dbtools.android.domain.date.DBToolsDateFormatter.dbStringToDate(cursor.getString(cursor.getColumnIndexOrThrow(C_SAMPLE_DATE_TIME)));
    }

    public static java.util.Date getBirthDate(Cursor cursor) {
        return org.dbtools.android.domain.date.DBToolsDateFormatter.dbStringToDate(cursor.getString(cursor.getColumnIndexOrThrow(C_BIRTH_DATE)));
    }

    public static java.util.Date getLastModified(Cursor cursor) {
        return !cursor.isNull(cursor.getColumnIndexOrThrow(C_LAST_MODIFIED)) ? new java.util.Date(cursor.getLong(cursor.getColumnIndexOrThrow(C_LAST_MODIFIED))) : null;
    }

    public static Integer getNumber(Cursor cursor) {
        return !cursor.isNull(cursor.getColumnIndexOrThrow(C_NUMBER)) ? cursor.getInt(cursor.getColumnIndexOrThrow(C_NUMBER)) : null;
    }

    public static String getPhone(Cursor cursor) {
        return !cursor.isNull(cursor.getColumnIndexOrThrow(C_PHONE)) ? cursor.getString(cursor.getColumnIndexOrThrow(C_PHONE)) : null;
    }

    public static String getEmail(Cursor cursor) {
        return !cursor.isNull(cursor.getColumnIndexOrThrow(C_EMAIL)) ? cursor.getString(cursor.getColumnIndexOrThrow(C_EMAIL)) : null;
    }

    public static byte[] getData(Cursor cursor) {
        return cursor.getBlob(cursor.getColumnIndexOrThrow(C_DATA));
    }

    public static Float getAmount1(Cursor cursor) {
        return !cursor.isNull(cursor.getColumnIndexOrThrow(C_AMOUNT1)) ? cursor.getFloat(cursor.getColumnIndexOrThrow(C_AMOUNT1)) : null;
    }

    public static Double getAmount2(Cursor cursor) {
        return cursor.getDouble(cursor.getColumnIndexOrThrow(C_AMOUNT2));
    }

    public static Boolean isEnabled(Cursor cursor) {
        return !cursor.isNull(cursor.getColumnIndexOrThrow(C_ENABLED)) ? cursor.getInt(cursor.getColumnIndexOrThrow(C_ENABLED)) != 0 ? true : false : null;
    }

    public static Long getSpouseIndividualId(Cursor cursor) {
        return !cursor.isNull(cursor.getColumnIndexOrThrow(C_SPOUSE_INDIVIDUAL_ID)) ? cursor.getLong(cursor.getColumnIndexOrThrow(C_SPOUSE_INDIVIDUAL_ID)) : null;
    }


}