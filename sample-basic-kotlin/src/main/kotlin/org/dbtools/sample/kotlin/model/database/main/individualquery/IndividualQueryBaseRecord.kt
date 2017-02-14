/*
 * IndividualQueryBaseRecord.kt
 *
 * GENERATED FILE - DO NOT EDIT
 * CHECKSTYLE:OFF
 * 
 */



package org.dbtools.sample.kotlin.model.database.main.individualquery

import org.dbtools.android.domain.AndroidBaseRecord
import org.dbtools.android.domain.database.statement.StatementWrapper
import org.dbtools.android.domain.database.contentvalues.DBToolsContentValues
import android.database.Cursor


@Suppress("LeakingThis", "unused", "RemoveEmptySecondaryConstructorBody", "ConvertSecondaryConstructorToPrimary")
@SuppressWarnings("all")
abstract class IndividualQueryBaseRecord  : AndroidBaseRecord {

     open var id: Long? = null
     open var name: String = ""

    constructor() {
    }

    override fun getIdColumnName() : String {
        return ""
    }

    override fun getPrimaryKeyId() : Long {
        return 0
    }

    override fun setPrimaryKeyId(id: Long) {
    }

    override fun getAllColumns() : Array<String> {
        return IndividualQueryConst.ALL_COLUMNS.clone()
    }

    fun getAllColumnsFull() : Array<String> {
        return IndividualQueryConst.ALL_COLUMNS_FULL.clone()
    }

    override fun getContentValues(values: DBToolsContentValues<*>) {
        values.put(IndividualQueryConst.C_ID, id)
        values.put(IndividualQueryConst.C_NAME, name)
    }

    override fun getValues() : Array<Any?> {
        return arrayOf(
            id,
            name)
    }

    open fun copy() : IndividualQuery {
        val copy = IndividualQuery()
        copy.id = id
        copy.name = name
        return copy
    }

    @Suppress("UNNECESSARY_NOT_NULL_ASSERTION")
    override fun bindInsertStatement(statement: StatementWrapper) {
        if (id != null) {
            statement.bindLong(1, id!!)
        } else {
            statement.bindNull(1)
        }
        statement.bindString(2, name)
    }

    @Suppress("UNNECESSARY_NOT_NULL_ASSERTION")
    override fun bindUpdateStatement(statement: StatementWrapper) {
        if (id != null) {
            statement.bindLong(1, id!!)
        } else {
            statement.bindNull(1)
        }
        statement.bindString(2, name)
    }

    override fun setContent(values: DBToolsContentValues<*>) {
        id = values.getAsLong(IndividualQueryConst.C_ID)
        name = values.getAsString(IndividualQueryConst.C_NAME)
    }

    override fun setContent(cursor: Cursor) {
        id = if (!cursor.isNull(cursor.getColumnIndexOrThrow(IndividualQueryConst.C_ID))) cursor.getLong(cursor.getColumnIndexOrThrow(IndividualQueryConst.C_ID)) else null
        name = cursor.getString(cursor.getColumnIndexOrThrow(IndividualQueryConst.C_NAME))
    }

    override fun isNewRecord() : Boolean {
        return primaryKeyId <= 0
    }


}