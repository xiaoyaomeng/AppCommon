package com.panghu.uikit.utils;

import android.app.Activity;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.core.content.ContextCompat;

import com.panghu.uikit.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.HashSet;
import java.util.Set;

public class KeyboardSwitcher {

    private Activity mActivity;
    private FloatingActionButton mSwitchButton;
    private int mDefaultBottomMargin;
    private int mHeight;
    private boolean mIsSwitchButtonVisible = true;
    private boolean mIsInMultiWindowMode = false;
    private OnSwitchInputTypeListener mOnSwitchInputTypeListener = null;
    private final Set<EditText> mEditTextCollection = new HashSet<EditText>();
    private final KeyboardUtil.SoftKeyboardHeightListener mKeyboardHeightObserver =
            (height -> {
                KeyboardSwitcher.this.mHeight = height;
                updateSwitchButton();
            });

    public KeyboardSwitcher(Activity activity) {
        this.mActivity = activity;
        initView();
    }

    public void destroy() {
        mEditTextCollection.clear();
        KeyboardUtil.removeSoftKeyboardHeightListener(mKeyboardHeightObserver);
    }

    private void initView() {
        LayoutInflater inflater =
                (LayoutInflater) mActivity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.keyboard_switcher, null, false);
        ViewGroup root = mActivity.findViewById(android.R.id.content);
        root.addView(popupView);

        mSwitchButton = popupView.findViewById(R.id.switchButton);
        mSwitchButton.setOnClickListener(view -> switchInputType());
        mSwitchButton.setImageResource(R.drawable.ic_letter_tint);
        updateSwitchButtonContentDescription(InputType.TYPE_CLASS_NUMBER);

        mDefaultBottomMargin =
                ((ViewGroup.MarginLayoutParams) mSwitchButton.getLayoutParams()).bottomMargin;
        KeyboardUtil.addSoftKeyboardHeightListener(mActivity, mKeyboardHeightObserver);
    }

    public void setOnSwitchInputTypeListener(OnSwitchInputTypeListener onSwitchInputTypeListener) {
        this.mOnSwitchInputTypeListener = onSwitchInputTypeListener;
    }

    public void setSwitchButtonVisible(boolean isSwitchButtonVisible) {
        this.mIsSwitchButtonVisible = isSwitchButtonVisible;
        updateSwitchButton();
    }

    public void setInMultiWindowMode(boolean inMultiWindowMode) {
        mIsInMultiWindowMode = inMultiWindowMode;
        updateSwitchButton();
    }

    private void updateSwitchButton() {
        ViewGroup.MarginLayoutParams params =
                (ViewGroup.MarginLayoutParams) mSwitchButton.getLayoutParams();
        params.bottomMargin = mHeight + mDefaultBottomMargin;
        mSwitchButton.setLayoutParams(params);
        if ((!mIsInMultiWindowMode && mHeight == 0) || !mIsSwitchButtonVisible) {
            if (mSwitchButton.isShown() || mSwitchButton.isOrWillBeShown()) {
                mSwitchButton.hide();
            }
        } else {
            if (!mSwitchButton.isShown() || mSwitchButton.isOrWillBeHidden()) {
                mSwitchButton.show();
            }
        }
    }

    private void switchInputType() {
        EditText editText = getFocusedEditText();
        if (editText != null) {
            if (mOnSwitchInputTypeListener != null) {
                mOnSwitchInputTypeListener.beforeChanged(editText.getInputType());
            }
            if (editText.getInputType() == InputType.TYPE_CLASS_NUMBER) {
                editText.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
            } else {
                editText.setInputType(InputType.TYPE_CLASS_NUMBER);
            }
            updateSwitchButtonResource(editText);
            if (mOnSwitchInputTypeListener != null) {
                mOnSwitchInputTypeListener.afterChanged(editText.getInputType());
            }
        }
    }

    private void updateSwitchButtonResource(EditText editText) {
        if (editText != null) {
            mSwitchButton.hide();
            mSwitchButton.setBackgroundTintList(
                    ContextCompat.getColorStateList(
                            editText.getContext(), R.color.colorPaletteBgIII));
            int inputType = editText.getInputType();
            int res = (inputType == InputType.TYPE_CLASS_NUMBER) ?
                    R.drawable.ic_letter_tint : R.drawable.ic_number_tint;
            mSwitchButton.setImageResource(res);
            updateSwitchButtonContentDescription(inputType);
            mSwitchButton.show();
        }
    }

    private void updateSwitchButtonContentDescription(int inputType) {
        String nextKeyboard = mSwitchButton.getContext().getString(R.string.accessibility_next_keyboard);
        String keyboard = mSwitchButton.getContext().getString(inputType == InputType.TYPE_CLASS_NUMBER ?
                R.string.accessibility_english_us : R.string.accessibility_number_pad);
        mSwitchButton.setContentDescription(String.format(nextKeyboard, keyboard));
    }

    public void bind(EditText editText) {
        EditText editTextInfo = find(editText);
        if (editTextInfo == null) {
            mEditTextCollection.add(editText);
        }
        updateSwitchButtonResource(editText);
    }

    public void unBind(EditText editText) {
        EditText editTextInfo = find(editText);
        if (editTextInfo != null) {
            mEditTextCollection.remove(editTextInfo);
        }
    }

    private EditText find(EditText editText) {
        if (mEditTextCollection.contains(editText)) {
            return editText;
        }
        return null;
    }

    private EditText getFocusedEditText() {
        for (EditText info : mEditTextCollection) {
            if (info.isFocused()) {
                return info;
            }
        }
        return null;
    }

    public interface OnSwitchInputTypeListener {
        void beforeChanged(int newInputType);

        void afterChanged(int newInputType);
    }
}
