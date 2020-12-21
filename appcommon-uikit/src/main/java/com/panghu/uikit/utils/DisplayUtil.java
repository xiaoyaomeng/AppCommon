package com.panghu.uikit.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.DisplayCutoutCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Display;
import android.view.DisplayCutout;
import android.view.View;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowManager;

public class DisplayUtil {

    public static int getScreenHeight(@NonNull Context context) {
        return getScreenSize(context).y;
    }

    public static int getScreenWidth(@NonNull Context context) {
        return getScreenSize(context).x;
    }

    public static Point getScreenSize(@NonNull Context context) {
        Point size = new Point();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getSize(size);
        return size;
    }

    public static int getStatusBarHeight(@NonNull Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static int getToolbarHeight(Activity activity) {
        int result = 0;
        if (activity instanceof AppCompatActivity) {
            AppCompatActivity appCompatActivity = (AppCompatActivity) activity;
            if (appCompatActivity.getSupportActionBar() != null) {
                result = appCompatActivity.getSupportActionBar().getHeight();
            }
        } else {
            if (activity.getActionBar() != null) {
                result = activity.getActionBar().getHeight();
            }
        }
        return result;
    }

    public static int getScreenOrientation(Activity act) {
        int result = Configuration.ORIENTATION_UNDEFINED;
        Configuration config = act.getResources().getConfiguration();
        if (config != null) {
            int tmpOrient = config.orientation;
            if (Configuration.ORIENTATION_UNDEFINED == tmpOrient) {
                Display display = act.getWindowManager().getDefaultDisplay();
                if (display != null) {
                    if (display.getWidth() == display.getHeight()) {
                        tmpOrient = Configuration.ORIENTATION_SQUARE;
                    } else if (display.getWidth() < display.getHeight()) {
                        tmpOrient = Configuration.ORIENTATION_PORTRAIT;
                    } else {
                        tmpOrient = Configuration.ORIENTATION_LANDSCAPE;
                    }
                }
            }
            result = tmpOrient;
        }

        return result;
    }

    public static void showSystemUI(Window window) {
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    public static void hideSystemUI(Window window) {
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_IMMERSIVE);
    }


}
