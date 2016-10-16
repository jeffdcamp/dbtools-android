package org.dbtools.sample.kotlin.model.database.main.individual

import org.dbtools.query.sql.SQLQueryBuilder
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

    @Test
    @Throws(Exception::class)
    fun testFindBy() {
        val databaseConfig = TestMainDatabaseConfig("java-test-individual.db")
        databaseConfig.deleteAllDatabaseFiles()
        val databaseManager = DatabaseManager(databaseConfig)
        MainDatabaseManagers.init(databaseManager)
        val individualManager = MainDatabaseManagers.individualManager ?: throw IllegalStateException("IndividualManager null")

        val ind1 = addIndividual("Darth", "Vader", 1, 1.1, true)
        val ind2 = addIndividual("Boba", "Fett", 2, 2.2, false)
        val ind3 = addIndividual("Luke", "Skywalker", 3, 3.3, true)
        val ind4 = addIndividual("Leia", "Organa", 4, 4.4, true)
        val ind5 = addIndividual("Han", "Solo", 5, 5.5, false)

        assertEquals("findCount", 5, individualManager.findCount())
        assertEquals("findCountByRawQuery", 5, individualManager.findCountByRawQuery("SELECT count(1) FROM " + IndividualConst.TABLE))
        assertEquals("findCountBySelection", 3, individualManager.findCountBySelection(IndividualConst.C_ENABLED + " = ?", SQLQueryBuilder.toSelectionArgs(1)))
        assertEquals("findByRawQuery", ind5.id, individualManager.findByRawQuery("SELECT * FROM " + IndividualConst.TABLE + " WHERE " + IndividualConst.C_ID + " = ?", SQLQueryBuilder.toSelectionArgs(ind5.id))!!.id)
        assertEquals("findAllByRawQuery", 3, individualManager.findAllByRawQuery("SELECT * FROM " + IndividualConst.TABLE + " WHERE " + IndividualConst.C_ENABLED + " = ?", SQLQueryBuilder.toSelectionArgs(1)).size.toLong())
        assertEquals("findValueByRawQuery", "Darth", individualManager.findValueByRawQuery(String::class.java, "SELECT " + IndividualConst.C_FIRST_NAME + " FROM " + IndividualConst.TABLE + " WHERE " + IndividualConst.C_ID + " = ?", SQLQueryBuilder.toSelectionArgs(1), ""))
        assertEquals("findAll", 5, individualManager.findAll().size.toLong())
        assertEquals("findByRowId", 1, individualManager.findByRowId(ind1.id)!!.id)
        assertEquals("findBySelection", ind1.id, individualManager.findBySelection(IndividualConst.C_ENABLED + " = ?", SQLQueryBuilder.toSelectionArgs(1), null)!!.id)
        assertEquals("findAllByRowIds", 2, individualManager.findAllByRowIds(longArrayOf(ind1.id, ind2.id, 99999)).size.toLong())
        assertEquals("findAllBySelection", 3, individualManager.findAllBySelection(IndividualConst.C_ENABLED + " = ?", SQLQueryBuilder.toSelectionArgs(1)).size.toLong())

        val integerList = individualManager.findAllValuesBySelection(Int::class.java, IndividualConst.C_NUMBER, IndividualConst.C_ENABLED + " = ?", SQLQueryBuilder.toSelectionArgs(1))
        assertEquals("findAllValuesBySelection1", 3, integerList.size.toLong())
        assertEquals("findAllValuesBySelection1 value check", ind3.number, integerList.get(1))

        val value = individualManager.findValueBySelection(Int::class.java, IndividualConst.C_NUMBER, ind4.id, 99)
        assertEquals("findAllValuesBySelection2 value check", ind4.number, value)

        val orderedList = individualManager.findAllOrderBy(IndividualConst.C_LAST_NAME)
        assertEquals("findAllOrderBy", 5, orderedList.size.toLong())
        assertEquals("findAllOrderBy value check", ind1.lastName, orderedList.get(4).lastName)

        assertEquals("tableExists", true, individualManager.tableExists(IndividualConst.TABLE))
        assertEquals("tableExists false", false, individualManager.tableExists("BadTableName"))

    }

    private fun addIndividual(first: String, last: String, number: Int, amount: Double, enabled: Boolean): Individual {
        val individualManager = MainDatabaseManagers.individualManager ?: throw IllegalStateException("IndividualManager null")
        val individual = Individual()
        individual.firstName = first
        individual.lastName = last
        //        individual.setSampleDateTime(new GregorianCalendar(1970,1,1).getTime());
        //        individual.setBirthDate(new GregorianCalendar(1970,1,2).getTime());
        //        individual.setLastModified(new GregorianCalendar(1970,1,3).getTime());
        individual.number = number
        //        individual.setPhone("555-555-1234");
        //        individual.setEmail("test@test.com");
        //        individual.setAmount1(amount);
        individual.amount2 = amount
        individual.enabled = enabled

        individualManager.save(individual)

        return individual
    }
}