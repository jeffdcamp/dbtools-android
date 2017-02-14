package org.dbtools.android.domain

import android.database.Cursor
import android.database.MatrixCursor
import android.database.MergeCursor
import org.dbtools.android.domain.config.DatabaseConfig
import org.dbtools.android.domain.database.DatabaseWrapper
import org.dbtools.android.domain.database.contentvalues.DBToolsContentValues
import org.dbtools.android.domain.database.statement.StatementWrapper
import java.util.*

@Suppress("unused")
abstract class KotlinAndroidBaseManager<T : AndroidBaseRecord> {

    abstract fun getReadableDatabase(databaseName: String = getDatabaseName()): DatabaseWrapper<in AndroidBaseRecord, in DBToolsContentValues<*>>

    abstract fun getWritableDatabase(databaseName: String = getDatabaseName()): DatabaseWrapper<in AndroidBaseRecord, in DBToolsContentValues<*>>

    abstract fun getAndroidDatabase(databaseName: String = getDatabaseName()): AndroidDatabase?

    abstract val tableName: String

    abstract val primaryKey: String

    abstract val allColumns: Array<String>

    abstract val dropSql: String

    abstract val createSql: String

    abstract val insertSql: String

    abstract val updateSql: String

    abstract fun getDatabaseName(): String

    abstract fun newRecord(): T

    abstract fun getDatabaseConfig(): DatabaseConfig

    /**
     * Return a database/platform specific version of DBToolsContentValues
     * Example:
     * - For Android platform, AndroidDBToolsContentValues
     * - For JDBC, JdbcDBToolsContentValues
     * @return new instance of DBToolsContentValues
     */
    open fun createNewDBToolsContentValues(): DBToolsContentValues<*> {
        return getDatabaseConfig().createNewDBToolsContentValues()
    }

    open fun createTable(databaseName: String = getDatabaseName()) {
        val db = getWritableDatabase(getDatabaseName())

        AndroidBaseManager.executeSql(db, createSql)
    }

    open fun dropTable(databaseName: String = getDatabaseName()) {
        val db = getWritableDatabase(getDatabaseName())

        AndroidBaseManager.executeSql(db, dropSql)
    }

    open fun cleanTable(dropSql: String = this.dropSql, createSql: String = this.createSql, databaseName: String = getDatabaseName()) {
        val db = getWritableDatabase(getDatabaseName())

        AndroidBaseManager.executeSql(db, dropSql)
        AndroidBaseManager.executeSql(db, createSql)
    }

    open fun getInsertStatement(db: DatabaseWrapper<in AndroidBaseRecord, in DBToolsContentValues<*>>): StatementWrapper {
        return db.getInsertStatement(tableName, insertSql)
    }

    open fun getUpdateStatement(db: DatabaseWrapper<in AndroidBaseRecord, in DBToolsContentValues<*>>): StatementWrapper {
        return db.getUpdateStatement(tableName, updateSql)
    }

    @JvmOverloads
    open fun executeSql(sql: String, bindArgs: Array<Any> = emptyArray(), databaseName: String = getDatabaseName()) {
        AndroidBaseManager.executeSql(getWritableDatabase(databaseName), sql, bindArgs)
    }

    @JvmOverloads
    open fun findCursorAll(databaseName: String = getDatabaseName()): Cursor? {
        return findCursorBySelection(databaseName)
    }

    @JvmOverloads
    open fun findCursorByRawQuery(rawQuery: String, selectionArgs: Array<String>? = null, databaseName: String = getDatabaseName()): Cursor? {
        return getReadableDatabase(databaseName).rawQuery(rawQuery, selectionArgs)
    }

    @JvmOverloads
    open fun findCursorBySelection(selection: String? = null,
                                   selectionArgs: Array<String>? = null,
                                   distinct: Boolean = true,
                                   columns: Array<String> = emptyArray(),
                                   groupBy: String? = null,
                                   having: String? = null,
                                   orderBy: String? = null,
                                   limit: String? = null,
                                   table: String = tableName,
                                   databaseName: String = getDatabaseName()): Cursor? {
        val cursor = getReadableDatabase(databaseName).query(distinct, table, columns, selection, selectionArgs, groupBy, having, orderBy, limit)

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                return cursor
            } else {
                cursor.close()
                return null
            }
        } else {
            return null
        }
    }

    @JvmOverloads
    open fun findAllBySelection(selection: String? = null,
                                selectionArgs: Array<String>? = null,
                                distinct: Boolean = true,
                                columns: Array<String> = emptyArray(),
                                groupBy: String? = null,
                                having: String? = null,
                                orderBy: String? = null,
                                limit: String? = null,
                                table: String = this.tableName,
                                databaseName: String = getDatabaseName()): List<T> {
        val cursor = findCursorBySelection(selection, selectionArgs, distinct, columns, groupBy, having, orderBy, limit, table, databaseName)
        return getAllItemsFromCursor(cursor)
    }

    @JvmOverloads
    open fun findCursorByRowId(rowId: Long, databaseName: String = getDatabaseName()): Cursor? {
        return findCursorBySelection(selection = primaryKey + "= ?", selectionArgs = arrayOf(rowId.toString()), databaseName = databaseName)
    }

    @JvmOverloads
    open fun findAll(orderBy: String? = null, databaseName: String = getDatabaseName()): List<T> {
        return findAllBySelection(orderBy = orderBy, databaseName = databaseName)
    }

    /**
     * Populate of List from a rawQuery.  The raw query must contain all of the columns names for the object

     * @param databaseName  Name of database
     * @param rawQuery      Custom query
     * @param selectionArgs query arguments
     * @return List of object T
     */
    @JvmOverloads
    open fun findAllByRawQuery(rawQuery: String, selectionArgs: Array<String>? = null, databaseName: String = getDatabaseName()): List<T> {
        return getAllItemsFromCursor(findCursorByRawQuery(rawQuery = rawQuery, selectionArgs = selectionArgs, databaseName = databaseName))
    }

    open fun getAllItemsFromCursor(cursor: Cursor?): List<T> {
        val foundItems: MutableList<T>
        if (cursor != null) {
            foundItems = ArrayList<T>(cursor.count)
            if (cursor.moveToFirst()) {
                do {
                    val record = newRecord()
                    record.setContent(cursor)
                    foundItems.add(record)
                } while (cursor.moveToNext())
            }
            cursor.close()
        } else {
            foundItems = ArrayList<T>()
        }

        return foundItems
    }

    @JvmOverloads
    open fun findByRowId(rowId: Long, databaseName: String = getDatabaseName()): T? {
        return findBySelection(selection = primaryKey + "= ?", selectionArgs = arrayOf(rowId.toString()), databaseName = databaseName)
    }

    @JvmOverloads
    open fun findBySelection(selection: String? = null,
                             selectionArgs: Array<String>? = null,
                             distinct: Boolean = true,
                             table: String = this.tableName,
                             columns: Array<String> = emptyArray(),
                             groupBy: String? = null,
                             having: String? = null,
                             orderBy: String? = null,
                             databaseName: String = getDatabaseName()): T? {
        val cursor = findCursorBySelection(selection, selectionArgs, distinct, columns, groupBy, having, orderBy, "1", table, databaseName)
        return createRecordFromCursor(cursor)
    }

    @JvmOverloads
    open fun findByRawQuery(rawQuery: String, selectionArgs: Array<String>? = null, databaseName: String = getDatabaseName()): T? {
        val cursor = findCursorByRawQuery(rawQuery, selectionArgs, databaseName)

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                return createRecordFromCursor(cursor)
            } else {
                cursor.close()
                return null
            }
        } else {
            return null
        }
    }

    open fun createRecordFromCursor(cursor: Cursor?): T? {
        if (cursor != null) {
            val record = newRecord()
            record.setContent(cursor)
            cursor.close()
            return record
        } else {
            return null
        }
    }

    @JvmOverloads
    open fun findAllByRowIds(rowIds: LongArray, orderBy: String? = null, databaseName: String = getDatabaseName()): List<T> {
        if (rowIds.isEmpty()) {
            return ArrayList()
        }

        val sb = StringBuilder()
        for (rowId in rowIds) {
            if (sb.isNotEmpty()) {
                sb.append(" OR ")
            }
            sb.append(primaryKey).append(" = ").append(rowId)
        }

        return findAllBySelection(selection = sb.toString(), orderBy = orderBy, databaseName = databaseName)
    }

    @JvmOverloads
    open fun findCount(databaseName: String = getDatabaseName()): Long {
        return findCountBySelection(databaseName = databaseName)
    }

    @JvmOverloads
    open fun findCountBySelection(selection: String? = null, selectionArgs: Array<String>? = null, databaseName: String = getDatabaseName()): Long {
        return AndroidBaseManager.findCountBySelection(getReadableDatabase(databaseName), tableName, selection, selectionArgs)
    }

    @JvmOverloads
    open fun findCountByRawQuery(rawQuery: String, selectionArgs: Array<String>? = null, databaseName: String = getDatabaseName()): Long {
        return AndroidBaseManager.findCountByRawQuery(getReadableDatabase(databaseName), rawQuery, selectionArgs)
    }

    /**
     * Return the first column and first row value as the value for given rawQuery and selectionArgs.
     *
     * @param databaseName  Name of database to query
     * @param valueType     Type to be used when getting data from database and what type is used on return (Integer.class, Boolean.class, etc)
     * @param rawQuery      Query contain first column which is the needed value
     * @param selectionArgs Query parameters
     * @param defaultValue  Value returned if nothing is found
     * @param <I>           Type of value
     *
     * @return query results value or defaultValue if no data was returned
     */
    @JvmOverloads
    fun <I> findValueByRawQuery(valueType: Class<out I>, rawQuery: String, selectionArgs: Array<String>? = null, defaultValue: I, databaseName: String = getDatabaseName()): I {
        val databaseValue = AndroidBaseManager.getDatabaseValue<I>(valueType)
        var value = defaultValue

        val c = getReadableDatabase(databaseName).rawQuery(rawQuery, selectionArgs)
        if (c != null) {
            if (c.moveToFirst()) {
                value = databaseValue.getColumnValue(c, 0, defaultValue) as I
            }
            c.close()
        }

        return value
    }

    /**
     * Return the value for the specified column and first row value as given type for given selection and selectionArgs.
     * @param valueType     Type to be used when getting data from database and what type is used on return (Integer.class, Boolean.class, etc)
     * @param column        Column which contains value
     * @param rowId         Primary key value (where clause)
     * @param defaultValue  Value returned if nothing is found
     * @param <I>           Type of value
     * @return query results value or defaultValue if no data was returned
     */
    @JvmOverloads
    open fun <I> findValueBySelection(valueType: Class<out I>, column: String, rowId: Long, defaultValue: I, databaseName: String = getDatabaseName()): I {
        return findValueBySelection<I>(valueType = valueType,
                column = column,
                defaultValue = defaultValue,
                selection = "$primaryKey = $rowId",
                databaseName = databaseName)
    }

    /**
     * Return the value for the specified column and first row value as given type for given selection and selectionArgs.
     *
     * @param databaseName  Name of database to query
     * @param valueType     Type to be used when getting data from database and what type is used on return (Integer.class, Boolean.class, etc)
     * @param column        Column which contains value
     * @param selection     Query selection
     * @param selectionArgs Query parameters
     * @param orderBy       Order by value(s)
     * @param defaultValue  Value returned if nothing is found
     * @param <I>           Type of value
     *
     * @return query results value or defaultValue if no data was returned
     */
    @JvmOverloads
    open fun <I> findValueBySelection(valueType: Class<out I>,
                                      column: String,
                                      defaultValue: I,
                                      selection: String? = null,
                                      selectionArgs: Array<String>? = null,
                                      having: String? = null,
                                      groupBy: String? = null,
                                      orderBy: String? = null,
                                      databaseName: String = getDatabaseName()): I {
        val databaseValue = AndroidBaseManager.getDatabaseValue<I>(valueType)
        var value = defaultValue

        val c = getReadableDatabase(databaseName).query(false, tableName, arrayOf(column), selection, selectionArgs, groupBy, having, orderBy, "1")
        if (c != null) {
            if (c.moveToFirst()) {
                value = databaseValue.getColumnValue(c, 0, defaultValue) as I
            }
            c.close()
        }
        return value
    }

    /**
     * Return a list of all of the first column values as a List for given rawQuery and selectionArgs.
     *
     * @param databaseName  Name of database to query
     * @param valueType     Type to be used when getting data from database and what type is used on return (Integer.class, Boolean.class, etc)
     * @param columnIndex   Index of column with value
     * @param rawQuery      Query contain first column contains value
     * @param selectionArgs Query parameters
     * @param <I>           Type of value
     *
     * @return query results List or empty List returned
     */
    @JvmOverloads
    open fun <I> findAllValuesByRawQuery(valueType: Class<I>,
                                         rawQuery: String,
                                         selectionArgs: Array<String>? = null,
                                         columnIndex: Int = 0,
                                         databaseName: String = getDatabaseName()): List<I> {
        return AndroidBaseManager.findAllValuesByRawQuery(getReadableDatabase(databaseName), valueType, columnIndex, rawQuery, selectionArgs)
    }

    /**
     * Return a list of all values for the specified column for given selection and selectionArgs.
     *
     * @param databaseName  Name of database to query
     * @param valueType     Type to be used when getting data from database and what type is used on return (Integer.class, Boolean.class, etc)
     * @param column        Column which contains value
     * @param selection     Query selection
     * @param selectionArgs Query parameters
     * @param orderBy       Query order by
     * @param <I>           Type of value
     *
     * @return query results List or empty List returned
     */
    @JvmOverloads
    open fun <I> findAllValuesBySelection(valueType: Class<I>,
                                          column: String,
                                          selection: String? = null,
                                          selectionArgs: Array<String>? = null,
                                          groupBy: String? = null,
                                          having: String? = null,
                                          orderBy: String? = null,
                                          limit: String? = null,
                                          databaseName: String = getDatabaseName()): List<I> {
        return AndroidBaseManager.findAllValuesBySelection(getReadableDatabase(databaseName), tableName, valueType, column, selection, selectionArgs, groupBy, having, orderBy, limit)
    }

    @JvmOverloads
    open fun tableExists(tableName: String = this.tableName, databaseName: String = getDatabaseName()): Boolean {
        return AndroidBaseManager.tableExists(getReadableDatabase(databaseName), tableName)
    }

    open fun toMatrixCursor(record: T): MatrixCursor {
        val list = ArrayList<T>(1)
        list.add(record)
        return toMatrixCursor(record.allColumns, list)
    }

    open fun toMatrixCursor(vararg records: T): MatrixCursor {
        return toMatrixCursor(Arrays.asList(*records))
    }

    open fun toMatrixCursor(records: List<T>): MatrixCursor {
        val record = records[0]

        return toMatrixCursor(record.allColumns, records)
    }

    open fun toMatrixCursor(columns: Array<String>, records: List<T>): MatrixCursor {
        val mx = MatrixCursor(columns)
        for (record in records) {
            mx.addRow(record.values)
        }
        return mx
    }

    open fun mergeCursors(vararg cursors: Cursor): Cursor {
        return MergeCursor(cursors)
    }

    open fun addAllToCursorTop(cursor: Cursor, records: List<T>): Cursor {
        return toMatrixCursor(records).let { mergeCursors(it, cursor) }
    }

    open fun addAllToCursorTop(cursor: Cursor, vararg records: T): Cursor {
        return toMatrixCursor(*records).let { mergeCursors(it, cursor) }
    }

    open fun addAllToCursorBottom(cursor: Cursor, records: List<T>): Cursor {
        return toMatrixCursor(records).let { mergeCursors(cursor, it) }
    }

    open fun addAllToCursorBottom(cursor: Cursor, vararg records: T): Cursor {
        return toMatrixCursor(*records).let { mergeCursors(cursor, it) }
    }
}