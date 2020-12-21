package com.panghu.uikit.base.dialogfragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import com.panghu.uikit.R;
import com.panghu.uikit.base.field.ListField;


/**
 * Created by panghu on 9/1/16.
 */
public class SingleChoiceListDialogFragment extends FieldDialogFragment {

    private static final String ARG_LIST_FIELD = "LIST_FIELD";
    private ListField mListField;
    private ListDialogAdapter mListAdapter;

    public static SingleChoiceListDialogFragment newInstance(ListField listField) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_LIST_FIELD, listField);
        SingleChoiceListDialogFragment fragment = new SingleChoiceListDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mListField = (ListField) getArguments().getSerializable(ARG_LIST_FIELD);
    }


    @SuppressLint("ResourceType")
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        if (mListField.getTitleResource() > 0) {
            builder.setTitle(mListField.getTitleResource());
        }

        if (!TextUtils.isEmpty(mListField.getMessage())) {
            builder.setMessage(mListField.getMessage());
        }
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (mOnFieldCompletedListener != null) {
                    mOnFieldCompletedListener.onFieldCompleted(mListField);
                }
            }
        });
        RecyclerView recyclerView = new RecyclerView(getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mListAdapter = new ListDialogAdapter();
        recyclerView.setAdapter(mListAdapter);
        recyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        builder.setView(recyclerView);
        return builder.create();
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        if (mOnFieldCompletedListener != null) {
            mOnFieldCompletedListener.onFieldCanceled(mListField);
        }
    }


    private class ListDialogAdapter extends RecyclerView.Adapter<ViewHolder> implements View.OnClickListener {

        private RadioButton mSelectedRadioButton;

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.common_single_choice_list_item_view, parent, false));
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            boolean isSelected = position == mListField.getSelection();
            ListItem listItem = mListField.getItem(position);
            holder.mRadioButton.setChecked(isSelected);
            holder.mRadioButton.setText(listItem.getCaption());
            holder.mRadioButton.setOnClickListener(this);
            holder.mRadioButton.setTag(position);
            if (isSelected) {
                mSelectedRadioButton = holder.mRadioButton;
            }
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getItemCount() {
            return mListField.getCount();
        }

        @Override
        public void onClick(View view) {
            RadioButton radioButton = (RadioButton) view;
            if (mSelectedRadioButton != radioButton) {
                if (mSelectedRadioButton != null) {
                    mSelectedRadioButton.setChecked(false);
                }
                mSelectedRadioButton = radioButton;
                mSelectedRadioButton.toggle();
                mListField.setSelection((int) radioButton.getTag());
            }
        }
    }

    private static class ViewHolder extends RecyclerView.ViewHolder {
        RadioButton mRadioButton;

        public ViewHolder(View itemView) {
            super(itemView);
            mRadioButton = (RadioButton) itemView.findViewById(R.id.radio_button);
        }
    }
}
