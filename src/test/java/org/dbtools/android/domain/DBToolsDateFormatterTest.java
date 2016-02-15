package org.dbtools.android.domain;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Before;
import org.threeten.bp.*;

import java.util.Date;

import static org.junit.Assert.*;

public class DBToolsDateFormatterTest {

    private static final String TEXT_TIMESTAMP = "1970-05-18T13:30:00.020";
    private static final String TEXT_DATE = "1970-05-18";
    private static final String TEXT_TIME = "13:30:00";

    public static final long LONG_TIMESTAMP = 11907000020L; // system default (includes timezone offset)
    public static final long LONG_TIMESTAMP_UTC = 11885400020L;

    // JSR 310
    private LocalDateTime jsr301DateTime;
    private LocalDate jsr301Date;
    private LocalTime jsr301Time;

    // Joda
    private DateTime jodaDateTime;
    private DateTime jodaDateTimeUtc;

    @Before
    public void setUp() throws Exception {
        jsr301DateTime = LocalDateTime.of(1970, Month.MAY, 18, 13, 30, 0, 20000000);
        jsr301Date = LocalDate.of(1970, Month.MAY, 18);
        jsr301Time = LocalTime.of(13, 30, 0);

        jodaDateTime = new DateTime(1970, 5, 18, 13, 30, 0, 20);
        jodaDateTimeUtc = new DateTime(1970, 5, 18, 13, 30, 0, 20, DateTimeZone.UTC);
    }

    @org.junit.Test
    public void testNow() throws Exception {
        System.out.println("Date:   " + new Date().getTime());
        System.out.println("Joda:   " + DateTime.now().getMillis());
        System.out.println("JSR310: " + Instant.now().toEpochMilli());
    }

    @org.junit.Test
    public void testDateTimeLong() throws Exception {
        // Convert to long
        long jsr301Long = DBToolsDateFormatter.localDateTimeToLong(jsr301DateTime);
        long jodaLong = DBToolsDateFormatter.dateTimeToLong(jodaDateTime);

        assertEquals(LONG_TIMESTAMP, jsr301Long);
        assertEquals(LONG_TIMESTAMP, jodaLong);

        // convert to object
        LocalDateTime resultJsr310DateTime = DBToolsDateFormatter.longToLocalDateTime(LONG_TIMESTAMP);
        DateTime resultJodaDateTime = DBToolsDateFormatter.longToDateTime(LONG_TIMESTAMP);

        assertTrue(jsr301DateTime.isEqual(resultJsr310DateTime));
        assertTrue(jodaDateTime.isEqual(resultJodaDateTime));
    }

    @org.junit.Test
    public void testDateTimeLongUtc() throws Exception {
        // Convert to long UTC
        long jsr301LongUtc = DBToolsDateFormatter.localDateTimeToLongUtc(jsr301DateTime);
        long jodaLongUtc = DBToolsDateFormatter.dateTimeToLongUtc(jodaDateTimeUtc);

        System.out.println("jsr: " + jsr301LongUtc);
        System.out.println("joda " + jodaLongUtc);
        System.out.println(jsr301DateTime);
        System.out.println(jodaDateTimeUtc);

        assertEquals(LONG_TIMESTAMP_UTC, jsr301LongUtc);
        assertEquals(LONG_TIMESTAMP_UTC, jodaLongUtc);

        // convert to object UTC
        LocalDateTime resultJsr310DateTime = DBToolsDateFormatter.longToLocalDateTimeUtc(LONG_TIMESTAMP_UTC);
        DateTime resultJodaDateTime = DBToolsDateFormatter.longToDateTimeUtc(LONG_TIMESTAMP_UTC);

        assertTrue(jsr301DateTime.isEqual(resultJsr310DateTime));
        assertTrue(jodaDateTimeUtc.isEqual(resultJodaDateTime));
    }

    // ======================== TEXT =============================

    @org.junit.Test
    public void testDateTimeString() throws Exception {
        // convert to text
        String jsr310ResultText = DBToolsDateFormatter.localDateTimeToDBString(jsr301DateTime);
        String jodaResultText = DBToolsDateFormatter.dateTimeToDBString(jodaDateTime);

        assertEquals(TEXT_TIMESTAMP, jsr310ResultText);
        assertEquals(TEXT_TIMESTAMP, jodaResultText);

        // convert back to object
        LocalDateTime resultJsr310DateTime = DBToolsDateFormatter.dbStringToLocalDateTime(jsr310ResultText);
        DateTime resultJodaDateTime = DBToolsDateFormatter.dbStringToDateTime(jodaResultText);

        assertNotNull(resultJsr310DateTime);
        assertTrue(jsr301DateTime.isEqual(resultJsr310DateTime));

        assertNotNull(resultJodaDateTime);
        assertTrue(jodaDateTime.isEqual(resultJodaDateTime));
    }

    @org.junit.Test
    public void testDateText() throws Exception {
        // convert to text
        String jsr310ResultText = DBToolsDateFormatter.localDateToDBString(jsr301Date);
//        String jodaResultText = DBToolsDateFormatter.dateTimeToDBString(jodaDateTime);

        assertEquals(TEXT_DATE, jsr310ResultText);
//        assertEquals(TEXT_TIMESTAMP, jodaResultText);

        // convert back to object
        LocalDate resultJsr310Date = DBToolsDateFormatter.dbStringToLocalDate(jsr310ResultText);
//        DateTime resultJodaDateTime = DBToolsDateFormatter.dbStringToDateTime(jodaResultText);

        assertNotNull(resultJsr310Date);
        assertTrue(jsr301Date.isEqual(resultJsr310Date));

//        assertNotNull(resultJodaDateTime);
//        assertTrue(jodaDateTime.isEqual(resultJodaDateTime));
    }

    @org.junit.Test
    public void testTimeText() throws Exception {
        // convert to text
        String jsr310ResultText = DBToolsDateFormatter.localTimeToDBString(jsr301Time);
//        String jodaResultText = DBToolsDateFormatter.dateTimeToDBString(jodaDateTime);

        assertEquals(TEXT_TIME, jsr310ResultText);
//        assertEquals(TEXT_TIMESTAMP, jodaResultText);

        // convert back to object
        LocalTime resultJsr310Time = DBToolsDateFormatter.dbStringToLocalTime(jsr310ResultText);
//        DateTime resultJodaDateTime = DBToolsDateFormatter.dbStringToDateTime(jodaResultText);

        assertNotNull(resultJsr310Time);
        assertTrue(jsr301Time.equals(resultJsr310Time));

//        assertNotNull(resultJodaDateTime);
//        assertTrue(jodaDateTime.isEqual(resultJodaDateTime));
    }
}