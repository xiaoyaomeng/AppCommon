package com.panghu.uikit.base.field;

import android.content.Context;
import androidx.annotation.StringRes;
import android.text.TextUtils;

import com.panghu.uikit.R;


/**
 * Created by barry.li on 9/6/16.
 */
public class TextField extends AbstractOptionField {

    private String mText;

    private int mTextRes;

    public TextField(FieldId id, @StringRes int iconResId, boolean hasDivider, boolean isVisible, @StringRes int titleResId, String text) {
        super(id, iconResId, hasDivider, isVisible, titleResId);
        mText = text;
    }

    public String getText() {
        return mText;
    }

    public void setText(String text) {
        mText = text;
    }

    public void setTextRes(int textRes) {
        mText = "";
        mTextRes = textRes;
    }

    @Override
    public String getSummary(Context context) {
        return TextUtils.isEmpty(mText) ? (mTextRes > 0 ? context.getString(mTextRes) : context.getString(R.string.none)) : mText;
    }
}
