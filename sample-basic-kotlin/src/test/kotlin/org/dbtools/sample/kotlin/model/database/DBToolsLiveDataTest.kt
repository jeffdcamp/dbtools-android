package org.dbtools.sample.kotlin.model.database

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.experimental.runBlocking
import org.dbtools.android.domain.DBToolsLiveData
import org.dbtools.sample.kotlin.model.database.main.MainDatabaseManagers
import org.dbtools.sample.kotlin.model.database.main.individual.Individual
import org.dbtools.sample.kotlin.model.database.main.individual.IndividualManager
import org.dbtools.sample.kotlin.model.database.main.individualdata.IndividualData
import org.dbtools.sample.kotlin.model.database.main.individualdata.IndividualDataManager
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

class DBToolsLiveDataTest {

    private lateinit var individualManager: IndividualManager
    private lateinit var individualDataManager: IndividualDataManager

    @Suppress("unused") // Required to run test on main thread
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        val databaseConfig = TestMainDatabaseConfig("LiveDataTest.db")
        databaseConfig.deleteAllDatabaseFiles()
        val databaseManager = DatabaseManager(databaseConfig)
        MainDatabaseManagers.init(databaseManager)
//        JdbcSqliteDatabaseWrapper.setEnableLogging(true) // show all statements

        individualManager = MainDatabaseManagers.individualManager
        individualDataManager = MainDatabaseManagers.individualDataManager
    }

    @Test
    fun `Test LiveData Resubscribe After Change`() = runBlocking {
        val individual1 = createIndividual("Jeff")
        val individual2 = createIndividual("Jordan")
        saveAllIndividuals(individual1, individual2)

        val liveData = DBToolsLiveData.toLiveData(individualManager) {
            individualManager.findAll()
        }

        val list1 = liveData.awaitValue(ignoreFirstValue = false) ?: error("No Data 1")
        assertTrue("List1 contains Jeff", list1.any { matches(it, individual1) })
        assertTrue("List1 contains Jordan", list1.any { matches(it, individual2) })

        individual1.lastName = "Campbell"
        individual2.lastName = "Hansen"
        saveAllIndividuals(individual1, individual2)

        val list2 = liveData.awaitValue()
        assertTrue("List2 contains Jeff Campbell", list2?.any { matches(it, individual1) } == true)
        assertTrue("List2 contains Jordan Hansen", list2?.any { matches(it, individual2) } == true)
    }

    @Test
    fun `Test LiveData Resubscribe After Change With Multiple Managers`() = runBlocking {
        val individual1 = createIndividual("Jeff")
        val individual2 = createIndividual("Jordan")
        saveAllIndividuals(individual1, individual2)

        val liveData = DBToolsLiveData.toLiveData(listOf(individualManager, individualDataManager)) {
            individualManager.findAll()
        }

        val list1 = liveData.awaitValue(ignoreFirstValue = false) ?: error("No Data 1")
        assertTrue("List1 contains Jeff", list1.any { matches(it, individual1) })
        assertTrue("List1 contains Jordan", list1.any { matches(it, individual2) })

        individual1.lastName = "Campbell"
        individual2.lastName = "Hansen"
        saveAllIndividuals(individual1, individual2)

        val list2 = liveData.awaitValue() ?: error("No Data 2")
        assertTrue("List2 contains Jeff Campbell", list2.any { matches(it, individual1) })
        assertTrue("List2 contains Jordan Hansen", list2.any { matches(it, individual2) })

        individualDataManager.save(IndividualData().apply {
            externalId = 123L
            name = "Brett"
        })

        val list3 = liveData.awaitValue() ?: error("No Data 3")
        assertTrue("List3 contains Jeff Campbell", list3.any { matches(it, individual1) })
        assertTrue("List3 contains Jordan Hansen", list3.any { matches(it, individual2) })
        assertTrue("List3 Doesn't Match List2", list3.minus(list2).size == 2)
    }

    private fun createIndividual(name: String): Individual {
        return Individual().apply {
            firstName = name
        }
    }

    private fun saveAllIndividuals(vararg individuals: Individual, manager: IndividualManager = individualManager) {
        manager.beginTransaction()
        var success = true
        try {
            success = individuals.fold(success) { result, individual -> result && manager.save(individual) }
        } finally {
            manager.endTransaction(success)
        }
    }

    private fun matches(one: Individual, two: Individual): Boolean {
        return one.id == two.id && one.firstName == two.firstName && one.lastName == two.lastName
    }

}

@Throws(InterruptedException::class)
fun <T> LiveData<T>.awaitValue(ignoreFirstValue: Boolean = true, secondsToWait: Long = 2L): T? {
    var value: T? = null
    val latch = CountDownLatch(1)
    val observer = object : Observer<T> {
        var ignore = ignoreFirstValue
        override fun onChanged(v: T?) {
            if (ignore) {
                ignore = false
                return
            }
            value = v
            latch.countDown()
            removeObserver(this)
        }
    }
    observeForever(observer)
    latch.await(secondsToWait, TimeUnit.SECONDS)
    return value
}