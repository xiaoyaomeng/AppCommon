package com.panghu.uikit.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.PopupWindow;

import androidx.annotation.NonNull;
import androidx.core.view.DisplayCutoutCompat;

import com.panghu.uikit.R;

import java.util.HashMap;
import java.util.List;

/**
 * The util functions for keyboard.
 * <p/>
 * Created by panghu on 5/6/16.
 */
public class KeyboardUtil {


    private static final HashMap<SoftKeyboardToggleListener, KeyboardHeightProvider> sListenerMap
            = new HashMap<>();

    private static final HashMap<SoftKeyboardHeightListener, KeyboardHeightProvider> sSoftKeyboardHeightListenerMap
            = new HashMap<>();

    public interface SoftKeyboardToggleListener
    {
        void onToggleSoftKeyboard(boolean isVisible);
    }

    public interface SoftKeyboardHeightListener
    {
        void onSoftKeyboardHeightChanged(int height);
    }

    private KeyboardUtil() {
    }

    /**
     * Add a new keyboard listener
     * @param activity calling activity
     * @param listener callback
     */
    public static void addKeyboardToggleListener(Activity activity,
                                                 SoftKeyboardToggleListener listener)
    {
        removeKeyboardToggleListener(listener);

        sListenerMap.put(listener, new KeyboardHeightProvider(activity).setListener(listener));
    }

    /**
     * Remove a registered listener
     * @param listener {@link SoftKeyboardToggleListener}
     */
    public static void removeKeyboardToggleListener(SoftKeyboardToggleListener listener)
    {
        if(sListenerMap.containsKey(listener))
        {
            KeyboardHeightProvider k = sListenerMap.get(listener);
            k.removeListener();

            sListenerMap.remove(listener);
        }
    }

    /**
     * Add a new keyboard listener
     * @param activity calling activity
     * @param listener callback
     */
    public static void addSoftKeyboardHeightListener(Activity activity,
                                                 SoftKeyboardHeightListener listener)
    {
        removeSoftKeyboardHeightListener(listener);

        sSoftKeyboardHeightListenerMap.put(listener, new KeyboardHeightProvider(activity).setListener(listener));
    }

    /**
     * Remove a registered listener
     * @param listener {@link SoftKeyboardToggleListener}
     */
    public static void removeSoftKeyboardHeightListener(SoftKeyboardHeightListener listener)
    {
        if(sSoftKeyboardHeightListenerMap.containsKey(listener))
        {
            KeyboardHeightProvider k = sSoftKeyboardHeightListenerMap.get(listener);
            k.removeListener();
            sSoftKeyboardHeightListenerMap.remove(listener);
        }
    }
    /**
     * Remove all registered keyboard listeners
     */
    public static void removeAllKeyboardToggleListeners()
    {
        for(SoftKeyboardToggleListener l : sListenerMap.keySet()) {
            sListenerMap.get(l).removeListener();
        }

        sListenerMap.clear();
    }

    /**
     * Manually toggle soft keyboard visibility
     * @param context calling context
     */
    public static void toggleKeyboardVisibility(Context context)
    {
        InputMethodManager inputMethodManager = (InputMethodManager)
                context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    /**
     * Force closes the soft keyboard
     * @param activeView the view with the keyboard focus
     */
    public static void forceCloseKeyboard(View activeView)
    {
        InputMethodManager inputMethodManager = (InputMethodManager)
                activeView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activeView.getWindowToken(), 0);
    }

    public static void hideKeyboard(Window window) {
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }

    public static void hideKeyboard(Context context, IBinder windowToken) {
        InputMethodManager inputManager = (InputMethodManager) context.getSystemService(
                Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(windowToken, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public static void showKeyboard(Context context, View view) {
        InputMethodManager inputManager = (InputMethodManager) context.getSystemService(
                Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
    }

    public static boolean isVirtualKeyEvent(KeyEvent keyEvent) {
        return keyEvent != null && keyEvent.getDevice() != null && keyEvent.getDevice().isVirtual();
    }

    public static boolean inKeyguardRestrictedInputMode(Context context) {
        KeyguardManager keyguardManager = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
        return keyguardManager != null && keyguardManager.inKeyguardRestrictedInputMode();
    }

    public static boolean isHardwareKeyboardAvailable(@NonNull Context context) {
        return context.getResources().getConfiguration().keyboard != Configuration.KEYBOARD_NOKEYS;
    }

    static class KeyboardHeightProvider extends PopupWindow {

        /**
         * The tag for logging purposes
         */
        private final static String TAG = "KeyboardHeightProvider";

        /**
         * The visibility of the keyboard
         */
        private boolean isKeyboardVisible = false;

        /**
         * The cached height of the keyboard
         */
        private int keyboardHeight = 0;

        /**
         * The view that is used to calculate the keyboard height
         */
        private View popupView;

        /**
         * The parent view
         */
        private View parentView;

        /**
         * The root activity that uses this KeyboardHeightProvider
         */
        private Activity activity;

        private final Handler mMainHandler = new Handler(Looper.getMainLooper());

        private final static long MAGIC_NUMBER_DELAY_INIT_KEYBOARD_MILLISECOND = 50;
        /**
         * Construct a new KeyboardHeightProvider
         *
         * @param activity The parent activity
         */
        KeyboardHeightProvider(Activity activity) {
            super(activity);
            this.activity = activity;

            LayoutInflater inflator = (LayoutInflater) activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            this.popupView = inflator.inflate(R.layout.keyboard_measurer, null, false);
            setContentView(popupView);

            setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
            setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);

            parentView = activity.findViewById(android.R.id.content);

            setWidth(0);
            setHeight(WindowManager.LayoutParams.MATCH_PARENT);

            popupView.getViewTreeObserver().addOnGlobalLayoutListener(mOnGlobalLayoutListener);
            mMainHandler.post(initPopWindowLocationRunnable);

        }

        private ViewTreeObserver.OnGlobalLayoutListener mOnGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                if (popupView != null) {
                    handleOnGlobalLayout();
                }
            }
        };

        private void removeListener() {
            close();
        }

        private SoftKeyboardToggleListener softKeyboardToggleListener;

        public KeyboardHeightProvider setListener(SoftKeyboardToggleListener listener) {
            softKeyboardToggleListener = listener;
            return this;
        }

        private SoftKeyboardHeightListener softKeyboardHeightListener;

        public KeyboardHeightProvider setListener(SoftKeyboardHeightListener listener) {
            softKeyboardHeightListener = listener;
            return this;
        }

        private Runnable initPopWindowLocationRunnable = () -> {
             boolean isStart = start();
             if(!isStart){
                 initPopWindowLocationDelay();
             }
        };

        private void initPopWindowLocationDelay(){
            mMainHandler.postDelayed(initPopWindowLocationRunnable,MAGIC_NUMBER_DELAY_INIT_KEYBOARD_MILLISECOND);
        }
        /**
         * Start the KeyboardHeightProvider, this must be called after the onResume of the Activity.
         * PopupWindows are not allowed to be registered before the onResume has finished
         * of the Activity.
         */
        public boolean start() {
            if (!isShowing() && parentView.getWindowToken() != null) {
                setBackgroundDrawable(new ColorDrawable(0));
                showAtLocation(parentView, Gravity.NO_GRAVITY, 0, 0);
                return true;
            }
            return false;
        }

        /**
         * Close the keyboard height provider,
         * this provider will not be used anymore.
         */
        public void close() {
            //addOnGlobalLayoutListener
            popupView.getViewTreeObserver().removeOnGlobalLayoutListener(mOnGlobalLayoutListener);
            mMainHandler.removeCallbacksAndMessages(null);
            this.softKeyboardToggleListener = null;
            this.softKeyboardHeightListener = null;
            dismiss();
        }

        /**
         * Popup window itself is as big as the window of the Activity.
         * The keyboard can then be calculated by extracting the popup view bottom
         * from the activity window height.
         */
        private void handleOnGlobalLayout() {

            Point displaySize = new Point();
            activity.getWindowManager().getDefaultDisplay().getSize(displaySize);

            Rect rect = new Rect();
            popupView.getWindowVisibleDisplayFrame(rect);

            // REMIND, you may like to change this using the fullscreen size of the phone
            // and also using the status bar and navigation bar heights of the phone to calculate
            // the keyboard height. But this worked fine on a Nexus.
            int topCutoutHeight = 0;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                topCutoutHeight = getTopCutoutHeight();
            }
            int visibleDisplayHeight = rect.bottom;
            if (DeviceUtil.isRunningOnChromebook(activity)) {
                // ChromeBook run android app in an embedded window on the screen,
                // the real popup view height is rect.bottom - rect.top
                visibleDisplayHeight -= rect.top;
            }

            int keyboardHeight = displaySize.y + topCutoutHeight - visibleDisplayHeight;

            if (this.keyboardHeight != keyboardHeight) {
                this.keyboardHeight = keyboardHeight;
                notifyKeyboardHeightChanged(this.keyboardHeight);
            }
        }

        @TargetApi(android.os.Build.VERSION_CODES.P)
        private int getTopCutoutHeight(){
            View decorView = activity.getWindow().getDecorView();
            if(decorView == null){
                return 0;
            }
            int cutOffHeight = 0;
            DisplayCutoutCompat displayCutout = DisplayCutoutHelper.getDisplayCutout(decorView);
            if (displayCutout != null) {
                List<Rect> list = displayCutout.getBoundingRects();
                for (Rect rect : list) {
                    if (rect.top == 0) {
                        cutOffHeight += rect.bottom - rect.top;
                    }
                }
            }

            return cutOffHeight;
        }

        private void notifyKeyboardHeightChanged(int height) {
            //In samsung dex mode, the keyboard height < 0
            boolean isKeyboardShow = (height > 0);
            if (this.isKeyboardVisible != isKeyboardShow) {
                this.isKeyboardVisible = isKeyboardShow;
                if (softKeyboardToggleListener != null) {
                    softKeyboardToggleListener.onToggleSoftKeyboard(this.isKeyboardVisible);
                }
            }

            if (softKeyboardHeightListener != null) {
                softKeyboardHeightListener.onSoftKeyboardHeightChanged(height);
            }
        }
    }
}
