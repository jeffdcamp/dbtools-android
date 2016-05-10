package org.dbtools.android.domain.date;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.Nullable;

public class DBToolsDateFormatter {
    public static final String DB_DATETIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS";
    public static final String DB_DATE_FORMAT = "yyyy-MM-dd";
    public static final String DB_TIME_FORMAT = "HH:mm:ss";

    private static ThreadLocal<DateFormat> dateFormat = new ThreadLocal<DateFormat>() {
        @Override
        protected DateFormat initialValue() {
            return new SimpleDateFormat(DB_DATETIME_FORMAT); // NOPMD This is the format for the database, not for user view
        }
    };

    // Date - String

    @Nullable
    public static String dateToDBString(@Nullable Date d) {
        if (d != null) {
            return dateFormat.get().format(d);
        } else {
            return null;
        }
    }

    @Nullable
    public static Date dbStringToDate(@Nullable String text) {
        if (text != null && text.length() > 0 && !text.equals("null")) {
            try {
                return dateFormat.get().parse(text);
            } catch (ParseException ex) {
                throw new IllegalArgumentException("Cannot parse date text: " + text, ex);
            }
        } else {
            return null;
        }
    }

    // Java Date - long

    @Nullable
    public static Long dateToLong(@Nullable Date d) {
        if (d == null) {
            return null;
        }

        return d.getTime();
    }

    @Nullable
    public static Date longToDate(@Nullable Long l) {
        if (l == null) {
            return null;
        }

        return new Date(l);
    }
}
