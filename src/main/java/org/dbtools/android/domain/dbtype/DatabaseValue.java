package org.dbtools.android.domain.dbtype;

import android.database.Cursor;

public interface DatabaseValue <T> {
    public T getColumnValue(Cursor cursor, int columnIndex, T defaultValue);
}
