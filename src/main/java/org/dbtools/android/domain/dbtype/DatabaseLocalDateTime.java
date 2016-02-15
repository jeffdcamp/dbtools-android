package org.dbtools.android.domain.dbtype;

import android.database.Cursor;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.ZoneOffset;

public class DatabaseLocalDateTime implements DatabaseValue<LocalDateTime> {
    @Override
    public LocalDateTime getColumnValue(Cursor cursor, int columnIndex, LocalDateTime defaultValue) {
        return !cursor.isNull(0) ? LocalDateTime.ofEpochSecond(cursor.getLong(0), 0, ZoneOffset.UTC) : defaultValue;
    }
}
