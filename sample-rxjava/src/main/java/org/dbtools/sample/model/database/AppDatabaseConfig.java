
package org.dbtools.sample.model.database;

import android.app.Application;
import org.dbtools.android.domain.config.DatabaseConfig;
import org.dbtools.android.domain.AndroidDatabase;
import org.dbtools.android.domain.AndroidDatabaseBaseManager;
import org.dbtools.android.domain.database.DatabaseWrapper;
import org.dbtools.android.domain.database.AndroidDatabaseWrapper;
import org.dbtools.android.domain.database.contentvalues.AndroidDBToolsContentValues;
import org.dbtools.android.domain.database.contentvalues.DBToolsContentValues;
import org.dbtools.android.domain.log.DBToolsAndroidLogger;
import org.dbtools.android.domain.log.DBToolsLogger;


public class AppDatabaseConfig implements DatabaseConfig {

    private Application application;

    public AppDatabaseConfig(Application application) {
        this.application = application;
    }

    public void identifyDatabases(AndroidDatabaseBaseManager databaseManager) {
        databaseManager.addDatabase(application, DatabaseManagerConst.MAIN_DATABASE_NAME, DatabaseManager.MAIN_TABLES_VERSION, DatabaseManager.MAIN_VIEWS_VERSION);
    }

    public DatabaseWrapper createNewDatabaseWrapper(AndroidDatabase androidDatabase) {
        return new AndroidDatabaseWrapper(androidDatabase.getPath());
    }

    public DBToolsContentValues createNewDBToolsContentValues() {
        return new AndroidDBToolsContentValues();
    }

    public DBToolsLogger createNewDBToolsLogger() {
        return new DBToolsAndroidLogger();
    }


}