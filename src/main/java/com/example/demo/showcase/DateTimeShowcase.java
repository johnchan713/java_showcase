package com.example.demo.showcase;

import java.time.*;
import java.time.chrono.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.*;
import java.util.Date;
import java.sql.Timestamp;

/**
 * Demonstrates Java Date/Time API including timestamps, chronology, and time operations
 */
public class DateTimeShowcase {

    public static void demonstrate() {
        System.out.println("\n========== DATE TIME SHOWCASE ==========\n");

        localDateTimeDemo();
        instantAndTimestampDemo();
        chronoDemo();
        durationAndPeriodDemo();
        dateTimeFormattingDemo();
        timeZonesDemo();
        legacyDateDemo();
    }

    // ========== LocalDate, LocalTime, LocalDateTime ==========

    private static void localDateTimeDemo() {
        System.out.println("--- LocalDate, LocalTime, LocalDateTime ---");
        System.out.println("ADVANTAGE: Immutable, thread-safe, clear API");
        System.out.println("USE: Date/time without timezone");

        // Current date and time
        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();
        LocalDateTime dateTime = LocalDateTime.now();

        System.out.println("Today: " + today);
        System.out.println("Now: " + now);
        System.out.println("DateTime: " + dateTime);

        // Creating specific date/time
        LocalDate birthday = LocalDate.of(1990, Month.JANUARY, 15);
        LocalTime meeting = LocalTime.of(14, 30, 0);
        LocalDateTime appointment = LocalDateTime.of(2024, 12, 25, 10, 30);

        System.out.println("Birthday: " + birthday);
        System.out.println("Meeting: " + meeting);
        System.out.println("Appointment: " + appointment);

        // Parsing from string
        LocalDate parsed = LocalDate.parse("2024-03-15");
        LocalTime parsedTime = LocalTime.parse("15:30:45");
        System.out.println("Parsed date: " + parsed);
        System.out.println("Parsed time: " + parsedTime);

        // Date arithmetic
        LocalDate nextWeek = today.plusWeeks(1);
        LocalDate lastMonth = today.minusMonths(1);
        LocalDate nextYear = today.plusYears(1);

        System.out.println("Next week: " + nextWeek);
        System.out.println("Last month: " + lastMonth);
        System.out.println("Next year: " + nextYear);

        // Extracting fields
        System.out.println("Year: " + today.getYear());
        System.out.println("Month: " + today.getMonth());
        System.out.println("Day of month: " + today.getDayOfMonth());
        System.out.println("Day of week: " + today.getDayOfWeek());
        System.out.println("Day of year: " + today.getDayOfYear());

        // Comparisons
        LocalDate date1 = LocalDate.of(2024, 1, 1);
        LocalDate date2 = LocalDate.of(2024, 6, 1);
        System.out.println("date1.isBefore(date2): " + date1.isBefore(date2));
        System.out.println("date1.isAfter(date2): " + date1.isAfter(date2));
        System.out.println("date1.equals(date2): " + date1.equals(date2));

        System.out.println();
    }

    // ========== Instant and Timestamp ==========

    private static void instantAndTimestampDemo() {
        System.out.println("--- Instant and Timestamp ---");
        System.out.println("Instant: Machine timestamp (nanosecond precision)");
        System.out.println("Timestamp: SQL timestamp");

        // Instant - point in time on the timeline
        Instant instant = Instant.now();
        System.out.println("Current instant: " + instant);

        // Epoch seconds and milliseconds
        long epochSecond = instant.getEpochSecond();
        long epochMilli = instant.toEpochMilli();
        System.out.println("Epoch seconds: " + epochSecond);
        System.out.println("Epoch milliseconds: " + epochMilli);

        // Create from epoch
        Instant fromEpoch = Instant.ofEpochSecond(1700000000L);
        Instant fromMilli = Instant.ofEpochMilli(System.currentTimeMillis());
        System.out.println("From epoch second: " + fromEpoch);
        System.out.println("From milliseconds: " + fromMilli);

        // Instant arithmetic
        Instant later = instant.plusSeconds(3600); // +1 hour
        Instant earlier = instant.minusSeconds(3600); // -1 hour
        System.out.println("1 hour later: " + later);
        System.out.println("1 hour earlier: " + earlier);

        // Nanosecond precision
        System.out.println("Nano: " + instant.getNano());

        // SQL Timestamp
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        System.out.println("\nSQL Timestamp: " + timestamp);
        System.out.println("Timestamp.getTime(): " + timestamp.getTime());
        System.out.println("Timestamp.getNanos(): " + timestamp.getNanos());

        // Convert between Instant and Timestamp
        Timestamp fromInstant = Timestamp.from(instant);
        Instant toInstant = timestamp.toInstant();
        System.out.println("Instant -> Timestamp: " + fromInstant);
        System.out.println("Timestamp -> Instant: " + toInstant);

        // System time in milliseconds
        long currentTimeMillis = System.currentTimeMillis();
        long nanoTime = System.nanoTime();
        System.out.println("\nSystem.currentTimeMillis(): " + currentTimeMillis);
        System.out.println("System.nanoTime(): " + nanoTime);

        System.out.println();
    }

    // ========== Chronology ==========

    private static void chronoDemo() {
        System.out.println("--- Chronology Systems ---");
        System.out.println("Support for different calendar systems");

        // ISO (default)
        LocalDate isoDate = LocalDate.now();
        System.out.println("ISO Date: " + isoDate);

        // Japanese calendar
        JapaneseDate japaneseDate = JapaneseDate.now();
        System.out.println("Japanese Date: " + japaneseDate);

        // Hijrah (Islamic) calendar
        HijrahDate hijrahDate = HijrahDate.now();
        System.out.println("Hijrah Date: " + hijrahDate);

        // Thai Buddhist calendar
        ThaiBuddhistDate thaiDate = ThaiBuddhistDate.now();
        System.out.println("Thai Buddhist Date: " + thaiDate);

        // Minguo (Taiwan) calendar
        MinguoDate minguoDate = MinguoDate.now();
        System.out.println("Minguo Date: " + minguoDate);

        // Convert between chronologies
        ChronoLocalDate isoToJapanese = JapaneseChronology.INSTANCE.date(isoDate);
        System.out.println("ISO to Japanese: " + isoToJapanese);

        // Chronology information
        System.out.println("\nChronology details:");
        System.out.println("ISO ID: " + IsoChronology.INSTANCE.getId());
        System.out.println("Japanese ID: " + JapaneseChronology.INSTANCE.getId());

        System.out.println();
    }

    // ========== Duration and Period ==========

    private static void durationAndPeriodDemo() {
        System.out.println("--- Duration and Period ---");
        System.out.println("Duration: Time-based amount (hours, minutes, seconds)");
        System.out.println("Period: Date-based amount (years, months, days)");

        // Duration (for time)
        Duration duration1 = Duration.ofHours(2);
        Duration duration2 = Duration.ofMinutes(90);
        Duration duration3 = Duration.ofSeconds(3600);

        System.out.println("2 hours: " + duration1);
        System.out.println("90 minutes: " + duration2);
        System.out.println("3600 seconds: " + duration3);

        // Duration between times
        Instant start = Instant.now();
        Instant end = start.plusSeconds(7325);
        Duration between = Duration.between(start, end);
        System.out.println("Duration between: " + between);
        System.out.println("In hours: " + between.toHours());
        System.out.println("In minutes: " + between.toMinutes());
        System.out.println("In seconds: " + between.getSeconds());

        // Duration arithmetic
        Duration sum = duration1.plus(duration2);
        Duration diff = duration1.minus(duration2);
        System.out.println("2h + 90m = " + sum);
        System.out.println("2h - 90m = " + diff);

        // Period (for dates)
        Period period1 = Period.ofYears(1);
        Period period2 = Period.ofMonths(6);
        Period period3 = Period.ofDays(30);
        Period period4 = Period.of(1, 6, 15); // 1 year, 6 months, 15 days

        System.out.println("\n1 year: " + period1);
        System.out.println("6 months: " + period2);
        System.out.println("30 days: " + period3);
        System.out.println("1y 6m 15d: " + period4);

        // Period between dates
        LocalDate birth = LocalDate.of(1990, 1, 15);
        LocalDate now = LocalDate.now();
        Period age = Period.between(birth, now);
        System.out.println("Age: " + age.getYears() + " years, " +
                          age.getMonths() + " months, " +
                          age.getDays() + " days");

        // Add period to date
        LocalDate futureDate = now.plus(period4);
        System.out.println("Date + period: " + futureDate);

        System.out.println();
    }

    // ========== Date/Time Formatting ==========

    private static void dateTimeFormattingDemo() {
        System.out.println("--- Date/Time Formatting ---");

        LocalDateTime dateTime = LocalDateTime.now();

        // Predefined formatters
        System.out.println("ISO_DATE_TIME: " + dateTime.format(DateTimeFormatter.ISO_DATE_TIME));
        System.out.println("ISO_DATE: " + dateTime.format(DateTimeFormatter.ISO_DATE));
        System.out.println("ISO_TIME: " + dateTime.format(DateTimeFormatter.ISO_TIME));

        // Custom patterns
        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter formatter3 = DateTimeFormatter.ofPattern("EEEE, MMMM dd, yyyy");
        DateTimeFormatter formatter4 = DateTimeFormatter.ofPattern("hh:mm:ss a");

        System.out.println("Custom 1: " + dateTime.format(formatter1));
        System.out.println("Custom 2: " + dateTime.format(formatter2));
        System.out.println("Custom 3: " + dateTime.format(formatter3));
        System.out.println("Custom 4: " + dateTime.format(formatter4));

        // Parsing with custom format
        String dateString = "2024-12-25 15:30:00";
        LocalDateTime parsed = LocalDateTime.parse(dateString, formatter1);
        System.out.println("Parsed: " + parsed);

        // Common patterns
        System.out.println("\nCommon patterns:");
        System.out.println("yyyy-MM-dd: " + dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        System.out.println("dd.MM.yyyy: " + dateTime.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        System.out.println("MM/dd/yyyy: " + dateTime.format(DateTimeFormatter.ofPattern("MM/dd/yyyy")));
        System.out.println("EEE, MMM d, ''yy: " + dateTime.format(DateTimeFormatter.ofPattern("EEE, MMM d, ''yy")));

        System.out.println();
    }

    // ========== Time Zones ==========

    private static void timeZonesDemo() {
        System.out.println("--- Time Zones ---");

        // ZonedDateTime
        ZonedDateTime zonedNow = ZonedDateTime.now();
        System.out.println("Current zoned: " + zonedNow);

        // Specific time zones
        ZonedDateTime newYork = ZonedDateTime.now(ZoneId.of("America/New_York"));
        ZonedDateTime tokyo = ZonedDateTime.now(ZoneId.of("Asia/Tokyo"));
        ZonedDateTime london = ZonedDateTime.now(ZoneId.of("Europe/London"));

        System.out.println("New York: " + newYork);
        System.out.println("Tokyo: " + tokyo);
        System.out.println("London: " + london);

        // Convert between zones
        ZonedDateTime tokyoTime = newYork.withZoneSameInstant(ZoneId.of("Asia/Tokyo"));
        System.out.println("NY time in Tokyo zone: " + tokyoTime);

        // OffsetDateTime (with fixed offset)
        OffsetDateTime offsetNow = OffsetDateTime.now();
        System.out.println("Offset DateTime: " + offsetNow);

        // ZoneOffset
        ZoneOffset offset = ZoneOffset.ofHours(5);
        OffsetDateTime withOffset = OffsetDateTime.now(offset);
        System.out.println("With +5 offset: " + withOffset);

        // Available zone IDs
        System.out.println("\nSample Zone IDs:");
        ZoneId.getAvailableZoneIds().stream()
            .filter(z -> z.startsWith("America/") || z.startsWith("Europe/"))
            .limit(10)
            .forEach(z -> System.out.println("  " + z));

        System.out.println();
    }

    // ========== Legacy Date/Time (pre-Java 8) ==========

    private static void legacyDateDemo() {
        System.out.println("--- Legacy Date/Time (Avoid in new code) ---");
        System.out.println("DISADVANTAGE: Mutable, not thread-safe, confusing API");
        System.out.println("USE: Only for legacy code compatibility");

        // Old Date
        Date date = new Date();
        System.out.println("Date: " + date);
        System.out.println("Date.getTime(): " + date.getTime());

        // Create from milliseconds
        Date fromMillis = new Date(System.currentTimeMillis());
        System.out.println("From millis: " + fromMillis);

        // SQL Timestamp (extends Date)
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        System.out.println("SQL Timestamp: " + timestamp);

        // Convert between old and new API
        System.out.println("\nConversion between old and new:");

        // Date -> Instant
        Instant instantFromDate = date.toInstant();
        System.out.println("Date -> Instant: " + instantFromDate);

        // Instant -> Date
        Date dateFromInstant = Date.from(Instant.now());
        System.out.println("Instant -> Date: " + dateFromInstant);

        // LocalDateTime -> Date
        LocalDateTime localDateTime = LocalDateTime.now();
        Instant instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
        Date dateFromLocal = Date.from(instant);
        System.out.println("LocalDateTime -> Date: " + dateFromLocal);

        // Date -> LocalDateTime
        LocalDateTime localFromDate = date.toInstant()
            .atZone(ZoneId.systemDefault())
            .toLocalDateTime();
        System.out.println("Date -> LocalDateTime: " + localFromDate);

        System.out.println("\nRECOMMENDATION: Use java.time.* (Java 8+) for all new code");

        System.out.println();
    }
}
