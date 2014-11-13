package org.dbtools.android.domain.event;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.Set;

public class DatabaseEndTransactionEvent extends DatabaseChangeEvent {
    private Set<String> tablesChanged = new HashSet<String>();
    private boolean success;

    public DatabaseEndTransactionEvent(boolean success, @Nonnull Set<String> tablesChanged) {
        super("");
        this.tablesChanged.addAll(tablesChanged);
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
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
