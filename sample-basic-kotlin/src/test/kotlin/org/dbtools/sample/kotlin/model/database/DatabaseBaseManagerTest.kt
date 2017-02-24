package org.dbtools.sample.kotlin.model.database

import org.dbtools.android.domain.DBToolsTableChangeListener
import org.dbtools.sample.kotlin.model.database.main.MainDatabaseManagers
import org.dbtools.sample.kotlin.model.database.main.individual.Individual
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.io.File

class DatabaseBaseManagerTest {

    private lateinit var databaseManager: DatabaseManager

    @Before
    @Throws(Exception::class)
    fun setUp() {
        val databaseConfig = TestMainDatabaseConfig("java-test-individual.db")
        databaseConfig.deleteAllDatabaseFiles()
        databaseManager = DatabaseManager(databaseConfig)
        MainDatabaseManagers.init(databaseManager)
    }

    /**
     * @return swap database File
     */
    private fun setupSwap(): File {
        // create a database to swap with
        val individualManager = MainDatabaseManagers.individualManager

        val ind1 = Individual()
        ind1.firstName = "A"

        individualManager.save(ind1)

        assertEquals(1, individualManager.findCount())
        assertEquals("A", individualManager.findAll()[0].firstName)

        // close the database
        databaseManager.closeDatabase(DatabaseManagerConst.MAIN_DATABASE_NAME)
        val databaseAPath = databaseManager.getDatabase(DatabaseManagerConst.MAIN_DATABASE_NAME)!!.path

        // move the database file to a swap database <database path>2
        val databaseAFile = File(databaseAPath)
        val swapDatabaseFile = File(databaseAPath + "2")
        assertTrue(databaseAFile.renameTo(swapDatabaseFile))


        // start to write again... which will create a new database
        val ind2 = Individual()
        ind2.firstName = "B"
        individualManager.save(ind2)

        val ind3 = Individual()
        ind3.firstName = "C"
        individualManager.save(ind3)

        // Make sure the new database exists
        val individua2lList = individualManager.findAll()
        assertEquals(2, individua2lList.size.toLong())
        assertEquals("B", individua2lList[0].firstName)
        assertEquals("C", individua2lList[1].firstName)

        return swapDatabaseFile
    }

    @Test
    fun testSwap() {
        val individualManager = MainDatabaseManagers.individualManager
        val swapDatabaseFile = setupSwap()

        assertTrue(databaseManager.swapDatabase(DatabaseManagerConst.MAIN_DATABASE_NAME, swapDatabaseFile))

        assertEquals(1, individualManager.findCount())
        assertEquals("A", individualManager.findAll()[0].firstName)
    }

    @Test
    fun testSwapInTransaction() {
        val individualManager = MainDatabaseManagers.individualManager
        val swapDatabaseFile = setupSwap()

        individualManager.beginTransaction()

        assertTrue(databaseManager.swapDatabase(DatabaseManagerConst.MAIN_DATABASE_NAME, swapDatabaseFile))

        individualManager.endTransaction(true)

        assertEquals(1, individualManager.findCount())
        assertEquals("A", individualManager.findAll()[0].firstName)
    }

    @Test
    fun testSwapErrors() {
        val individualManager = MainDatabaseManagers.individualManager
        val swapDatabaseFile = setupSwap()

        assertFalse(databaseManager.swapDatabase(DatabaseManagerConst.MAIN_DATABASE_NAME, File("/data/bad-file.db")))
        assertFalse(databaseManager.swapDatabase("BAD_DATABASE_NAME", swapDatabaseFile))

        // original database should NOT have changed
        val individua2lList = individualManager.findAll()
        assertEquals(2, individua2lList.size.toLong())
        assertEquals("B", individua2lList[0].firstName)
        assertEquals("C", individua2lList[1].firstName)
    }

    private var listenerMessageCount: Int = 0 // listener
    @Test
    fun testSwapListener() {
        listenerMessageCount = 0

        val individualManager = MainDatabaseManagers.individualManager
        individualManager.addTableChangeListener(DBToolsTableChangeListener { tableChange ->
            println("Table Changed: " + tableChange.table + " ID: " + tableChange.rowId)
            listenerMessageCount++
        })

        val swapDatabaseFile = setupSwap()

        val lastListenerCount = listenerMessageCount

        assertTrue(databaseManager.swapDatabase(DatabaseManagerConst.MAIN_DATABASE_NAME, swapDatabaseFile))

        val newIndividual = Individual()
        newIndividual.firstName = "New"
        individualManager.save(newIndividual)

        assertEquals((lastListenerCount + 1).toLong(), listenerMessageCount.toLong())
    }
}