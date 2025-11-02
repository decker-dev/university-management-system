package shared.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm");
    private static final SimpleDateFormat DATETIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    public static String formatDate(Date date) {
        if (date == null) return "";
        return DATE_FORMAT.format(date);
    }

    public static Date parseDate(String dateStr) {
        if (dateStr == null || dateStr.trim().isEmpty()) return null;
        try {
            return DATE_FORMAT.parse(dateStr);
        } catch (ParseException e) {
            System.err.println("Error parsing date: " + dateStr);
            return null;
        }
    }

    public static String formatTime(Date time) {
        if (time == null) return "";
        return TIME_FORMAT.format(time);
    }

    public static Date parseTime(String timeStr) {
        if (timeStr == null || timeStr.trim().isEmpty()) return null;
        try {
            return TIME_FORMAT.parse(timeStr);
        } catch (ParseException e) {
            System.err.println("Error parsing time: " + timeStr);
            return null;
        }
    }

    public static String formatDateTime(Date dateTime) {
        if (dateTime == null) return "";
        return DATETIME_FORMAT.format(dateTime);
    }

    public static Date parseDateTime(String dateTimeStr) {
        if (dateTimeStr == null || dateTimeStr.trim().isEmpty()) return null;
        try {
            return DATETIME_FORMAT.parse(dateTimeStr);
        } catch (ParseException e) {
            System.err.println("Error parsing datetime: " + dateTimeStr);
            return null;
        }
    }

    public static Date now() {
        return new Date();
    }
}

