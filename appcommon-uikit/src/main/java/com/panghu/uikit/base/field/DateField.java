package com.panghu.uikit.base.field;

import android.content.Context;
import androidx.annotation.StringRes;


import com.panghu.uikit.R;

import java.text.SimpleDateFormat;

/**
 * Created by panghu on 8/31/16.
 */
public class DateField extends AbstractOptionField {
    private long mDate;
    private boolean mNoneable;
    private boolean mClearable;

    public DateField(FieldId id, @StringRes int iconResId, boolean hasDivider, boolean isVisible, @StringRes int titleResId, long date, boolean noneable, boolean clearable) {
        super(id, iconResId, hasDivider, isVisible, titleResId);
        mDate = date;
        setNoneable(noneable);
        setClearable(clearable);
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

    public void setDate(long date) {
        mDate = date;
    }

    public long getDate() {
        return mDate;
    }

    @Override
    public String getSummary(Context context) {
        return mDate == 0 && isNoneable() ? context.getString(R.string.none) : new SimpleDateFormat("MM/dd/yyyy").format(mDate);
    }

}
