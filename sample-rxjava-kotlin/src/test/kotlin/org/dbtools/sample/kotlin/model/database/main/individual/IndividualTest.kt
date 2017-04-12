package org.dbtools.sample.kotlin.model.database.main.individual

import io.reactivex.Maybe
import io.reactivex.Single
import org.dbtools.query.sql.SQLQueryBuilder
import org.dbtools.sample.kotlin.model.database.DatabaseManager
import org.dbtools.sample.kotlin.model.database.TestMainDatabaseConfig
import org.dbtools.sample.kotlin.model.database.main.MainDatabaseManagers
import org.dbtools.sample.kotlin.model.database.main.individualdata.IndividualData
import org.dbtools.sample.kotlin.model.type.IndividualType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import java.util.Arrays
import java.util.GregorianCalendar

class IndividualTest {
    @Before
    fun setUp() {
        // SETUP
        val databaseConfig = TestMainDatabaseConfig("kotlin-test-individual.db")
        databaseConfig.deleteAllDatabaseFiles()
        val databaseManager = DatabaseManager(databaseConfig)
        MainDatabaseManagers.init(databaseManager)

//        JdbcSqliteDatabaseWrapper.setEnableLogging(true)
    }

    @Test
    @Throws(Exception::class)
    fun testIndividual() {
        val individualManager = MainDatabaseManagers.individualManager

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
        val testIndividual = individualManager.findByRowId(individual.id) ?: throw IllegalStateException("Could not find test individual")
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
        val individualManager = MainDatabaseManagers.individualManager

        val ind1 = addIndividual("Darth", "Vader", 1, 1.1, true)
        val ind2 = addIndividual("Boba", "Fett", 2, 2.2, false)
        val ind3 = addIndividual("Luke", "Skywalker", 3, 3.3, true)
        val ind4 = addIndividual("Leia", "Organa", 4, 4.4, true)
        val ind5 = addIndividual("Han", "Solo", 5, 5.5, false)

        assertEqualsRx("findCountRx", 5, individualManager.findCountRx())
        assertEqualsRx("findCountByRawQueryRx", 5, individualManager.findCountByRawQueryRx("SELECT count(1) FROM " + IndividualConst.TABLE))
        assertEqualsRx("findCountBySelectionRx", 3, individualManager.findCountBySelectionRx("${IndividualConst.C_ENABLED} = ?", SQLQueryBuilder.toSelectionArgs(1)))
        assertEqualsIndividualRx("findByRawQueryRx", ind5, individualManager.findByRawQueryRx("SELECT * FROM ${IndividualConst.TABLE} WHERE ${IndividualConst.C_ID} = ?", SQLQueryBuilder.toSelectionArgs(ind5.id)))
        assertEqualsIndividualListRx("findAllByRawQueryRx", Arrays.asList(ind1, ind3, ind4), individualManager.findAllByRawQueryRx("SELECT * FROM ${IndividualConst.TABLE} WHERE ${IndividualConst.C_ENABLED} = ?", SQLQueryBuilder.toSelectionArgs(1)))
        assertEqualsRx("findValueByRawQueryRx", "Darth", individualManager.findValueByRawQueryRx(String::class.java, "", "SELECT ${IndividualConst.C_FIRST_NAME} FROM ${IndividualConst.TABLE} WHERE ${IndividualConst.C_ID} = ?", SQLQueryBuilder.toSelectionArgs(1)))
        assertEqualsIndividualListRx("findAllRx", Arrays.asList(ind1, ind2, ind3, ind4, ind5), individualManager.findAllRx())
        assertEqualsIndividualRx("findByRowIdRx", ind1, individualManager.findByRowIdRx(ind1.id))
        assertEqualsIndividualRx("findBySelectionRx", ind1, individualManager.findBySelectionRx(selection = IndividualConst.C_ENABLED + " = ?", selectionArgs = SQLQueryBuilder.toSelectionArgs(1)))
        assertEqualsIndividualListRx("findAllByRowIdsRx", Arrays.asList(ind1, ind2), individualManager.findAllByRowIdsRx(longArrayOf(ind1.id, ind2.id, 99999)))
        assertEqualsIndividualListRx("findAllBySelectionRx", Arrays.asList(ind1, ind3, ind4), individualManager.findAllBySelectionRx(selection = "${IndividualConst.C_ENABLED} = ?", selectionArgs = SQLQueryBuilder.toSelectionArgs(1)))

        assertEqualsRx("findAllValuesBySelectionRx", Arrays.asList(1, 3, 4), individualManager.findAllValuesBySelectionRx(Int::class.java, IndividualConst.C_NUMBER, IndividualConst.C_ENABLED + " = ?", SQLQueryBuilder.toSelectionArgs(1)))
        assertEqualsRx("findValueBySelectionRx", ind4.number!!, individualManager.findValueByRowIdRx(Int::class.java, IndividualConst.C_NUMBER, ind4.id, 99))
        assertEqualsIndividualListRx("findAllOrderByRx", Arrays.asList(ind2, ind4, ind3, ind5, ind1), individualManager.findAllRx(IndividualConst.C_LAST_NAME))
        assertEqualsRx("tableExistsRx", true, individualManager.tableExistsRx(IndividualConst.TABLE))
        assertEqualsRx("tableExistsRx false", false, individualManager.tableExistsRx("BadTableName"))

        val integerList = individualManager.findAllValuesBySelection(Int::class.java, IndividualConst.C_NUMBER, IndividualConst.C_ENABLED + " = ?", SQLQueryBuilder.toSelectionArgs(1))
        assertEquals("findAllValuesBySelection1", 3, integerList.size.toLong())
        assertEquals("findAllValuesBySelection1 value check", ind3.number, integerList.get(1))

        val value = individualManager.findValueByRowId(Int::class.java, IndividualConst.C_NUMBER, ind4.id, 99)
        assertEquals("findAllValuesBySelection2 value check", ind4.number, value)

        val orderedList = individualManager.findAll(orderBy = IndividualConst.C_LAST_NAME)
        assertEquals("findAllOrderBy", 5, orderedList.size.toLong())
        assertEquals("findAllOrderBy value check", ind1.lastName, orderedList[4].lastName)

        assertEquals("tableExists", true, individualManager.tableExists(IndividualConst.TABLE))
        assertEquals("tableExists false", false, individualManager.tableExists("BadTableName"))

    }

    private fun <I> assertEqualsRx(message: String, expected: I, singleObservable: Single<I>) {
        val blah = singleObservable.blockingGet()
        assertEquals(message, expected, blah)
    }

    private fun assertEqualsIndividualRx(message: String, expected: Individual, singleObservable: Maybe<Individual>) {
        val individual = singleObservable.blockingGet()
        assertEquals(message, expected.id, individual.id)
    }

    private fun assertEqualsIndividualRx(message: String, expected: Individual, singleObservable: Single<Individual>) {
        val individual = singleObservable.blockingGet()
        assertEquals(message, expected.id, individual.id)
    }

    private fun assertEqualsIndividualListRx(message: String, expected: List<Individual>, observable: Single<List<Individual>>) {
        val individuals = observable.blockingGet()

        assertEquals(message, expected.size.toLong(), individuals.size.toLong())

        for (i in individuals.indices) {
            assertEquals(message, expected[i].id, individuals[i].id)
        }
    }


    private fun addIndividual(first: String, last: String, number: Int, amount: Double, enabled: Boolean): Individual {
        val individualManager = MainDatabaseManagers.individualManager
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
        val individualDataManager = MainDatabaseManagers.individualDataManager

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
        val individualManager = MainDatabaseManagers.individualManager

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

    @Test
    fun testEnumColumns() {
        // === CREATE / INSERT ===
        val individualManager = MainDatabaseManagers.individualManager

        val i1 = Individual()
        i1.firstName = "Jeff"
        i1.individualTypeText = IndividualType.HEAD
        i1.individualType = IndividualType.HEAD
        individualManager.save(i1)

        val i2 = Individual()
        i2.firstName = "Tanner"
        i2.individualTypeText = IndividualType.CHILD
        i2.individualType = IndividualType.CHILD
        individualManager.save(i2)

        assertEquals(IndividualType.HEAD, individualManager.findByRowId(i1.id)!!.individualType)
        assertEquals(IndividualType.HEAD, individualManager.findByRowId(i1.id)!!.individualTypeText)
        assertEquals(IndividualType.CHILD, individualManager.findByRowId(i2.id)!!.individualType)
        assertEquals(IndividualType.CHILD, individualManager.findByRowId(i2.id)!!.individualTypeText)
    }
}