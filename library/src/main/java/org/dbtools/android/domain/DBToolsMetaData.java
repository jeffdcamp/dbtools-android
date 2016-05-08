package org.dbtools.android.domain;

@SuppressWarnings("UnusedDeclaration")
public class DBToolsMetaData {
    public static final String TABLE = "DBTOOLS_METADATA";
    public static final String C_KEY = "DB_KEY";
    public static final String C_VALUE = "DB_VALUE";

    public static final String KEY_SELECTION = C_KEY + " = ? ";

    public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE + " (" +
            "_id INTEGER PRIMARY KEY  AUTOINCREMENT," +
            "DB_KEY TEXT," +
            "DB_VALUE TEXT" +
            ");";
    public static final String DROP_TABLE = "DROP TABLE IF EXISTS DATABASE_METADATA;";

}
