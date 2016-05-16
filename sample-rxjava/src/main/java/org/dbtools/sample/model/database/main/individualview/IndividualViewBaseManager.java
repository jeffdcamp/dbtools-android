/*
 * IndividualViewBaseManager.java
 *
 * GENERATED FILE - DO NOT EDIT
 * 
 */



package org.dbtools.sample.model.database.main.individualview;

import org.dbtools.sample.model.database.DatabaseManager;
import org.dbtools.android.domain.database.DatabaseWrapper;
import org.dbtools.android.domain.RxAndroidBaseManagerReadOnly;


@SuppressWarnings("all")
public abstract class IndividualViewBaseManager extends RxAndroidBaseManagerReadOnly<IndividualView> {

    private DatabaseManager databaseManager;

    public IndividualViewBaseManager(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    @javax.annotation.Nonnull
    public String getDatabaseName() {
        return IndividualViewConst.DATABASE;
    }

    @javax.annotation.Nonnull
    public IndividualView newRecord() {
        return new IndividualView();
    }

    @javax.annotation.Nonnull
    public String getTableName() {
        return IndividualViewConst.TABLE;
    }

    @javax.annotation.Nonnull
    public String[] getAllColumns() {
        return IndividualViewConst.ALL_COLUMNS;
    }

    @javax.annotation.Nonnull
    public DatabaseWrapper getReadableDatabase(@javax.annotation.Nonnull String databaseName) {
        return databaseManager.getReadableDatabase(databaseName);
    }

    @javax.annotation.Nonnull
    public DatabaseWrapper getReadableDatabase() {
        return databaseManager.getReadableDatabase(getDatabaseName());
    }

    @javax.annotation.Nonnull
    public DatabaseWrapper getWritableDatabase(@javax.annotation.Nonnull String databaseName) {
        return databaseManager.getWritableDatabase(databaseName);
    }

    @javax.annotation.Nonnull
    public DatabaseWrapper getWritableDatabase() {
        return databaseManager.getWritableDatabase(getDatabaseName());
    }

    @javax.annotation.Nonnull
    public org.dbtools.android.domain.AndroidDatabase getAndroidDatabase(@javax.annotation.Nonnull String databaseName) {
        return databaseManager.getDatabase(databaseName);
    }

    public org.dbtools.android.domain.config.DatabaseConfig getDatabaseConfig() {
        return databaseManager.getDatabaseConfig();
    }

    @javax.annotation.Nonnull
    public String getPrimaryKey() {
        return null;
    }

    @javax.annotation.Nonnull
    public String getDropSql() {
        return IndividualView.DROP_VIEW;
    }

    @javax.annotation.Nonnull
    public String getCreateSql() {
        return IndividualView.CREATE_VIEW;
    }

    @javax.annotation.Nonnull
    public String getInsertSql() {
        return "";
    }

    @javax.annotation.Nonnull
    public String getUpdateSql() {
        return "";
    }


}