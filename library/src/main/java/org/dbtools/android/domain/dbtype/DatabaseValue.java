package org.dbtools.android.domain.dbtype;

import android.database.Cursor;

import javax.annotation.Nullable;

public interface DatabaseValue <T> {
    public T getColumnValue(Cursor cursor, int columnIndex, @Nullable T defaultValue);
}
