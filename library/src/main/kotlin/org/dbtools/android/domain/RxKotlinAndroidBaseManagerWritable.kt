package org.dbtools.android.domain

abstract class RxKotlinAndroidBaseManagerWritable<T : AndroidBaseRecord> : RxAndroidBaseManagerWritable<T>() {
    fun inTransaction(func: () -> Boolean) {
        var success = false
        try {
            beginTransaction()
            success = func()
        } finally {
            endTransaction(success)
        }
    }
}
