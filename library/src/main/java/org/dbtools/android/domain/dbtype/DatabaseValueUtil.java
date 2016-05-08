package org.dbtools.android.domain.dbtype;

import java.util.Date;

public final class DatabaseValueUtil {
    private DatabaseValueUtil() {
    }

    public static <I> DatabaseValue getDatabaseValue(Class<I> type) {
        if (type == Integer.class) {
            return new DatabaseInteger();
        } else if (type == Long.class) {
            return new DatabaseLong();
        } else if (type == String.class) {
            return new DatabaseString();
        } else if (type == Boolean.class) {
            return new DatabaseBoolean();
        } else if (type == Date.class) {
            return new DatabaseDate();
        } else if (type == Float.class) {
            return new DatabaseFloat();
        } else if (type == Double.class) {
            return new DatabaseDouble();
        } else if (type == byte[].class) {
            return new DatabaseBlob();
        }

        // for DateTime, LocalDateTime, etc.... force the user to use Long / String... so there is no
        // hard dependency on 3rd party libraries

        throw new IllegalArgumentException("Unsupported type: " + type);
    }
}
