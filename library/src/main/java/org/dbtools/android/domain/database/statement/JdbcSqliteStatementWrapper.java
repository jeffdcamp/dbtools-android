package org.dbtools.android.domain.database.statement;

import javax.annotation.Nullable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JdbcSqliteStatementWrapper implements StatementWrapper {
    private List<Object> bindArgs = new ArrayList<Object>();
    private PreparedStatement statement;
    private Connection conn;
    private String sql;

    public JdbcSqliteStatementWrapper(Connection conn, String sql) {
        this.conn = conn;
        this.sql = sql;
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
            statement = conn.prepareStatement(sql);
            bindArgs(statement, bindArgs.toArray());
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int executeUpdateDelete() {
        try {
            statement = conn.prepareStatement(sql);
            bindArgs(statement, bindArgs.toArray());
            return statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public long executeInsert() {
        return executeUpdateDelete();
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
        if (bindArgs != null) {
            bindArgs.clear();
        }
    }

    private void bind(int index, @Nullable Object value) {
        bindArgs.add(index -1, value);
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
