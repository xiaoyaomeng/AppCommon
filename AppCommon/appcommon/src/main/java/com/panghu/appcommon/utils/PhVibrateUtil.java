package com.panghu.appcommon.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Vibrator;

/**
 * PhVibrateUtil
 * 震动操作类
 *
 * @desc 基础震动/规律震动/
 * @autor lijiangping
 * @wechat ljphhj
 * @date 2019年12月19日17:27:12
 *
 **/
public class PhVibrateUtil {

    /**
     * need permission : <uses-permission android:name="android.permission.VIBRATE" />
     * 震动milliseconds毫秒
     *
     * @param context
     * @param milliseconds
     */
    @SuppressLint("MissingPermission")
    public static void vibrate(Context context, long milliseconds) {
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(milliseconds);
    }


    /**
     *
     * need permission : <uses-permission android:name="android.permission.VIBRATE" />
     * 按照pattern的规律震动,可设置重复
     *
     * @param context
     * @param pattern
     * @param repeat
     */
    @SuppressLint("MissingPermission")
    public static void vibrate(Context context, long[] pattern, int repeat) {
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(pattern, repeat);
    }

    /**
     * need permission : <uses-permission android:name="android.permission.VIBRATE" />
     * 取消震动
     *
     * @param context
     * @return
     */
    @SuppressLint("MissingPermission")
    public static void cancelVibrate(Context context){
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.cancel();
    }
}
