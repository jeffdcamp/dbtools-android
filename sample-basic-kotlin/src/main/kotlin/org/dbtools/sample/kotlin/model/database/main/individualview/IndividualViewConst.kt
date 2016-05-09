/*
 * IndividualViewBaseRecord.kt
 *
 * GENERATED FILE - DO NOT EDIT
 * CHECKSTYLE:OFF
 * 
 */



package org.dbtools.sample.kotlin.model.database.main.individualview

import android.database.Cursor


@SuppressWarnings("all")
object IndividualViewConst {

    const val DATABASE = "main"
    const val TABLE = "INDIVIDUAL_VIEW"
    const val FULL_TABLE = "main.INDIVIDUAL_VIEW"
    const val C_ID = "id"
    const val FULL_C_ID = "INDIVIDUAL_VIEW.id"
    const val C_NAME = "NAME"
    const val FULL_C_NAME = "INDIVIDUAL_VIEW.NAME"
    val ALL_COLUMNS = arrayOf(
        C_ID,
        C_NAME)
    val ALL_COLUMNS_FULL = arrayOf(
        FULL_C_ID,
        FULL_C_NAME)

    fun getId(cursor: Cursor): Long? {
        return if (!cursor.isNull(cursor.getColumnIndexOrThrow(C_ID))) cursor.getLong(cursor.getColumnIndexOrThrow(C_ID)) else null
    }

    fun getName(cursor: Cursor): String {
        return cursor.getString(cursor.getColumnIndexOrThrow(C_NAME))
    }


}