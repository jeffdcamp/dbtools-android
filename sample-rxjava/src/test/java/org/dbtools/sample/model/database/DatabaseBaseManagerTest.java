package org.dbtools.sample.model.database;

import org.dbtools.android.domain.DBToolsTableChangeListener;
import org.dbtools.android.domain.DatabaseTableChange;
import org.dbtools.android.domain.config.TestDatabaseConfig;
import org.dbtools.sample.model.database.main.MainDatabaseManagers;
import org.dbtools.sample.model.database.main.individual.Individual;
import org.dbtools.sample.model.database.main.individual.IndividualManager;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.List;

import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class DatabaseBaseManagerTest {

    private DatabaseManager databaseManager;

    @Before
    public void setUp() throws Exception {
        TestDatabaseConfig databaseConfig = new TestMainDatabaseConfig("java-test-individual.db");
        databaseConfig.deleteAllDatabaseFiles();
        databaseManager = new DatabaseManager(databaseConfig);
        MainDatabaseManagers.init(databaseManager);
    }

    /**
     * @return swap database File
     */
    private File setupSwap() {
        // create a database to swap with
        IndividualManager individualManager = MainDatabaseManagers.getIndividualManager();

        Individual ind1 = new Individual();
        ind1.setFirstName("A");

        individualManager.save(ind1);

        assertEquals(1, individualManager.findCount());
        assertEquals("A", individualManager.findAll().get(0).getFirstName());

        // close the database
        databaseManager.closeDatabase(DatabaseManagerConst.MAIN_DATABASE_NAME);
        String databaseAPath = databaseManager.getDatabase(DatabaseManagerConst.MAIN_DATABASE_NAME).getPath();

        // move the database file to a swap database <database path>2
        File databaseAFile = new File(databaseAPath);
        File swapDatabaseFile = new File(databaseAPath + "2");
        assertTrue(databaseAFile.renameTo(swapDatabaseFile));


        // start to write again... which will create a new database
        Individual ind2 = new Individual();
        ind2.setFirstName("B");
        individualManager.save(ind2);

        Individual ind3 = new Individual();
        ind3.setFirstName("C");
        individualManager.save(ind3);

        // Make sure the new database exists
        List<Individual> individua2lList = individualManager.findAll();
        assertEquals(2, individua2lList.size());
        assertEquals("B", individua2lList.get(0).getFirstName());
        assertEquals("C", individua2lList.get(1).getFirstName());

        return swapDatabaseFile;
    }

    @Test
    public void testSwap() {
        IndividualManager individualManager = MainDatabaseManagers.getIndividualManager();
        File swapDatabaseFile = setupSwap();

        assertTrue(databaseManager.swapDatabase(DatabaseManagerConst.MAIN_DATABASE_NAME, swapDatabaseFile));

        assertEquals(1, individualManager.findCount());
        assertEquals("A", individualManager.findAll().get(0).getFirstName());
    }

    @Test
    public void testSwapInTransaction() {
        IndividualManager individualManager = MainDatabaseManagers.getIndividualManager();
        File swapDatabaseFile = setupSwap();

        individualManager.beginTransaction();

        assertTrue(databaseManager.swapDatabase(DatabaseManagerConst.MAIN_DATABASE_NAME, swapDatabaseFile));

        individualManager.endTransaction(true);

        assertEquals(1, individualManager.findCount());
        assertEquals("A", individualManager.findAll().get(0).getFirstName());
    }

    @Test
    public void testSwapErrors() {
        IndividualManager individualManager = MainDatabaseManagers.getIndividualManager();
        File swapDatabaseFile = setupSwap();

        assertFalse(databaseManager.swapDatabase(DatabaseManagerConst.MAIN_DATABASE_NAME, new File("/data/bad-file.db")));
        assertFalse(databaseManager.swapDatabase("BAD_DATABASE_NAME", swapDatabaseFile));

        // original database should NOT have changed
        List<Individual> individua2lList = individualManager.findAll();
        assertEquals(2, individua2lList.size());
        assertEquals("B", individua2lList.get(0).getFirstName());
        assertEquals("C", individua2lList.get(1).getFirstName());
    }

    private int subscribeMessageCount; // listener
    private int listenerMessageCount; // listener
    @Test
    public void testSwapSubscriptionAndListener() {
        subscribeMessageCount = 0;
        listenerMessageCount = 0;

        IndividualManager individualManager = MainDatabaseManagers.getIndividualManager();
        individualManager.addTableChangeListener(new DBToolsTableChangeListener() {
            @Override
            public void onTableChange(DatabaseTableChange tableChange) {
                System.out.println("Table Changed: " + tableChange.getTable() + " ID: " + tableChange.getRowId());
                listenerMessageCount++;
            }
        });

        individualManager.tableChanges()
                .subscribeOn(Schedulers.trampoline())
                .observeOn(Schedulers.trampoline())
                .subscribe(new Consumer<DatabaseTableChange>() {
                    @Override
                    public void accept(DatabaseTableChange tableChange) {
                        System.out.println("Table Changed: " + tableChange.getTable() + " ID: " + tableChange.getRowId());
                        subscribeMessageCount++;
                    }
                });

        File swapDatabaseFile = setupSwap();

        int lastListenerCount = listenerMessageCount;
        int lastSubscribeCount = subscribeMessageCount;

        assertTrue(databaseManager.swapDatabase(DatabaseManagerConst.MAIN_DATABASE_NAME, swapDatabaseFile));

        Individual newIndividual = new Individual();
        newIndividual.setFirstName("New");
        individualManager.save(newIndividual);

        assertEquals(lastListenerCount + 1, listenerMessageCount);
        assertEquals(lastSubscribeCount + 1, subscribeMessageCount);
    }
}