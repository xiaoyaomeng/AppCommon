package com.panghu.uikit.base.field;

import android.content.Context;
import androidx.annotation.StringRes;

import com.panghu.uikit.R;
import com.panghu.uikit.utils.TimeFormat;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

/**
 * Created by panghu on 8/31/16.
 */
public class TimeField extends AbstractOptionField {
    public static final int TIME_UNINITIALIZED = -1;
    private long mTime = TIME_UNINITIALIZED;
    private boolean mNoneable;
    private boolean mClearable;

    public TimeField(FieldId id, @StringRes int iconResId, boolean hasDivider, boolean isVisible, @StringRes int titleResId, long time, boolean noneable, boolean clearable) {
        super(id, iconResId, hasDivider, isVisible, titleResId);
        setTime(time);
        setNoneable(noneable);
        setClearable(clearable);
    }


    public void setTime(long time) {
        mTime = time;
    }

    public long getTime() {
        return mTime;
    }

    public boolean isNoneable() {
        return mNoneable;
    }

    public void setNoneable(boolean noneable) {
        this.mNoneable = noneable;
    }

    public boolean isClearable() {
        return mClearable;
    }

    public void setClearable(boolean clearable) {
        this.mClearable = clearable;
    }

    public void reset(){
        mTime = TIME_UNINITIALIZED;
    }

    public boolean isValidTime () {
        return mTime > TIME_UNINITIALIZED;
    }

    @Override
    public String getSummary(Context context) {
        if (!isValidTime() && isNoneable()) {
            return context.getString(R.string.none);
        } else {
            int hour = (int) TimeUnit.MILLISECONDS.toHours(mTime);
            int minute = (int) (TimeUnit.MILLISECONDS.toMinutes(mTime - TimeUnit.HOURS.toMillis(hour)));
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, hour);
            calendar.set(Calendar.MINUTE, minute);
            calendar.set(Calendar.SECOND, calendar.getActualMinimum(Calendar.SECOND));
            calendar.set(Calendar.MILLISECOND, calendar.getActualMinimum(Calendar.MILLISECOND));
            return TimeFormat.formatTime(calendar.getTimeInMillis(), context);
        }
    }
}
