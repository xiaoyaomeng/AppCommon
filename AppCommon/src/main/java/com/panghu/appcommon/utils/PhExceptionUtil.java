package com.panghu.appcommon.utils;

/**
 * PhExceptionUtil
 *
 * @desc
 * @autor lijiangping
 * @wechat ljphhj
 * @date 2019年12月19日
 **/
public class PhExceptionUtil {

    public static StackTraceElement getCallerStackTraceElement() {
        return Thread.currentThread().getStackTrace()[4];
    }

}
