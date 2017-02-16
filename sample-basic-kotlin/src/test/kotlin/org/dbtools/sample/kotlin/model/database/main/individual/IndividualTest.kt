package org.dbtools.sample.kotlin.model.database.main.individual

import org.dbtools.android.domain.database.JdbcSqliteDatabaseWrapper
import org.dbtools.query.sql.SQLQueryBuilder
import org.dbtools.sample.kotlin.model.database.DatabaseManager
import org.dbtools.sample.kotlin.model.database.TestMainDatabaseConfig
import org.dbtools.sample.kotlin.model.database.main.MainDatabaseManagers
import org.dbtools.sample.kotlin.model.database.main.individualdata.IndividualData
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.util.*

class IndividualTest {
    @Before
    fun setUp() {
        // SETUP
        val databaseConfig = TestMainDatabaseConfig("kotlin-test-individual.db")
        databaseConfig.deleteAllDatabaseFiles()
        val databaseManager = DatabaseManager(databaseConfig)
        MainDatabaseManagers.init(databaseManager)
    }

    @Test
    @Throws(Exception::class)
    fun testIndividual() {
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

        val dbFirstName = individualManager.findValueBySelection(String::class.java, IndividualConst.C_FIRST_NAME,  "", IndividualConst.C_ID + " = " + individual.id, null)
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
        assertEquals("findBySelection", ind1.id, individualManager.findBySelection(selection = IndividualConst.C_ENABLED + " = ?", selectionArgs = SQLQueryBuilder.toSelectionArgs(1))!!.id)
        assertEquals("findAllByRowIds", 2, individualManager.findAllByRowIds(longArrayOf(ind1.id, ind2.id, 99999)).size.toLong())
        assertEquals("findAllBySelection", 3, individualManager.findAllBySelection(selection = IndividualConst.C_ENABLED + " = ?", selectionArgs = SQLQueryBuilder.toSelectionArgs(1)).size.toLong())

        val integerList = individualManager.findAllValuesBySelection(Int::class.java, IndividualConst.C_NUMBER, IndividualConst.C_ENABLED + " = ?", SQLQueryBuilder.toSelectionArgs(1))
        assertEquals("findAllValuesBySelection1", 3, integerList.size.toLong())
        assertEquals("findAllValuesBySelection1 value check", ind3.number, integerList.get(1))

        val value = individualManager.findValueBySelection(Int::class.java, IndividualConst.C_NUMBER, ind4.id, 99)
        assertEquals("findAllValuesBySelection2 value check", ind4.number, value)

        val orderedList = individualManager.findAll(orderBy = IndividualConst.C_LAST_NAME)
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

    @Test
    fun testNoPrimaryKeyAndUnique() {
        val individualDataManager = MainDatabaseManagers.individualDataManager ?: throw IllegalStateException("IndividualDataManager null")

        val data = IndividualData()
        data.externalId = 555
        data.typeId = 1
        data.name = "Foo"

        // save
        individualDataManager.save(data)
        assertEquals(1, individualDataManager.findAll().size.toLong())

        // save again... unique constraint should replace the existing conflict
        individualDataManager.save(data)
        assertEquals(1, individualDataManager.findAll().size.toLong())
    }

    @Test
    fun testNullColumns() {
        JdbcSqliteDatabaseWrapper.setEnableLogging(true)

        val individualManager = MainDatabaseManagers.individualManager ?: throw IllegalStateException("IndividualManager null")

        val individual1 = Individual()
        individual1.firstName = "Bob"
        individual1.phone = "555"

        val individual2 = Individual()
        individual2.firstName = "Sam"

        // Make sure all null fields save
        individualManager.save(individual1)
        individualManager.save(individual2)

        // Make sure all null fields load
        val individual1a = individualManager.findByRowId(individual1.id)
        val individual2a = individualManager.findByRowId(individual2.id)

        assertNotNull(individual1a)
        assertNotNull(individual2a)
        if (individual1a == null || individual2a == null) {
            return
        }

        assertEquals(individual1a.firstName, "Bob")
        assertEquals(individual1a.lastName, "") // not null column
        assertEquals(individual1a.phone, "555") // null column
        assertNull(individual1a.number)

        assertEquals(individual2a.firstName, "Sam")
        assertEquals(individual2a.lastName, "") // not null column
        assertNull(individual2a.phone) // null column.. Make sure statement does NOT carry over individual1 data
        assertNull(individual2a.number)
    }
}