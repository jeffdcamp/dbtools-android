package org.dbtools.sample.kotlin.model.database

import org.dbtools.android.domain.AndroidDatabase
import org.dbtools.android.domain.DBToolsTableChangeListener
import org.dbtools.android.domain.config.BuildEnv
import org.dbtools.android.domain.config.TestDatabaseConfig
import org.dbtools.android.domain.database.JdbcSqliteDatabaseWrapper
import org.dbtools.sample.kotlin.model.database.main.MainDatabaseManagers
import org.dbtools.sample.kotlin.model.database.main.individual.Individual
import org.dbtools.sample.kotlin.model.database.main.individual.IndividualManager
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.util.*
import java.util.concurrent.atomic.AtomicInteger

class MultiDatabaseTableChangeListenerTest {

    private val insertEventCount = AtomicInteger(0)
    private val updateEventCount = AtomicInteger(0)
    private val deleteEventCount = AtomicInteger(0)
    private val bulkOperationCount = AtomicInteger(0)

    private lateinit var individualManager: IndividualManager

    private val databaseName1 = DatabaseManagerConst.MAIN_DATABASE_NAME + "1"
    private val databaseName2 = DatabaseManagerConst.MAIN_DATABASE_NAME + "2"

    @Before
    @Throws(Exception::class)
    fun setUp() {
        val androidDatabases = ArrayList<AndroidDatabase>()
        androidDatabases.add(AndroidDatabase(databaseName1, BuildEnv.GRADLE.testDbDir + databaseName1 + ".db", DatabaseManager.MAIN_TABLES_VERSION, DatabaseManager.MAIN_VIEWS_VERSION))
        androidDatabases.add(AndroidDatabase(databaseName2, BuildEnv.GRADLE.testDbDir + databaseName2 + ".db", DatabaseManager.MAIN_TABLES_VERSION, DatabaseManager.MAIN_VIEWS_VERSION))

        val databaseConfig = TestDatabaseConfig(androidDatabases)
        databaseConfig.deleteAllDatabaseFiles()
        val databaseManager = MultiDatabaseManager(databaseConfig)
        MainDatabaseManagers.init(databaseManager)
        JdbcSqliteDatabaseWrapper.setEnableLogging(true) // show all statements

        individualManager = MainDatabaseManagers.individualManager

        // setup table change listener
        individualManager.addTableChangeListener(DBToolsTableChangeListener { tableChange ->
            if (tableChange.isInsert) {
                insertEventCount.incrementAndGet()
            } else if (tableChange.isUpdate) {
                updateEventCount.incrementAndGet()
            } else if (tableChange.isDelete) {
                deleteEventCount.incrementAndGet()
            }

            if (tableChange.isBulkOperation) {
                bulkOperationCount.incrementAndGet()
            }
        }, databaseName1)
    }

    @Test
    @Throws(Exception::class)
    fun testRecordCrud() {
        // === INSERT ===
        val individual1 = createIndividual("Jeff")
        individualManager.save(individual1, databaseName1)

        val individual2 = createIndividual("Jordan")
        individualManager.save(individual2, databaseName2)

        // test save
        assertEquals(1, individualManager.findCount(databaseName1))
        assertEquals(1, individualManager.findCount(databaseName2))
        assertCountValuesDatabase1(1, 0, 0)

        // === UPDATE ===
        individual1.firstName = "Jeffery"
        individualManager.save(individual1, databaseName1)

        individual2.firstName = "G"
        individualManager.save(individual2, databaseName2)

        // test ONLY change on database1
        assertCountValuesDatabase1(1, 1, 0)

        // === DELETE ===
        individualManager.delete(individual1, databaseName1)
        assertEquals(0, individualManager.findCount(databaseName1))

        individualManager.delete(individual2, databaseName2)
        assertEquals(0, individualManager.findCount(databaseName2))

        assertCountValuesDatabase1(1, 1, 1)
    }

    @Test
    @Throws(Exception::class)
    fun testTransaction() {
        val individual1 = createIndividual("Jeff")
        val individual2 = createIndividual("Ty")


        val individual3 = createIndividual("Bob")
        val individual4 = createIndividual("Sam")

        // === INSERT ===
        // DB1
        individualManager.beginTransaction(databaseName1)
        individualManager.save(individual1, databaseName1)
        individualManager.save(individual2, databaseName1)
        individualManager.endTransaction(true, databaseName1)

        // DB2
        individualManager.beginTransaction(databaseName2)
        individualManager.save(individual3, databaseName2)
        individualManager.save(individual4, databaseName2)
        individualManager.endTransaction(true, databaseName2)

        // test
        assertEquals(2, individualManager.findCount(databaseName1))
        assertEquals(2, individualManager.findCount(databaseName2))

        assertCountValuesDatabase1(0, 0, 0)
        assertBulkOperationDatabase1(1)

        // === UPDATE ===
        individualManager.beginTransaction(databaseName1)
        individualManager.update(individual1, databaseName1)
        individualManager.update(individual2, databaseName1)
        individualManager.endTransaction(true, databaseName1)

        individualManager.beginTransaction(databaseName2)
        individualManager.update(individual3, databaseName2)
        individualManager.update(individual4, databaseName2)
        individualManager.endTransaction(true, databaseName2)

        // test
        assertEquals(2, individualManager.findCount(databaseName1))
        assertEquals(2, individualManager.findCount(databaseName2))

        assertCountValuesDatabase1(0, 0, 0)
        assertBulkOperationDatabase1(2)


        // === DELETE ===
        individualManager.beginTransaction(databaseName1)
        individualManager.delete(individual1, databaseName1)
        individualManager.delete(individual2, databaseName1)
        individualManager.endTransaction(true, databaseName1)

        individualManager.beginTransaction(databaseName2)
        individualManager.delete(individual3, databaseName2)
        individualManager.delete(individual4, databaseName2)
        individualManager.endTransaction(true, databaseName2)

        // test
        assertEquals(0, individualManager.findCount(databaseName1))
        assertEquals(0, individualManager.findCount(databaseName2))
        assertCountValuesDatabase1(0, 0, 0)
        assertBulkOperationDatabase1(3)
    }

    private fun createIndividual(name: String): Individual {
        val individual = Individual()
        individual.firstName = name
        return individual
    }

    private fun assertBulkOperationDatabase1(expectedCount: Int) {
        assertEquals("Bulk Operation", expectedCount.toLong(), bulkOperationCount.get().toLong())
    }

    private fun assertCountValuesDatabase1(expectedInsertEventCount: Int, expectedUpdateEventCount: Int, expectedDeleteEventCount: Int) {
        assertEquals("Insert Event Count", expectedInsertEventCount.toLong(), insertEventCount.get().toLong())
        assertEquals("Update Event Count", expectedUpdateEventCount.toLong(), updateEventCount.get().toLong())
        assertEquals("Delete Event Count", expectedDeleteEventCount.toLong(), deleteEventCount.get().toLong())
    }
}