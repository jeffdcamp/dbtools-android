package org.dbtools.android.domain;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.dbtools.android.domain.database.DatabaseWrapper;
import org.dbtools.android.domain.database.contentvalues.DBToolsContentValues;

interface NotifiableManager {
    @NonNull
    String getTableName();
    void notifyTableListeners(@NonNull String databaseName, boolean forceNotify, @Nullable DatabaseWrapper<? super AndroidBaseRecord, ? super DBToolsContentValues<?>> databaseWrapper, @NonNull DatabaseTableChange databaseTableChange);
}
