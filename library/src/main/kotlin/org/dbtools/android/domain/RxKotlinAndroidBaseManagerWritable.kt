package org.dbtools.android.domain

import org.dbtools.android.domain.database.DatabaseWrapper
import org.dbtools.android.domain.database.contentvalues.DBToolsContentValues
import rx.Observable
import rx.subjects.PublishSubject
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.locks.ReentrantLock

@Suppress("unused")
abstract class RxKotlinAndroidBaseManagerWritable<T : AndroidBaseRecord> : RxKotlinAndroidBaseManager<T>() {
    private val transactionInsertOccurred = AtomicBoolean(false)
    private val transactionUpdateOccurred = AtomicBoolean(false)
    private val transactionDeleteOccurred = AtomicBoolean(false)
    var lastTableModifiedTs = -1L
        private set

    private val listenerLock = ReentrantLock()
    private val tableChangeListeners = HashSet<DBToolsTableChangeListener>()
    private val tableChanges = PublishSubject.create<DatabaseTableChange>()

    inline fun inTransaction(func: () -> Boolean) {
        var success = false
        try {
            beginTransaction()
            success = func()
        } finally {
            endTransaction(success)
        }
    }

    @JvmOverloads
    open fun inTransaction(databaseName: String = this.getDatabaseName()): Boolean {
        return getWritableDatabase(databaseName).inTransaction()
    }

    @JvmOverloads
    open fun beginTransaction(databaseName: String = getDatabaseName()) {
        getWritableDatabase(databaseName).beginTransaction()
    }

    @JvmOverloads
    open fun endTransaction(success: Boolean = true, databaseName: String = getDatabaseName()) {
        val db = getWritableDatabase(databaseName)
        if (success) {
            db.setTransactionSuccessful()
        }

        // determine if there are changes
        val tableChange = DatabaseTableChange(tableName, transactionInsertOccurred.get(), transactionUpdateOccurred.get(), transactionDeleteOccurred.get())
        transactionInsertOccurred.set(false)
        transactionUpdateOccurred.set(false)
        transactionDeleteOccurred.set(false)

        // end transaction
        db.endTransaction()

        // post end transaction event
        if (tableChange.hasChange()) {
            notifyTableListeners(true, db, tableChange)
        }
    }

    /**
     * Save Record.
     *
     * @param databaseName database name to use
     * @param record            Record to be saved
     *
     * @return true if record was saved
     */
    @JvmOverloads
    open fun save(record: T?, databaseName: String = getDatabaseName()): Boolean {
        if (record == null) {
            return false
        }

        if (record.isNewRecord) {
            val newId = insert(record, databaseName)
            return newId > 0
        } else {
            val count = update(record, databaseName)
            return count != 0
        }
    }

    /**
     * Insert record into database.
     *
     * @param record record to be inserted
     *
     * @return long value of new id
     */
    @JvmOverloads
    open fun insert(record: T?, databaseName: String = getDatabaseName()): Long {
        val db = getWritableDatabase(databaseName)

        if (record == null) {
            return -1
        }

        var rowId: Long = -1

        // Make sure that if there is an error (LockedException), that we try again.
        var success = false
        var tryCount = 0
        while (tryCount < AndroidBaseManager.MAX_TRY_COUNT && !success) {
            try {
                // statement
                val statement = getInsertStatement(db)
                statement.clearBindings();
                record.bindInsertStatement(statement)
                rowId = statement.executeInsert()

                record.primaryKeyId = rowId

                notifyTableListeners(false, db, DatabaseTableChange(tableName, rowId, true, false, false))

                success = true
            } catch (ex: Exception) {
                ex.printStackTrace()
            }

            tryCount++
        } // retry
        return rowId
    }

    @JvmOverloads
    open fun update(record: T?, databaseName: String = getDatabaseName()): Int {
        val db = getWritableDatabase(databaseName)

        if (record == null) {
            return 0
        }

        val rowId = record.primaryKeyId
        if (rowId <= 0) {
            throw IllegalArgumentException("Invalid rowId [$rowId] be sure to call create(...) before update(...)")
        }

        // Statement
        val statement = getUpdateStatement(db)
        statement.clearBindings()
        record.bindUpdateStatement(statement)
        val rowsAffectedCount = statement.executeUpdateDelete()

        if (rowsAffectedCount > 0) {
            notifyTableListeners(false, db, DatabaseTableChange(tableName, rowId, false, true, false))
        }

        return rowsAffectedCount
    }

    @JvmOverloads
    open fun update(values: DBToolsContentValues<*>, rowId: Long, databaseName: String = getDatabaseName()): Int {
        return update(values, primaryKey + " = ?", arrayOf(rowId.toString()), databaseName)
    }

    @JvmOverloads
    open fun update(contentValues: DBToolsContentValues<*>, where: String?, whereArgs: Array<String>?, databaseName: String = getDatabaseName()): Int {
        val db = getWritableDatabase(databaseName)

        var rowsAffectedCount = 0

        // Make sure that if there is an error (LockedException), that we try again.
        var success = false
        var tryCount = 0
        while (tryCount < AndroidBaseManager.MAX_TRY_COUNT && !success) {
            try {
                rowsAffectedCount = db.update(tableName, contentValues, where, whereArgs)
                success = true
            } catch (ex: Exception) {
                ex.printStackTrace()
            }

            tryCount++
        }

        if (success && rowsAffectedCount > 0) {
            notifyTableListeners(false, db, DatabaseTableChange(tableName, false, true, false))
        }

        return rowsAffectedCount
    }

    @JvmOverloads
    open fun delete(record: T?, databaseName: String = getDatabaseName()): Int {
        if (record == null) {
            return 0
        }

        val rowId = record.primaryKeyId
        if (rowId <= 0) {
            throw IllegalArgumentException("Invalid rowId [$rowId]")
        }

        return delete(record.idColumnName + " = ?", arrayOf(rowId.toString()), databaseName)
    }

    @JvmOverloads
    open fun delete(rowId: Long, databaseName: String = getDatabaseName()): Int {
        return delete(primaryKey + " = ?", arrayOf(rowId.toString()), databaseName)
    }

    @JvmOverloads
    open fun delete(where: String?, whereArgs: Array<String>?, databaseName: String = getDatabaseName()): Int {
        val db = getWritableDatabase(databaseName)

        var rowCountAffected = 0

        // Make sure that if there is an error (LockedException), that we try again.
        var success = false
        var tryCount = 0
        while (tryCount < AndroidBaseManager.MAX_TRY_COUNT && !success) {
            try {
                rowCountAffected = db.delete(tableName, where, whereArgs)
                success = true
            } catch (ex: Exception) {
                ex.printStackTrace()
            }

            tryCount++
        }

        if (success && rowCountAffected > 0) {
            notifyTableListeners(false, db, DatabaseTableChange(tableName, false, false, true))
        }

        return rowCountAffected
    }

    @JvmOverloads
    open fun deleteAll(databaseName: String = getDatabaseName()): Long {
        return delete(null, null, databaseName).toLong()
    }

    // ===== Listeners =====

    open fun addTableChangeListener(listener: DBToolsTableChangeListener) {
        listenerLock.lock()
        try {
            tableChangeListeners.add(listener)
        } finally {
            listenerLock.unlock()
        }
    }

    open fun removeTableChangeListener(listener: DBToolsTableChangeListener) {
        listenerLock.lock()
        try {
            tableChangeListeners.remove(listener)
        } finally {
            listenerLock.unlock()
        }
    }

    open fun clearTableChangeListeners() {
        listenerLock.lock()
        try {
            tableChangeListeners.clear()
        } finally {
            listenerLock.unlock()
        }
    }

    private fun notifyTableListeners(forceNotify: Boolean, db: DatabaseWrapper<in AndroidBaseRecord, in DBToolsContentValues<*>>?, changeType: DatabaseTableChange) {
        updateLastTableModifiedTs()

        if (forceNotify || !(db != null && db.inTransaction())) {
            tableChanges.onNext(changeType)

            listenerLock.lock()
            try {
                for (tableChangeListener in tableChangeListeners) {
                    tableChangeListener.onTableChange(changeType)
                }
            } finally {
                listenerLock.unlock()
            }
        } else {
            if (changeType.isInsert) {
                transactionInsertOccurred.set(true)
            } else if (changeType.isUpdate) {
                transactionUpdateOccurred.set(true)
            } else if (changeType.isDelete) {
                transactionDeleteOccurred.set(true)
            }
        }
    }

    // ===== Observables =====

    open fun tableChanges(): Observable<DatabaseTableChange> {
        return tableChanges.asObservable()
    }

    // ===== Table Change =====
    private fun updateLastTableModifiedTs() {
        this.lastTableModifiedTs = System.currentTimeMillis()
    }
}
