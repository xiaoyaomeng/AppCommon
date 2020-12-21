package com.panghu.uikit.base.field;

import androidx.annotation.StringRes;

import java.util.List;

/**
 * Created by panghu on 8/31/16.
 */
public class ObjectListField<T> extends ListField implements IFieldData<T> {
    private static final String TAG = "ObjectListField";
    private List<T> mDatas;

    public ObjectListField(FieldId id, @StringRes int iconResId, boolean hasDivider, boolean isVisible, @StringRes int titleResId, boolean isSelectable) {
        super(id, iconResId, hasDivider, isVisible, titleResId, isSelectable);
    }

    public void setDatas(List<T> datas) {
        this.mDatas = datas;
    }

    @Override
    public List<T> getDatas() {
        return mDatas;
    }
}
