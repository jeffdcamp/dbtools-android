package org.dbtools.sample.model.database;

import org.dbtools.android.domain.DBToolsTableChangeListener;
import org.dbtools.android.domain.DatabaseTableChange;
import org.dbtools.android.domain.config.TestDatabaseConfig;
import org.dbtools.android.domain.database.JdbcSqliteDatabaseWrapper;
import org.dbtools.android.domain.database.contentvalues.DBToolsContentValues;
import org.dbtools.sample.model.database.main.MainDatabaseManagers;
import org.dbtools.sample.model.database.main.individual.Individual;
import org.dbtools.sample.model.database.main.individual.IndividualConst;
import org.dbtools.sample.model.database.main.individual.IndividualManager;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertEquals;

public class TableChangeListenerTest {

    private final AtomicInteger insertEventCount = new AtomicInteger(0);
    private final AtomicInteger updateEventCount = new AtomicInteger(0);
    private final AtomicInteger deleteEventCount = new AtomicInteger(0);

    private IndividualManager individualManager;

    @Before
    public void setUp() throws Exception {
        TestDatabaseConfig databaseConfig = new TestMainDatabaseConfig("java-test-tablechange.db");
        databaseConfig.deleteAllDatabaseFiles();
        DatabaseManager databaseManager = new DatabaseManager(databaseConfig);
        MainDatabaseManagers.init(databaseManager);
        JdbcSqliteDatabaseWrapper.setEnableLogging(true); // show all statements

        individualManager = MainDatabaseManagers.getIndividualManager();

        // setup table change listener
        individualManager.addTableChangeListener(new DBToolsTableChangeListener() {
            @Override
            public void onTableChange(DatabaseTableChange tableChange) {
                if (tableChange.isInsert()) {
                    insertEventCount.incrementAndGet();
                } else if (tableChange.isUpdate()) {
                    updateEventCount.incrementAndGet();
                } else if (tableChange.isDelete()) {
                    deleteEventCount.incrementAndGet();
                }
            }
        });
    }

    @Test
    public void testRecordCrud() throws Exception {
        // === INSERT ===
        Individual individual = createIndividual("Jeff");
        individualManager.save(individual);

        // test save
        assertEquals(1, individualManager.findCount());
        assertCountValues(1, 0, 0);

        // === UPDATE ===
        individual.setFirstName("Jeffery");
        individualManager.save(individual);
        assertCountValues(1, 1, 0);

        // === DELETE ===
        individualManager.delete(individual);
        assertEquals(0, individualManager.findCount());
        assertCountValues(1, 1, 1);
    }

    @Test
    public void testTransaction() throws Exception {
        Individual individual1 = createIndividual("Jeff");
        Individual individual2 = createIndividual("Ty");
        Individual individual3 = createIndividual("Bob");
        Individual individual4 = createIndividual("Sam");

        // === INSERT ===
        individualManager.beginTransaction();

        individualManager.save(individual1);
        individualManager.save(individual2);
        individualManager.save(individual3);
        individualManager.save(individual4);

        individualManager.endTransaction(true);

        // test
        assertEquals(4, individualManager.findCount());
        assertCountValues(1, 0, 0);

        // === UPDATE ===
        individualManager.beginTransaction();

        individualManager.update(individual1);
        individualManager.update(individual2);
        individualManager.update(individual3);
        individualManager.update(individual4);

        individualManager.endTransaction(true);

        // test
        assertEquals(4, individualManager.findCount());
        assertCountValues(1, 1, 0);


        // === DELETE ===
        individualManager.beginTransaction();

        individualManager.delete(individual1);
        individualManager.delete(individual2);
        individualManager.delete(individual3);
        individualManager.delete(individual4);

        individualManager.endTransaction(true);

        // test
        assertEquals(0, individualManager.findCount());
        assertCountValues(1, 1, 1);
    }

    @Test
    public void testUpdate() throws Exception {
        // === INSERT ===
        Individual individual = createIndividual("Jeff");
        individualManager.save(individual);

        // test save
        assertEquals(1, individualManager.findCount());
        assertCountValues(1, 0, 0);

        // === Record UPDATE ===
        individual.setFirstName("Jeff1");
        individualManager.save(individual);

        assertCountValues(1, 1, 0);

        // == Update single column
        DBToolsContentValues contentValues = individualManager.createNewDBToolsContentValues();
        contentValues.put(IndividualConst.C_FIRST_NAME, "Jeff2");
        individualManager.update(contentValues, individual.getId());

        assertCountValues(1, 2, 0);

        // Update a record that does not exist (no event should trigger)
        contentValues.put(IndividualConst.C_FIRST_NAME, "Jeff2");
        individualManager.update(contentValues, 1000); // 1000 is a primary key that should NEVER exist in this test

        assertCountValues(1, 2, 0);

    }

    @Test
    public void testDelete() throws Exception {
        // === INSERT ===
        Individual individual1 = createIndividual("Jeff");
        individualManager.save(individual1);
        Individual individual2 = createIndividual("Ty");
        individualManager.save(individual2);

        // test save
        assertEquals(2, individualManager.findCount());
        assertCountValues(2, 0, 0);

        // Basic delete
        individualManager.delete(individual1.getId());
        assertCountValues(2, 0, 1);

        // Delete all
        individualManager.deleteAll();
        assertCountValues(2, 0, 2);

        // Delete all (with NO records... no new event should trigger)
        individualManager.deleteAll();
        assertCountValues(2, 0, 2);
    }


    private Individual createIndividual(String name) {
        Individual individual = new Individual();
        individual.setFirstName(name);
        return individual;
    }

    private void assertCountValues(int expectedInsertEventCount, int expectedUpdateEventCount, int expectedDeleteEventCount) {
        assertEquals("Insert Event Count", expectedInsertEventCount, insertEventCount.get());
        assertEquals("Update Event Count", expectedUpdateEventCount, updateEventCount.get());
        assertEquals("Delete Event Count", expectedDeleteEventCount, deleteEventCount.get());
    }
}