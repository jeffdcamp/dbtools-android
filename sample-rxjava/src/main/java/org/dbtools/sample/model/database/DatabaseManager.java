
package org.dbtools.sample.model.database;

import org.dbtools.android.domain.AndroidDatabase;
import org.dbtools.android.domain.config.DatabaseConfig;


public class DatabaseManager extends DatabaseBaseManager {

    public static final int MAIN_TABLES_VERSION = 1;
    public static final int MAIN_VIEWS_VERSION = 1;

    public DatabaseManager(DatabaseConfig databaseConfig) {
        super(databaseConfig);
    }

    public void onUpgrade(AndroidDatabase androidDatabase, int oldVersion, int newVersion) {
        String databaseName = androidDatabase.getName();
        getLogger().i(TAG, "Upgrading database [" + databaseName + "] from version " + oldVersion + " to " + newVersion);
    }

    public void onUpgradeViews(AndroidDatabase androidDatabase, int oldVersion, int newVersion) {
        String databaseName = androidDatabase.getName();
        getLogger().i(TAG, "Upgrading database [" + databaseName + "] VIEWS from version " + oldVersion + " to " + newVersion);
        // automatically drop/create views
        super.onUpgradeViews(androidDatabase, oldVersion, newVersion);
    }


}