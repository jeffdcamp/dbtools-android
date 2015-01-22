package org.dbtools.android.domain.dbtype;

import android.database.Cursor;

public class DatabaseLong implements DatabaseValue<Long> {
    @Override
    public Long getColumnValue(Cursor cursor, int columnIndex, Long defaultValue) {
        return !cursor.isNull(columnIndex) ? cursor.getLong(columnIndex) : defaultValue;
    }
}
