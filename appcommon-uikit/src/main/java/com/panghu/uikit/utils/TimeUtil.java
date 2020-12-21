package com.panghu.uikit.utils;

import android.content.Context;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.panghu.uikit.R;
import com.google.gson.internal.bind.util.ISO8601Utils;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

/**
 * Created by steve.chen on 6/4/16.
 */
public class TimeUtil {
    public static final String TAG = "TimeUtil";
    public static final int FORMAT_SHOW_TODAY = 0x00001;
    public static final int FORMAT_SHOW_TOMORROW = 0x00002;
    public static final int FORMAT_SHOW_YESTERDAY = 0x00004;

    public static final int IS_TODAY = 0x1;
    public static final int IS_TOMORROW = 0x2;
    public static final int IS_YESTERDAY = 0x4;
    public static final int IS_THISYEAR = 0x8;
    public static final int IS_OTHERYEAR = 0x10;

    private static final int DefaultMagicDay = 86400000;

    public static final int FORMAT_SPECIAL_DAY = TimeUtil.FORMAT_SHOW_TODAY | TimeUtil.FORMAT_SHOW_YESTERDAY | TimeUtil.FORMAT_SHOW_TOMORROW;
    public static final long NOTE_LOCK_EXPIRE_TIME = 5 * 60 * 60 * 1000;

    private static final String FORMAT_TIME_UTC = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    private static final String FORMAT_TIME_UTC_NUMBER = "yyyyMMdd'T'HHmmss'Z'";
    private static final String FORMAT_TIME_LOG = "HH:mm:ss.SSS";

    private static final String DOUBLE_RAIL = "--";
    private static final String RAIL = "-";
    private static final String DIAGONAL = "/";

    public static boolean isSameDay(long startDateTime, long endDateTime) {
        Calendar startTime = Calendar.getInstance();
        startTime.setTimeInMillis(startDateTime);

        Calendar endTime = Calendar.getInstance();
        endTime.setTimeInMillis(endDateTime);

        return (startTime.get(Calendar.ERA) == endTime.get(Calendar.ERA) &&
                startTime.get(Calendar.YEAR) == endTime.get(Calendar.YEAR) &&
                startTime.get(Calendar.DAY_OF_YEAR) == endTime.get(Calendar.DAY_OF_YEAR));
    }

    public static int durationDays(long start, long end) {
        return (int) Math.ceil((float) (end - start) / DefaultMagicDay);
    }

    public static int datePosition(long dateTimeInMillis) {
        Calendar calendar = TimeUtil.getCalendar(dateTimeInMillis);
        Calendar now = Calendar.getInstance();
        int result = IS_OTHERYEAR;
        if (now.get(Calendar.YEAR) == calendar.get(Calendar.YEAR)) {
            result = IS_THISYEAR;
        }
        long diffDays = daysToToday(dateTimeInMillis);
        if (diffDays == 0) {
            return result | IS_TODAY;
        } else if (diffDays == 1) {
            return result | (dateTimeInMillis < now.getTimeInMillis() ? IS_YESTERDAY : IS_TOMORROW);
        }
        return result;
    }

    public static long daysToToday(long dateTime) {
        Calendar dateCalendar = Calendar.getInstance();
        dateCalendar.setTimeInMillis(dateTime);
        clearTime(dateCalendar);
        long daysToDate = TimeUnit.DAYS.convert(dateCalendar.getTimeInMillis(), TimeUnit.MILLISECONDS);
        Calendar nowCalendar = Calendar.getInstance();
        clearTime(nowCalendar);
        long daysToNow = TimeUnit.DAYS.convert(nowCalendar.getTimeInMillis(), TimeUnit.MILLISECONDS);
        return Math.abs(daysToNow - daysToDate);
    }

    public static long getDayStartTime(long date, int dayOffset) {
        Calendar calendar = getCalendar(date);
        clearTime(calendar);
        calendar.add(Calendar.DATE, dayOffset);
        return calendar.getTimeInMillis();
    }

    public static long getDayEndTime(long date, int dayOffset) {
        Calendar calendar = getCalendar(date);
        calendar.add(Calendar.DATE, dayOffset);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTimeInMillis();
    }

    public static long convertUtcDateTimeToDateTime(String utcTime) {
        long result = 0L;
        SimpleDateFormat utcFormatter = new SimpleDateFormat(FORMAT_TIME_UTC, Locale.getDefault());
        utcFormatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            Date date = utcFormatter.parse(utcTime);
            result = date.getTime();
        } catch (ParseException e) {
            Log.e(TAG, "Failed to convert the datetime of utc format to dateTime.", e);
        }
        return result;
    }

    public static long getCalendarDate(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        calendar.set(Calendar.HOUR_OF_DAY, calendar.getActualMinimum(Calendar.HOUR_OF_DAY));
        calendar.set(Calendar.MINUTE, calendar.getActualMinimum(Calendar.MINUTE));
        calendar.set(Calendar.SECOND, calendar.getActualMinimum(Calendar.SECOND));
        calendar.set(Calendar.MILLISECOND, calendar.getActualMinimum(Calendar.MILLISECOND));
        return calendar.getTimeInMillis();
    }

    // get Calendar date by offset days from base time
    public static long getCalendarDate(long time, int offsetDays) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        calendar.add(Calendar.DATE, offsetDays);
        calendar.set(Calendar.HOUR_OF_DAY, calendar.getActualMinimum(Calendar.HOUR_OF_DAY));
        calendar.set(Calendar.MINUTE, calendar.getActualMinimum(Calendar.MINUTE));
        calendar.set(Calendar.SECOND, calendar.getActualMinimum(Calendar.SECOND));
        calendar.set(Calendar.MILLISECOND, calendar.getActualMinimum(Calendar.MILLISECOND));
        return calendar.getTimeInMillis();
    }

    public static long getTomorrow(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        calendar.add(Calendar.DATE, 1);
        calendar.set(Calendar.HOUR_OF_DAY, calendar.getActualMinimum(Calendar.HOUR_OF_DAY));
        calendar.set(Calendar.MINUTE, calendar.getActualMinimum(Calendar.MINUTE));
        calendar.set(Calendar.SECOND, calendar.getActualMinimum(Calendar.SECOND));
        calendar.set(Calendar.MILLISECOND, calendar.getActualMinimum(Calendar.MILLISECOND));
        return calendar.getTimeInMillis();
    }

    public static long getCalendarDate(int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, dayOfMonth);
        return getCalendarDate(calendar.getTimeInMillis());
    }

    public static long getCalendarTime(long time) {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        return ((long) hour * 60 + minute) * 60 * 1000;
    }

    public static int getDuration(long startDate, long endDate) {
        if (startDate >= endDate) {
            return 1;
        }
        return (int) ((endDate - startDate) / DefaultMagicDay) + 1;
    }

    public static long getNextHourInMills() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR_OF_DAY, 1);
        calendar.set(Calendar.MINUTE, calendar.getActualMinimum(Calendar.MINUTE));
        calendar.set(Calendar.SECOND, calendar.getActualMinimum(Calendar.SECOND));
        calendar.set(Calendar.MILLISECOND, calendar.getActualMinimum(Calendar.MILLISECOND));
        return calendar.getTimeInMillis();
    }

    public static long secondsToMillis(long timeInSeconds) {
        return TimeUnit.SECONDS.toMillis(timeInSeconds);
    }

    public static long millisToSeconds(long timeInMillis) {
        return TimeUnit.MILLISECONDS.toSeconds(timeInMillis);
    }

    public static long secondsToMinutes(long timeInSeconds) {
        return TimeUnit.SECONDS.toMinutes(timeInSeconds);
    }

    public static long minutesToHours(long timeInMinutes) {
        return TimeUnit.MINUTES.toHours(timeInMinutes);
    }

    public static String convertToUtcNumber(long timeInMillis) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(FORMAT_TIME_UTC_NUMBER, Locale.getDefault());
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return simpleDateFormat.format(timeInMillis);
    }

    public static String convertToLogTime(long timeInMillis) {
        return new SimpleDateFormat(FORMAT_TIME_LOG, Locale.getDefault()).format(timeInMillis);
    }

    public static String convertToISO8601(long time) {
        return ISO8601Utils.format(new Date(time), false, TimeZone.getDefault());
    }

    public static Date convertFromISO8601(String time) {
        try {
            return ISO8601Utils.parse(time, new ParsePosition(0));
        } catch (ParseException e) {
            Log.e(TAG, "Error in parsing time:" + time, e);
        }
        return null;
    }

    public static String createGmtOffsetString(boolean includeGmt, boolean includeMinuteSeparator, int offsetMillis) {
        int offsetMinutes = offsetMillis / 60000;
        char sign = '+';
        if (offsetMinutes < 0) {
            sign = '-';
            offsetMinutes = -offsetMinutes;
        }
        StringBuilder builder = new StringBuilder(9);
        if (includeGmt) {
            builder.append("GMT");
        }
        builder.append(sign);
        appendNumber(builder, 2, offsetMinutes / 60);
        if (includeMinuteSeparator) {
            builder.append(':');
        }
        appendNumber(builder, 2, offsetMinutes % 60);
        return builder.toString();
    }

    private static Calendar getCalendar(long date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date);
        return calendar;
    }

    private static void clearTime(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
    }

    private static void appendNumber(StringBuilder builder, int count, int value) {
        String string = Integer.toString(value);
        for (int i = 0; i < count - string.length(); i++) {
            builder.append('0');
        }
        builder.append(string);
    }

    /**
     * format time from --07-08 to 07/08
     *
     * @param date
     * @return time string
     */
    public static String formatWithoutYear(String date) {
        if (!TextUtils.isEmpty(date) && date.startsWith(DOUBLE_RAIL)) {
            date = date.replaceFirst(DOUBLE_RAIL, "").replace(RAIL, DIAGONAL);
        }
        return date;
    }

    /**
     * Convert to a have suffixes time string
     * e.g: 5 seconds、1 minute、5 minutes 、1 hour、 5 hours
     *
     * @param context
     * @param ms
     * @return
     */
    public static String convertToTimeWithSuffix(@NonNull Context context, long ms) {
        String timeString;
        long seconds = millisToSeconds(ms);
        long minutes = secondsToMinutes(seconds);
        long hours = minutesToHours(minutes);

        if (seconds < 60L) {
            timeString = context.getResources().getQuantityString(R.plurals.how_seconds, (int) seconds, seconds);
        } else if (minutes < 60L) {
            timeString = context.getResources().getQuantityString(R.plurals.how_minutes, (int) minutes, minutes);
        } else {
            timeString = context.getResources().getQuantityString(R.plurals.how_hours, (int) hours, hours);
        }
        return timeString;
    }
}
