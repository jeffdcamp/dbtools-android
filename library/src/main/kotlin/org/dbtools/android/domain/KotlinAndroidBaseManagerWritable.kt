package org.dbtools.android.domain

abstract class KotlinAndroidBaseManagerWritable<T : AndroidBaseRecord> : AndroidBaseManagerWritable<T>() {
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
