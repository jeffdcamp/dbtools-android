package org.dbtools.android.domain;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.threeten.bp.*;

import javax.annotation.Nullable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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

    public static DateTimeFormatter DB_DATE_FORMATTER = null;
    public static org.threeten.bp.format.DateTimeFormatter DB_DATETIME_FORMATTER310;
    public static org.threeten.bp.format.DateTimeFormatter DB_DATE_FORMATTER310;
    public static org.threeten.bp.format.DateTimeFormatter DB_TIME_FORMATTER310;

    static {
        // Joda try...
        try {
            DB_DATE_FORMATTER = DateTimeFormat.forPattern(DB_DATETIME_FORMAT);
        } catch (NoClassDefFoundError e) {
            // NOSONAR - ok
        }

        // JSR-310 try...
        try {
            DB_DATETIME_FORMATTER310 = org.threeten.bp.format.DateTimeFormatter.ofPattern(DB_DATETIME_FORMAT);
            DB_DATE_FORMATTER310 = org.threeten.bp.format.DateTimeFormatter.ofPattern(DB_DATE_FORMAT);
            DB_TIME_FORMATTER310 = org.threeten.bp.format.DateTimeFormatter.ofPattern(DB_TIME_FORMAT);
        } catch (NoClassDefFoundError e) {
            // NOSONAR - ok
        }
    }


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


    // ========== JODA ==========
    // DateTime - long

    @Nullable
    public static Long dateTimeToLong(@Nullable DateTime d) {
        if (d == null) {
            return null;
        }

        return d.getMillis();
    }

    @Nullable
    public static DateTime longToDateTime(@Nullable Long l) {
        if (l == null) {
            return null;
        }

        return new org.joda.time.DateTime(l);
    }

    @Nullable
    public static Long dateTimeToLongUtc(@Nullable DateTime d) {
        if (d == null) {
            return null;
        }

        return d.withZone(DateTimeZone.UTC).getMillis();
    }

    @Nullable
    public static DateTime longToDateTimeUtc(@Nullable Long l) {
        if (l == null) {
            return null;
        }

        return new org.joda.time.DateTime(l, DateTimeZone.UTC);
    }


    // DateTime - String

    @Nullable
    public static String dateTimeToDBString(@Nullable DateTime d) {
        if (d != null) {
            return d.toString(DB_DATETIME_FORMAT);
        } else {
            return null;
        }
    }

    @Nullable
    public static DateTime dbStringToDateTime(@Nullable String text) {
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

    // ========== JSR 310 ==========
    // JSR 310 LocalDateTime - long

    @Nullable
    public static Long localDateTimeToLong(@Nullable LocalDateTime d) {
        if (d == null) {
            return null;
        }

        return d.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    @Nullable
    public static LocalDateTime longToLocalDateTime(@Nullable Long l) {
        if (l == null) {
            return null;
        }

        return Instant.ofEpochMilli(l).atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    @Nullable
    public static Long localDateTimeToLongUtc(@Nullable LocalDateTime d) {
        if (d == null) {
            return null;
        }

        return d.toInstant(ZoneOffset.UTC).toEpochMilli();
    }

    @Nullable
    public static LocalDateTime longToLocalDateTimeUtc(@Nullable Long l) {
        if (l == null) {
            return null;
        }

        return Instant.ofEpochMilli(l).atZone(ZoneOffset.UTC).toLocalDateTime();
    }

    // JSR 310 LocalDateTime - String

    // Date and Time
    @Nullable
    public static String localDateTimeToDBString(@Nullable LocalDateTime d) {
        if (d != null) {
            return DB_DATETIME_FORMATTER310.format(d);
        } else {
            return null;
        }
    }

    @Nullable
    public static LocalDateTime dbStringToLocalDateTime(@Nullable String text) {
        if (text != null && text.length() > 0 && !text.equals("null")) {
            try {
                return LocalDateTime.parse(text, DB_DATETIME_FORMATTER310);
            } catch (Exception ex) {
                throw new IllegalArgumentException("Cannot parse date time text: " + text, ex);
            }
        } else {
            return null;
        }
    }

    // Date only
    @Nullable
    public static String localDateToDBString(@Nullable LocalDate d) {
        if (d != null) {
            return DB_DATE_FORMATTER310.format(d);
        } else {
            return null;
        }
    }

    @Nullable
    public static LocalDate dbStringToLocalDate(@Nullable String text) {
        if (text != null && text.length() > 0 && !text.equals("null")) {
            try {
                return LocalDate.parse(text, DB_DATE_FORMATTER310);
            } catch (Exception ex) {
                throw new IllegalArgumentException("Cannot parse date text: " + text, ex);
            }
        } else {
            return null;
        }
    }

    // Time only
    @Nullable
    public static String localTimeToDBString(@Nullable LocalTime d) {
        if (d != null) {
            return DB_TIME_FORMATTER310.format(d);
        } else {
            return null;
        }
    }

    @Nullable
    public static LocalTime dbStringToLocalTime(@Nullable String text) {
        if (text != null && text.length() > 0 && !text.equals("null")) {
            try {
                return LocalTime.parse(text, DB_TIME_FORMATTER310);
            } catch (Exception ex) {
                throw new IllegalArgumentException("Cannot parse time text: " + text, ex);
            }
        } else {
            return null;
        }
    }

}
