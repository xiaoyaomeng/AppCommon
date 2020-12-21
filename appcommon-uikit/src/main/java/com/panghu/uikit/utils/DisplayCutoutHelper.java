package com.panghu.uikit.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.DisplayCutoutCompat;

import android.view.DisplayCutout;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowManager;

/**
 */
public class DisplayCutoutHelper {
    /**
     * adapter cutOut layout in Fragment,adjust the topMargin dynamically
     *
     * @param activity activity
     * @param view     rootView
     */
    public static void adaptFragmentInCutout(@NonNull Activity activity, @NonNull View view) {
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            int statusBarHeight = DisplayUtil.getStatusBarHeight(activity);
            ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            if (layoutParams.topMargin != statusBarHeight) {
                layoutParams.topMargin = statusBarHeight;
            }


        }
    }

    /**
     * adjust the  cutout layout in activity by setFitsSystemWindows
     *
     * @param activity    activity
     * @param isLandScape the device orientation when the device orientation is LandScape
     *  the value is true,when the device orientation is portrait the value is false
     *
     */
    @TargetApi(Build.VERSION_CODES.P)
    public static void adjustActivityCutout(@NonNull Activity activity, boolean isLandScape) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            if (!activity.isFinishing()) {
                Window window = activity.getWindow();
                if (window != null) {
                    WindowManager.LayoutParams lp = window.getAttributes();
                    lp.layoutInDisplayCutoutMode = isLandScape ?
                            WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_NEVER :
                            WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
                    window.setAttributes(lp);

                }

            }
        }


    }

    /**
     * reset the layout when ConfigureChanged
     *
     * @param activity      activity
     * @param configuration current Activity configuration when configureChanged
     */
    public static void reLayoutOnConfigureChanged(@NonNull Activity activity, Configuration configuration) {
        if (configuration != null) {
            if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                adjustActivityCutout(activity, true);
            } else if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                adjustActivityCutout(activity, false);

            }
        }

    }

    @TargetApi(Build.VERSION_CODES.P)
    public static void showWindowInCutOut(Window window) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            WindowManager.LayoutParams layoutParams = window.getAttributes();
            layoutParams.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
            window.setAttributes(layoutParams);
        }
    }

    public static float getCutoutSafeInsetTop(View view) {
        DisplayCutoutCompat displayCutout = getDisplayCutout(view);
        if (displayCutout != null) {
            return displayCutout.getSafeInsetTop();
        } else {
            return 0f;
        }
    }

    @Nullable
    public static DisplayCutoutCompat getDisplayCutout(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            WindowInsets windowInsets = view.getRootWindowInsets();
            if (windowInsets != null) {
                DisplayCutout displayCutout = windowInsets.getDisplayCutout();
                if (displayCutout != null) {
                    Rect safeInsets = new Rect(displayCutout.getSafeInsetLeft(),
                            displayCutout.getSafeInsetTop(),
                            displayCutout.getSafeInsetRight(),
                            displayCutout.getSafeInsetBottom());
                    return new DisplayCutoutCompat(safeInsets, displayCutout.getBoundingRects());
                }
            }
        }
        return null;
    }

    /**
     * Used to determine whether there is Cutout
     *
     * @param view view
     * @return Whether there is cutout
     */
    public static boolean hasCutout(@NonNull View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            WindowInsets windowInsets = view.getRootWindowInsets();
            if (windowInsets != null) {
                return null != windowInsets.getDisplayCutout();
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
}
