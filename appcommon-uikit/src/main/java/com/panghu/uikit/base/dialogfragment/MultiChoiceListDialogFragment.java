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
import android.widget.CheckedTextView;

import com.panghu.uikit.R;
import com.panghu.uikit.base.field.FieldId;
import com.panghu.uikit.base.field.MultiChoiceListField;


/**
 * Created by nathan.wei on 5/21/18.
 */

public class MultiChoiceListDialogFragment extends FieldDialogFragment {

    private static final String ARG_LIST_FIELD = "LIST_FIELD";
    private MultiChoiceListField mListField;
    private ListDialogAdapter mListAdapter;

    @NonNull
    public static MultiChoiceListDialogFragment newInstance(@NonNull MultiChoiceListField listField) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_LIST_FIELD, listField);
        MultiChoiceListDialogFragment fragment = new MultiChoiceListDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mListField = (MultiChoiceListField) getArguments().getSerializable(ARG_LIST_FIELD);
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
        builder.setNegativeButton(R.string.cancel, (dialog, which) -> dialog.cancel());

        builder.setPositiveButton(mListField.getId() == FieldId.ATTACHMENT_DELETE_FIELD_ID ? R.string.remove : R.string.ok, (dialog, which) -> {
            if (mOnFieldCompletedListener != null) {
                mOnFieldCompletedListener.onFieldCompleted(mListField);
            }
            getDialog().dismiss();
        });
        RecyclerView recyclerView = new RecyclerView(requireContext());
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

        @Override
        @NonNull
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.common_multi_choice_list_item_view, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            boolean isSelected = mListField.getSelections().contains(position);
            ListItem listItem = mListField.getItem(position);
            holder.mCheckedTextView.setChecked(isSelected);
            holder.mCheckedTextView.setText(listItem.getCaption());
            holder.mCheckedTextView.setOnClickListener(this);
            holder.mCheckedTextView.setTag(position);
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
            CheckedTextView checkedTextView = (CheckedTextView) view;
            checkedTextView.toggle();
            mListField.toggleSelection((int) checkedTextView.getTag());
        }
    }

    private static class ViewHolder extends RecyclerView.ViewHolder {
        CheckedTextView mCheckedTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            mCheckedTextView = itemView.findViewById(R.id.checked_view);
        }
    }
}
