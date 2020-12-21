package com.panghu.uikit.base.field;

import androidx.annotation.StringRes;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kasni.huang on 18/10/2017.
 */

public class JoinNowListField extends ListField {
    private List<String> mReplyEmails;
    private String mEventTitle;

    public JoinNowListField(FieldId id, @StringRes int iconResId, boolean hasDivider, boolean isVisible, @StringRes int titleResId, boolean isSelectable) {
        super(id, iconResId, hasDivider, isVisible, titleResId, isSelectable);
    }

    public List<String> getReplyEmails() {
        return mReplyEmails;
    }

    public void setReplyEmails(List<String> mReplyEmails) {
        this.mReplyEmails = mReplyEmails;
    }

    public String getEventTitle() {
        return mEventTitle;
    }

    public void setEventTitle(String eventTitle) {
        this.mEventTitle = eventTitle;
    }
}
