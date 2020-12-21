package com.panghu.uikit.base.field;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

/**
 * Created by panghu on 9/1/16.
 */
public class EditTextField extends AbstractField {
    @StringRes
    private int mHintResId = 0;
    private String mHintText;
    private String mText;
    private int mMaxLine;
    private boolean mIsAutoFocusable = false;

    public EditTextField(FieldId id, @StringRes int iconResId, boolean hasDivider, boolean isVisible, @StringRes int hintResId, int maxLine, String text) {
        super(id, iconResId, hasDivider, isVisible);
        mHintResId = hintResId;
        mMaxLine = maxLine;
        setText(text);
    }

    public EditTextField(FieldId id, @StringRes int iconResId, boolean hasDivider, boolean isVisible, @Nullable String hintText, int maxLine, String text) {
        super(id, iconResId, hasDivider, isVisible);
        mHintText = hintText;
        mMaxLine = maxLine;
        setText(text);
    }

    public void setText(String text) {
        mText = text;
    }

    public String getText() {
        return mText;
    }

    @StringRes
    public int getHintResource() {
        return mHintResId;
    }

    @Nullable
    public String getHintText() {
        return mHintText;
    }

    public int getMaxLine() {
        return mMaxLine;
    }

    public boolean isAutoFocusable() {
        return mIsAutoFocusable;
    }

    public void setAutoFocusable(boolean isAutoFocusable) {
        mIsAutoFocusable = isAutoFocusable;
    }
}
