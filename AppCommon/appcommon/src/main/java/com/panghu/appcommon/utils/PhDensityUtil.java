package com.panghu.appcommon.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * PhDensityUtil
 * px和dp互相转换 、px和sp互相转换
 *
 * @desc
 * @autor lijiangping
 * @wechat ljphhj
 *
 **/
public class PhDensityUtil {

    private static float mScaledDensity = 0.0f;
    private static float mDensity = 0.0f;

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dp2px(Context context, float dpValue) {
        if(mDensity == 0) {
            mDensity = context.getResources().getDisplayMetrics().density;
        }
        return (int) (0.5f + dpValue * mDensity);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static float px2dp(Context context, float pxValue) {
        if(mDensity == 0) {
            mDensity = context.getResources().getDisplayMetrics().density;
        }
        return (pxValue / context.getResources().getDisplayMetrics().density);
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     * @param pxValue （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int px2sp(Context context, float pxValue) {
        if(mScaledDensity == 0)
            mScaledDensity = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / mScaledDensity + 0.5f);
    }

    /**
     * sp -> px
     *
     * @return
     */
    public static int sp2px(Context context, float spValue) {
        if(mScaledDensity==0)
            mScaledDensity = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * mScaledDensity + 0.5f);
    }


    /**
     * 获取当前的Density名
     *
     * @param context
     * @return
     */
    public static String getDensity(Context context) {
        String densityStr = null;
        final int density = context.getResources().getDisplayMetrics().densityDpi;
        switch (density) {
            case DisplayMetrics.DENSITY_LOW:
                densityStr = "LDPI";
                break;
            case DisplayMetrics.DENSITY_MEDIUM:
                densityStr = "MDPI";
                break;
            case DisplayMetrics.DENSITY_TV:
                densityStr = "TVDPI";
                break;
            case DisplayMetrics.DENSITY_HIGH:
                densityStr = "HDPI";
                break;
            case DisplayMetrics.DENSITY_XHIGH:
                densityStr = "XHDPI";
                break;
            case DisplayMetrics.DENSITY_400:
                densityStr = "XMHDPI";
                break;
            case DisplayMetrics.DENSITY_XXHIGH:
                densityStr = "XXHDPI";
                break;
            case DisplayMetrics.DENSITY_XXXHIGH:
                densityStr = "XXXHDPI";
                break;
        }
        return densityStr;
    }

}
