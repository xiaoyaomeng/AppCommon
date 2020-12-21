package com.panghu.uikit.base.field;

import androidx.annotation.ColorRes;
import androidx.annotation.StringRes;

import java.io.Serializable;

/**
 * Created by panghu on 8/30/16.
 */
public abstract class AbstractField implements Serializable {
    private static final long serialVersionUID = 1L;
    private final FieldId mId;
    @StringRes
    private int mIconResId;
    @ColorRes
    private int mIconColor = -1;
    private boolean mHasDivider;
    private boolean mIsVisible;
    private boolean mIsLocked;
    private transient OnFieldVisibilityChangeListener mFieldVisibilityChangeListener;

    public AbstractField(FieldId id, @StringRes int iconResId, boolean hasDivider, boolean isVisible) {
        this.mId = id;
        this.mIconResId = iconResId;
        this.mHasDivider = hasDivider;
        this.mIsVisible = isVisible;
        this.mIsLocked = false;
    }

    public AbstractField(FieldId id, @StringRes int iconResId, boolean hasDivider, boolean isVisible, boolean isEnabled) {
        this.mId = id;
        this.mIconResId = iconResId;
        this.mHasDivider = hasDivider;
        this.mIsVisible = isVisible;
        this.mIsLocked = isEnabled;
    }


    public FieldId getId() {
        return mId;
    }

    public int getIconResId() {
        return mIconResId;
    }

    public void setIconResId(int iconResId) {
        mIconResId = iconResId;
    }

    public boolean hasDivider() {
        return mHasDivider;
    }

    public void setDivider(boolean hasDivider) {
        mHasDivider = hasDivider;
    }

    public boolean isVisible() {
        return mIsVisible;
    }

    public void setVisible(boolean isVisible) {
        if (isVisible != mIsVisible) {
            mIsVisible = isVisible;
            if (mFieldVisibilityChangeListener != null) {
                mFieldVisibilityChangeListener.onFieldVisibilityChange(this);
            }
        }
    }

    void setFieldVisibilityChangeListener(OnFieldVisibilityChangeListener fieldVisibilityChangeListener) {
        mFieldVisibilityChangeListener = fieldVisibilityChangeListener;
    }

    @ColorRes
    public int getIconColor() {
        return mIconColor;
    }

    public void setIconColor(int iconColor) {
        mIconColor = iconColor;
    }

    public boolean isLocked() {
        return mIsLocked;
    }

    public void setIsLocked(boolean isLocked) {
        this.mIsLocked = isLocked;
    }

}
