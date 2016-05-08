package org.dbtools.android.domain.dbtype;

import android.database.Cursor;

public class DatabaseBoolean implements DatabaseValue<Boolean> {
    @Override
    public Boolean getColumnValue(Cursor cursor, int columnIndex, Boolean defaultValue) {
        return !cursor.isNull(columnIndex) ? cursor.getInt(0) != 0 : defaultValue;
    }
}
