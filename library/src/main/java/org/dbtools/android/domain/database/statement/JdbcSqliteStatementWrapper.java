package org.dbtools.android.domain.database.statement;

import org.dbtools.android.domain.database.JdbcSqliteDatabaseWrapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

import javax.annotation.Nullable;

public class JdbcSqliteStatementWrapper implements StatementWrapper {
    private String sql;
    private Object[] bindArgs;
    private PreparedStatement statement;

    public JdbcSqliteStatementWrapper(Connection conn, String sql) {
        try {
            this.sql = sql;
            statement = conn.prepareStatement(sql);
            int paramCount = statement.getParameterMetaData().getParameterCount();
            bindArgs = new Object[paramCount];
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void bindNull(int index) {
        bind(index, null);
    }

    public void bindLong(int index, long value) {
        bind(index, value);
    }

    public void bindDouble(int index, double value) {
        bind(index, value);
    }

    public void bindString(int index, String value) {
        bind(index, value);
    }

    public void bindBlob(int index, byte[] value) {
        bind(index, value);
    }

    public void execute() {
        try {
            bindArgs(statement, bindArgs);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int executeUpdateDelete() {
        try {
            JdbcSqliteDatabaseWrapper.logQuery("Statement Update Delete", sql, bindArgs);
            bindArgs(statement, bindArgs);
            return statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public long executeInsert() {
        try {
            JdbcSqliteDatabaseWrapper.logQuery("Statement Insert", sql, bindArgs);
            bindArgs(statement, bindArgs);
            statement.executeUpdate();

            // get the last inserted id
            ResultSet rs = statement.getGeneratedKeys();
            rs.next();
            return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public long simpleQueryForLong() {
        return executeUpdateDelete();
    }

    public String simpleQueryForString() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public String toString() {
        return "";
    }

    public void bindAllArgsAsStrings(String[] bindArgs) {
        if (bindArgs != null) {
            for (int i = bindArgs.length; i != 0; i--) {
                bindString(i, bindArgs[i - 1]);
            }
        }
    }

    public void clearBindings() {
        Arrays.fill(bindArgs, null);
    }

    private void bind(int index, @Nullable Object value) {
        bindArgs[index - 1] = value;
    }

    public static void bindArgs(PreparedStatement statement, @Nullable Object[] bindArgs) throws SQLException {
        if (bindArgs != null && bindArgs.length > 0) {
            for (int i = 0; i < bindArgs.length; i++) {
                Object arg = bindArgs[i];

                int index = i + 1; // 1 based

                if (arg instanceof Integer) {
                    statement.setInt(index, (Integer) arg);
                } else if (arg instanceof String) {
                    statement.setString(index, (String) arg);
                } else if (arg instanceof Long) {
                    statement.setLong(index, (Long) arg);
                } else if (arg instanceof Float) {
                    statement.setFloat(index, (Float) arg);
                } else if (arg instanceof Double) {
                    statement.setDouble(index, (Double) arg);
                } else if (arg instanceof Boolean) {
                    statement.setBoolean(index, (Boolean) arg);
                }
            }
        }
    }
}
