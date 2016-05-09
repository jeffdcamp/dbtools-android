package org.dbtools.sample.kotlin

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import org.dbtools.sample.kotlin.model.database.main.MainDatabaseManagers
import org.dbtools.sample.kotlin.model.database.main.individual.Individual
import org.dbtools.sample.kotlin.model.database.main.individual.IndividualManager
import java.util.*

class MainActivity : AppCompatActivity() {

    private var individualManager: IndividualManager

    init {
        individualManager = MainDatabaseManagers.individualManager!!
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun createIndividual(view: View) {
        val individual = Individual()
        individual.firstName = "Jeff"
        individual.lastName = "Campbell"
        individual.sampleDateTime = Date()
        individual.birthDate = GregorianCalendar(1970, 1, 1).time
        individual.lastModified = Date()
        individual.number = 1234
        individual.phone = "555-555-1234"
        individual.email = "test@test.com"
        individual.amount1 = 19.95f
        individual.amount2 = 1000000000.25
        individual.enabled = true

        individualManager.save(individual)

        Toast.makeText(this, "Individual Count: " + individualManager.findCount(), Toast.LENGTH_SHORT).show()
    }

    fun deleteLastIndividual(view: View) {
        // check to make sure there is individuals
        if (!hasRecords()) {
            return
        }

        val lastIndividualId = individualManager.findLastIndividualId()

        if (individualManager.delete(lastIndividualId) > 0) {
            Toast.makeText(this, "Last individual deleted", Toast.LENGTH_SHORT).show()
        }
    }

    fun updateLastIndividual(view: View) {
        // check to make sure there is individuals
        if (!hasRecords()) {
            return
        }

        val individual = individualManager.findLastIndividual()
        if (individual != null) {
            individual!!.firstName = "Jeffery"
            individualManager.update(individual)
            Toast.makeText(this, "Last individual updated", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Failed to find last individual", Toast.LENGTH_SHORT).show()
        }
    }

    fun showLastIndividualName(view: View) {
        // check to make sure there is individuals
        if (!hasRecords()) {
            return
        }

        val lastIndividualId = individualManager.findLastIndividualId()
        Toast.makeText(this, "Last Individual First Name: " + individualManager.findFirstNameById(lastIndividualId), Toast.LENGTH_SHORT).show()
    }

    private fun hasRecords(): Boolean {
        val count = individualManager.findCount()

        if (count <= 0) {
            Toast.makeText(this, "No Records exist", Toast.LENGTH_SHORT).show()
            return false
        } else {
            return true
        }
    }

}
