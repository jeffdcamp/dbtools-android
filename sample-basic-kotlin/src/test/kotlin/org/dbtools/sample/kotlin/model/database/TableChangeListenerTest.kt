package org.dbtools.sample.kotlin.model.database

import org.dbtools.android.domain.DBToolsTableChangeListener
import org.dbtools.android.domain.database.JdbcSqliteDatabaseWrapper
import org.dbtools.sample.kotlin.model.database.main.MainDatabaseManagers
import org.dbtools.sample.kotlin.model.database.main.individual.Individual
import org.dbtools.sample.kotlin.model.database.main.individual.IndividualConst
import org.dbtools.sample.kotlin.model.database.main.individual.IndividualManager
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.util.concurrent.atomic.AtomicInteger

class TableChangeListenerTest {

    private val insertEventCount = AtomicInteger(0)
    private val updateEventCount = AtomicInteger(0)
    private val deleteEventCount = AtomicInteger(0)

    private var individualManager: IndividualManager? = null

    @Before
    @Throws(Exception::class)
    fun setUp() {
        val databaseConfig = TestMainDatabaseConfig("kotlin-test-tablechange.db")
        databaseConfig.deleteAllDatabaseFiles()
        val databaseManager = DatabaseManager(databaseConfig)
        MainDatabaseManagers.init(databaseManager)
        JdbcSqliteDatabaseWrapper.setEnableLogging(true) // show all statements

        individualManager = MainDatabaseManagers.individualManager

        // setup table change listener
        individualManager!!.addTableChangeListener(DBToolsTableChangeListener { tableChange ->
            if (tableChange.isInsert) {
                insertEventCount.incrementAndGet()
            } else if (tableChange.isUpdate) {
                updateEventCount.incrementAndGet()
            } else if (tableChange.isDelete) {
                deleteEventCount.incrementAndGet()
            }
        })
    }

    @Test
    @Throws(Exception::class)
    fun testRecordCrud() {
        // === INSERT ===
        val individual = createIndividual("Jeff")
        individualManager!!.save(individual)

        // test save
        assertEquals(1, individualManager!!.findCount())
        assertCountValues(1, 0, 0)

        // === UPDATE ===
        individual.firstName = "Jeffery"
        individualManager!!.save(individual)
        assertCountValues(1, 1, 0)

        // === DELETE ===
        individualManager!!.delete(individual)
        assertEquals(0, individualManager!!.findCount())
        assertCountValues(1, 1, 1)
    }

    @Test
    @Throws(Exception::class)
    fun testTransaction() {
        val individual1 = createIndividual("Jeff")
        val individual2 = createIndividual("Ty")
        val individual3 = createIndividual("Bob")
        val individual4 = createIndividual("Sam")

        // === INSERT ===
        individualManager!!.beginTransaction()

        individualManager!!.save(individual1)
        individualManager!!.save(individual2)
        individualManager!!.save(individual3)
        individualManager!!.save(individual4)

        individualManager!!.endTransaction(true)

        // test
        assertEquals(4, individualManager!!.findCount())
        assertCountValues(1, 0, 0)

        // === UPDATE ===
        individualManager!!.beginTransaction()

        individualManager!!.update(individual1)
        individualManager!!.update(individual2)
        individualManager!!.update(individual3)
        individualManager!!.update(individual4)

        individualManager!!.endTransaction(true)

        // test
        assertEquals(4, individualManager!!.findCount())
        assertCountValues(1, 1, 0)


        // === DELETE ===
        individualManager!!.beginTransaction()

        individualManager!!.delete(individual1)
        individualManager!!.delete(individual2)
        individualManager!!.delete(individual3)
        individualManager!!.delete(individual4)

        individualManager!!.endTransaction(true)

        // test
        assertEquals(0, individualManager!!.findCount())
        assertCountValues(1, 1, 1)
    }

    @Test
    @Throws(Exception::class)
    fun testUpdate() {
        // === INSERT ===
        val individual = createIndividual("Jeff")
        individualManager!!.save(individual)

        // test save
        assertEquals(1, individualManager!!.findCount())
        assertCountValues(1, 0, 0)

        // === Record UPDATE ===
        individual.firstName = "Jeff1"
        individualManager!!.save(individual)

        assertCountValues(1, 1, 0)

        // == Update single column
        val contentValues = individualManager!!.createNewDBToolsContentValues()
        contentValues.put(IndividualConst.C_FIRST_NAME, "Jeff2")
        individualManager!!.update(contentValues, individual.id)

        assertCountValues(1, 2, 0)

        // Update a record that does not exist (no event should trigger)
        contentValues.put(IndividualConst.C_FIRST_NAME, "Jeff2")
        individualManager!!.update(contentValues, 1000) // 1000 is a primary key that should NEVER exist in this test

        assertCountValues(1, 2, 0)

    }

    @Test
    @Throws(Exception::class)
    fun testDelete() {
        // === INSERT ===
        val individual1 = createIndividual("Jeff")
        individualManager!!.save(individual1)
        val individual2 = createIndividual("Ty")
        individualManager!!.save(individual2)

        // test save
        assertEquals(2, individualManager!!.findCount())
        assertCountValues(2, 0, 0)

        // Basic delete
        individualManager!!.delete(individual1.id)
        assertCountValues(2, 0, 1)

        // Delete all
        individualManager!!.deleteAll()
        assertCountValues(2, 0, 2)

        // Delete all (with NO records... no new event should trigger)
        individualManager!!.deleteAll()
        assertCountValues(2, 0, 2)
    }


    private fun createIndividual(name: String): Individual {
        val individual = Individual()
        individual.firstName = name
        return individual
    }

    private fun assertCountValues(expectedInsertEventCount: Int, expectedUpdateEventCount: Int, expectedDeleteEventCount: Int) {
        assertEquals("Insert Event Count", expectedInsertEventCount.toLong(), insertEventCount.get().toLong())
        assertEquals("Update Event Count", expectedUpdateEventCount.toLong(), updateEventCount.get().toLong())
        assertEquals("Delete Event Count", expectedDeleteEventCount.toLong(), deleteEventCount.get().toLong())
    }
}