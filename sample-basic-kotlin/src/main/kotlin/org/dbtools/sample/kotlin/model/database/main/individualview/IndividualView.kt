/*
 * IndividualView.kt
 *
 * Created: 05/09/2016 09:21:13
 */



package org.dbtools.sample.kotlin.model.database.main.individualview

import org.dbtools.sample.kotlin.model.database.main.individual.IndividualConst

class IndividualView : IndividualViewBaseRecord() {

    companion object {
        val DROP_VIEW: String
        val CREATE_VIEW = "CREATE VIEW IF NOT EXISTS " + IndividualViewConst.TABLE + " AS SELECT " +
            IndividualConst.FULL_C_ID + " AS " + IndividualViewConst.C_ID + ", " +
            IndividualConst.FULL_C_FIRST_NAME + " AS " + IndividualViewConst.C_NAME +
            " FROM " + IndividualConst.TABLE
        init {
        DROP_VIEW = "DROP VIEW IF EXISTS " + IndividualViewConst.TABLE + ""
        }
    }



}