package org.dbtools.android.domain.event;

public class DatabaseInsertEvent extends DatabaseChangeEvent {
    private long newId;

    public DatabaseInsertEvent(String tableName, long newId) {
        super(tableName);
        this.newId = newId;
    }

    public long getNewId() {
        return newId;
    }
}
