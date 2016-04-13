package org.dbtools.android.domain;

public class DatabaseTableChange {
    private final boolean insert;
    private final boolean update;
    private final boolean delete;

    public DatabaseTableChange(boolean insert, boolean update, boolean delete) {
        this.insert = insert;
        this.update = update;
        this.delete = delete;
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

    public boolean hasChange() {
        return insert || update || delete;
    }
}
