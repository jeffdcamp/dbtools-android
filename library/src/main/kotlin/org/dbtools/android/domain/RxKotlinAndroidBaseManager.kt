package org.dbtools.android.domain

import android.database.Cursor
import android.database.MatrixCursor
import android.database.MergeCursor
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.Single
import io.reactivex.subjects.PublishSubject
import java.util.Arrays

@Suppress("unused")
abstract class RxKotlinAndroidBaseManager<T : AndroidBaseRecord>(androidDatabaseManager: AndroidDatabaseManager) : KotlinAndroidBaseManager<T>(androidDatabaseManager) {

    protected val tableChangeSubjectMap = HashMap<String, PublishSubject<DatabaseTableChange>>()

    @JvmOverloads
    open fun findCursorAllRx(databaseName: String = getDatabaseName()): Single<Cursor> {
        return Single.create { it.onSuccess(findCursorAll(databaseName) ?: throw IllegalStateException("findCursorAllRx(...) cannot return null")) }
    }

    @JvmOverloads
    open fun findCursorByRawQueryRx(rawQuery: String, selectionArgs: Array<String>?, databaseName: String = getDatabaseName()): Single<Cursor> {
        return Single.create { it.onSuccess(getReadableDatabase(databaseName).rawQuery(rawQuery, selectionArgs)) }
    }

    @JvmOverloads
    open fun findCursorBySelectionRx(selection: String? = null, selectionArgs: Array<String>? = null, distinct: Boolean = false, columns: Array<String>, groupBy: String? = null, having: String? = null, orderBy: String? = null, limit: String? = null, table: String = getTableName(), databaseName: String = getDatabaseName()): Single<Cursor> {
        return Single.create { it.onSuccess(findCursorBySelection(selection, selectionArgs, columns = columns, databaseName = databaseName, table = table, groupBy = groupBy, having = having, orderBy = orderBy, distinct = distinct, limit = limit) ?: throw IllegalStateException("findCursorBySelectionRx(...) cannot return null")) }
    }

    @JvmOverloads
    open fun findCursorByRowIdRx(rowId: Long, databaseName: String = getDatabaseName()): Single<Cursor> {
        return Single.create { it.onSuccess(findCursorBySelection(selection = primaryKey + "= ?", selectionArgs = arrayOf(rowId.toString()), databaseName = databaseName) ?: throw IllegalStateException("findCursorByRowIdRx(...) cannot return null")) }
    }

    @JvmOverloads
    open fun findAllRx(orderBy: String? = null, databaseName: String = getDatabaseName()): Single<List<T>> {
        return findAllBySelectionRx(orderBy = orderBy, databaseName = databaseName)
    }

    @JvmOverloads
    open fun findAllRxStream(orderBy: String? = null, databaseName: String = getDatabaseName()): Observable<T> {
        return findAllBySelectionRxStream(orderBy = orderBy, databaseName = databaseName)
    }

    @JvmOverloads
    open fun findByRowIdRx(rowId: Long, databaseName: String = getDatabaseName()): Maybe<T> {
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
                                     databaseName: String = getDatabaseName()): Maybe<T> {

        return Maybe.create {
            val result = findBySelection(selection = selection, selectionArgs = selectionArgs, distinct = distinct, table = table, columns = columns, groupBy = groupBy, having = having, orderBy = orderBy, databaseName = databaseName)
            when (result) {
                null -> it.onComplete()
                else -> it.onSuccess(result)
            }
        }
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
                                        databaseName: String = getDatabaseName()): Single<List<T>> {
        return Single.create { it.onSuccess(findAllBySelection(selection, selectionArgs, distinct = distinct, columns = columns, groupBy = groupBy, having = having, orderBy = orderBy, limit = limit, table = table, databaseName = databaseName)) }
    }

    @JvmOverloads
    open fun findAllBySelectionRxStream(selection: String? = null,
                                            selectionArgs: Array<String>? = null,
                                            distinct: Boolean = true,
                                            columns: Array<String> = emptyArray(),
                                            groupBy: String? = null,
                                            having: String? = null,
                                            orderBy: String? = null,
                                            limit: String? = null,
                                            table: String = getTableName(),
                                            databaseName: String = getDatabaseName()): Observable<T> {
        return Observable.create<T>({
            emitAllItemsFromCursor(it, { findCursorBySelection(selection, selectionArgs, distinct = distinct, columns = columns, groupBy = groupBy, having = having, orderBy = orderBy, limit = limit, table = table, databaseName = databaseName) })
        })
    }

    @JvmOverloads
    open fun findByRawQueryRx(rawQuery: String,
                                    selectionArgs: Array<String>? = null,
                                    databaseName: String = getDatabaseName()): Maybe<T> {
        return Maybe.create {
            val result = findByRawQuery(rawQuery = rawQuery, selectionArgs = selectionArgs, databaseName = databaseName)
            when (result) {
                null -> it.onComplete()
                else -> it.onSuccess(result)
            }
        }
    }

    @JvmOverloads
    open fun findAllByRawQueryRx(rawQuery: String,
                                       selectionArgs: Array<String>? = null,
                                       databaseName: String = getDatabaseName()): Single<List<T>> {
        return Single.create { it.onSuccess(findAllByRawQuery(rawQuery = rawQuery, selectionArgs = selectionArgs, databaseName = databaseName)) }
    }

    @JvmOverloads
    open fun findAllByRawQueryRxStream(rawQuery: String,
                                           selectionArgs: Array<String>? = null,
                                           databaseName: String = getDatabaseName()): Observable<T> {
        return Observable.create<T>({
            emitAllItemsFromCursor(it, { findCursorByRawQuery(rawQuery, selectionArgs, databaseName) })
        })
    }

    @JvmOverloads
    open fun findAllByRowIdsRx(rowIds: LongArray, orderBy: String? = null, databaseName: String = getDatabaseName()): Single<List<T>> {
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
    open fun findAllByRowIdsRxStream(rowIds: LongArray, orderBy: String? = null, databaseName: String = getDatabaseName()): Observable<T> {
        val selection = StringBuilder()
        for (rowId in rowIds) {
            if (selection.isNotEmpty()) {
                selection.append(" OR ")
            }
            selection.append(primaryKey).append(" = ").append(rowId)
        }

        return Observable.create<T>({
            emitAllItemsFromCursor(it, { findCursorBySelection(selection.toString(), orderBy = orderBy, databaseName = databaseName) })
        })
    }

    @JvmOverloads
    open fun findCountRx(databaseName: String = getDatabaseName()): Single<Long> {
        return Single.create { it.onSuccess(findCountBySelection(databaseName = databaseName)) }
    }

    open fun findCountBySelectionRx(selection: String?, selectionArgs: Array<String>?, databaseName: String = getDatabaseName()): Single<Long> {
        return Single.create { it.onSuccess(AndroidBaseManager.findCountBySelection(getReadableDatabase(databaseName), getTableName(), selection, selectionArgs)) }
    }

    @JvmOverloads
    open fun findCountByRawQueryRx(rawQuery: String, selectionArgs: Array<String>? = null, databaseName: String = getDatabaseName()): Single<Long> {
        return Single.create { it.onSuccess(AndroidBaseManager.findCountByRawQuery(getReadableDatabase(databaseName), rawQuery, selectionArgs)) }
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
    open fun <I> findValueByRawQueryRx(valueType: Class<out I>, defaultValue: I, rawQuery: String, selectionArgs: Array<String>? = null, databaseName: String = getDatabaseName()): Single<I> {
        return Single.create { it.onSuccess(findValueByRawQuery(valueType, defaultValue, rawQuery, selectionArgs, databaseName)) }
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
    open fun <I> findValueByRowIdRx(valueType: Class<out I>, column: String, rowId: Long, defaultValue: I, databaseName: String = getDatabaseName()): Single<I> {
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
    open fun <I> findValueBySelectionRx(valueType: Class<out I>, column: String, defaultValue: I, selection: String? = null, selectionArgs: Array<String>? = null, groupBy: String? = null, having: String? = null, orderBy: String? = null, databaseName: String = getDatabaseName()): Single<I> {
        return Single.create { it.onSuccess(findValueBySelection(valueType, column, defaultValue, selection, selectionArgs, having, groupBy, orderBy, databaseName)) }
    }

    /**
     * Return the value for the specified column and first row value as given type for given selection and selectionArgs.
     * @return query results List or empty List returned
     */
    @JvmOverloads
    open fun <I> findAllValuesBySelectionRx(valueType: Class<I>, column: String, selection: String? = null, selectionArgs: Array<String>? = null, groupBy: String? = null, having: String? = null, orderBy: String? = null, limit: String? = null, databaseName: String = getDatabaseName()): Single<List<I>> {
        return RxAndroidBaseManager.findAllValuesBySelectionRx(getReadableDatabase(databaseName), getTableName(), valueType, column, selection, selectionArgs, groupBy, having, orderBy, limit)
    }

    open fun tableExistsRx(tableName: String, databaseName: String = getDatabaseName()): Single<Boolean> {
        return Single.create { it.onSuccess(AndroidBaseManager.tableExists(getReadableDatabase(databaseName), tableName)) }
    }

    open fun toMatrixCursorRx(record: T): Single<MatrixCursor> {
        return Single.create { it.onSuccess(toMatrixCursor(record)) }
    }

    open fun toMatrixCursorRx(vararg records: T): Single<MatrixCursor> {
        return Single.create { it.onSuccess(toMatrixCursor(Arrays.asList(*records))) }
    }

    open fun toMatrixCursorRx(records: List<T>): Single<MatrixCursor> {
        return Single.create { it.onSuccess(toMatrixCursor(records)) }
    }

    open fun toMatrixCursorRx(columns: Array<String>, records: List<T>): Single<MatrixCursor> {
        return Single.create { it.onSuccess(toMatrixCursor(columns, records)) }
    }

    open fun mergeCursorsRx(vararg cursors: Cursor): Single<Cursor> {
        return Single.create { it.onSuccess(MergeCursor(cursors)) }
    }

    open fun addAllToCursorTopRx(cursor: Cursor, records: List<T>): Single<Cursor> {
        return Single.create { it.onSuccess(mergeCursors(toMatrixCursor(records), cursor)) }
    }

    open fun addAllToCursorTopRx(cursor: Cursor, vararg records: T): Single<Cursor> {
        return Single.create { it.onSuccess(mergeCursors(toMatrixCursor(*records), cursor)) }
    }

    open fun addAllToCursorBottomRx(cursor: Cursor, records: List<T>): Single<Cursor> {
        return Single.create { it.onSuccess(mergeCursors(cursor, toMatrixCursor(records))) }
    }

    open fun addAllToCursorBottomRx(cursor: Cursor, vararg records: T): Single<Cursor> {
        return Single.create { it.onSuccess(mergeCursors(cursor, toMatrixCursor(*records))) }
    }

    // ===== Observables =====

    @JvmOverloads
    fun tableChanges(databaseName: String = getDatabaseName()): Observable<DatabaseTableChange> {
        listenerLock.lock()

        try {
            var subject: PublishSubject<DatabaseTableChange>? = tableChangeSubjectMap.get(databaseName)
            if (subject == null) {
                subject = PublishSubject.create<DatabaseTableChange>()
                tableChangeSubjectMap.put(databaseName, subject)
            }
            return subject!!
        } finally {
            listenerLock.unlock()
        }
    }

    private fun emitAllItemsFromCursor(e: ObservableEmitter<T>, cursorFunc: () -> Cursor?) {
        try {
            val cursor = cursorFunc() // execute query

            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    do {
                        val record = newRecord()
                        record.setContent(cursor)
                        e.onNext(record)
                    } while (cursor.moveToNext())
                }
                cursor.close()
            }

            e.onComplete()
        } catch (ex: Exception) {
            e.onError(ex)
        }
    }
}
