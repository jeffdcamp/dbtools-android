/*
 * IndividualViewBaseRecord.java
 *
 * GENERATED FILE - DO NOT EDIT
 * CHECKSTYLE:OFF
 * 
 */



package org.dbtools.sample.model.database.main.individualview;

import org.dbtools.android.domain.AndroidBaseRecord;
import org.dbtools.android.domain.database.statement.StatementWrapper;
import org.dbtools.android.domain.database.contentvalues.DBToolsContentValues;
import android.database.Cursor;


@SuppressWarnings("all")
public abstract class IndividualViewBaseRecord extends AndroidBaseRecord {

    private Long id = null;
    private String name = "";

    public IndividualViewBaseRecord() {
    }

    @Override
    public String getIdColumnName() {
        return null;
    }

    @Override
    public long getPrimaryKeyId() {
        return 0;
    }

    @Override
    public void setPrimaryKeyId(long id) {
    }

    @Override
    public String[] getAllColumns() {
        return IndividualViewConst.ALL_COLUMNS.clone();
    }

    public String[] getAllColumnsFull() {
        return IndividualViewConst.ALL_COLUMNS_FULL.clone();
    }

    @Override
    public void getContentValues(DBToolsContentValues values) {
        values.put(IndividualViewConst.C_ID, id);
        values.put(IndividualViewConst.C_NAME, name);
    }

    @Override
    public Object[] getValues() {
        Object[] values = new Object[]{
            id,
            name,
        };
        return values;
    }

    public IndividualView copy() {
        IndividualView copy = new IndividualView();
        copy.setId(id);
        copy.setName(name);
        return copy;
    }

    @Override
    public void bindInsertStatement(StatementWrapper statement) {
        if (id != null) {
            statement.bindLong(1, id);
        } else {
            statement.bindNull(1);
        }
        statement.bindString(2, name);
    }

    @Override
    public void bindUpdateStatement(StatementWrapper statement) {
        if (id != null) {
            statement.bindLong(1, id);
        } else {
            statement.bindNull(1);
        }
        statement.bindString(2, name);
    }

    public void setContent(DBToolsContentValues values) {
        id = values.getAsLong(IndividualViewConst.C_ID);
        name = values.getAsString(IndividualViewConst.C_NAME);
    }

    @Override
    public void setContent(Cursor cursor) {
        id = !cursor.isNull(cursor.getColumnIndexOrThrow(IndividualViewConst.C_ID)) ? cursor.getLong(cursor.getColumnIndexOrThrow(IndividualViewConst.C_ID)) : null;
        name = cursor.getString(cursor.getColumnIndexOrThrow(IndividualViewConst.C_NAME));
    }

    public boolean isNewRecord() {
        return getPrimaryKeyId() <= 0;
    }

    @javax.annotation.Nullable
    public Long getId() {
        return id;
    }

    public void setId(@javax.annotation.Nullable Long id) {
        this.id = id;
    }

    @javax.annotation.Nonnull
    public String getName() {
        return name;
    }

    public void setName(@javax.annotation.Nonnull String name) {
        this.name = name;
    }


}