/*
 * IndividualViewBaseRecord.kt
 *
 * GENERATED FILE - DO NOT EDIT
 * CHECKSTYLE:OFF
 * 
 */



package org.dbtools.sample.kotlin.model.database.main.individualview

import org.dbtools.android.domain.AndroidBaseRecord
import org.dbtools.android.domain.database.statement.StatementWrapper
import org.dbtools.android.domain.database.contentvalues.DBToolsContentValues
import android.database.Cursor


@SuppressWarnings("all")
abstract class IndividualViewBaseRecord : AndroidBaseRecord() {

     var id: Long? = 0
     var name: String = ""

    override fun getIdColumnName(): String {
        return ""
    }

    override fun getPrimaryKeyId(): Long {
        return 0
    }

    override fun setPrimaryKeyId(id: Long) {
    }

    override fun getAllColumns(): Array<String> {
        return IndividualViewConst.ALL_COLUMNS.clone()
    }

    fun getAllColumnsFull(): Array<String> {
        return IndividualViewConst.ALL_COLUMNS_FULL.clone()
    }

    override fun getContentValues(values: DBToolsContentValues<*>) {
        values.put(IndividualViewConst.C_ID, id)
        values.put(IndividualViewConst.C_NAME, name)
    }

    override fun getValues(): Array<Any?> {
        return arrayOf(
            id,
            name)
    }

    fun copy(): IndividualView {
        var copy = IndividualView()
        copy.id = id
        copy.name = name
        return copy
    }

    override fun bindInsertStatement(statement: StatementWrapper) {
        if (id != null) {
            statement.bindLong(1, id!!)
        } else {
            statement.bindNull(1)
        }
        statement.bindString(2, name)
    }

    override fun bindUpdateStatement(statement: StatementWrapper) {
        if (id != null) {
            statement.bindLong(1, id!!)
        } else {
            statement.bindNull(1)
        }
        statement.bindString(2, name)
    }

    override fun setContent(values: DBToolsContentValues<*>) {
        id = values.getAsLong(IndividualViewConst.C_ID)
        name = values.getAsString(IndividualViewConst.C_NAME)
    }

    override fun setContent(cursor: Cursor) {
        id = if (!cursor.isNull(cursor.getColumnIndexOrThrow(IndividualViewConst.C_ID))) cursor.getLong(cursor.getColumnIndexOrThrow(IndividualViewConst.C_ID)) else null
        name = cursor.getString(cursor.getColumnIndexOrThrow(IndividualViewConst.C_NAME))
    }

    override fun isNewRecord(): Boolean {
        return primaryKeyId <= 0
    }


}