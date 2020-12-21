package com.panghu.uikit.base.field;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

import android.text.TextUtils;

import com.panghu.uikit.base.dialogfragment.DrawableIconListItem;
import com.panghu.uikit.base.dialogfragment.ListItem;
import com.panghu.uikit.utils.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by panghu on 8/31/16.
 */
public class ListField extends AbstractOptionField {
    private static final String TAG = "ListField";
    private ListItem[] mListItems;
    private int mSelection;
    private String mMessage;
    private final boolean mIsSelectable;
    private int mIconSize;

    public ListField(FieldId id, @StringRes int iconResId, boolean hasDivider, boolean isVisible, @StringRes int titleResId, boolean isSelectable) {
        super(id, iconResId, hasDivider, isVisible, titleResId);
        mIsSelectable = isSelectable;
        mListItems = new ListItem[0];
        mIconSize = 0;
    }

    public void setListItems(@NonNull ListItem[] listItems) {
        mListItems = listItems;
    }

    public ListItem[] getItems() {
        return mListItems;
    }

    public void setListItems(@NonNull String[] captions, @NonNull String[] values) {
        if (captions.length != values.length) {
            Log.e(TAG, "Captions.length != values.length");
            return;
        }
        int length = captions.length;
        mListItems = new ListItem[length];
        for (int i = 0; i < length; ++i) {
            mListItems[i] = new ListItem(captions[i], values[i], false, 0);
        }
    }

    public void setDrawableIconListItems(@NonNull int[] icons, @NonNull String[] captions, @NonNull String[] values) {
        if (icons.length != captions.length) {
            Log.e(TAG, "Icons.length != captions.length");
            return;
        }

        if (captions.length != values.length) {
            Log.e(TAG, "Captions.length != values.length");
            return;
        }
        int length = captions.length;
        mListItems = new ListItem[length];
        for (int i = 0; i < length; ++i) {
            mListItems[i] = new DrawableIconListItem(captions[i], values[i], false, 0, icons[i]);
        }
    }

    public int getCount() {
        return mListItems.length;
    }

    public ListItem getItem(int index) {
        if (index < mListItems.length && index >= 0) {
            return mListItems[index];
        } else {
            Log.e(TAG, "Invalid index " + index);
            return null;
        }
    }

    public void removeItem(@NonNull String value) {
        List<ListItem> items = new ArrayList<>(Arrays.asList(mListItems));
        ListItem found = null;
        for (int i = 0; i < items.size(); i++) {
            if (value.equals(items.get(i).getValue())) {
                found = items.get(i);
                break;
            }
        }
        items.remove(found);
        mListItems = items.toArray(new ListItem[0]);
    }


    @Override
    public String getSummary(@NonNull Context context) {
        if (mSelection < mListItems.length && mSelection >= 0) {
            return mListItems[mSelection].getCaption();
        } else {
            Log.e(TAG, "Invalid selection " + mSelection);
            return "";
        }
    }

    public String getSelectedValue() {
        if (mSelection < mListItems.length && mSelection >= 0) {
            return mListItems[mSelection].getValue();
        } else {
            Log.e(TAG, "Invalid selection " + mSelection);
            return "";
        }
    }

    public void setSelectedValue(String value) {
        for (int i = 0; i < mListItems.length; ++i) {
            if (TextUtils.equals(mListItems[i].getValue(), value)) {
                mSelection = i;
                return;
            }
        }
        Log.e(TAG, "Invalid value " + value);
    }

    public int getSelection() {
        return mSelection;
    }

    public void setSelection(int selection) {
        if (selection < mListItems.length && selection >= 0) {
            mSelection = selection;
        } else {
            Log.e(TAG, "Invalid selection " + selection);
        }
    }

    @Nullable
    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    public boolean isSelectable() {
        return mIsSelectable;
    }

    public void setIconSize(int size) {
        mIconSize = size;
    }

    public int getIconSize() {
        return mIconSize;
    }

}
