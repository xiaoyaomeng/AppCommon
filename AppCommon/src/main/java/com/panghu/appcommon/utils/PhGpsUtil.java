package com.panghu.appcommon.utils;

import android.content.Context;
import android.net.ConnectivityManager;

import java.lang.reflect.Method;

/**
 * PhGpsUtil
 *
 * @desc Gps相关操作类
 * @autor lijiangping
 * @wechat ljphhj
 *
 **/
public class PhGpsUtil {

    /**
     * 检测GPRS是否打开
     *
     * @param context
     * @return
     */
    public static boolean isOpen(Context context) {
        ConnectivityManager mCM;
        mCM = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        Class cmClass = mCM.getClass();
        Class[] argClasses = null;
        Object[] argObject = null;

        Boolean isOpen = false;
        try {
            Method method = cmClass.getMethod("getMobileDataEnabled", argClasses);

            isOpen = (Boolean) method.invoke(mCM, argObject);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return isOpen;
    }

    /**
     * 开启/关闭GPRS
     *
     * @param context
     * @param isEnable
     */
    public static void openGps(Context context, boolean isEnable) {
        ConnectivityManager mCM;
        mCM = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        Class cmClass = mCM.getClass();
        Class[] argClasses = new Class[1];
        argClasses[0] = boolean.class;

        try {
            Method method = cmClass.getMethod("setMobileDataEnabled", argClasses);
            method.invoke(mCM, isEnable);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
