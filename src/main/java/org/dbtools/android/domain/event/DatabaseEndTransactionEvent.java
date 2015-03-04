package org.dbtools.android.domain.event;

import javax.annotation.Nonnull;
import java.util.Set;

public class DatabaseEndTransactionEvent extends DatabaseChangeEvent {
    private String databaseName;
    private Set<String> tablesChanged;
    private boolean success;

    public DatabaseEndTransactionEvent(boolean success, @Nonnull String databaseName, @Nonnull Set<String> tablesChanged) {
        super("");
        this.databaseName = databaseName;
        this.tablesChanged = tablesChanged;
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public Set<String> getTablesChanged() {
        return tablesChanged;
    }

    public boolean containsTable(String tableName) {
        return tablesChanged.contains(tableName);
    }

    public String getAllTableName() {
        StringBuilder sb = new StringBuilder();

        for (String tableName : tablesChanged) {
            if (sb.length() > 0) {
                sb.append(", ");
            }
            sb.append(tableName);
        }

        return sb.toString();
    }
}
