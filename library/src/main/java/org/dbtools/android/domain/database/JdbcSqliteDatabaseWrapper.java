package org.dbtools.android.domain.database;

import android.database.Cursor;

import org.dbtools.android.domain.database.contentvalues.JdbcDBToolsContentValues;
import org.dbtools.android.domain.database.cursor.JdbcMemoryCursor;
import org.dbtools.android.domain.database.statement.JdbcSqliteStatementWrapper;
import org.dbtools.android.domain.database.statement.StatementWrapper;
import org.dbtools.query.shared.filter.RawFilter;
import org.dbtools.query.sql.SQLQueryBuilder;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

public class JdbcSqliteDatabaseWrapper implements DatabaseWrapper<Connection, JdbcDBToolsContentValues> {

    private Connection conn;
    private Map<String, StatementWrapper> insertStatementMap = new HashMap<>();
    private Map<String, StatementWrapper> updateStatementMap = new HashMap<>();
    private static boolean enableLogging = false;

    public JdbcSqliteDatabaseWrapper() {
        this("jdbc:sqlite::memory:");
    }

    /**
     * Reletive path dbURL:
     * jdbc:sqlite:product.db
     *
     * Full path dbURL:
     * jdbc:sqlite:C:/work/product.db
     *
     * Memory database dbURL:
     * jdbc:sqlite::memory:
     * jdbc:sqlite:
     *
     * @param dbURL
     */
    public JdbcSqliteDatabaseWrapper(String dbURL) {
        try {
//            Class.forName("org.sqlite.JDBC");
            DriverManager.registerDriver(new org.sqlite.JDBC());
        } catch (Exception e) {
            throw new IllegalStateException("Could load sqlite-jdbc driver... is the sqlite-jdbc driver included in the dependencies?", e);
        }

        try {
            conn = DriverManager.getConnection(dbURL);
            if (conn != null) {
                System.out.println("Connected to the database");
                DatabaseMetaData dm = conn.getMetaData();
                System.out.println("Driver name: " + dm.getDriverName());
                System.out.println("Driver version: " + dm.getDriverVersion());
                System.out.println("Product name: " + dm.getDatabaseProductName());
                System.out.println("Product version: " + dm.getDatabaseProductVersion());
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public Connection getDatabase() {
        return conn;
    }

    @Override
    public JdbcDBToolsContentValues newContentValues() {
        return new JdbcDBToolsContentValues();
    }

    @Override
    public void attachDatabase(String toDbPath, String toDbName, String toDbPassword) {
        throw new UnsupportedOperationException("");
    }

    @Override
    public void detachDatabase(String dbName) {
        throw new UnsupportedOperationException("");
    }

    @Override
    public boolean isOpen() {
        try {
            return !conn.isClosed();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public int getVersion() {
        return 0;
    }

    @Override
    public void setVersion(int version) {

    }

    @Override
    public long insert(String table, @Nullable String o, JdbcDBToolsContentValues initialValues) {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("INSERT");
            sql.append(" INTO ");
            sql.append(table);
            sql.append('(');

            Object[] bindArgs = null;
            int size = (initialValues != null && initialValues.size() > 0) ? initialValues.size() : 0;
            if (size > 0) {
                bindArgs = new Object[size];
                int i = 0;
                for (String colName : initialValues.keySet()) {
                    sql.append((i > 0) ? "," : "");
                    sql.append(colName);
                    bindArgs[i++] = initialValues.get(colName);
                }
                sql.append(')');
                sql.append(" VALUES (");
                for (i = 0; i < size; i++) {
                    sql.append((i > 0) ? ",?" : "?");
                }
            }
            sql.append(')');

            String insertSql = sql.toString();
            logQuery("Insert", insertSql, bindArgs);

            PreparedStatement statement = conn.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS);
            JdbcSqliteStatementWrapper.bindArgs(statement, bindArgs);

            try {
                statement.executeUpdate();

                // get the last inserted id
                ResultSet rs = statement.getGeneratedKeys();
                rs.next();
                return rs.getInt(1);
            } finally {
                statement.close();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public int update(String table, JdbcDBToolsContentValues values, @Nullable String where, @Nullable String[] whereArgs) {
        if (values == null || values.size() == 0) {
            throw new IllegalArgumentException("Empty values");
        }

        try {
            StringBuilder sql = new StringBuilder(120);
            sql.append("UPDATE ");
            sql.append(table);
            sql.append(" SET ");

            // move all bind args to one array
            int setValuesSize = values.size();
            int bindArgsSize = (whereArgs == null) ? setValuesSize : (setValuesSize + whereArgs.length);
            Object[] bindArgs = new Object[bindArgsSize];
            int i = 0;
            for (String colName : values.keySet()) {
                sql.append((i > 0) ? "," : "");
                sql.append(colName);
                bindArgs[i++] = values.get(colName);
                sql.append("=?");
            }
            if (whereArgs != null) {
                for (i = setValuesSize; i < bindArgsSize; i++) {
                    bindArgs[i] = whereArgs[i - setValuesSize];
                }
            }
            if (where != null && !where.isEmpty()) {
                sql.append(" WHERE ");
                sql.append(where);
            }

            String updateSql = sql.toString();
            logQuery("Update", updateSql, bindArgs);

            PreparedStatement statement = conn.prepareStatement(sql.toString());
            JdbcSqliteStatementWrapper.bindArgs(statement, bindArgs);

            try {
                return statement.executeUpdate();
            } finally {
                statement.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    @Override
    public int delete(String table, @Nullable String where, @Nullable String[] whereArgs) {
        try {
            String sql = "DELETE FROM " + table;
            if (where != null && !where.isEmpty()) {
                sql += " WHERE " + where;
            }

            logQuery("Delete", sql, whereArgs);

            PreparedStatement statement = conn.prepareStatement(sql);
            JdbcSqliteStatementWrapper.bindArgs(statement, whereArgs);

            try {
                return statement.executeUpdate();
            } finally {
                statement.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    @Override
    public StatementWrapper compileStatement(String sql) {
        return new JdbcSqliteStatementWrapper(conn, sql);
    }

    @Override
    public void execSQL(String sql) {
        execSQL(sql, null);
    }

    @Override
    public void execSQL(String sql, @Nullable  Object[] bindArgs) {
        try {
            logQuery("execSQL", sql, bindArgs);

            PreparedStatement statement = conn.prepareStatement(sql);
            JdbcSqliteStatementWrapper.bindArgs(statement, bindArgs);

            try {
                statement.executeUpdate();
            } finally {
                statement.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    @Nullable
    public Cursor query(String table, String[] columns, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String groupBy, @Nullable String having, @Nullable String orderBy) {
        return query(true, table, columns, selection, selectionArgs, groupBy, having, orderBy, null);
    }

    @Override
    @Nullable
    public Cursor query(boolean distinct, String table, String[] columns, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String groupBy, @Nullable String having, @Nullable String orderBy, @Nullable String limit) {
        SQLQueryBuilder builder = new SQLQueryBuilder()
                .distinct(distinct)
                .table(table)
                .fields(columns)
                .filter(RawFilter.create(selection))
                .groupBy(groupBy)
                .having(having)
                .orderBy(orderBy);

        try {
            String sql = builder.buildQuery();
            logQuery("Query", sql, selectionArgs);

//            PreparedStatement statement = conn.prepareStatement(builder.buildQuery(), ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            PreparedStatement statement = conn.prepareStatement(sql);
            JdbcSqliteStatementWrapper.bindArgs(statement, selectionArgs);

            ResultSet resultSet = statement.executeQuery();
            return new JdbcMemoryCursor(resultSet);
//            return new JdbcCursor(resultSet); // not supported because cursors need to go bidirectional on the ResultSet
        } catch (SQLException e) {
            e.printStackTrace();
        }


        return null;
    }

    @Override
    @Nullable
    public Cursor rawQuery(String rawQuery, @Nullable String[] selectionArgs) {
        try {
            logQuery("Raw Query", rawQuery, selectionArgs);

            PreparedStatement statement = conn.prepareStatement(rawQuery);
            JdbcSqliteStatementWrapper.bindArgs(statement, selectionArgs);

            ResultSet resultSet = statement.executeQuery();
            return new JdbcMemoryCursor(resultSet);
//            return new JdbcCursor(resultSet); // not supported because cursors need to go bidirectional on the ResultSet
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public boolean inTransaction() {
        try {
            return !conn.getAutoCommit();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void beginTransaction() {
        try {
            conn.setAutoCommit(false);
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void endTransaction() {
        try {
            try {
                conn.commit();
            } catch (SQLException e1) {
                conn.rollback();
                e1.printStackTrace();
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setTransactionSuccessful() {
        // do nothing
    }

    @Override
    public void close() {
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // cleanup statements
        DatabaseWrapperUtil.closeStatements(insertStatementMap);
        DatabaseWrapperUtil.closeStatements(updateStatementMap);
    }

    @Override
    public StatementWrapper getInsertStatement(String tableName, String sql) {
        return DatabaseWrapperUtil.createStatement(this, tableName, sql, insertStatementMap);
    }

    @Override
    public StatementWrapper getUpdateStatement(String tableName, String sql) {
        return DatabaseWrapperUtil.createStatement(this, tableName, sql, updateStatementMap);
    }

    public static boolean isEnableLogging() {
        return enableLogging;
    }

    public static void setEnableLogging(boolean enableLogging) {
        JdbcSqliteDatabaseWrapper.enableLogging = enableLogging;
    }

    public static void logQuery(String name, String sql, Object[] args) {
        if (enableLogging) {
            System.out.println(name + " sql: [" + sql + "] args: [" + getTextLogArgs(args) + "]");
        }
    }

    public static String getTextLogArgs(@Nullable Object[] args) {
        if (args == null) {
            return "";
        }

        StringBuilder builder = new StringBuilder();
        for (Object arg : args) {
            if (builder.length() > 0) {
                builder.append(", ");
            }
            builder.append(arg);
        }

        return builder.toString();
    }
}
