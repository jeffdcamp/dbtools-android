/*
 * IndividualViewBaseManager.java
 *
 * GENERATED FILE - DO NOT EDIT
 * 
 */



package org.dbtools.sample.model.database.main.individualview;

import org.dbtools.sample.model.database.DatabaseManager;
import org.dbtools.android.domain.RxAndroidBaseManagerReadOnly;


@SuppressWarnings("all")
public abstract class IndividualViewBaseManager extends RxAndroidBaseManagerReadOnly<IndividualView> {


    public IndividualViewBaseManager(DatabaseManager databaseManager) {
        super(databaseManager);
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
    public String getPrimaryKey() {
        return "<NO_PRIMARY_KEY_ON_VIEWS>";
    }

    @javax.annotation.Nonnull
    public String getDropSql() {
        return IndividualViewManager.DROP_VIEW;
    }

    @javax.annotation.Nonnull
    public String getCreateSql() {
        return IndividualViewManager.CREATE_VIEW;
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