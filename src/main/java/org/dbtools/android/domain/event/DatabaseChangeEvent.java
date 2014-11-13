package org.dbtools.android.domain.event;

public class DatabaseChangeEvent {
    private String tableName;

    public DatabaseChangeEvent(String tableName) {
        this.tableName = tableName;
    }

    public String getTableName() {
        return tableName;
    }
}
