package org.dbtools.android.domain;

import android.database.sqlite.SQLiteDatabase;

public class AndroidDatabase {
    private boolean encrypted = false;
    private String name;
    private String path;
    private int version;

    private String password;

    private SQLiteDatabase sqLiteDatabase;
    private net.sqlcipher.database.SQLiteDatabase secureSqLiteDatabase;

    public AndroidDatabase(String name, String path, int version) {
        this.name = name;
        this.path = path;
        this.version = version;
    }

    public AndroidDatabase(String name, String password, String path, int version) {
        this.name = name;
        this.password = password;
        this.encrypted = password != null;
        this.path = path;
        this.version = version;
    }

    public boolean isEncrypted() {
        return encrypted;
    }

    public void setEncrypted(boolean encrypted) {
        this.encrypted = encrypted;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    public int getVersion() {
        return version;
    }

    public String getPassword() {
        return password;
    }

    public SQLiteDatabase getSqLiteDatabase() {
        return sqLiteDatabase;
    }

    public void setSqLiteDatabase(SQLiteDatabase sqLiteDatabase) {
        this.sqLiteDatabase = sqLiteDatabase;
    }

    public net.sqlcipher.database.SQLiteDatabase getSecureSqLiteDatabase() {
        return secureSqLiteDatabase;
    }

    public void setSecureSqLiteDatabase(net.sqlcipher.database.SQLiteDatabase secureSqLiteDatabase) {
        this.secureSqLiteDatabase = secureSqLiteDatabase;
    }
}
