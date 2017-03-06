/*
 * IndividualDataBaseManager.java
 *
 * GENERATED FILE - DO NOT EDIT
 * 
 */



package org.dbtools.sample.model.database.main.individualdata;

import org.dbtools.sample.model.database.DatabaseManager;
import org.dbtools.android.domain.AndroidBaseManagerWritable;


@SuppressWarnings("all")
public abstract class IndividualDataBaseManager extends AndroidBaseManagerWritable<IndividualData> {


    public IndividualDataBaseManager(DatabaseManager databaseManager) {
        super(databaseManager);
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
    public String getPrimaryKey() {
        return "NO_PRIMARY_KEY";
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