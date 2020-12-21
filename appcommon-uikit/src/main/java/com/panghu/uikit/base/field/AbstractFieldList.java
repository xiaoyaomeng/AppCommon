package com.panghu.uikit.base.field;

import com.panghu.uikit.utils.Log;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by panghu on 9/4/16.
 */
public abstract class AbstractFieldList implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final String TAG = "AbstractFieldList";
    protected ArrayList<AbstractField> mFieldItems;

    private transient OnFieldDataChangeListener mFieldDataChangeListener;

    public AbstractFieldList() {
        this.mFieldItems = new ArrayList<>();
    }

    public void setFieldDataChangeListener(OnFieldDataChangeListener fieldDataChangeListener) {
        mFieldDataChangeListener = fieldDataChangeListener;
    }

    public void setFieldVisibilityChangeListener(OnFieldVisibilityChangeListener fieldVisibilityChangeListener) {
        for (AbstractField field : mFieldItems) {
            field.setFieldVisibilityChangeListener(fieldVisibilityChangeListener);
        }
    }

    public ArrayList<AbstractField> getVisibleFieldItems() {
        ArrayList<AbstractField> list = new ArrayList<>();
        for (AbstractField field : mFieldItems) {
            if (field.isVisible()) {
                list.add(field);
            }
        }
        return list;
    }

    public void updateField(AbstractField field) {
        for (int i = 0; i < mFieldItems.size(); ++i) {
            if (mFieldItems.get(i).getId() == field.getId()) {
                mFieldItems.set(i, field);
                if (mFieldDataChangeListener != null) {
                    mFieldDataChangeListener.onFieldDataChange(mFieldItems.get(i));
                }
                break;
            }
        }
    }

    public AbstractField findField(FieldId fieldId) {
        for (int i = 0; i < mFieldItems.size(); ++i) {
            if (mFieldItems.get(i).getId() == fieldId) {
                return mFieldItems.get(i);
            }
        }
        Log.w(TAG, "Can't find field " + fieldId);
        return null;
    }

    public void addField(AbstractField field) {
        mFieldItems.add(field);
    }
}
