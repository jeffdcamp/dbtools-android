package org.dbtools.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import org.dbtools.sample.model.database.main.MainDatabaseManagers;
import org.dbtools.sample.model.database.main.individual.Individual;
import org.dbtools.sample.model.database.main.individual.IndividualManager;

import java.util.Date;
import java.util.GregorianCalendar;

public class MainActivity extends AppCompatActivity {

    private IndividualManager individualManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        individualManager = MainDatabaseManagers.getIndividualManager();
    }

    public void createIndividual(View view) {
        Individual individual = new Individual();
        individual.setFirstName("Jeff");
        individual.setLastName("Campbell");
        individual.setSampleDateTime(new Date());
        individual.setBirthDate(new GregorianCalendar(1970,1,1).getTime());
        individual.setLastModified(new Date());
        individual.setNumber(1234);
        individual.setPhone("555-555-1234");
        individual.setEmail("test@test.com");
        individual.setAmount1(19.95F);
        individual.setAmount2(1000000000.25D);
        individual.setEnabled(true);

        individualManager.save(individual);

        Toast.makeText(this, "Individual Count: " + individualManager.findCount(), Toast.LENGTH_SHORT).show();
    }

    public void deleteLastIndividual(View view) {
        // check to make sure there is individuals
        if (!hasRecords()) {
            return;
        }

        long lastIndividualId = individualManager.findLastIndividualId();

        if (individualManager.delete(lastIndividualId) > 0) {
            Toast.makeText(this, "Last individual deleted", Toast.LENGTH_SHORT).show();
        }
    }

    public void updateLastIndividual(View view) {
        // check to make sure there is individuals
        if (!hasRecords()) {
            return;
        }

        Individual individual = individualManager.findLastIndividual();
        if (individual != null) {
            individual.setFirstName("Jeffery");
            individualManager.update(individual);
            Toast.makeText(this, "Last individual updated", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Failed to find last individual", Toast.LENGTH_SHORT).show();
        }
    }

    public void showLastIndividualName(View view) {
        // check to make sure there is individuals
        if (!hasRecords()) {
            return;
        }

        long lastIndividualId = individualManager.findLastIndividualId();
        Toast.makeText(this, "Last Individual First Name: " + individualManager.findFirstNameById(lastIndividualId), Toast.LENGTH_SHORT).show();
    }

    private boolean hasRecords() {
        long count = individualManager.findCount();

        if (count <= 0) {
            Toast.makeText(this, "No Records exist", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

}
