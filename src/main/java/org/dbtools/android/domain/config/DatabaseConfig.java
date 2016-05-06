package org.dbtools.android.domain.config;

import org.dbtools.android.domain.AndroidDatabase;
import org.dbtools.android.domain.AndroidDatabaseBaseManager;
import org.dbtools.android.domain.database.DatabaseWrapper;
import org.dbtools.android.domain.database.contentvalues.DBToolsContentValues;
import org.dbtools.android.domain.log.DBToolsLogger;

public interface DatabaseConfig {
    DatabaseWrapper createNewDatabaseWrapper(AndroidDatabase androidDatabase);
    /**
     * Identify what databases will be used with this app.  This method should call addDatabase(...) for each database
     */
    void identifyDatabases(AndroidDatabaseBaseManager databaseManager);
    DBToolsLogger createNewDBToolsLogger();
    DBToolsContentValues createNewDBToolsContentValues();
}
