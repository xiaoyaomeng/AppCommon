package com.panghu.appcommon.utils;

import android.os.Handler;
import android.os.Looper;

/**
 * PhMainHandlerUtil
 *
 *
 * @desc 维护一个全局的主线程Handler
 * @autor lijiangping
 * @wechat ljphhj
 * @date 2019年12月19日
 *
 **/
public class PhMainHandlerUtil {
    public static final Handler MAIN_HANDLER = new Handler(Looper.getMainLooper());

    public static void runOnUiThread(Runnable runnable){
        MAIN_HANDLER.post(runnable);
    }

    public static void runOnUiThreadDelay(Runnable runnable, long delayMillis){
        MAIN_HANDLER.postDelayed(runnable, delayMillis);
    }

    public static void removeRunable(Runnable runnable){
        MAIN_HANDLER.removeCallbacks(runnable);
    }

    public static void removeCallbacksAndMessages(Object token){
        MAIN_HANDLER.removeCallbacksAndMessages(token);
    }
}
