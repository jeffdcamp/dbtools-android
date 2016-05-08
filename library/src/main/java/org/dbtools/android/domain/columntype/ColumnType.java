package org.dbtools.android.domain.columntype;

public class ColumnType<T> {
    private String columnName;
    private Class<T> columnType;

    public ColumnType(Class<T> columnType, String columnName) {
        this.columnName = columnName;
        this.columnType = columnType;
    }

    public String getColumnName() {
        return columnName;
    }

    public Class<T> getColumnType() {
        return columnType;
    }
}
