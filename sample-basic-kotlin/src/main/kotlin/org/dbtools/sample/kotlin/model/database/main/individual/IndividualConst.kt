/*
 * IndividualBaseRecord.kt
 *
 * GENERATED FILE - DO NOT EDIT
 * CHECKSTYLE:OFF
 * 
 */



package org.dbtools.sample.kotlin.model.database.main.individual

import android.database.Cursor
import org.dbtools.sample.kotlin.model.database.main.individualtype.IndividualType


@SuppressWarnings("all")
object IndividualConst {

    const val DATABASE = "main"
    const val TABLE = "INDIVIDUAL"
    const val FULL_TABLE = "main.INDIVIDUAL"
    const val PRIMARY_KEY_COLUMN = "_id"
    const val C_ID = "_id"
    const val FULL_C_ID = "INDIVIDUAL._id"
    const val C_INDIVIDUAL_TYPE = "INDIVIDUAL_TYPE_ID"
    const val FULL_C_INDIVIDUAL_TYPE = "INDIVIDUAL.INDIVIDUAL_TYPE_ID"
    const val C_FIRST_NAME = "FIRST_NAME"
    const val FULL_C_FIRST_NAME = "INDIVIDUAL.FIRST_NAME"
    const val C_LAST_NAME = "LAST_NAME"
    const val FULL_C_LAST_NAME = "INDIVIDUAL.LAST_NAME"
    const val C_SAMPLE_DATE_TIME = "SAMPLE_DATE_TIME"
    const val FULL_C_SAMPLE_DATE_TIME = "INDIVIDUAL.SAMPLE_DATE_TIME"
    const val C_BIRTH_DATE = "BIRTH_DATE"
    const val FULL_C_BIRTH_DATE = "INDIVIDUAL.BIRTH_DATE"
    const val C_LAST_MODIFIED = "LAST_MODIFIED"
    const val FULL_C_LAST_MODIFIED = "INDIVIDUAL.LAST_MODIFIED"
    const val C_NUMBER = "NUMBER"
    const val FULL_C_NUMBER = "INDIVIDUAL.NUMBER"
    const val C_PHONE = "PHONE"
    const val FULL_C_PHONE = "INDIVIDUAL.PHONE"
    const val C_EMAIL = "EMAIL"
    const val FULL_C_EMAIL = "INDIVIDUAL.EMAIL"
    const val C_DATA = "DATA"
    const val FULL_C_DATA = "INDIVIDUAL.DATA"
    const val C_AMOUNT1 = "AMOUNT1"
    const val FULL_C_AMOUNT1 = "INDIVIDUAL.AMOUNT1"
    const val C_AMOUNT2 = "AMOUNT2"
    const val FULL_C_AMOUNT2 = "INDIVIDUAL.AMOUNT2"
    const val C_ENABLED = "ENABLED"
    const val FULL_C_ENABLED = "INDIVIDUAL.ENABLED"
    const val C_SPOUSE_INDIVIDUAL_ID = "SPOUSE_INDIVIDUAL_ID"
    const val FULL_C_SPOUSE_INDIVIDUAL_ID = "INDIVIDUAL.SPOUSE_INDIVIDUAL_ID"
    const val CREATE_TABLE = "CREATE TABLE IF NOT EXISTS INDIVIDUAL (" + 
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
        ""
    const val DROP_TABLE = "DROP TABLE IF EXISTS INDIVIDUAL;"
    const val INSERT_STATEMENT = "INSERT INTO INDIVIDUAL (INDIVIDUAL_TYPE_ID,FIRST_NAME,LAST_NAME,SAMPLE_DATE_TIME,BIRTH_DATE,LAST_MODIFIED,NUMBER,PHONE,EMAIL,DATA,AMOUNT1,AMOUNT2,ENABLED,SPOUSE_INDIVIDUAL_ID) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)"
    const val UPDATE_STATEMENT = "UPDATE INDIVIDUAL SET INDIVIDUAL_TYPE_ID=?, FIRST_NAME=?, LAST_NAME=?, SAMPLE_DATE_TIME=?, BIRTH_DATE=?, LAST_MODIFIED=?, NUMBER=?, PHONE=?, EMAIL=?, DATA=?, AMOUNT1=?, AMOUNT2=?, ENABLED=?, SPOUSE_INDIVIDUAL_ID=? WHERE _id = ?"
    val ALL_COLUMNS = arrayOf(
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
        C_SPOUSE_INDIVIDUAL_ID)
    val ALL_COLUMNS_FULL = arrayOf(
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
        FULL_C_SPOUSE_INDIVIDUAL_ID)

    fun getId(cursor: Cursor): Long {
        return cursor.getLong(cursor.getColumnIndexOrThrow(C_ID))
    }

    fun getIndividualType(cursor: Cursor): IndividualType {
        return IndividualType.values()[cursor.getInt(cursor.getColumnIndexOrThrow(C_INDIVIDUAL_TYPE))]
    }

    fun getFirstName(cursor: Cursor): String {
        return cursor.getString(cursor.getColumnIndexOrThrow(C_FIRST_NAME))
    }

    fun getLastName(cursor: Cursor): String {
        return cursor.getString(cursor.getColumnIndexOrThrow(C_LAST_NAME))
    }

    fun getSampleDateTime(cursor: Cursor): java.util.Date? {
        return org.dbtools.android.domain.date.DBToolsDateFormatter.dbStringToDate(cursor.getString(cursor.getColumnIndexOrThrow(C_SAMPLE_DATE_TIME)))
    }

    fun getBirthDate(cursor: Cursor): java.util.Date? {
        return org.dbtools.android.domain.date.DBToolsDateFormatter.dbStringToDate(cursor.getString(cursor.getColumnIndexOrThrow(C_BIRTH_DATE)))
    }

    fun getLastModified(cursor: Cursor): java.util.Date? {
        return if (!cursor.isNull(cursor.getColumnIndexOrThrow(C_LAST_MODIFIED))) java.util.Date(cursor.getLong(cursor.getColumnIndexOrThrow(C_LAST_MODIFIED))) else null
    }

    fun getNumber(cursor: Cursor): Int? {
        return if (!cursor.isNull(cursor.getColumnIndexOrThrow(C_NUMBER))) cursor.getInt(cursor.getColumnIndexOrThrow(C_NUMBER)) else null
    }

    fun getPhone(cursor: Cursor): String? {
        return if (!cursor.isNull(cursor.getColumnIndexOrThrow(C_PHONE))) cursor.getString(cursor.getColumnIndexOrThrow(C_PHONE)) else null
    }

    fun getEmail(cursor: Cursor): String? {
        return if (!cursor.isNull(cursor.getColumnIndexOrThrow(C_EMAIL))) cursor.getString(cursor.getColumnIndexOrThrow(C_EMAIL)) else null
    }

    fun getData(cursor: Cursor): ByteArray? {
        return cursor.getBlob(cursor.getColumnIndexOrThrow(C_DATA))
    }

    fun getAmount1(cursor: Cursor): Float? {
        return if (!cursor.isNull(cursor.getColumnIndexOrThrow(C_AMOUNT1))) cursor.getFloat(cursor.getColumnIndexOrThrow(C_AMOUNT1)) else null
    }

    fun getAmount2(cursor: Cursor): Double? {
        return if (!cursor.isNull(cursor.getColumnIndexOrThrow(C_AMOUNT2))) cursor.getDouble(cursor.getColumnIndexOrThrow(C_AMOUNT2)) else null
    }

    fun getEnabled(cursor: Cursor): Boolean? {
        return if (!cursor.isNull(cursor.getColumnIndexOrThrow(C_ENABLED))) (if (cursor.getInt(cursor.getColumnIndexOrThrow(C_ENABLED)) != 0) true else false) else null
    }

    fun getSpouseIndividualId(cursor: Cursor): Long? {
        return if (!cursor.isNull(cursor.getColumnIndexOrThrow(C_SPOUSE_INDIVIDUAL_ID))) cursor.getLong(cursor.getColumnIndexOrThrow(C_SPOUSE_INDIVIDUAL_ID)) else null
    }


}