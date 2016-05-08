package org.dbtools.android.domain.database.contentvalues;

import android.content.ContentValues;

import java.util.Map;
import java.util.Set;

public class AndroidDBToolsContentValues implements DBToolsContentValues<ContentValues> {
    private ContentValues contentValues = new ContentValues();

    @Override
    public ContentValues getContentValues() {
        return contentValues;
    }

    @Override
    public void put(String key, String value) {
        contentValues.put(key, value);
    }

    @Override
    public void putAll(ContentValues other) {
        contentValues.putAll(other);
    }

    @Override
    public void put(String key, Byte value) {
        contentValues.put(key, value);
    }

    @Override
    public void put(String key, Short value) {
        contentValues.put(key, value);
    }

    @Override
    public void put(String key, Integer value) {
        contentValues.put(key, value);

    }

    @Override
    public void put(String key, Long value) {
        contentValues.put(key, value);

    }

    @Override
    public void put(String key, Float value) {
        contentValues.put(key, value);

    }

    @Override
    public void put(String key, Double value) {
        contentValues.put(key, value);

    }

    @Override
    public void put(String key, Boolean value) {
        contentValues.put(key, value);

    }

    @Override
    public void put(String key, byte[] value) {
        contentValues.put(key, value);

    }

    @Override
    public void putNull(String key) {
        contentValues.putNull(key);

    }

    @Override
    public int size() {
        return contentValues.size();
    }

    @Override
    public void remove(String key) {
        contentValues.remove(key);
    }

    @Override
    public void clear() {
contentValues.clear();
    }

    @Override
    public boolean containsKey(String key) {
        return contentValues.containsKey(key);
    }

    @Override
    public Object get(String key) {
        return contentValues.get(key);
    }

    @Override
    public String getAsString(String key) {
        return contentValues.getAsString(key);
    }

    @Override
    public Long getAsLong(String key) {
        return contentValues.getAsLong(key);
    }

    @Override
    public Integer getAsInteger(String key) {
        return contentValues.getAsInteger(key);
    }

    @Override
    public Short getAsShort(String key) {
        return contentValues.getAsShort(key);
    }

    @Override
    public Byte getAsByte(String key) {
        return contentValues.getAsByte(key);
    }

    @Override
    public Double getAsDouble(String key) {
        return contentValues.getAsDouble(key);
    }

    @Override
    public Float getAsFloat(String key) {
        return contentValues.getAsFloat(key);
    }

    @Override
    public Boolean getAsBoolean(String key) {
        return contentValues.getAsBoolean(key);
    }

    @Override
    public byte[] getAsByteArray(String key) {
        return contentValues.getAsByteArray(key);
    }

    @Override
    public Set<Map.Entry<String, Object>> valueSet() {
        return contentValues.valueSet();
    }

    @Override
    public Set<String> keySet() {
        return contentValues.keySet();
    }
}
