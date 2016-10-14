package org.dbtools.android.domain;

public class DatabaseTableChange {
    public static final long UNKNOWN_ROW_ID = 0L; // no specific id could be identified for this event (due to transactional changes, mass update/delete, etc)

    private final String table;
    private final long rowId;

    private final boolean insert;
    private final boolean update;
    private final boolean delete;

    public DatabaseTableChange(String table, long rowId, boolean insert, boolean update, boolean delete) {
        this.table = table;
        this.rowId = rowId;
        this.insert = insert;
        this.update = update;
        this.delete = delete;
    }

    public long getRowId() {
        return rowId;
    }

    public boolean isInsert() {
        return insert;
    }

    public boolean isUpdate() {
        return update;
    }

    public boolean isDelete() {
        return delete;
    }

    /**
     * If there were any changes to this table, then return true
     * @return true if a change occurred on this table
     */
    public boolean hasChange() {
        return insert || update || delete;
    }

    /**
     * If there were any changes to the given row or there are any unknown changes (transaction, mass update/delete) to this table, then return true
     * @param rowId id of row
     * @return true if the row may have changed
     */
    public boolean hasChangeForRowId(long rowId) {
        return hasChange() && (this.rowId == rowId || this.rowId == UNKNOWN_ROW_ID);
    }
}
