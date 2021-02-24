package ua.nure.style.form;

import ua.nure.style.entity.Booking;
import ua.nure.style.entity.Status;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class DateUtils {
    private static final DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm");

    public static Long convertHtmlDateTimeLocalToMillliseconds(String htmlDateTimeLocal) throws ParseException {
        return formatter.parse(htmlDateTimeLocal).getTime();
    }

    public static Long getMillisecondsOfDateDaysAgo(int days) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -days);
        return cal.getTime().getTime();
    }

    public static Long getMillisecondsOfDateDaysAfter(int days) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, days);
        return cal.getTime().getTime();
    }

    public static long daysToMillis(int days) {
        return TimeUnit.DAYS.toMillis(days);
    }

    public static long hoursToMillis(int hours) {
        return TimeUnit.HOURS.toMillis(hours);
    }

    public static boolean isBetween(long a, long b, long c) {
        return b >= a ? c >= a && c <= b : c >= b && c <= a;
    }

    public static Long htmlDateToMillis(String date) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyy-MM-dd");
        Date parsed = format.parse(date);
        return parsed.getTime();
    }

    public static Long htmlTimeToMillis(String time) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        Date parsed = format.parse(time);
        return parsed.getTime();
    }

    public static Long htmlDateTimeToMills(String date) throws ParseException {
        String dateTime = String.join(" ", date);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date returnDate = format.parse(dateTime);
        return returnDate.getTime();
    }

    public static boolean areColliding(Booking one, Booking another, Status cancelledStatus) {
        return false;
    }
}