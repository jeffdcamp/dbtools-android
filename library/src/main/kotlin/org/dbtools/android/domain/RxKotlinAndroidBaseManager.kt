package org.dbtools.android.domain

import android.database.Cursor
import android.database.MatrixCursor
import android.database.MergeCursor
import rx.Observable
import java.util.*

@Suppress("unused")
abstract class RxKotlinAndroidBaseManager<T : AndroidBaseRecord>(androidDatabaseManager: AndroidDatabaseManager) : KotlinAndroidBaseManager<T>(androidDatabaseManager) {

    @JvmOverloads
    open fun findCursorAllRx(databaseName: String = getDatabaseName()): Observable<Cursor> {
        return DBToolsRxUtil.just { findCursorAll(databaseName) }
    }

    @JvmOverloads
    open fun findCursorByRawQueryRx(rawQuery: String, selectionArgs: Array<String>?, databaseName: String = getDatabaseName()): Observable<Cursor> {
        return DBToolsRxUtil.just { getReadableDatabase(databaseName).rawQuery(rawQuery, selectionArgs) }
    }

    @JvmOverloads
    open fun findCursorBySelectionRx(selection: String? = null, selectionArgs: Array<String>? = null, distinct: Boolean = false, columns: Array<String>, groupBy: String? = null, having: String? = null, orderBy: String? = null, limit: String? = null, table: String = getTableName(), databaseName: String = getDatabaseName()): Observable<Cursor> {
        return DBToolsRxUtil.just { findCursorBySelection(selection, selectionArgs, columns = columns, databaseName = databaseName, table = table, groupBy = groupBy, having = having, orderBy = orderBy, distinct = distinct, limit = limit) }
    }

    @JvmOverloads
    open fun findCursorByRowIdRx(rowId: Long, databaseName: String = getDatabaseName()): Observable<Cursor> {
        return DBToolsRxUtil.just { findCursorBySelection(selection = primaryKey + "= ?", selectionArgs = arrayOf(rowId.toString()), databaseName = databaseName) }
    }

    @JvmOverloads
    open fun findAllRx(orderBy: String? = null, databaseName: String = getDatabaseName()): Observable<List<T>> {
        return findAllBySelectionRx(orderBy = orderBy, databaseName = databaseName)
    }

    @JvmOverloads
    open fun findByRowIdRx(rowId: Long, databaseName: String = getDatabaseName()): Observable<T> {
        return findBySelectionRx(selection = primaryKey + "= ?", selectionArgs = arrayOf(rowId.toString()), databaseName = databaseName)
    }

    @JvmOverloads
    open fun findBySelectionRx(selection: String? = null,
                               selectionArgs: Array<String>? = null,
                               distinct: Boolean = true,
                               columns: Array<String> = emptyArray(),
                               groupBy: String? = null,
                               having: String? = null,
                               orderBy: String? = null,
                               table: String = this.getTableName(),
                               databaseName: String = getDatabaseName()): Observable<T> {
        return DBToolsRxUtil.just { findBySelection(selection = selection, selectionArgs = selectionArgs, distinct = distinct, table = table, columns = columns, groupBy = groupBy, having = having, orderBy = orderBy, databaseName = databaseName) }
    }

    @JvmOverloads
    open fun findAllBySelectionRx(selection: String? = null,
                                  selectionArgs: Array<String>? = null,
                                  distinct: Boolean = true,
                                  columns: Array<String> = emptyArray(),
                                  groupBy: String? = null,
                                  having: String? = null,
                                  orderBy: String? = null,
                                  limit: String? = null,
                                  table: String = getTableName(),
                                  databaseName: String = getDatabaseName()): Observable<List<T>> {
        return DBToolsRxUtil.just { findAllBySelection(selection, selectionArgs, distinct = distinct, columns = columns, groupBy = groupBy, having = having, orderBy = orderBy, limit = limit, table = table, databaseName = databaseName) }
    }

    @JvmOverloads
    open fun findByRawQueryRx(rawQuery: String,
                              selectionArgs: Array<String>? = null,
                              databaseName: String = getDatabaseName()): Observable<T> {
        return DBToolsRxUtil.just { findByRawQuery(rawQuery = rawQuery, selectionArgs = selectionArgs, databaseName = databaseName) }
    }

    @JvmOverloads
    open fun findAllByRawQueryRx(rawQuery: String,
                                 selectionArgs: Array<String>? = null,
                                 databaseName: String = getDatabaseName()): Observable<List<T>> {
        return DBToolsRxUtil.just { findAllByRawQuery(rawQuery = rawQuery, selectionArgs = selectionArgs, databaseName = databaseName) }
    }

    @JvmOverloads
    open fun findAllByRowIdsRx(rowIds: LongArray, orderBy: String? = null, databaseName: String = getDatabaseName()): Observable<List<T>> {
        val selection = StringBuilder()
        for (rowId in rowIds) {
            if (selection.isNotEmpty()) {
                selection.append(" OR ")
            }
            selection.append(primaryKey).append(" = ").append(rowId)
        }

        return findAllBySelectionRx(selection.toString(), orderBy = orderBy, databaseName = databaseName)
    }

    @JvmOverloads
    open fun findCountRx(databaseName: String = getDatabaseName()): Observable<Long> {
        return DBToolsRxUtil.just { findCountBySelection(databaseName = databaseName) }
    }

    open fun findCountBySelectionRx(selection: String?, selectionArgs: Array<String>?, databaseName: String): Observable<Long> {
        return DBToolsRxUtil.just { AndroidBaseManager.findCountBySelection(getReadableDatabase(databaseName), getTableName(), selection, selectionArgs) }
    }

    @JvmOverloads
    open fun findCountByRawQueryRx(rawQuery: String, selectionArgs: Array<String>? = null, databaseName: String = getDatabaseName()): Observable<Long> {
        return DBToolsRxUtil.just { AndroidBaseManager.findCountByRawQuery(getReadableDatabase(databaseName), rawQuery, selectionArgs) }
    }

    /**
     * Return the first column and first row value as the value for given rawQuery and selectionArgs.
     * @param databaseName  Name of database to query
     * @param valueType     Type to be used when getting data from database and what type is used on return (Integer.class, Boolean.class, etc)
     * @param rawQuery      Query contain first column which is the needed value
     * @param selectionArgs Query parameters
     * @param <I>           Type of value
     * @return query results value or defaultValue if no data was returned
     */
    @JvmOverloads
    open fun <I> findValueByRawQueryRx(valueType: Class<out I>, rawQuery: String, defaultValue: I, selectionArgs: Array<String>? = null, databaseName: String = getDatabaseName()): Observable<I> {
        return DBToolsRxUtil.just {
            findValueByRawQuery(valueType, rawQuery, selectionArgs, defaultValue, databaseName)
        }
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
    open fun <I> findValueBySelectionRx(valueType: Class<out I>, column: String, rowId: Long, defaultValue: I, databaseName: String = getDatabaseName()): Observable<I> {
        return findValueBySelectionRx(valueType = valueType,
                column = column,
                defaultValue = defaultValue,
                selection = "$primaryKey = $rowId",
                databaseName = databaseName)
    }

    /**
     * Return the value for the specified column and first row value as given type for given selection and selectionArgs.
     * @param databaseName  Name of database to query
     * @param valueType     Type to be used when getting data from database and what type is used on return (Integer.class, Boolean.class, etc)
     * @param column        Column which contains value
     * @param selection     Query selection
     * @param selectionArgs Query parameters
     * @param orderBy       Order by value(s)
     * @param defaultValue  Value returned if nothing is found
     * @param <I>           Type of value
     * @return query results value or defaultValue if no data was returned
     */
    @JvmOverloads
    open fun <I> findValueBySelectionRx(valueType: Class<out I>, column: String, defaultValue: I, selection: String? = null, selectionArgs: Array<String>? = null, groupBy: String? = null, having: String? = null, orderBy: String? = null, databaseName: String = getDatabaseName()): Observable<I> {
        return DBToolsRxUtil.just {
            findValueBySelection<I>(valueType, column, defaultValue, selection, selectionArgs, having, groupBy, orderBy, databaseName)
        }
    }

    /**
     * Return the value for the specified column and first row value as given type for given selection and selectionArgs.

     * @return query results List or empty List returned
     */
    @JvmOverloads
    open fun <I> findAllValuesBySelectionRx(valueType: Class<I>, column: String, selection: String? = null, selectionArgs: Array<String>? = null, groupBy: String? = null, having: String? = null, orderBy: String? = null, limit: String? = null, databaseName: String = getDatabaseName()): Observable<List<I>> {
        return RxAndroidBaseManager.findAllValuesBySelectionRx(getReadableDatabase(databaseName), getTableName(), valueType, column, selection, selectionArgs, groupBy, having, orderBy, limit)
    }

    open fun tableExistsRx(tableName: String, databaseName: String = getDatabaseName()): Observable<Boolean> {
        return DBToolsRxUtil.just { AndroidBaseManager.tableExists(getReadableDatabase(databaseName), tableName) }
    }

    open fun toMatrixCursorRx(record: T): Observable<MatrixCursor> {
        return DBToolsRxUtil.just { toMatrixCursor(record) }
    }

    open fun toMatrixCursorRx(vararg records: T): Observable<MatrixCursor> {
        return DBToolsRxUtil.just { toMatrixCursor(Arrays.asList(*records)) }
    }

    open fun toMatrixCursorRx(records: List<T>): Observable<MatrixCursor> {
        return DBToolsRxUtil.just { toMatrixCursor(records) }
    }

    open fun toMatrixCursorRx(columns: Array<String>, records: List<T>): Observable<MatrixCursor> {
        return DBToolsRxUtil.just { toMatrixCursor(columns, records) }
    }

    open fun mergeCursorsRx(vararg cursors: Cursor): Observable<Cursor> {
        return DBToolsRxUtil.just { MergeCursor(cursors) }
    }

    open fun addAllToCursorTopRx(cursor: Cursor, records: List<T>): Observable<Cursor> {
        return DBToolsRxUtil.just { mergeCursors(toMatrixCursor(records), cursor) }
    }

    open fun addAllToCursorTopRx(cursor: Cursor, vararg records: T): Observable<Cursor> {
        return DBToolsRxUtil.just { mergeCursors(toMatrixCursor(*records), cursor) }
    }

    open fun addAllToCursorBottomRx(cursor: Cursor, records: List<T>): Observable<Cursor> {
        return DBToolsRxUtil.just { mergeCursors(cursor, toMatrixCursor(records)) }
    }

    open fun addAllToCursorBottomRx(cursor: Cursor, vararg records: T): Observable<Cursor> {
        return DBToolsRxUtil.just { mergeCursors(cursor, toMatrixCursor(*records)) }
    }
}
