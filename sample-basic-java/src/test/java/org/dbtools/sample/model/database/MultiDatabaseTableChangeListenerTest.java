package org.dbtools.sample.model.database;

import org.dbtools.android.domain.AndroidDatabase;
import org.dbtools.android.domain.DBToolsTableChangeListener;
import org.dbtools.android.domain.DatabaseTableChange;
import org.dbtools.android.domain.config.BuildEnv;
import org.dbtools.android.domain.config.TestDatabaseConfig;
import org.dbtools.android.domain.database.JdbcSqliteDatabaseWrapper;
import org.dbtools.sample.model.database.main.MainDatabaseManagers;
import org.dbtools.sample.model.database.main.individual.Individual;
import org.dbtools.sample.model.database.main.individual.IndividualManager;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertEquals;

public class MultiDatabaseTableChangeListenerTest {

    private final AtomicInteger insertEventCount = new AtomicInteger(0);
    private final AtomicInteger updateEventCount = new AtomicInteger(0);
    private final AtomicInteger deleteEventCount = new AtomicInteger(0);
    private final AtomicInteger bulkOperationCount = new AtomicInteger(0);

    private IndividualManager individualManager;

    private String databaseName1 = DatabaseManagerConst.MAIN_DATABASE_NAME + "1";
    private String databaseName2 = DatabaseManagerConst.MAIN_DATABASE_NAME + "2";

    @Before
    public void setUp() throws Exception {
        List<AndroidDatabase> androidDatabases = new ArrayList<>();
        androidDatabases.add(new AndroidDatabase(databaseName1, BuildEnv.GRADLE.getTestDbDir() + databaseName1 + ".db", DatabaseManager.MAIN_TABLES_VERSION, DatabaseManager.MAIN_VIEWS_VERSION));
        androidDatabases.add(new AndroidDatabase(databaseName2, BuildEnv.GRADLE.getTestDbDir() + databaseName2 + ".db", DatabaseManager.MAIN_TABLES_VERSION, DatabaseManager.MAIN_VIEWS_VERSION));

        TestDatabaseConfig databaseConfig = new TestDatabaseConfig(androidDatabases);
        databaseConfig.deleteAllDatabaseFiles();
        DatabaseManager databaseManager = new MultiDatabaseManager(databaseConfig);
        MainDatabaseManagers.init(databaseManager);
        JdbcSqliteDatabaseWrapper.setEnableLogging(true); // show all statements

        individualManager = MainDatabaseManagers.getIndividualManager();

        // setup table change listener
        individualManager.addTableChangeListener(databaseName1, new DBToolsTableChangeListener() {
            @Override
            public void onTableChange(DatabaseTableChange tableChange) {
                if (tableChange.isInsert()) {
                    insertEventCount.incrementAndGet();
                } else if (tableChange.isUpdate()) {
                    updateEventCount.incrementAndGet();
                } else if (tableChange.isDelete()) {
                    deleteEventCount.incrementAndGet();
                }

                if (tableChange.isBulkOperation()) {
                    bulkOperationCount.incrementAndGet();
                }
            }
        });
    }

    @Test
    public void testRecordCrud() throws Exception {
        // === INSERT ===
        Individual individual1 = createIndividual("Jeff");
        individualManager.save(databaseName1, individual1);

        Individual individual2 = createIndividual("Jordan");
        individualManager.save(databaseName2, individual2);

        // test save
        assertEquals(1, individualManager.findCount(databaseName1));
        assertEquals(1, individualManager.findCount(databaseName2));
        assertCountValuesDatabase1(1, 0, 0);

        // === UPDATE ===
        individual1.setFirstName("Jeffery");
        individualManager.save(databaseName1, individual1);

        individual2.setFirstName("G");
        individualManager.save(databaseName2, individual2);

        // test ONLY change on database1
        assertCountValuesDatabase1(1, 1, 0);

        // === DELETE ===
        individualManager.delete(databaseName1, individual1);
        assertEquals(0, individualManager.findCount(databaseName1));

        individualManager.delete(databaseName2, individual2);
        assertEquals(0, individualManager.findCount(databaseName2));

        assertCountValuesDatabase1(1, 1, 1);
    }

    @Test
    public void testTransaction() throws Exception {
        Individual individual1 = createIndividual("Jeff");
        Individual individual2 = createIndividual("Ty");


        Individual individual3 = createIndividual("Bob");
        Individual individual4 = createIndividual("Sam");

        // === INSERT ===
        // DB1
        individualManager.beginTransaction(databaseName1);
        individualManager.save(databaseName1, individual1);
        individualManager.save(databaseName1, individual2);
        individualManager.endTransaction(databaseName1, true);

        // DB2
        individualManager.beginTransaction(databaseName2);
        individualManager.save(databaseName2, individual3);
        individualManager.save(databaseName2, individual4);
        individualManager.endTransaction(databaseName2, true);

        // test
        assertEquals(2, individualManager.findCount(databaseName1));
        assertEquals(2, individualManager.findCount(databaseName2));

        assertCountValuesDatabase1(0, 0, 0);
        assertBulkOperationDatabase1(1);

        // === UPDATE ===
        individualManager.beginTransaction(databaseName1);
        individualManager.update(databaseName1, individual1);
        individualManager.update(databaseName1, individual2);
        individualManager.endTransaction(databaseName1, true);

        individualManager.beginTransaction(databaseName2);
        individualManager.update(databaseName2, individual3);
        individualManager.update(databaseName2, individual4);
        individualManager.endTransaction(databaseName2, true);

        // test
        assertEquals(2, individualManager.findCount(databaseName1));
        assertEquals(2, individualManager.findCount(databaseName2));

        assertCountValuesDatabase1(0, 0, 0);
        assertBulkOperationDatabase1(2);


        // === DELETE ===
        individualManager.beginTransaction(databaseName1);
        individualManager.delete(databaseName1, individual1);
        individualManager.delete(databaseName1, individual2);
        individualManager.endTransaction(databaseName1, true);

        individualManager.beginTransaction(databaseName2);
        individualManager.delete(databaseName2, individual3);
        individualManager.delete(databaseName2, individual4);
        individualManager.endTransaction(databaseName2, true);

        // test
        assertEquals(0, individualManager.findCount(databaseName1));
        assertEquals(0, individualManager.findCount(databaseName2));
        assertCountValuesDatabase1(0, 0, 0);
        assertBulkOperationDatabase1(3);
    }

    private Individual createIndividual(String name) {
        Individual individual = new Individual();
        individual.setFirstName(name);
        return individual;
    }

    private void assertBulkOperationDatabase1(int expectedCount) {
        assertEquals("Bulk Operation", expectedCount, bulkOperationCount.get());
    }

    private void assertCountValuesDatabase1(int expectedInsertEventCount, int expectedUpdateEventCount, int expectedDeleteEventCount) {
        assertEquals("Insert Event Count", expectedInsertEventCount, insertEventCount.get());
        assertEquals("Update Event Count", expectedUpdateEventCount, updateEventCount.get());
        assertEquals("Delete Event Count", expectedDeleteEventCount, deleteEventCount.get());
    }
}