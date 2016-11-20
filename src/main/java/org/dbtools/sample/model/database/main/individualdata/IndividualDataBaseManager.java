/*
 * IndividualDataBaseManager.java
 *
 * GENERATED FILE - DO NOT EDIT
 * 
 */



package org.dbtools.sample.model.database.main.individualdata;

import org.dbtools.android.domain.AndroidBaseManagerWritable;
import org.dbtools.android.domain.database.DatabaseWrapper;
import org.dbtools.sample.model.database.DatabaseManager;


@SuppressWarnings("all")
public abstract class IndividualDataBaseManager extends AndroidBaseManagerWritable<IndividualData> {

    private DatabaseManager databaseManager;

    public IndividualDataBaseManager(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    @javax.annotation.Nonnull
    public String getDatabaseName() {
        return IndividualDataConst.DATABASE;
    }

    @javax.annotation.Nonnull
    public IndividualData newRecord() {
        return new IndividualData();
    }

    @javax.annotation.Nonnull
    public String getTableName() {
        return IndividualDataConst.TABLE;
    }

    @javax.annotation.Nonnull
    public String[] getAllColumns() {
        return IndividualDataConst.ALL_COLUMNS;
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
        return IndividualDataConst.PRIMARY_KEY_COLUMN;
    }

    @javax.annotation.Nonnull
    public String getDropSql() {
        return IndividualDataConst.DROP_TABLE;
    }

    @javax.annotation.Nonnull
    public String getCreateSql() {
        return IndividualDataConst.CREATE_TABLE;
    }

    @javax.annotation.Nonnull
    public String getInsertSql() {
        return IndividualDataConst.INSERT_STATEMENT;
    }

    @javax.annotation.Nonnull
    public String getUpdateSql() {
        return IndividualDataConst.UPDATE_STATEMENT;
    }


}