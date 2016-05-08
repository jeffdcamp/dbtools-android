package org.dbtools.android.domain.dbtype;

import android.database.Cursor;
import org.joda.time.DateTime;

public class DatabaseDateTime implements DatabaseValue<DateTime> {
    @Override
    public DateTime getColumnValue(Cursor cursor, int columnIndex, DateTime defaultValue) {
        return !cursor.isNull(0) ? new org.joda.time.DateTime(cursor.getLong(0)) : defaultValue;
    }
}
