package org.dbtools.android.domain.dbtype;

import android.database.Cursor;

public class DatabaseDouble implements DatabaseValue<Double> {
    @Override
    public Double getColumnValue(Cursor cursor, int columnIndex, Double defaultValue) {
        return !cursor.isNull(columnIndex) ? cursor.getDouble(columnIndex) : defaultValue;
    }
}
