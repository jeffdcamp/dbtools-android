/*
 * IndividualDataBaseRecord.java
 *
 * GENERATED FILE - DO NOT EDIT
 * CHECKSTYLE:OFF
 * 
 */



package org.dbtools.sample.model.database.main.individualdata;

import android.database.Cursor;

import org.dbtools.android.domain.AndroidBaseRecord;
import org.dbtools.android.domain.database.contentvalues.DBToolsContentValues;
import org.dbtools.android.domain.database.statement.StatementWrapper;


@SuppressWarnings("all")
public abstract class IndividualDataBaseRecord extends AndroidBaseRecord {

    private Long externalId = null;
    private Integer typeId = null;
    private String name = "";

    public IndividualDataBaseRecord(IndividualData record) {
        this.externalId = record.getExternalId();
        this.typeId = record.getTypeId();
        this.name = record.getName();
    }

    public IndividualDataBaseRecord() {
    }

    @Override
    public String[] getAllColumns() {
        return IndividualDataConst.ALL_COLUMNS.clone();
    }

    public String[] getAllColumnsFull() {
        return IndividualDataConst.ALL_COLUMNS_FULL.clone();
    }

    @Override
    public void getContentValues(DBToolsContentValues values) {
        values.put(IndividualDataConst.C_EXTERNAL_ID, externalId);
        values.put(IndividualDataConst.C_TYPE_ID, typeId);
        values.put(IndividualDataConst.C_NAME, name);
    }

    @Override
    public Object[] getValues() {
        Object[] values = new Object[]{
            externalId,
            typeId,
            name,
        };
        return values;
    }

    public IndividualData copy() {
        IndividualData copy = new IndividualData();
        copy.setExternalId(externalId);
        copy.setTypeId(typeId);
        copy.setName(name);
        return copy;
    }

    @Override
    public void bindInsertStatement(StatementWrapper statement) {
        if (externalId != null) {
            statement.bindLong(1, externalId);
        } else {
            statement.bindNull(1);
        }
        if (typeId != null) {
            statement.bindLong(2, typeId);
        } else {
            statement.bindNull(2);
        }
        if (name != null) {
            statement.bindString(3, name);
        } else {
            statement.bindNull(3);
        }
    }

    @Override
    public void bindUpdateStatement(StatementWrapper statement) {
        if (externalId != null) {
            statement.bindLong(1, externalId);
        } else {
            statement.bindNull(1);
        }
        if (typeId != null) {
            statement.bindLong(2, typeId);
        } else {
            statement.bindNull(2);
        }
        if (name != null) {
            statement.bindString(3, name);
        } else {
            statement.bindNull(3);
        }
    }

    public void setContent(DBToolsContentValues values) {
        externalId = values.getAsLong(IndividualDataConst.C_EXTERNAL_ID);
        typeId = values.getAsInteger(IndividualDataConst.C_TYPE_ID);
        name = values.getAsString(IndividualDataConst.C_NAME);
    }

    @Override
    public void setContent(Cursor cursor) {
        externalId = !cursor.isNull(cursor.getColumnIndexOrThrow(IndividualDataConst.C_EXTERNAL_ID)) ? cursor.getLong(cursor.getColumnIndexOrThrow(IndividualDataConst.C_EXTERNAL_ID)) : null;
        typeId = !cursor.isNull(cursor.getColumnIndexOrThrow(IndividualDataConst.C_TYPE_ID)) ? cursor.getInt(cursor.getColumnIndexOrThrow(IndividualDataConst.C_TYPE_ID)) : null;
        name = !cursor.isNull(cursor.getColumnIndexOrThrow(IndividualDataConst.C_NAME)) ? cursor.getString(cursor.getColumnIndexOrThrow(IndividualDataConst.C_NAME)) : null;
    }

    public boolean isNewRecord() {
        return getPrimaryKeyId() <= 0;
    }

    @javax.annotation.Nullable
    public Long getExternalId() {
        return externalId;
    }

    public void setExternalId(@javax.annotation.Nullable Long externalId) {
        this.externalId = externalId;
    }

    @javax.annotation.Nullable
    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(@javax.annotation.Nullable Integer typeId) {
        this.typeId = typeId;
    }

    @javax.annotation.Nullable
    public String getName() {
        return name;
    }

    public void setName(@javax.annotation.Nullable String name) {
        this.name = name;
    }


}