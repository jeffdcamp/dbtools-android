package org.dbtools.sample;

import android.app.Application;

import org.dbtools.sample.model.database.AppDatabaseConfig;
import org.dbtools.sample.model.database.DatabaseManager;
import org.dbtools.sample.model.database.main.MainDatabaseManagers;

public class App extends Application {

    private static DatabaseManager databaseManager;

    @Override
    public void onCreate() {
        super.onCreate();

        databaseManager = new DatabaseManager(new AppDatabaseConfig(this));
        MainDatabaseManagers.init(databaseManager);
    }
}
