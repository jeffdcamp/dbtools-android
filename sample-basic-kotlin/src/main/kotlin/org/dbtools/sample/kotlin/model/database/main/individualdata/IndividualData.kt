/*
 * IndividualData.kt
 *
 * Created: 11/20/2016 11:05:51
 */



package org.dbtools.sample.kotlin.model.database.main.individualdata

import android.database.Cursor
import org.dbtools.android.domain.database.contentvalues.DBToolsContentValues


class IndividualData : IndividualDataBaseRecord {


    constructor() {
    }

    constructor(record: IndividualData) : super(record) {
    }

    constructor(cursor: Cursor) {
        setContent(cursor)
    }

    constructor(values: DBToolsContentValues<*>) {
        setContent(values)
    }


}