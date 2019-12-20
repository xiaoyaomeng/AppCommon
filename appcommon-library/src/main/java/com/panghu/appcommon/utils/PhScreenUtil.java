package com.panghu.appcommon.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * PhScreenUtil
 *
 * 屏幕操作类
 *
 * @desc
 * @autor lijiangping
 * @wechat ljphhj
 * @email lijiangping.zz@gmail.com
 *
 **/
public class PhScreenUtil {


    /**
     * 获取屏幕宽度
     *
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    /**
     * 获取屏幕高度
     *
     * @param context
     * @return
     */
    public static int getScreenHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    /**
     * 获取准确的屏幕高度
     *
     * @param context
     * @return
     */
    public static int getScreenRealHeight(Context context) {
        int h;
        WindowManager winMgr = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = winMgr.getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        if (Build.VERSION.SDK_INT >= 17) {
            display.getRealMetrics(dm);
            h = dm.heightPixels;
        } else {
            try {
                Method method = Class.forName("android.view.Display").getMethod("getRealMetrics", DisplayMetrics.class);
                method.invoke(display, dm);
                h = dm.heightPixels;
            } catch (Exception e) {
                display.getMetrics(dm);
                h = dm.heightPixels;
            }
        }
        return h;
    }

    /**
     * 获取状态栏高度[可能取不到]
     *
     * @param context
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        Class<?> c;
        Object obj;
        Field field;
        int statusBarHeight = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            int x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = context.getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return statusBarHeight;
    }

    private static  int mStatusBarHeight = 0;

    /**
     * 获取状态栏高度
     *
     * @param activity
     * @return
     */
    public static int getStatusBarHeight(Activity activity) {
        if(mStatusBarHeight != 0){
            return mStatusBarHeight;
        }

        int statusBarHeight = 0;

        //尝试第一种获取方式
        int resourceId = activity.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = activity.getResources().getDimensionPixelSize(resourceId);
            if(statusBarHeight > 0){
                mStatusBarHeight = statusBarHeight;
                return statusBarHeight;
            }
        }
        //尝试第二种获取方式
        if(statusBarHeight <= 0){
            Rect rectangle = new Rect();
            Window window = activity.getWindow();
            window.getDecorView().getWindowVisibleDisplayFrame(rectangle);
            statusBarHeight = rectangle.top;
            if(statusBarHeight > 0){
                mStatusBarHeight = statusBarHeight;
                return statusBarHeight;
            }
        }
        //尝试第三种获取方式
        if(statusBarHeight <= 0 ){
            try {
                getStatusBarHeight(activity.getApplicationContext());
                return statusBarHeight;
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        mStatusBarHeight =  PhDensityUtil.dp2px(activity, 20);
        return mStatusBarHeight;
    }

    /**
     * 获取NavigationBar的高度
     * @param context
     * @return
     */
    public static int getNavigationBarHeight(Context context) {
        Resources resources = context.getResources();
        int identifier = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        return resources.getDimensionPixelOffset(identifier);
    }

}
