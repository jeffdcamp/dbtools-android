package org.dbtools.android.domain.database.contentvalues;


import java.util.Map;
import java.util.Set;

public interface DBToolsContentValues<T> {
    T getContentValues();

    void put(String key, String value);

    void putAll(T other);

    void put(String key, Byte value);

    void put(String key, Short value);

    void put(String key, Integer value);

    void put(String key, Long value);

    void put(String key, Float value);

    void put(String key, Double value);

    void put(String key, Boolean value);

    void put(String key, byte[] value);

    void putNull(String key);

    int size();

    void remove(String key);

    void clear();

    boolean containsKey(String key);

    Object get(String key);

    String getAsString(String key);

    Long getAsLong(String key);

    Integer getAsInteger(String key);

    Short getAsShort(String key);

    Byte getAsByte(String key);

    Double getAsDouble(String key);

    Float getAsFloat(String key);

    Boolean getAsBoolean(String key);

    byte[] getAsByteArray(String key);

    Set<Map.Entry<String, Object>> valueSet();

    Set<String> keySet();

    String toString();
}
