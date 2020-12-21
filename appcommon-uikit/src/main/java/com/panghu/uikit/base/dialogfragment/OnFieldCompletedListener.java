package com.panghu.uikit.base.dialogfragment;

import com.panghu.uikit.base.field.AbstractField;

/**
 * Created by panghu on 9/1/16.
 */
public interface OnFieldCompletedListener {
    void onFieldCompleted(AbstractField field);
    void onFieldCanceled(AbstractField field);
}
