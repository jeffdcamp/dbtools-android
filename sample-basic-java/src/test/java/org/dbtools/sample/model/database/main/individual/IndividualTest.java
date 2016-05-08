package org.dbtools.sample.model.database.main.individual;

import org.dbtools.android.domain.config.TestDatabaseConfig;
import org.dbtools.sample.TestMainDatabaseConfig;
import org.dbtools.sample.model.database.DatabaseManager;
import org.dbtools.sample.model.database.main.MainDatabaseManagers;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class IndividualTest {
    @Test
    public void testIndividual() throws Exception {
        TestDatabaseConfig databaseConfig = new TestMainDatabaseConfig("test-individual.db");
        databaseConfig.deleteDatabaseFile();
        DatabaseManager databaseManager = new DatabaseManager(databaseConfig);
        MainDatabaseManagers.init(databaseManager);

        // === CREATE / INSERT ===
        IndividualManager individualManager = MainDatabaseManagers.getIndividualManager();
        Individual individual = new Individual();
        individual.setFirstName("Jeff");
        individual.setLastName("Campbell");
        individualManager.save(individual);

        assertEquals(1, individualManager.findCount());

        // === UPDATE ===
        individual.setFirstName("Jeffery");
        individualManager.save(individual);

        String dbFirstName = individualManager.findValueBySelection(String.class, IndividualConst.C_FIRST_NAME, IndividualConst.C_ID + " = " + individual.getId(), null, "");
        assertEquals("Jeffery", dbFirstName);

        // === DELETE ===
        individualManager.delete(individual);
        assertEquals(0, individualManager.findCount());
    }
}