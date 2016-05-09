/*
 * IndividualQuery.kt
 *
 * Created: 05/09/2016 10:10:08
 */



package org.dbtools.sample.kotlin.model.database.main.individualquery

import android.database.Cursor
import org.dbtools.android.domain.database.contentvalues.DBToolsContentValues
import org.dbtools.sample.kotlin.model.database.main.individual.IndividualConst

class IndividualQuery : IndividualQueryBaseRecord {

    companion object {
        val QUERY = "(" +
            "SELECT " +
            IndividualConst.FULL_C_ID + " AS " + IndividualQueryConst.C_ID + ", " +
            IndividualConst.FULL_C_FIRST_NAME + " AS " + IndividualQueryConst.C_NAME +
            " FROM SOME TABLE(S)" +
            ")"
        val QUERY_RAW = "SELECT * FROM " + QUERY
    }

    constructor() {
    }

    constructor(cursor: Cursor) {
        setContent(cursor)
    }

    constructor(values: DBToolsContentValues<*>) {
        setContent(values)
    }


}