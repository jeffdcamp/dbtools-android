package org.dbtools.android.domain.dbtype;

import android.database.Cursor;

public class DatabaseBlob implements DatabaseValue<byte[]> {
    @Override
    public byte[] getColumnValue(Cursor cursor, int columnIndex, byte[] defaultValue) {
        return !cursor.isNull(columnIndex) ? cursor.getBlob(columnIndex) : defaultValue;
    }
}
