package org.dbtools.android.domain.dbtype;

import android.database.Cursor;

public class DatabaseFloat implements DatabaseValue<Float> {
    @Override
    public Float getColumnValue(Cursor cursor, int columnIndex, Float defaultValue) {
        return !cursor.isNull(columnIndex) ? cursor.getFloat(columnIndex) : defaultValue;
    }
}
