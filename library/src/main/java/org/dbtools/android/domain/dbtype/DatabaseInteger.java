package org.dbtools.android.domain.dbtype;

import android.database.Cursor;

public class DatabaseInteger implements DatabaseValue<Integer> {
    @Override
    public Integer getColumnValue(Cursor cursor, int columnIndex, Integer defaultValue) {
        return !cursor.isNull(columnIndex) ? cursor.getInt(columnIndex) : defaultValue;

    }
}
