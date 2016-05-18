package org.dbtools.sample.kotlin.model.database.main.individual

import org.dbtools.sample.kotlin.model.database.DatabaseManager
import org.dbtools.sample.kotlin.model.database.TestMainDatabaseConfig
import org.dbtools.sample.kotlin.model.database.main.MainDatabaseManagers
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.*

class IndividualTest {
    @Test
    @Throws(Exception::class)
    fun testIndividual() {
        // SETUP
        val databaseConfig = TestMainDatabaseConfig("kotlin-test-individual.db")
        databaseConfig.deleteAllDatabaseFiles()
        val databaseManager = DatabaseManager(databaseConfig)
        MainDatabaseManagers.init(databaseManager)
        val individualManager = MainDatabaseManagers.individualManager ?: throw IllegalStateException("IndividualManager null")

        // === CREATE / INSERT ===
        val individual = Individual()
        individual.firstName = "Jeff"
        individual.lastName = "Campbell"
        individual.sampleDateTime = GregorianCalendar(1970, 1, 1).time
        individual.birthDate = GregorianCalendar(1970, 1, 2).time
        individual.lastModified = GregorianCalendar(1970, 1, 3).time
        individual.number = 1234
        individual.phone = "555-555-1234"
        individual.email = "test@test.com"
        individual.amount1 = 19.95F
        individual.amount2 = 1000000000.25
        individual.enabled = true
        individualManager.save(individual)

        // test save
        assertEquals(1, individualManager.findCount())

        // read save... verify values
        val testIndividual = individualManager.findByRowId(individual.id);
        if (testIndividual == null) {
            throw IllegalStateException("Could not find test individual")
        }
        assertEquals("First", individual.firstName, testIndividual.firstName)
        assertEquals("Last", individual.lastName, testIndividual.lastName)
        assertEquals("Sample Date", individual.sampleDateTime!!.time, testIndividual.sampleDateTime!!.time)
        assertEquals("Sample Date", individual.birthDate!!.time, testIndividual.birthDate!!.time)
        assertEquals("Sample Date", individual.lastModified!!.time, testIndividual.lastModified!!.time)
        assertEquals("Number", individual.number, testIndividual.number)
        assertEquals("Phone", individual.phone, testIndividual.phone)
        assertEquals("Email", individual.email, testIndividual.email)
        assertEquals("Enabled", individual.enabled, testIndividual.enabled)


        // === UPDATE ===
        individual.firstName = "Jeffery"
        individualManager.save(individual)

        val dbFirstName = individualManager.findValueBySelection(String::class.java, IndividualConst.C_FIRST_NAME, IndividualConst.C_ID + " = " + individual.id, null, "")
        assertEquals("Jeffery", dbFirstName)

        // === DELETE ===
        individualManager.delete(individual)
        assertEquals(0, individualManager.findCount())
    }
}