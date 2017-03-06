package org.dbtools.android.domain;

public abstract class AndroidBaseManagerReadOnly<T extends AndroidBaseRecord> extends AndroidBaseManager<T> {

    public AndroidBaseManagerReadOnly(AndroidDatabaseManager androidDatabaseManager) {
        super(androidDatabaseManager);
    }
}
