package org.dbtools.android.domain

import org.dbtools.android.domain.database.DatabaseWrapper
import org.dbtools.android.domain.database.contentvalues.DBToolsContentValues
import java.util.concurrent.ConcurrentHashMap

@Suppress("unused")
abstract class KotlinAndroidBaseManagerWritable<T : AndroidBaseRecord>(androidDatabaseManager: AndroidDatabaseManager) : KotlinAndroidBaseManager<T>(androidDatabaseManager), NotifiableManager {
    private val lastTableModifiedTsMap = ConcurrentHashMap<String, Long>()

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
    open fun update(contentValues: DBToolsContentValues<*>, where: String? = null, whereArgs: Array<String>? = null, databaseName: String = getDatabaseName()): Int {
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
    open fun delete(where: String?, whereArgs: Array<String>? = null, databaseName: String = getDatabaseName()): Int {
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

    override fun notifyTableListeners(databaseName: String, forceNotify: Boolean, databaseWrapper: DatabaseWrapper<in AndroidBaseRecord, in DBToolsContentValues<*>>?, changeType: DatabaseTableChange) {
        updateLastTableModifiedTs()

        if (forceNotify || !(databaseWrapper != null && databaseWrapper.inTransaction())) {
            listenerLock.lock()
            try {
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

    private fun updateLastTableModifiedTs(databaseName: String = getDatabaseName()) {
        lastTableModifiedTsMap.put(databaseName, System.currentTimeMillis())
    }
}
