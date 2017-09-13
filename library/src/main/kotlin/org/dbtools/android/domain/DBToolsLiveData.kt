@file:Suppress("MemberVisibilityCanPrivate")

package org.dbtools.android.domain

import android.arch.lifecycle.LiveData
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.launch
import kotlin.coroutines.experimental.CoroutineContext

@Suppress("unused")
object DBToolsLiveData {
    /**
     * Return data retrieved via func parameter as LiveData on the CommonPool thread
     *
     * @return LiveData<T>
     */
    fun <T> toLiveData(func: suspend () -> T): LiveData<T> {
        return toLiveDataInternal(CommonPool, null, null, func)
    }

    /**
     * Return data retrieved via func parameter as LiveData
     *
     * @param coroutineContext Thread in which func is executed on
     * @param func Function that is executed to get data
     *
     * @return LiveData<T>
     */
    fun <T> toLiveData(coroutineContext: CoroutineContext, func: suspend () -> T): LiveData<T> {
        return toLiveDataInternal(coroutineContext, null, null, func)
    }

    /**
     * Return data retrieved via func parameter as LiveData on the CommonPool thread
     *
     * @param tableChangeManager Table that will cause this LiveData to be triggered
     * @param func Function that is executed to get data
     *
     * @return LiveData<T>
     */
    fun <T, M: KotlinAndroidBaseManager<*>> toLiveData(tableChangeManager: M?, func: suspend () -> T): LiveData<T> {
        return toLiveDataInternal(CommonPool, tableChangeManager, null, func)
    }

    /**
     * Return data retrieved via func parameter as LiveData
     *
     * @param coroutineContext Thread in which func is executed on
     * @param tableChangeManager Table that will cause this LiveData to be triggered
     * @param func Function that is executed to get data
     *
     * @return LiveData<T>
     */
    fun <T, M: KotlinAndroidBaseManager<*>> toLiveData(coroutineContext: CoroutineContext, tableChangeManager: M?, func: suspend () -> T): LiveData<T> {
        return toLiveDataInternal(coroutineContext, tableChangeManager, null, func)
    }

    /**
     * Return data retrieved via func parameter as LiveData
     *
     * @param coroutineContext Thread in which func is executed on
     * @param tableChangeManagers Tables that will cause this LiveData to be triggered
     * @param func Function that is executed to get data
     *
     * @return LiveData<T>
     */
    fun <T, M: KotlinAndroidBaseManager<*>> toLiveData(coroutineContext: CoroutineContext, tableChangeManagers: List<M>?, func: suspend () -> T): LiveData<T> {
        return toLiveDataInternal(coroutineContext, null, tableChangeManagers, func)
    }

    private fun <T, M: KotlinAndroidBaseManager<*>> toLiveDataInternal(coroutineContext: CoroutineContext, tableChangeManager: M?, tableChangeManagers: List<M>?, func: suspend () -> T): LiveData<T> {
        return object: LiveData<T>(), DBToolsTableChangeListener {

            private lateinit var job: Job

            override fun onActive() {
                job = Job()

                tableChangeManager?.addTableChangeListener(this)
                tableChangeManagers?.forEach { it.addTableChangeListener(this) }
                getData()
            }

            override fun onInactive() {
                tableChangeManager?.removeTableChangeListener(this)
                tableChangeManagers?.forEach { it.removeTableChangeListener(this) }
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