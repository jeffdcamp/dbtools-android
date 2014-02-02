package org.dbtools.android.domain;

import android.content.ContentValues;
import android.database.Cursor;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 *
 * @author jcampbell
 */
public abstract class AndroidBaseRecord implements Serializable {

    public static final String DB_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss.sss";

    private static ThreadLocal<DateFormat> dateFormat = new ThreadLocal<DateFormat>() {
        @Override
        protected DateFormat initialValue() {
            return new SimpleDateFormat(DB_DATE_FORMAT); // NOPMD This is the format for the database, not for user view
        }
    };

    public static final DateTimeFormatter DB_DATE_FORMATTER = DateTimeFormat.forPattern(DB_DATE_FORMAT);

    public abstract void setPrimaryKeyID(long id);
    public abstract long getPrimaryKeyID();
    public abstract String getRowIDKey();
    public abstract String getDatabaseName();
    public abstract String getTableName();

    public abstract String[] getAllKeys();

    /**
     * getContentValues.
     * @return values for database (DO NOT INCLUDE THE ID COLUMN)
     */
    public abstract ContentValues getContentValues();
    public abstract void setContent(Cursor cursor);
    public abstract boolean isNewRecord();

    public static int booleanToInt(boolean b) {
        return b ? 1 : 0;
    }

    public static int booleanToLong(boolean b) {
        return b ? 1 : 0;
    }

    public static boolean intToBoolean(int b) {
        return b != 0;
    }

    public boolean longToBoolean(long b) {
        return b != 0;
    }

    // Date - String

    public static String dateToDBString(Date d) {
        if (d != null) {
            return dateFormat.get().format(d);
        } else {
            return null;
        }
    }

    public static Date dbStringToDate(String text) {
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

    // DateTime - String

    public static String dateTimeToDBString(DateTime d) {
        if (d != null) {
            return d.toString(DB_DATE_FORMAT);
        } else {
            return null;
        }
    }

    public static DateTime dbStringToDateTime(String text) {
        if (text != null && text.length() > 0 && !text.equals("null")) {
            try {
                return DB_DATE_FORMATTER.parseDateTime(text);
            } catch (Exception ex) {
                throw new IllegalArgumentException("Cannot parse date text: " + text, ex);
            }
        } else {
            return null;
        }
    }

    // DateTime - long

    public static long dateTimeToLong(DateTime d) {
        return d.getMillis();
    }

    public static DateTime longToDateTime(long l) {
        return new org.joda.time.DateTime(l);
    }

    // Date - long

    public static long dateToLong(Date d) {
        return d.getTime();
    }

    public static Date longToDate(long l) {
        return new Date(l);
    }
}
