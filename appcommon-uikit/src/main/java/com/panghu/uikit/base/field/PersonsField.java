package com.panghu.uikit.base.field;

import androidx.annotation.StringRes;

import com.glip.widgets.tokenautocomplete.Contact;

import java.util.List;

/**
 * Created by panghu on 8/30/16.
 */
public class PersonsField extends AbstractField {
    @StringRes
    private final int mHintResId;
    private List<Contact> mPersons;


    public PersonsField(FieldId id, @StringRes int iconResId, boolean hasDivider, boolean isVisible, @StringRes int hintResId) {
        super(id, iconResId, hasDivider, isVisible);
        mHintResId = hintResId;
    }

    @StringRes
    public int getHintResource() {
        return mHintResId;
    }

    public void setPersons(List<Contact> persons) {
        mPersons = persons;
    }

    public List<Contact> getPersons() {
        return mPersons;
    }

    public long[] getPersonIds() {
        if (mPersons == null || mPersons.size() <= 0) {
            return null;
        }
        long[] personIds = new long[mPersons.size()];
        for (int i = 0; i < personIds.length; ++i) {
            personIds[i] = mPersons.get(i).getId();
        }
        return personIds;
    }
}
