/*
 * IndividualQueryBaseRecord.java
 *
 * GENERATED FILE - DO NOT EDIT
 * CHECKSTYLE:OFF
 * 
 */



package org.dbtools.sample.model.database.main.individualquery;

import org.dbtools.android.domain.AndroidBaseRecord;
import org.dbtools.android.domain.database.statement.StatementWrapper;
import org.dbtools.android.domain.database.contentvalues.DBToolsContentValues;
import android.database.Cursor;


@SuppressWarnings("all")
public abstract class IndividualQueryBaseRecord extends AndroidBaseRecord {

    private Long id = null;
    private String name = "";

    public IndividualQueryBaseRecord() {
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
        return IndividualQueryConst.ALL_COLUMNS.clone();
    }

    public String[] getAllColumnsFull() {
        return IndividualQueryConst.ALL_COLUMNS_FULL.clone();
    }

    @Override
    public void getContentValues(DBToolsContentValues values) {
        values.put(IndividualQueryConst.C_ID, id);
        values.put(IndividualQueryConst.C_NAME, name);
    }

    @Override
    public Object[] getValues() {
        Object[] values = new Object[]{
            id,
            name,
        };
        return values;
    }

    public IndividualQuery copy() {
        IndividualQuery copy = new IndividualQuery();
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
        id = values.getAsLong(IndividualQueryConst.C_ID);
        name = values.getAsString(IndividualQueryConst.C_NAME);
    }

    @Override
    public void setContent(Cursor cursor) {
        id = !cursor.isNull(cursor.getColumnIndexOrThrow(IndividualQueryConst.C_ID)) ? cursor.getLong(cursor.getColumnIndexOrThrow(IndividualQueryConst.C_ID)) : null;
        name = cursor.getString(cursor.getColumnIndexOrThrow(IndividualQueryConst.C_NAME));
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