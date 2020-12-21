package com.panghu.uikit.base.dialogfragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.panghu.uikit.R;
import com.panghu.uikit.base.field.ListField;
import com.glip.widgets.utils.AccessibilityUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Created by panghu on 9/1/16.
 */
public class ListDialogFragment extends FieldDialogFragment implements ListDialogAdapter.OnItemClickListener,
        DialogInterface.OnClickListener {

    private static final String ARG_LIST_FIELD = "LIST_FIELD";
    private static final String ARG_POSITIVE_TEXT = "POSITIVE_TEXT";
    private static final String ARG_NEGATIVE_TEXT = "NEGATIVE_TEXT";
    private ListField mListField;
    private int mPositiveButtonTextId;
    private int mNegativeButtonTextId;
    private ListItem mCancelItem = null;

    @NonNull
    public static ListDialogFragment newInstance(@NonNull ListField listField) {
        return newInstance(listField, 0, 0);
    }

    @NonNull
    public static ListDialogFragment newInstance(@NonNull ListField listField, @StringRes int positiveButtonTextId,
                                                 @StringRes int negativeButtonTextId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_LIST_FIELD, listField);
        args.putInt(ARG_POSITIVE_TEXT, positiveButtonTextId);
        args.putInt(ARG_NEGATIVE_TEXT, negativeButtonTextId);
        ListDialogFragment fragment = new ListDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null) {
            mListField = (ListField) arguments.getSerializable(ARG_LIST_FIELD);
            mPositiveButtonTextId = arguments.getInt(ARG_POSITIVE_TEXT, 0);
            mNegativeButtonTextId = arguments.getInt(ARG_NEGATIVE_TEXT, 0);
        }
    }

    @SuppressLint("ResourceType")
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        if (mListField.getTitleResource() > 0) {
            builder.setTitle(mListField.getTitleResource());
        }

        if (!TextUtils.isEmpty(mListField.getMessage())) {
            builder.setMessage(mListField.getMessage());
        }
        RecyclerView recyclerView = new RecyclerView(requireContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        List<ListItem> listItems = new ArrayList<>(Arrays.asList(mListField.getItems()));
        if (AccessibilityUtils.isAccessibilityOn(requireContext())) {
            listItems.add(getAccessibilityCancelItem());
        }
        ListDialogAdapter listDialogAdapter = new ListDialogAdapter(mListField.isSelectable());
        listDialogAdapter.setListItems(listItems);
        listDialogAdapter.setOnItemClickListener(ListDialogFragment.this);
        listDialogAdapter.setSelection(mListField.getSelection());
        listDialogAdapter.setIconSize(mListField.getIconSize());
        recyclerView.setAdapter(listDialogAdapter);
        recyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        builder.setView(recyclerView);
        if (mPositiveButtonTextId > 0) {
            builder.setPositiveButton(mPositiveButtonTextId, this);
        }
        if (mNegativeButtonTextId > 0) {
            builder.setNegativeButton(mNegativeButtonTextId, this);
        }
        return builder.create();
    }

    private ListItem getAccessibilityCancelItem() {
        if (mCancelItem == null) {
            mCancelItem = new ListItem(getString(R.string.cancel), "", false);
        }
        return mCancelItem;
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        if (mOnFieldCompletedListener != null) {
            mOnFieldCompletedListener.onFieldCanceled(mListField);
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        if (mCancelItem != null && position >= mListField.getCount()) {
            getDialog().dismiss();
            return;
        }
        mListField.setSelection(position);
        if (mOnFieldCompletedListener != null) {
            mOnFieldCompletedListener.onFieldCompleted(mListField);
        }
        getDialog().dismiss();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        Fragment targetFragment = getTargetFragment();
        if (targetFragment instanceof DialogInterface.OnClickListener) {
            ((DialogInterface.OnClickListener) targetFragment).onClick(dialog, which);
        }
    }
}
