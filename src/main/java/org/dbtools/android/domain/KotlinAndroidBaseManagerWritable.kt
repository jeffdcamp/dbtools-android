package org.dbtools.android.domain

abstract class KotlinAndroidBaseManagerWritable<T : AndroidBaseRecord> : AndroidBaseManagerWritable<T>() {
    fun inTransaction(func: () -> Boolean) {
        beginTransaction()
        endTransaction(func())
    }
}
