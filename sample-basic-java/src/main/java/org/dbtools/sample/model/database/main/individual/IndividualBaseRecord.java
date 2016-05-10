/*
 * IndividualBaseRecord.java
 *
 * GENERATED FILE - DO NOT EDIT
 * CHECKSTYLE:OFF
 * 
 */



package org.dbtools.sample.model.database.main.individual;

import android.database.Cursor;

import org.dbtools.android.domain.AndroidBaseRecord;
import org.dbtools.android.domain.database.contentvalues.DBToolsContentValues;
import org.dbtools.android.domain.database.statement.StatementWrapper;
import org.dbtools.sample.model.database.main.individualtype.IndividualType;


@SuppressWarnings("all")
public abstract class IndividualBaseRecord extends AndroidBaseRecord {

    private long id = 0;
    private IndividualType individualType = IndividualType.HEAD;
    private String firstName = "";
    private String lastName = "";
    private java.util.Date sampleDateTime = null;
    private java.util.Date birthDate = null;
    private java.util.Date lastModified = null;
    private Integer number = null;
    private String phone = "";
    private String email = "";
    private byte[] data = null;
    private Float amount1 = null;
    private Double amount2 = null;
    private Boolean enabled = false;
    private Long spouseIndividualId = null;

    public IndividualBaseRecord() {
    }

    @Override
    public String getIdColumnName() {
        return IndividualConst.C_ID;
    }

    @Override
    public long getPrimaryKeyId() {
        return id;
    }

    @Override
    public void setPrimaryKeyId(long id) {
        this.id = id;
    }

    @Override
    public String[] getAllColumns() {
        return IndividualConst.ALL_COLUMNS.clone();
    }

    public String[] getAllColumnsFull() {
        return IndividualConst.ALL_COLUMNS_FULL.clone();
    }

    @Override
    public void getContentValues(DBToolsContentValues values) {
        values.put(IndividualConst.C_INDIVIDUAL_TYPE, individualType.ordinal());
        values.put(IndividualConst.C_FIRST_NAME, firstName);
        values.put(IndividualConst.C_LAST_NAME, lastName);
        values.put(IndividualConst.C_SAMPLE_DATE_TIME, org.dbtools.android.domain.date.DBToolsDateFormatter.dateToDBString(sampleDateTime));
        values.put(IndividualConst.C_BIRTH_DATE, org.dbtools.android.domain.date.DBToolsDateFormatter.dateToDBString(birthDate));
        values.put(IndividualConst.C_LAST_MODIFIED, org.dbtools.android.domain.date.DBToolsDateFormatter.dateToLong(lastModified));
        values.put(IndividualConst.C_NUMBER, number);
        values.put(IndividualConst.C_PHONE, phone);
        values.put(IndividualConst.C_EMAIL, email);
        values.put(IndividualConst.C_DATA, data);
        values.put(IndividualConst.C_AMOUNT1, amount1);
        values.put(IndividualConst.C_AMOUNT2, amount2);
        values.put(IndividualConst.C_ENABLED, enabled != null ? (enabled ? 1 : 0) : 0);
        values.put(IndividualConst.C_SPOUSE_INDIVIDUAL_ID, spouseIndividualId);
    }

    @Override
    public Object[] getValues() {
        Object[] values = new Object[]{
            id,
            individualType.ordinal(),
            firstName,
            lastName,
            org.dbtools.android.domain.date.DBToolsDateFormatter.dateToDBString(sampleDateTime),
            org.dbtools.android.domain.date.DBToolsDateFormatter.dateToDBString(birthDate),
            org.dbtools.android.domain.date.DBToolsDateFormatter.dateToLong(lastModified),
            number,
            phone,
            email,
            data,
            amount1,
            amount2,
            enabled != null ? (enabled ? 1 : 0) : 0,
            spouseIndividualId,
        };
        return values;
    }

    public Individual copy() {
        Individual copy = new Individual();
        copy.setId(id);
        copy.setIndividualType(individualType);
        copy.setFirstName(firstName);
        copy.setLastName(lastName);
        copy.setSampleDateTime(sampleDateTime != null ? new java.util.Date(sampleDateTime.getTime()) : null );
        copy.setBirthDate(birthDate != null ? new java.util.Date(birthDate.getTime()) : null );
        copy.setLastModified(lastModified != null ? new java.util.Date(lastModified.getTime()) : null );
        copy.setNumber(number);
        copy.setPhone(phone);
        copy.setEmail(email);
        copy.setData(data);
        copy.setAmount1(amount1);
        copy.setAmount2(amount2);
        copy.setEnabled(enabled);
        copy.setSpouseIndividualId(spouseIndividualId);
        return copy;
    }

    @Override
    public void bindInsertStatement(StatementWrapper statement) {
        statement.bindLong(1, individualType.ordinal());
        statement.bindString(2, firstName);
        statement.bindString(3, lastName);
        if (sampleDateTime != null) {
            statement.bindString(4, org.dbtools.android.domain.date.DBToolsDateFormatter.dateToDBString(sampleDateTime));
        } else {
            statement.bindNull(4);
        }
        if (birthDate != null) {
            statement.bindString(5, org.dbtools.android.domain.date.DBToolsDateFormatter.dateToDBString(birthDate));
        } else {
            statement.bindNull(5);
        }
        if (lastModified != null) {
            statement.bindLong(6, org.dbtools.android.domain.date.DBToolsDateFormatter.dateToLong(lastModified));
        } else {
            statement.bindNull(6);
        }
        if (number != null) {
            statement.bindLong(7, number);
        } else {
            statement.bindNull(7);
        }
        if (phone != null) {
            statement.bindString(8, phone);
        } else {
            statement.bindNull(8);
        }
        if (email != null) {
            statement.bindString(9, email);
        } else {
            statement.bindNull(9);
        }
        if (data != null) {
            statement.bindBlob(10, data);
        } else {
            statement.bindNull(10);
        }
        if (amount1 != null) {
            statement.bindDouble(11, amount1);
        } else {
            statement.bindNull(11);
        }
        if (amount2 != null) {
            statement.bindDouble(12, amount2);
        } else {
            statement.bindNull(12);
        }
        if (enabled != null) {
            statement.bindLong(13, enabled != null ? (enabled ? 1 : 0) : 0);
        } else {
            statement.bindNull(13);
        }
        if (spouseIndividualId != null) {
            statement.bindLong(14, spouseIndividualId);
        } else {
            statement.bindNull(14);
        }
    }

    @Override
    public void bindUpdateStatement(StatementWrapper statement) {
        statement.bindLong(1, individualType.ordinal());
        statement.bindString(2, firstName);
        statement.bindString(3, lastName);
        if (sampleDateTime != null) {
            statement.bindString(4, org.dbtools.android.domain.date.DBToolsDateFormatter.dateToDBString(sampleDateTime));
        } else {
            statement.bindNull(4);
        }
        if (birthDate != null) {
            statement.bindString(5, org.dbtools.android.domain.date.DBToolsDateFormatter.dateToDBString(birthDate));
        } else {
            statement.bindNull(5);
        }
        if (lastModified != null) {
            statement.bindLong(6, org.dbtools.android.domain.date.DBToolsDateFormatter.dateToLong(lastModified));
        } else {
            statement.bindNull(6);
        }
        if (number != null) {
            statement.bindLong(7, number);
        } else {
            statement.bindNull(7);
        }
        if (phone != null) {
            statement.bindString(8, phone);
        } else {
            statement.bindNull(8);
        }
        if (email != null) {
            statement.bindString(9, email);
        } else {
            statement.bindNull(9);
        }
        if (data != null) {
            statement.bindBlob(10, data);
        } else {
            statement.bindNull(10);
        }
        if (amount1 != null) {
            statement.bindDouble(11, amount1);
        } else {
            statement.bindNull(11);
        }
        if (amount2 != null) {
            statement.bindDouble(12, amount2);
        } else {
            statement.bindNull(12);
        }
        if (enabled != null) {
            statement.bindLong(13, enabled != null ? (enabled ? 1 : 0) : 0);
        } else {
            statement.bindNull(13);
        }
        if (spouseIndividualId != null) {
            statement.bindLong(14, spouseIndividualId);
        } else {
            statement.bindNull(14);
        }
        statement.bindLong(15, id);
    }

    public void setContent(DBToolsContentValues values) {
        individualType = IndividualType.values()[values.getAsInteger(IndividualConst.C_INDIVIDUAL_TYPE)];
        firstName = values.getAsString(IndividualConst.C_FIRST_NAME);
        lastName = values.getAsString(IndividualConst.C_LAST_NAME);
        sampleDateTime = org.dbtools.android.domain.date.DBToolsDateFormatter.dbStringToDate(values.getAsString(IndividualConst.C_SAMPLE_DATE_TIME));
        birthDate = org.dbtools.android.domain.date.DBToolsDateFormatter.dbStringToDate(values.getAsString(IndividualConst.C_BIRTH_DATE));
        lastModified = new java.util.Date(values.getAsLong(IndividualConst.C_LAST_MODIFIED));
        number = values.getAsInteger(IndividualConst.C_NUMBER);
        phone = values.getAsString(IndividualConst.C_PHONE);
        email = values.getAsString(IndividualConst.C_EMAIL);
        data = values.getAsByteArray(IndividualConst.C_DATA);
        amount1 = values.getAsFloat(IndividualConst.C_AMOUNT1);
        amount2 = values.getAsDouble(IndividualConst.C_AMOUNT2);
        enabled = values.getAsBoolean(IndividualConst.C_ENABLED);
        spouseIndividualId = values.getAsLong(IndividualConst.C_SPOUSE_INDIVIDUAL_ID);
    }

    @Override
    public void setContent(Cursor cursor) {
        id = cursor.getLong(cursor.getColumnIndexOrThrow(IndividualConst.C_ID));
        individualType = IndividualType.values()[cursor.getInt(cursor.getColumnIndexOrThrow(IndividualConst.C_INDIVIDUAL_TYPE))];
        firstName = cursor.getString(cursor.getColumnIndexOrThrow(IndividualConst.C_FIRST_NAME));
        lastName = cursor.getString(cursor.getColumnIndexOrThrow(IndividualConst.C_LAST_NAME));
        sampleDateTime = org.dbtools.android.domain.date.DBToolsDateFormatter.dbStringToDate(cursor.getString(cursor.getColumnIndexOrThrow(IndividualConst.C_SAMPLE_DATE_TIME)));
        birthDate = org.dbtools.android.domain.date.DBToolsDateFormatter.dbStringToDate(cursor.getString(cursor.getColumnIndexOrThrow(IndividualConst.C_BIRTH_DATE)));
        lastModified = !cursor.isNull(cursor.getColumnIndexOrThrow(IndividualConst.C_LAST_MODIFIED)) ? new java.util.Date(cursor.getLong(cursor.getColumnIndexOrThrow(IndividualConst.C_LAST_MODIFIED))) : null;
        number = !cursor.isNull(cursor.getColumnIndexOrThrow(IndividualConst.C_NUMBER)) ? cursor.getInt(cursor.getColumnIndexOrThrow(IndividualConst.C_NUMBER)) : null;
        phone = !cursor.isNull(cursor.getColumnIndexOrThrow(IndividualConst.C_PHONE)) ? cursor.getString(cursor.getColumnIndexOrThrow(IndividualConst.C_PHONE)) : null;
        email = !cursor.isNull(cursor.getColumnIndexOrThrow(IndividualConst.C_EMAIL)) ? cursor.getString(cursor.getColumnIndexOrThrow(IndividualConst.C_EMAIL)) : null;
        data = cursor.getBlob(cursor.getColumnIndexOrThrow(IndividualConst.C_DATA));
        amount1 = !cursor.isNull(cursor.getColumnIndexOrThrow(IndividualConst.C_AMOUNT1)) ? cursor.getFloat(cursor.getColumnIndexOrThrow(IndividualConst.C_AMOUNT1)) : null;
        amount2 = cursor.getDouble(cursor.getColumnIndexOrThrow(IndividualConst.C_AMOUNT2));
        enabled = !cursor.isNull(cursor.getColumnIndexOrThrow(IndividualConst.C_ENABLED)) ? cursor.getInt(cursor.getColumnIndexOrThrow(IndividualConst.C_ENABLED)) != 0 ? true : false : null;
        spouseIndividualId = !cursor.isNull(cursor.getColumnIndexOrThrow(IndividualConst.C_SPOUSE_INDIVIDUAL_ID)) ? cursor.getLong(cursor.getColumnIndexOrThrow(IndividualConst.C_SPOUSE_INDIVIDUAL_ID)) : null;
    }

    public boolean isNewRecord() {
        return getPrimaryKeyId() <= 0;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @javax.annotation.Nonnull
    public IndividualType getIndividualType() {
        return individualType;
    }

    public void setIndividualType(@javax.annotation.Nonnull IndividualType individualType) {
        this.individualType = individualType;
    }

    @javax.annotation.Nonnull
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(@javax.annotation.Nonnull String firstName) {
        this.firstName = firstName;
    }

    @javax.annotation.Nonnull
    public String getLastName() {
        return lastName;
    }

    public void setLastName(@javax.annotation.Nonnull String lastName) {
        this.lastName = lastName;
    }

    @javax.annotation.Nullable
    public java.util.Date getSampleDateTime() {
        if (sampleDateTime != null) {
            return (java.util.Date)sampleDateTime.clone();
        } else {
            return null;
        }
    }

    public void setSampleDateTime(@javax.annotation.Nullable java.util.Date sampleDateTime) {
        if (sampleDateTime != null) {
            this.sampleDateTime = (java.util.Date) sampleDateTime.clone();
        } else {
            this.sampleDateTime = null;
        }
    }

    @javax.annotation.Nullable
    public java.util.Date getBirthDate() {
        if (birthDate != null) {
            return (java.util.Date)birthDate.clone();
        } else {
            return null;
        }
    }

    public void setBirthDate(@javax.annotation.Nullable java.util.Date birthDate) {
        if (birthDate != null) {
            this.birthDate = (java.util.Date) birthDate.clone();
        } else {
            this.birthDate = null;
        }
    }

    @javax.annotation.Nullable
    public java.util.Date getLastModified() {
        if (lastModified != null) {
            return (java.util.Date)lastModified.clone();
        } else {
            return null;
        }
    }

    public void setLastModified(@javax.annotation.Nullable java.util.Date lastModified) {
        if (lastModified != null) {
            this.lastModified = (java.util.Date) lastModified.clone();
        } else {
            this.lastModified = null;
        }
    }

    @javax.annotation.Nullable
    public Integer getNumber() {
        return number;
    }

    public void setNumber(@javax.annotation.Nullable Integer number) {
        this.number = number;
    }

    @javax.annotation.Nullable
    public String getPhone() {
        return phone;
    }

    public void setPhone(@javax.annotation.Nullable String phone) {
        this.phone = phone;
    }

    @javax.annotation.Nullable
    public String getEmail() {
        return email;
    }

    public void setEmail(@javax.annotation.Nullable String email) {
        this.email = email;
    }

    @javax.annotation.Nullable
    public byte[] getData() {
        return data;
    }

    public void setData(@javax.annotation.Nullable byte[] data) {
        this.data = data;
    }

    @javax.annotation.Nullable
    public Float getAmount1() {
        return amount1;
    }

    public void setAmount1(@javax.annotation.Nullable Float amount1) {
        this.amount1 = amount1;
    }

    @javax.annotation.Nullable
    public Double getAmount2() {
        return amount2;
    }

    public void setAmount2(@javax.annotation.Nullable Double amount2) {
        this.amount2 = amount2;
    }

    @javax.annotation.Nullable
    public Boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(@javax.annotation.Nullable Boolean enabled) {
        this.enabled = enabled;
    }

    @javax.annotation.Nullable
    public Long getSpouseIndividualId() {
        return spouseIndividualId;
    }

    public void setSpouseIndividualId(@javax.annotation.Nullable Long spouseIndividualId) {
        this.spouseIndividualId = spouseIndividualId;
    }


}