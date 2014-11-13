package org.dbtools.android.domain.event;

public class DatabaseUpdateEvent extends DatabaseChangeEvent {
    private int rowsAffected;

    public DatabaseUpdateEvent(String tableName, int rowsAffected) {
        super(tableName);
        this.rowsAffected = rowsAffected;
    }

    public int getRowsAffected() {
        return rowsAffected;
    }
}
