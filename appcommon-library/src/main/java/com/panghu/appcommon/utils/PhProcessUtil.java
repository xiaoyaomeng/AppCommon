package com.panghu.appcommon.utils;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.os.Process;

import java.lang.reflect.Method;
import java.util.List;

/**
 * PhProcessUtil
 * 提供进程操作和判断的类
 *
 * @desc
 * @autor lijiangping
 * @wechat ljphhj
 * @email lijiangping.zz@gmail.com
 *
 **/
public class PhProcessUtil {

    /**
     * need permission:
     * <uses-permission android:name =“android.permission.GET_TASKS” />
     *
     * 判断当前进程是否前台运行
     *
     * @param context
     * @return
     */
    public static boolean isRunningForeground(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> taskList = am.getRunningTasks(1);
        if (taskList != null && !taskList.isEmpty()) {
            ComponentName componentName = taskList.get(0).topActivity;
            if (componentName != null && componentName.getPackageName().equals(context.getPackageName())) {
                return true;
            }
        }
        return false;
    }


    /**
     * 是否是主进程
     * @param context
     * @return
     */
    public static boolean isMainThreadProcess(Context context) {
        String procName = getCurrentProcessName(context);
        return procName == null || procName.equalsIgnoreCase(context.getPackageName());
    }

    /**
     * 获取当前进程名字
     * @param context
     * @return
     */
    public static String getCurrentProcessName(Context context) {
        int pid = Process.myPid();
        ActivityManager mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : mActivityManager.getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }

    /**
     * 杀死当前进程
     */
    public static void killCurrentProcess() {
        Process.killProcess(Process.myPid());
        System.exit(0);
    }

    /**
     * 杀死指定进程
     *
     * need permission :<uses-permission android:name="android.permission.KILL_BACKGROUND_PROCESSES" />
     * @param context
     * @param pid
     * @param processName
     */
    @SuppressLint("MissingPermission")
    public static void killProcesses(Context context, int pid, String processName) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        String packageName;
        try {
            if (!processName.contains(":")) {
                packageName = processName;
            } else {
                packageName = processName.split(":")[0];
            }
            activityManager.killBackgroundProcesses(packageName);
            Method forceStopPackage = activityManager.getClass().getDeclaredMethod("forceStopPackage", String.class);
            forceStopPackage.setAccessible(true);
            forceStopPackage.invoke(activityManager, packageName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
