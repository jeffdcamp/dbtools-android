package org.dbtools.android.domain

abstract class RxKotlinAndroidBaseManagerWritable<T : AndroidBaseRecord> : RxAndroidBaseManagerWritable<T>() {
    fun inTransaction(func: () -> Boolean) {
        beginTransaction()
        endTransaction(func())
    }
//
//    fun save(databaseName: String = getDatabaseName(), e: T) {
//        super.save(databaseName, e)
//    }
//
//    fun save(databaseWrapper: AndroidDatabaseWrapper, e: T) {
//        super.save(databaseWrapper, e)
//    }
//
//    fun insert(databaseName: String = getDatabaseName(), e: T) {
//        super.insert(databaseName, e)
//    }
//
//    fun insert(databaseWrapper: AndroidDatabaseWrapper, e: T) {
//        super.insert(databaseWrapper, e)
//    }
}
