@file:Suppress("MemberVisibilityCanPrivate")

package org.dbtools.android.domain

import android.arch.lifecycle.LiveData
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.launch
import kotlin.coroutines.experimental.CoroutineContext

@Suppress("unused")
object DBToolsLiveData {
    fun <T> toLiveData(func: suspend () -> T): LiveData<T> {
        return toLiveData(CommonPool, null, func)
    }

    fun <T> toLiveData(coroutineContext: CoroutineContext, func: suspend () -> T): LiveData<T> {
        return toLiveData(coroutineContext, null, func)
    }

    fun <T, M: KotlinAndroidBaseManager<*>> toLiveData(tableChangeManager: M?, func: suspend () -> T): LiveData<T> {
        return toLiveData(CommonPool, tableChangeManager, func)
    }

    fun <T, M: KotlinAndroidBaseManager<*>> toLiveData(coroutineContext: CoroutineContext, tableChangeManager: M?, func: suspend () -> T): LiveData<T> {
        return object: LiveData<T>(), DBToolsTableChangeListener {

            private lateinit var job: Job

            override fun onActive() {
                job = Job()
                tableChangeManager?.addTableChangeListener(this)
                getData()
            }

            override fun onInactive() {
                tableChangeManager?.removeTableChangeListener(this)
                job.cancel()
            }

            override fun onTableChange(tableChange: DatabaseTableChange?) {
                job.cancel()
                job = Job()
                getData()
            }

            private fun getData() {
                launch(coroutineContext + job) {
                    val value = func()
                    postValue(value)
                }
            }
        }
    }
}