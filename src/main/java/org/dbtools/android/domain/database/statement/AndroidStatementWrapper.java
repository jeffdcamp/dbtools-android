package org.dbtools.android.domain.database.statement;

import android.database.sqlite.SQLiteStatement;

public class AndroidStatementWrapper implements StatementWrapper {
    private final SQLiteStatement statement;

    public AndroidStatementWrapper(SQLiteStatement statement) {
        this.statement = statement;
    }

    public void close() {
        statement.close();
    }

    public void bindNull(int index) {
        statement.bindNull(index);
    }

    public void bindLong(int index, long value) {
        statement.bindLong(index, value);
    }

    public void bindDouble(int index, double value) {
        statement.bindDouble(index, value);
    }

    public void bindString(int index, String value) {
        statement.bindString(index, value);
    }

    public void bindBlob(int index, byte[] value) {
        statement.bindBlob(index, value);
    }

    public void clearBindings() {
        statement.clearBindings();
    }

    public void bindAllArgsAsStrings(String[] bindArgs) {
        statement.bindAllArgsAsStrings(bindArgs);
    }

    public void execute() {
        statement.execute();
    }

    public int executeUpdateDelete() {
        return statement.executeUpdateDelete();
    }

    public long executeInsert() {
        return statement.executeInsert();
    }

    public long simpleQueryForLong() {
        return statement.simpleQueryForLong();
    }

    public String simpleQueryForString() {
        return statement.simpleQueryForString();
    }

    public String toString() {
        return statement.toString();
    }
}
