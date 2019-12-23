package com.panghu.appcommon.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

/**
 * PhAlarmUtil
 *
 * @desc 定时器操作类
 * @autor lijiangping
 * @wechat ljphhj
 * @email lijiangping.zz@gmail.com
 *
 **/
public class PhAlarmUtil {

    /**
     * 开启定时器
     */
    public static void startAlarmIntent(Context context, int triggerAtMillis, PendingIntent pendingIntent) {
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        manager.set(AlarmManager.RTC_WAKEUP,triggerAtMillis, pendingIntent);
    }

    /**
     * 关闭定时器
     */
    public static void stopAlarmIntent(Context context, PendingIntent pendingIntent) {
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        manager.cancel(pendingIntent);
    }

    /**
     * 开启轮询服务
     */
    public static void startAlarmService(Context context, int triggerAtMillis, Class<?> cls, String action) {
        Intent intent = new Intent(context, cls);
        intent.setAction(action);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        startAlarmIntent(context, triggerAtMillis,pendingIntent);
    }

    /**
     * 停止启轮询服务
     */
    public static void stopAlarmService(Context context, Class<?> cls, String action) {
        Intent intent = new Intent(context, cls);
        intent.setAction(action);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        stopAlarmIntent(context, pendingIntent);
    }

}
