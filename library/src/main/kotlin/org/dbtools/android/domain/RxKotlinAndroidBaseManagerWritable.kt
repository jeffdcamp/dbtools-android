package org.dbtools.android.domain

import org.dbtools.android.domain.database.DatabaseWrapper
import org.dbtools.android.domain.database.contentvalues.DBToolsContentValues
import rx.Observable
import rx.subjects.PublishSubject
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.locks.ReentrantLock

@Suppress("unused")
abstract class RxKotlinAndroidBaseManagerWritable<T : AndroidBaseRecord>(androidDatabaseManager: AndroidDatabaseManager) : RxKotlinAndroidBaseManager<T>(androidDatabaseManager), NotifiableManager {
    private val lastTableModifiedTsMap = ConcurrentHashMap<String, Long>()

    private val listenerLock = ReentrantLock()
    private val tableChangeListenersMap = mutableMapOf<String, MutableSet<DBToolsTableChangeListener>>()
    private val tableChangeSubjectMap = HashMap<String, PublishSubject<DatabaseTableChange>>()

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
        // log warning
        val writableDatabase = getWritableDatabase(databaseName)
        if (writableDatabase.inTransaction()) {
            androidDatabaseManager.getLogger().w(AndroidDatabaseBaseManager.TAG, "WARNING: Already in transaction.")
        }

        // clear table changes and start transaction
        androidDatabaseManager.clearTransactionTableChange(databaseName)
        writableDatabase.beginTransaction()
    }

    @JvmOverloads
    open fun endTransaction(success: Boolean = true, databaseName: String = getDatabaseName()) {
        val db = getWritableDatabase(databaseName)
        if (success) {
            db.setTransactionSuccessful()
        }

        // end transaction
        db.endTransaction()

        // post end transaction event
        if (success) {
            androidDatabaseManager.notifyTransactionEnded(databaseName)
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
                statement.clearBindings()
                record.bindInsertStatement(statement)
                rowId = statement.executeInsert()

                record.primaryKeyId = rowId

                notifyTableListeners(databaseName, false, db, DatabaseTableChange(getTableName(), rowId, true, false, false))

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
            notifyTableListeners(databaseName, false, db, DatabaseTableChange(getTableName(), rowId, false, true, false))
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
                rowsAffectedCount = db.update(getTableName(), contentValues, where, whereArgs)
                success = true
            } catch (ex: Exception) {
                ex.printStackTrace()
            }

            tryCount++
        }

        if (success && rowsAffectedCount > 0) {
            notifyTableListeners(databaseName, false, db, DatabaseTableChange(getTableName(), false, true, false))
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
                rowCountAffected = db.delete(getTableName(), where, whereArgs)
                success = true
            } catch (ex: Exception) {
                ex.printStackTrace()
            }

            tryCount++
        }

        if (success && rowCountAffected > 0) {
            notifyTableListeners(databaseName, false, db, DatabaseTableChange(getTableName(), false, false, true))
        }

        return rowCountAffected
    }

    @JvmOverloads
    open fun deleteAll(databaseName: String = getDatabaseName()): Long {
        return delete(null, null, databaseName).toLong()
    }

    // ===== Listeners =====

    @JvmOverloads
    open fun addTableChangeListener(listener: DBToolsTableChangeListener, databaseName: String = getDatabaseName()) {
        listenerLock.lock()
        try {
            var tableChangeListeners: MutableSet<DBToolsTableChangeListener>? = tableChangeListenersMap[databaseName]
            if (tableChangeListeners == null) {
                tableChangeListeners = HashSet<DBToolsTableChangeListener>()
                tableChangeListenersMap[databaseName] = tableChangeListeners
            }

            tableChangeListeners.add(listener)
        } finally {
            listenerLock.unlock()
        }
    }

    @JvmOverloads
    open fun removeTableChangeListener(listener: DBToolsTableChangeListener, databaseName: String = getDatabaseName()) {
        listenerLock.lock()
        try {
            val tableChangeListeners = tableChangeListenersMap[databaseName]
            tableChangeListeners?.remove(listener)
        } finally {
            listenerLock.unlock()
        }
    }

    @JvmOverloads
    open fun clearTableChangeListeners(databaseName: String = getDatabaseName()) {
        listenerLock.lock()
        try {
            val tableChangeListeners = tableChangeListenersMap[databaseName]
            tableChangeListeners?.clear()
        } finally {
            listenerLock.unlock()
        }
    }

    override fun notifyTableListeners(databaseName: String, forceNotify: Boolean, databaseWrapper: DatabaseWrapper<in AndroidBaseRecord, in DBToolsContentValues<*>>?, changeType: DatabaseTableChange) {
        updateLastTableModifiedTs()

        if (forceNotify || !(databaseWrapper != null && databaseWrapper.inTransaction())) {
            listenerLock.lock()
            try {
                val subject = tableChangeSubjectMap[databaseName]
                subject?.onNext(changeType)


                val tableChangeListeners = tableChangeListenersMap[databaseName]

                if (tableChangeListeners != null) {
                    for (tableChangeListener in tableChangeListeners) {
                        tableChangeListener.onTableChange(changeType)
                    }
                }
            } finally {
                listenerLock.unlock()
            }
        } else {
            androidDatabaseManager.addTransactionTableChange(databaseName, this)
        }
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
            return subject!!.asObservable()
        } finally {
            listenerLock.unlock()
        }
    }

    // ===== Table Change =====
    /**
     * Returns the last modification long ts

     * @return long ts value of the last modification to this table using this manager, or -1 if no modifications have occurred since app launch
     */
    @JvmOverloads
    fun getLastTableModifiedTs(databaseName: String = getDatabaseName()): Long {
        val lastTableModifiedTs = lastTableModifiedTsMap[databaseName] ?: return -1L

        return lastTableModifiedTs
    }

    @JvmOverloads
    private fun updateLastTableModifiedTs(databaseName: String = getDatabaseName()) {
        lastTableModifiedTsMap.put(databaseName, System.currentTimeMillis())
    }
}
