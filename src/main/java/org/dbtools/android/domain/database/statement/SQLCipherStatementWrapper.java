package org.dbtools.android.domain.database.statement;


import net.sqlcipher.database.SQLiteStatement;

public class SQLCipherStatementWrapper implements StatementWrapper {
    private final SQLiteStatement statement;

    public SQLCipherStatementWrapper(SQLiteStatement statement) {
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

    public void execute() {
        statement.execute();
    }

    public long executeInsert() {
        return statement.executeInsert();
    }

    public int executeUpdateDelete() {
        return (int) statement.executeUpdateDelete();
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
