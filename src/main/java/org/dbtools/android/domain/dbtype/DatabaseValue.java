package org.dbtools.android.domain.dbtype;

import android.database.Cursor;
import org.jetbrains.annotations.Nullable;

public interface DatabaseValue <T> {
    public T getColumnValue(Cursor cursor, int columnIndex, @Nullable T defaultValue);
}
