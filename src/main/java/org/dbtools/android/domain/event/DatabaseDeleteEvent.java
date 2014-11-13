package org.dbtools.android.domain.event;

public class DatabaseDeleteEvent extends DatabaseChangeEvent {
    private int rowsAffected;

    public DatabaseDeleteEvent(String tableName, int rowsAffected) {
        super(tableName);
        this.rowsAffected = rowsAffected;
    }

    public int getRowsAffected() {
        return rowsAffected;
    }
}
