package org.dbtools.android.domain.date;

import org.threeten.bp.Instant;
import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.LocalTime;
import org.threeten.bp.ZoneId;
import org.threeten.bp.ZoneOffset;

import javax.annotation.Nullable;

public class DBToolsThreeTenFormatter {
    private static org.threeten.bp.format.DateTimeFormatter DB_DATETIME_FORMATTER310;
    private static org.threeten.bp.format.DateTimeFormatter DB_DATE_FORMATTER310;
    private static org.threeten.bp.format.DateTimeFormatter DB_TIME_FORMATTER310;

    static {
        // JSR-310 try...
        try {
            DB_DATETIME_FORMATTER310 = org.threeten.bp.format.DateTimeFormatter.ofPattern(DBToolsDateFormatter.DB_DATETIME_FORMAT);
            DB_DATE_FORMATTER310 = org.threeten.bp.format.DateTimeFormatter.ofPattern(DBToolsDateFormatter.DB_DATE_FORMAT);
            DB_TIME_FORMATTER310 = org.threeten.bp.format.DateTimeFormatter.ofPattern(DBToolsDateFormatter.DB_TIME_FORMAT);
        } catch (NoClassDefFoundError e) {
            // NOSONAR - ok
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
