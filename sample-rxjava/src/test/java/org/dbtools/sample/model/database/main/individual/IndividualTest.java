package org.dbtools.sample.model.database.main.individual;

import org.dbtools.android.domain.config.TestDatabaseConfig;
import org.dbtools.sample.model.database.DatabaseManager;
import org.dbtools.sample.model.database.TestMainDatabaseConfig;
import org.dbtools.sample.model.database.main.MainDatabaseManagers;
import org.junit.Test;

import java.util.GregorianCalendar;

import static org.junit.Assert.assertEquals;

public class IndividualTest {
    @Test
    public void testIndividual() throws Exception {
        TestDatabaseConfig databaseConfig = new TestMainDatabaseConfig("java-test-individual.db");
        databaseConfig.deleteAllDatabaseFiles();
        DatabaseManager databaseManager = new DatabaseManager(databaseConfig);
        MainDatabaseManagers.init(databaseManager);

        // === CREATE / INSERT ===
        IndividualManager individualManager = MainDatabaseManagers.getIndividualManager();
        Individual individual = new Individual();
        individual.setFirstName("Jeff");
        individual.setLastName("Campbell");
        individual.setSampleDateTime(new GregorianCalendar(1970,1,1).getTime());
        individual.setBirthDate(new GregorianCalendar(1970,1,2).getTime());
        individual.setLastModified(new GregorianCalendar(1970,1,3).getTime());
        individual.setNumber(1234);
        individual.setPhone("555-555-1234");
        individual.setEmail("test@test.com");
        individual.setAmount1(19.95F);
        individual.setAmount2(1000000000.25D);
        individual.setEnabled(true);

        individualManager.save(individual);

        // test save
        assertEquals(1, individualManager.findCount());

        // read save... verify values
        Individual testIndividual = individualManager.findByRowId(individual.getId());
        if (testIndividual == null) {
            throw new IllegalStateException("Could not find test individual");
        }
        assertEquals("First", individual.getFirstName(), testIndividual.getFirstName());
        assertEquals("Last", individual.getLastName(), testIndividual.getLastName());
        assertEquals("Sample Date", individual.getSampleDateTime().getTime(), testIndividual.getSampleDateTime().getTime());
        assertEquals("Sample Date", individual.getBirthDate().getTime(), testIndividual.getBirthDate().getTime());
        assertEquals("Sample Date", individual.getLastModified().getTime(), testIndividual.getLastModified().getTime());
        assertEquals("Number", individual.getNumber(), testIndividual.getNumber());
        assertEquals("Phone", individual.getPhone(), testIndividual.getPhone());
        assertEquals("Email", individual.getEmail(), testIndividual.getEmail());
        assertEquals("Amount1", individual.getAmount1(), testIndividual.getAmount1());
        assertEquals("Amount2", individual.getAmount2(), testIndividual.getAmount2());
        assertEquals("Enabled", individual.isEnabled(), testIndividual.isEnabled());


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