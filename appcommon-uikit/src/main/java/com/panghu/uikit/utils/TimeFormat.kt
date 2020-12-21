package com.panghu.uikit.utils

import android.content.Context
import android.text.TextUtils
import android.text.format.DateFormat
import android.text.format.DateUtils
import com.panghu.uikit.R
import com.panghu.uikit.utils.TimeUtil.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * @author locke.peng on 4/1/19.
 *
 * SHORT is completely numeric, such as 12/13/52 or 3:30pm
 * MEDIUM is longer, such as Jan 12, 1952
 * LONG is longer, such as January 12, 1952 or 3:30:32pm
 * FULL is pretty completely specified, such as Tuesday, April 12, 1952 AD or 3:30:42pm PST.
 *
 * TODO Refactor `Format` methods in 6.4(The same format rules will be used).
 */
object TimeFormat {
    private const val TAG = "TimeFormatUtil"
    private const val FORMAT_DATE_SHORT_ZH = "yyyy/MM/dd"
    private const val FORMAT_DATE_SHORT_US = "MM/dd/yyyy"
    private const val FORMAT_DATE_FULL_US = "EEEE, MMMM dd, yyyy"

    @JvmStatic
    fun formatDateTimeWithWeek(dateTime: Long, context: Context): String {
        val weekDay = formatWeekDay(dateTime, context)
        val dateInShort = formatDateInShort(dateTime, context)
        val time = formatTime(dateTime, context)
        return "$weekDay $dateInShort $time"
    }

    @JvmStatic
    fun formatDateTime(dateTime: Long, context: Context): String {
        return formatDateTime(dateTime, 0, context)
    }

    @JvmStatic
    fun formatDateTime(dateTime: Long, flag: Int, context: Context): String {
        return formatDate(dateTime, flag, context) + " " + formatTime(dateTime, context)
    }

    @JvmStatic
    fun formatDate(dateTime: Long, flag: Int, context: Context): String {
        var formattedStr = ""

        if (flag > 0) {
            val datePos = TimeUtil.datePosition(dateTime)
            if (flag and FORMAT_SHOW_TODAY > 0 && datePos and IS_TODAY == IS_TODAY) {
                formattedStr = context.resources.getString(R.string.today)
            } else if (flag and FORMAT_SHOW_TOMORROW > 0 && datePos and IS_TOMORROW == IS_TOMORROW) {
                formattedStr = context.resources.getString(R.string.tomorrow)
            } else if (flag and FORMAT_SHOW_YESTERDAY > 0 && datePos and IS_YESTERDAY == IS_YESTERDAY) {
                formattedStr = context.resources.getString(R.string.yesterday)
            }

            if (TextUtils.isEmpty(formattedStr)) {
                formattedStr = formatDateWithWeek(dateTime, context)
            }
        } else if (!DateUtils.isToday(dateTime)) {
            val dayDiff = TimeUtil.daysToToday(dateTime)
            formattedStr = if (dayDiff >= 7) {
                formatDateInShort(dateTime, context)
            } else {
                formatWeekDay(dateTime, context)
            }
        }

        return formattedStr
    }

    /**
     * Format the date in full mode(display by device local setting).
     *
     *
     * # For dates in the current year, show: <day of week>, <month> <day>. Do not show year as it is not useful information.
     * Example(US): Monday, March 10
     * # For dates in the next or previous year, append the year to the end of the date.
     * Example(US): Thursday, January 2, 2020
     * # For today’s date, append “Today” plus a bullet point to the front of the date.
     * Example(US): Today · Saturday, March 9
     * # For tomorrow’s date.
     * Example(US): Tomorrow · Sunday, March 10
     * # For yesterday’s date.
     * Example(US): Yesterday · Friday, March 8
    </day></month></day> *
     *
     * @param context
     * @param dateTimeInMillis
     * @return
     */
    @JvmStatic
    fun formatDateInFull(context: Context, dateTimeInMillis: Long): String {
        var formattedRecentDay: String? = null
        var flags = DateUtils.FORMAT_SHOW_WEEKDAY or DateUtils.FORMAT_SHOW_DATE

        val datePos = datePosition(dateTimeInMillis)
        when {
            datePos and IS_TODAY == IS_TODAY -> formattedRecentDay =
                context.resources.getString(R.string.today)
            datePos and IS_TOMORROW == IS_TOMORROW -> formattedRecentDay =
                context.resources.getString(R.string.tomorrow)
            datePos and IS_YESTERDAY == IS_YESTERDAY -> formattedRecentDay =
                context.resources.getString(R.string.yesterday)
        }
        if (datePos and IS_OTHERYEAR == TimeUtil.IS_OTHERYEAR) {
            flags = flags or DateUtils.FORMAT_SHOW_YEAR
        }

        val dateString = DateUtils.formatDateTime(context, dateTimeInMillis, flags)
        return if (formattedRecentDay == null) {
            dateString
        } else {
            String.format("%s · %s", formattedRecentDay, dateString)
        }
    }

    /**
     * Format the date in [java.text.DateFormat.SHORT] form according to the context's locale.
     */
    @JvmStatic
    fun formatDateInShort(dateTimeInMillis: Long, context: Context): String {
        val formattedStr: String
        val dateFormat = DateFormat.getDateFormat(context)
        // TODO `dateFormat` cannot be null, `return` of DateFormat.getDateFormat(context) is not nullable.
        formattedStr = if (dateFormat != null) {
            dateFormat.format(Date(dateTimeInMillis))
        } else {
            DateUtils.formatDateTime(
                context,
                dateTimeInMillis,
                DateUtils.FORMAT_SHOW_DATE or DateUtils.FORMAT_SHOW_YEAR or DateUtils.FORMAT_NUMERIC_DATE
            )
        }
        return formattedStr
    }

    /**
     * Format the date to 'MM/dd/yyyy'(US style)
     */
    @JvmStatic
    fun formatDateInShortUs(dateTimeInMills: Long): String {
        return SimpleDateFormat(FORMAT_DATE_SHORT_US, Locale.getDefault()).format(
            dateTimeInMills
        )
    }

    /**
     * Format the date to 'yyyy/MM/dd'(Zh style)
     */
    @JvmStatic
    fun formatDateInShortZh(dateTimeInMillis: Long): String {
        return SimpleDateFormat(FORMAT_DATE_SHORT_ZH, Locale.getDefault()).format(
            dateTimeInMillis
        )
    }

    /**
     * Format the date to 'EEEE, MMMM dd, yyyy'(US style)
     */
    @JvmStatic
    fun formatDateInFullUs(dateTimeInMillis: Long): String {
        return SimpleDateFormat(FORMAT_DATE_FULL_US, Locale.getDefault()).format(
            dateTimeInMillis
        )
    }

    @JvmStatic
    fun formatDateWithWeek(dateTime: Long, context: Context): String {
        return formatWeekDay(dateTime, context) + " " + formatDateInShort(dateTime, context)
    }

    @JvmStatic
    fun formatWeekDay(dateTime: Long, context: Context): String {
        return DateUtils.formatDateTime(
            context,
            dateTime,
            DateUtils.FORMAT_SHOW_WEEKDAY or DateUtils.FORMAT_ABBREV_WEEKDAY
        )
    }

    @JvmStatic
    fun formatTime(dateTime: Long, context: Context): String {
        return DateUtils.formatDateTime(context, dateTime, DateUtils.FORMAT_SHOW_TIME)
    }

    @JvmStatic
    fun formatDateTimeForPreview(utcTime: String, context: Context): String {
        var result = ""
        val dataTime = TimeUtil.convertUtcDateTimeToDateTime(utcTime)
        if (dataTime > 0L) {
            result = formatDateTimeForPreview(dataTime, false, context)
        } else {
            Log.w(TAG, "Failed to format date time for preview.")
        }
        return result
    }

    @JvmStatic
    fun formatDateTimeForPreview(dateTime: Long, isShowToday: Boolean, context: Context): String {
        val formattedStr: String
        val datePos = datePosition(dateTime)
        when {
            datePos and IS_TODAY == IS_TODAY -> formattedStr = if (isShowToday) {
                context.resources.getString(R.string.today)
            } else {
                DateUtils.formatDateTime(context, dateTime, DateUtils.FORMAT_SHOW_TIME)
            }
            datePos and IS_YESTERDAY == IS_YESTERDAY -> formattedStr =
                context.resources.getString(R.string.yesterday)
            else -> {
                val dayDiff = daysToToday(dateTime)
                formattedStr = if (dayDiff >= 7) {
                    formatDateInShort(dateTime, context)
                } else {
                    DateUtils.formatDateTime(context, dateTime, DateUtils.FORMAT_SHOW_WEEKDAY)
                }
            }
        }
        return formattedStr
    }

    /**
     * The minimum unit of time is Seconds.
     * For example: `10 sec` or `1 min` or `1 min 10 sec` or `1 hr 10 min`
     */
    @JvmStatic
    fun formatDurationTime(secondDuration: Long, context: Context): String {
        var result = ""
        when {
            secondDuration < 0 -> return result
            secondDuration < 60 -> result =
                String.format(context.getString(R.string.second_format), secondDuration)
            secondDuration < 60 * 60 -> {
                val min = secondDuration / 60
                val second = secondDuration % 60

                result = if (0L == second) {
                    String.format(context.getString(R.string.minute_format), min)
                } else {
                    String.format(context.getString(R.string.minute_second_format), min, second)
                }
            }
            else -> {
                val hour = secondDuration / (60 * 60)
                val min = (secondDuration - hour * 60 * 60) / 60
                result = String.format(context.getString(R.string.hour_minute_format), hour, min)
            }
        }
        return result
    }

    /**
     * The minimum unit of time is Minutes.
     * For example: `1 min` or `1 hr` or `1 hr 10 min`
     * @param context
     * @param durationInSeconds want to format second
     * @param upwardFormat set true to return a upward-rounded format time.
     * for example: 0-59s -> `1 min`, 60s-119s -> `2 min`, 1hr -> `1 hr 1 min`
     * @return format time
     */
    @JvmStatic
    fun formatDurationTimeNoSeconds(
        context: Context, durationInSeconds: Long, upwardFormat: Boolean = false
    ): String {
        val result: String
        val duration = if (upwardFormat) {
            durationInSeconds + TimeUnit.MINUTES.toSeconds(1)
        } else {
            durationInSeconds
        }
        result = if (duration < 60) {
            context.getString(R.string.minute_format, 1)
        } else if (duration < 60 * 60) {
            val min = (duration / 60).toInt()
            context.getString(R.string.minute_format, min)
        } else {
            val hour = (duration / (60 * 60)).toInt()
            val min = ((duration - hour * 60 * 60) / 60).toInt()

            if (min.toLong() == 0L) {
                context.getString(R.string.hour_format, hour)
            } else {
                context.getString(R.string.hour_minute_format, hour, min)
            }
        }
        return result
    }

    @JvmStatic
    fun formatElapsedTime(elapsedSeconds: Long): String {
        return formatElapsedTime(elapsedSeconds, false)
    }

    @JvmStatic
    fun formatElapsedTime(elapsedSeconds: Long, formatWithoutDoubleDigitLimit: Boolean): String {
        var timeInSeconds = elapsedSeconds
        // Break the elapsed seconds into hours, minutes, and seconds.
        var hours: Long = 0
        var minutes: Long = 0
        val seconds: Long
        if (timeInSeconds >= 3600) {
            hours = timeInSeconds / 3600
            timeInSeconds -= hours * 3600
        }
        if (timeInSeconds >= 60) {
            minutes = timeInSeconds / 60
            timeInSeconds -= minutes * 60
        }
        seconds = timeInSeconds

        val sb = StringBuilder(8)

        val formatter = Formatter(sb, Locale.getDefault())
        return if (hours > 0) {
            val format =
                if (formatWithoutDoubleDigitLimit) "%1\$d:%2$02d:%3$02d" else "%1$02d:%2$02d:%3$02d"
            formatter.format(format, hours, minutes, seconds).toString()
        } else {
            val format = if (formatWithoutDoubleDigitLimit) "%1\$d:%2$02d" else "%1$02d:%2$02d"
            formatter.format(format, minutes, seconds).toString()
        }
    }
}