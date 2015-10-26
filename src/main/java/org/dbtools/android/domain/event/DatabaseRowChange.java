package org.dbtools.android.domain.event;

public class DatabaseRowChange {
    private DatabaseChangeType databaseChangeType;
    private Long rowId;

    public DatabaseRowChange(DatabaseChangeType databaseChangeType, Long rowId) {
        this.databaseChangeType = databaseChangeType;
        this.rowId = rowId;
    }

    public DatabaseChangeType getDatabaseChangeType() {
        return databaseChangeType;
    }

    public Long getRowId() {
        return rowId;
    }
}
