@file:Suppress("MemberVisibilityCanPrivate")

package org.dbtools.android.domain

import android.arch.lifecycle.LiveData
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.launch
import java.util.concurrent.atomic.AtomicBoolean
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
    fun <T, M : KotlinAndroidBaseManager<*>> toLiveData(tableChangeManager: M?, func: suspend () -> T): LiveData<T> {
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
    fun <T, M : KotlinAndroidBaseManager<*>> toLiveData(coroutineContext: CoroutineContext, tableChangeManager: M?, func: suspend () -> T): LiveData<T> {
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
    fun <T, M : KotlinAndroidBaseManager<*>> toLiveData(coroutineContext: CoroutineContext, tableChangeManagers: List<M>?, func: suspend () -> T): LiveData<T> {
        return toLiveDataInternal(coroutineContext, null, tableChangeManagers, func)
    }

    private fun <T, M : KotlinAndroidBaseManager<*>> toLiveDataInternal(coroutineContext: CoroutineContext, tableChangeManager: M?, tableChangeManagers: List<M>?, func: suspend () -> T): LiveData<T> {
        return object : LiveData<T>(), DBToolsTableChangeListener {
            private val computing = AtomicBoolean(false)
            private val invalid = AtomicBoolean(true)
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
                invalid.set(true)
                getData()
            }

            private fun getData() {
                launch(coroutineContext, parent = job) {
                    var computed: Boolean
                    do {
                        computed = false
                        if (computing.compareAndSet(false, true)) {
                            try {
                                var value: T? = null
                                while (invalid.compareAndSet(true, false)) {
                                    computed = true
                                    value = func()
                                }
                                if (computed) {
                                    postValue(value)
                                }
                            } finally {
                                // release compute lock
                                computing.set(false)
                            }
                        }

                        // Why all the ugly code: (Copied from ComputableLiveData)
                        // check invalid after releasing compute lock to avoid the following scenario.
                        // Thread A runs compute()
                        // Thread A checks invalid, it is false
                        // Main thread sets invalid to true
                        // Thread B runs, fails to acquire compute lock and skips
                        // Thread A releases compute lock
                        // We've left invalid in set state. The check below recovers.
                    } while (computed && invalid.get())
                }
            }
        }
    }
}