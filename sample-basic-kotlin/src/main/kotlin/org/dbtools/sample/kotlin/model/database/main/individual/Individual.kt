/*
 * Individual.kt
 *
 * Created: 05/09/2016 09:21:13
 */



package org.dbtools.sample.kotlin.model.database.main.individual

import android.database.Cursor
import org.dbtools.android.domain.database.contentvalues.DBToolsContentValues


class Individual : IndividualBaseRecord {


    constructor() {
    }

    constructor(cursor: Cursor) {
        setContent(cursor)
    }

    constructor(values: DBToolsContentValues<*>) {
        setContent(values)
    }


}