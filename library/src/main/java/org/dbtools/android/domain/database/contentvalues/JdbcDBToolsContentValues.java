package org.dbtools.android.domain.database.contentvalues;


import android.os.Parcel;
import android.os.Parcelable;

import org.dbtools.android.domain.log.DBToolsJavaLogger;
import org.dbtools.android.domain.log.DBToolsLogger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nullable;

@SuppressWarnings("unused")
public class JdbcDBToolsContentValues implements DBToolsContentValues<JdbcDBToolsContentValues>, Parcelable {
    private static final String TAG = "JdbcDBToolsContentValues";

    /**
     * Holds the actual values
     */
    private HashMap<String, Object> values;
    private DBToolsLogger log = new DBToolsJavaLogger();

    /**
     * Creates an empty set of values using the default initial size
     */
    public JdbcDBToolsContentValues() {
        // Choosing a default size of 8 based on analysis of typical
        // consumption by applications.
        values = new HashMap<String, Object>(8);
    }

    /**
     * Creates an empty set of values using the given initial size
     *
     * @param size the initial size of the set of values
     */
    public JdbcDBToolsContentValues(int size) {
        values = new HashMap<String, Object>(size, 1.0f);
    }

    /**
     * Creates a set of values copied from the given set
     *
     * @param from the values to copy
     */
    public JdbcDBToolsContentValues(JdbcDBToolsContentValues from) {
        values = new HashMap<String, Object>(from.values);
    }

    /**
     * Creates a set of values copied from the given HashMap. This is used
     * by the Parcel unmarshalling code.
     *
     * @param values the values to start with
     *               {@hide}
     */
    private JdbcDBToolsContentValues(HashMap<String, Object> values) {
        this.values = values;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof JdbcDBToolsContentValues)) {
            return false;
        }
        return values.equals(((JdbcDBToolsContentValues) object).values);
    }

    @Override
    public int hashCode() {
        return values.hashCode();
    }

    @Override
    public JdbcDBToolsContentValues getContentValues() {
        return this;
    }

    /**
     * Adds a value to the set.
     *
     * @param key   the name of the value to put
     * @param value the data for the value to put
     */
    public void put(String key, String value) {
        values.put(key, value);
    }

    /**
     * Adds all values from the passed in ContentValues.
     *
     * @param other the ContentValues from which to copy
     */
    public void putAll(JdbcDBToolsContentValues other) {
        values.putAll(other.values);
    }

    /**
     * Adds a value to the set.
     *
     * @param key   the name of the value to put
     * @param value the data for the value to put
     */
    public void put(String key, Byte value) {
        values.put(key, value);
    }

    /**
     * Adds a value to the set.
     *
     * @param key   the name of the value to put
     * @param value the data for the value to put
     */
    public void put(String key, Short value) {
        values.put(key, value);
    }

    /**
     * Adds a value to the set.
     *
     * @param key   the name of the value to put
     * @param value the data for the value to put
     */
    public void put(String key, Integer value) {
        values.put(key, value);
    }

    /**
     * Adds a value to the set.
     *
     * @param key   the name of the value to put
     * @param value the data for the value to put
     */
    public void put(String key, Long value) {
        values.put(key, value);
    }

    /**
     * Adds a value to the set.
     *
     * @param key   the name of the value to put
     * @param value the data for the value to put
     */
    public void put(String key, Float value) {
        values.put(key, value);
    }

    /**
     * Adds a value to the set.
     *
     * @param key   the name of the value to put
     * @param value the data for the value to put
     */
    public void put(String key, Double value) {
        values.put(key, value);
    }

    /**
     * Adds a value to the set.
     *
     * @param key   the name of the value to put
     * @param value the data for the value to put
     */
    public void put(String key, Boolean value) {
        values.put(key, value);
    }

    /**
     * Adds a value to the set.
     *
     * @param key   the name of the value to put
     * @param value the data for the value to put
     */
    public void put(String key, byte[] value) {
        values.put(key, value);
    }

    /**
     * Adds a null value to the set.
     *
     * @param key the name of the value to make null
     */
    public void putNull(String key) {
        values.put(key, null);
    }

    /**
     * Returns the number of values.
     *
     * @return the number of values
     */
    public int size() {
        return values.size();
    }

    /**
     * Remove a single value.
     *
     * @param key the name of the value to remove
     */
    public void remove(String key) {
        values.remove(key);
    }

    /**
     * Removes all values.
     */
    public void clear() {
        values.clear();
    }

    /**
     * Returns true if this object has the named value.
     *
     * @param key the value to check for
     * @return {@code true} if the value is present, {@code false} otherwise
     */
    public boolean containsKey(String key) {
        return values.containsKey(key);
    }

    /**
     * Gets a value. Valid value types are {@link String}, {@link Boolean}, and
     * {@link Number} implementations.
     *
     * @param key the value to get
     * @return the data for the value
     */
    public Object get(String key) {
        return values.get(key);
    }

    /**
     * Gets a value and converts it to a String.
     *
     * @param key the value to get
     * @return the String for the value
     */
    @Nullable
    public String getAsString(String key) {
        Object value = values.get(key);
        return value != null ? value.toString() : null;
    }

    /**
     * Gets a value and converts it to a Long.
     *
     * @param key the value to get
     * @return the Long value, or null if the value is missing or cannot be converted
     */
    @Nullable
    public Long getAsLong(String key) {
        Object value = values.get(key);
        try {
            return value != null ? ((Number) value).longValue() : null;
        } catch (ClassCastException e) {
            if (value instanceof CharSequence) {
                try {
                    return Long.valueOf(value.toString());
                } catch (NumberFormatException e2) {
                    log.e(TAG, "Cannot parse Long value for " + value + " at key " + key);
                    return null;
                }
            } else {
                log.e(TAG, "Cannot cast value for " + key + " to a Long: " + value, e);
                return null;
            }
        }
    }

    /**
     * Gets a value and converts it to an Integer.
     *
     * @param key the value to get
     * @return the Integer value, or null if the value is missing or cannot be converted
     */
    @Nullable
    public Integer getAsInteger(String key) {
        Object value = values.get(key);
        try {
            return value != null ? ((Number) value).intValue() : null;
        } catch (ClassCastException e) {
            if (value instanceof CharSequence) {
                try {
                    return Integer.valueOf(value.toString());
                } catch (NumberFormatException e2) {
                    log.e(TAG, "Cannot parse Integer value for " + value + " at key " + key);
                    return null;
                }
            } else {
                log.e(TAG, "Cannot cast value for " + key + " to a Integer: " + value, e);
                return null;
            }
        }
    }

    /**
     * Gets a value and converts it to a Short.
     *
     * @param key the value to get
     * @return the Short value, or null if the value is missing or cannot be converted
     */
    @Nullable
    public Short getAsShort(String key) {
        Object value = values.get(key);
        try {
            return value != null ? ((Number) value).shortValue() : null;
        } catch (ClassCastException e) {
            if (value instanceof CharSequence) {
                try {
                    return Short.valueOf(value.toString());
                } catch (NumberFormatException e2) {
                    log.e(TAG, "Cannot parse Short value for " + value + " at key " + key);
                    return null;
                }
            } else {
                log.e(TAG, "Cannot cast value for " + key + " to a Short: " + value, e);
                return null;
            }
        }
    }

    /**
     * Gets a value and converts it to a Byte.
     *
     * @param key the value to get
     * @return the Byte value, or null if the value is missing or cannot be converted
     */
    @Nullable
    public Byte getAsByte(String key) {
        Object value = values.get(key);
        try {
            return value != null ? ((Number) value).byteValue() : null;
        } catch (ClassCastException e) {
            if (value instanceof CharSequence) {
                try {
                    return Byte.valueOf(value.toString());
                } catch (NumberFormatException e2) {
                    log.e(TAG, "Cannot parse Byte value for " + value + " at key " + key);
                    return null;
                }
            } else {
                log.e(TAG, "Cannot cast value for " + key + " to a Byte: " + value, e);
                return null;
            }
        }
    }

    /**
     * Gets a value and converts it to a Double.
     *
     * @param key the value to get
     * @return the Double value, or null if the value is missing or cannot be converted
     */
    @Nullable
    public Double getAsDouble(String key) {
        Object value = values.get(key);
        try {
            return value != null ? ((Number) value).doubleValue() : null;
        } catch (ClassCastException e) {
            if (value instanceof CharSequence) {
                try {
                    return Double.valueOf(value.toString());
                } catch (NumberFormatException e2) {
                    log.e(TAG, "Cannot parse Double value for " + value + " at key " + key);
                    return null;
                }
            } else {
                log.e(TAG, "Cannot cast value for " + key + " to a Double: " + value, e);
                return null;
            }
        }
    }

    /**
     * Gets a value and converts it to a Float.
     *
     * @param key the value to get
     * @return the Float value, or null if the value is missing or cannot be converted
     */
    @Nullable
    public Float getAsFloat(String key) {
        Object value = values.get(key);
        try {
            return value != null ? ((Number) value).floatValue() : null;
        } catch (ClassCastException e) {
            if (value instanceof CharSequence) {
                try {
                    return Float.valueOf(value.toString());
                } catch (NumberFormatException e2) {
                    log.e(TAG, "Cannot parse Float value for " + value + " at key " + key);
                    return null;
                }
            } else {
                log.e(TAG, "Cannot cast value contentValuesfor " + key + " to a Float: " + value, e);
                return null;
            }
        }
    }

    /**
     * Gets a value and converts it to a Boolean.
     *
     * @param key the value to get
     * @return the Boolean value, or null if the value is missing or cannot be converted
     */
    public Boolean getAsBoolean(String key) {
        Object value = values.get(key);
        try {
            return (Boolean) value;
        } catch (ClassCastException e) {
            if (value instanceof CharSequence) {
                return Boolean.valueOf(value.toString());
            } else if (value instanceof Number) {
                return ((Number) value).intValue() != 0;
            } else {
                log.e(TAG, "Cannot cast value for " + key + " to a Boolean: " + value, e);
                return null;
            }
        }
    }

    /**
     * Gets a value that is a byte array. Note that this method will not convert
     * any other types to byte arrays.
     *
     * @param key the value to get
     * @return the byte[] value, or null is the value is missing or not a byte[]
     */
    @Nullable
    public byte[] getAsByteArray(String key) {
        Object value = values.get(key);
        if (value instanceof byte[]) {
            return (byte[]) value;
        } else {
            return null;
        }
    }

    /**
     * Returns a set of all of the keys and values
     *
     * @return a set of all of the keys and values
     */
    public Set<Map.Entry<String, Object>> valueSet() {
        return values.entrySet();
    }

    /**
     * Returns a set of all of the keys
     *
     * @return a set of all of the keys
     */
    public Set<String> keySet() {
        return values.keySet();
    }

    public static final Parcelable.Creator<JdbcDBToolsContentValues> CREATOR =
            new Parcelable.Creator<JdbcDBToolsContentValues>() {
                @SuppressWarnings({"deprecation", "unchecked"})
                public JdbcDBToolsContentValues createFromParcel(Parcel in) {
                    // TODO - what ClassLoader should be passed to readHashMap?
                    HashMap<String, Object> values = in.readHashMap(null);
                    return new JdbcDBToolsContentValues(values);
                }

                public JdbcDBToolsContentValues[] newArray(int size) {
                    return new JdbcDBToolsContentValues[size];
                }
            };

    public int describeContents() {
        return 0;
    }

    @SuppressWarnings("deprecation")
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeMap(values);
    }

    /**
     * Unsupported, here until we get proper bulk insert APIs.
     * {@hide}
     */
    @Deprecated
    public void putStringArrayList(String key, ArrayList<String> value) {
        values.put(key, value);
    }

    /**
     * Unsupported, here until we get proper bulk insert APIs.
     * {@hide}
     */
    @SuppressWarnings("unchecked")
    @Deprecated
    public ArrayList<String> getStringArrayList(String key) {
        return (ArrayList<String>) values.get(key);
    }

    /**
     * Returns a string containing a concise, human-readable description of this object.
     *
     * @return a printable representation of this object.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (String name : values.keySet()) {
            String value = getAsString(name);
            if (sb.length() > 0) sb.append(" ");
            sb.append(name + "=" + value);
        }
        return sb.toString();
    }
}
