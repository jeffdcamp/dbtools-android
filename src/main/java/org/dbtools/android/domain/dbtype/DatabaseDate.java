package org.dbtools.android.domain.dbtype;

import android.database.Cursor;

import java.util.Date;

public class DatabaseDate implements DatabaseValue<Date> {
    @Override
    public Date getColumnValue(Cursor cursor, int columnIndex, Date defaultValue) {
        return !cursor.isNull(0) ? new Date(cursor.getLong(0)) : defaultValue;

    }
}
