package com.panghu.uikit.base.field;

import android.annotation.SuppressLint;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import com.panghu.uikit.R;


/**
 * Created by panghu on 9/1/16.
 */
public class EditFieldPresenter extends AbstractFieldPresenter {

    @Override
    protected void onCreateCustomView(FieldViewHolder viewHolder) {
        ViewGroup fieldView = (ViewGroup) viewHolder.itemView;
        fieldView.setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
        final EditText editText = (EditText) viewHolder.mCustomView;
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                EditTextField editTextField = (EditTextField)editText.getTag();
                if (editTextField != null) {
                    editTextField.setText(s.toString());
                }
            }
        });
    }

    @SuppressLint("ResourceType")
    @Override
    protected void onBindCustomView(View customView, AbstractField field) {
        final EditTextField editTextField = (EditTextField) field;
        EditText editText = (EditText) customView;
        String hintText = editTextField.getHintText();
        if (!TextUtils.isEmpty(hintText)) {
            editText.setHint(hintText);
            editText.setContentDescription(hintText);
        } else if (editTextField.getHintResource() > 0) {
            String hint = editText.getResources().getString(editTextField.getHintResource());
            editText.setHint(hint);
            editText.setContentDescription(hint);
        }
        editText.setMaxLines(editTextField.getMaxLine());
        editText.setSingleLine(editTextField.getMaxLine() == 1);
        editText.setTag(field);
        editText.setText(editTextField.getText());
        if (editTextField.isAutoFocusable()) {
            editText.requestFocus();
            int selection = editText.getText().length();
            editText.setSelection(selection);
        }
    }

    @Override
    protected int onInflateCustomView() {
        return R.layout.common_field_custom_edit_view;
    }
}
