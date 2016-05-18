package org.dbtools.android.domain.database;

import org.dbtools.android.domain.database.statement.StatementWrapper;

import java.util.Map;

public class DatabaseWrapperUtil {
    public static StatementWrapper createStatement(DatabaseWrapper databaseWrapper, String tableName, String sql, Map<String, StatementWrapper> statementMap) {
        StatementWrapper statement = statementMap.get(tableName);
        if (statement == null) {
            statement = databaseWrapper.compileStatement(sql);
            statementMap.put(tableName, statement);
        }
        return statement;
    }

    public static void closeStatements(Map<String, StatementWrapper> statementMap) {
        for (StatementWrapper statement : statementMap.values()) {
            statement.close();
        }
        statementMap.clear();
    }
}
