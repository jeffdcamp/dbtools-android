package org.dbtools.android.domain.database.statement;

public interface StatementWrapper {
    void close();
    void execute();
    long executeInsert();
    int executeUpdateDelete();
    long simpleQueryForLong();
    String simpleQueryForString();
    void clearBindings();
    void bindString(int index, String name);
    void bindNull(int index);
    void bindLong(int index, long aLong);
    void bindDouble(int index, double aDouble);
    void bindBlob(int index, byte[] bytes);
}
