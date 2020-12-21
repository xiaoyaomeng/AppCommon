package com.panghu.uikit.base.field;

import android.content.Context;
import androidx.annotation.StringRes;

/**
 * Created by panghu on 8/31/16.
 */
public abstract class AbstractOptionField extends AbstractField {
    @StringRes
    private final int mTitleResId;

    public AbstractOptionField(FieldId id, @StringRes int iconResId, boolean hasDivider, boolean isVisible, @StringRes int titleResId) {
        super(id, iconResId, hasDivider, isVisible);
        mTitleResId = titleResId;
    }

    @StringRes
    public int getTitleResource() {
        return mTitleResId;
    }


    public abstract String getSummary(Context context);

}
