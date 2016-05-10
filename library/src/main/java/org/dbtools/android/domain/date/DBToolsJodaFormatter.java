package org.dbtools.android.domain.date;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import javax.annotation.Nullable;

public class DBToolsJodaFormatter {
    private static DateTimeFormatter DB_DATE_FORMATTER = null;

    static {
        try {
            DB_DATE_FORMATTER = DateTimeFormat.forPattern(DBToolsDateFormatter.DB_DATETIME_FORMAT);
        } catch (NoClassDefFoundError e) {
            // NOSONAR - ok
        }
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

        return new DateTime(l);
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

        return new DateTime(l, DateTimeZone.UTC);
    }


    // DateTime - String

    @Nullable
    public static String dateTimeToDBString(@Nullable DateTime d) {
        if (d != null) {
            return d.toString(DBToolsDateFormatter.DB_DATETIME_FORMAT);
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
}
