package org.dbtools.android.domain.dbtype;

import android.database.Cursor;

public class DatabaseString implements DatabaseValue<String> {
    @Override
    public String getColumnValue(Cursor cursor, int columnIndex, String defaultValue) {
        return !cursor.isNull(columnIndex) ? cursor.getString(columnIndex) : defaultValue;
    }
}
