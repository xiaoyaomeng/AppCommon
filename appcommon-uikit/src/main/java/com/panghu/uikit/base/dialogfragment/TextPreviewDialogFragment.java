package com.panghu.uikit.base.dialogfragment;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.core.view.GestureDetectorCompat;
import android.text.method.ScrollingMovementMethod;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.panghu.uikit.R;

/**
 * @author Created by locke.peng on 10/17/17.
 */

public class TextPreviewDialogFragment extends DialogFragment {

    private static final String ARG_CONTENT = "CONTENT";
    private String mContentText;

    public static TextPreviewDialogFragment newInstance(String content) {
        TextPreviewDialogFragment fragment = new TextPreviewDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARG_CONTENT, content);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initArguments(getArguments());
    }

    private void initArguments(Bundle bundle) {
        if (bundle != null) {
            mContentText = bundle.getString(ARG_CONTENT);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity(), R.style.GlipTheme);
        dialog.setContentView(R.layout.common_text_boom_view);
        TextView textView = dialog.findViewById(R.id.content_text_view);
        textView.setMovementMethod(ScrollingMovementMethod.getInstance());
        textView.setText(mContentText);
        textView.setOnTouchListener(new OnPreviewTextTouchListener(getActivity(), e -> dismiss()));

        Window window = dialog.getWindow();
        if (window != null) {
            window.setWindowAnimations(R.style.GlipWidget_DialogAnimation_Alpha);
            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        return dialog;
    }

    private interface OnSingleTapListener {
        void onSingleTapUp(MotionEvent e);
    }

    private static class OnPreviewTextTouchListener implements View.OnTouchListener, GestureDetector.OnGestureListener {

        private GestureDetectorCompat mDetector;
        private OnSingleTapListener mSingleTapListener;

        public OnPreviewTextTouchListener(Context context, OnSingleTapListener singleTapListener) {
            mDetector = new GestureDetectorCompat(context, this);
            mSingleTapListener = singleTapListener;
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            mDetector.onTouchEvent(event);
            return false;
        }

        @Override
        public boolean onDown(MotionEvent e) {
            return false;
        }

        @Override
        public void onShowPress(MotionEvent e) {
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            if (mSingleTapListener != null) {
                mSingleTapListener.onSingleTapUp(e);
            }
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return false;
        }
    }
}
