package org.dbtools.android.domain

abstract class RxKotlinAndroidBaseManagerWritable<T : AndroidBaseRecord> : RxAndroidBaseManagerWritable<T>() {
    inline fun inTransaction(func: () -> Boolean) {
        var success = false
        try {
            beginTransaction()
            success = func()
        } finally {
            endTransaction(success)
        }
    }
}
