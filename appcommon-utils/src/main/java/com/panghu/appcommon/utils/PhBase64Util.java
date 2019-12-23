package com.panghu.appcommon.utils;

import android.util.Base64;

/**
 * PhBase64Util
 *
 * @desc 提供base64和字符串转换操作
 * @autor lijiangping
 * @wechat ljphhj
 * @email lijiangping.zz@gmail.com
 *
 **/
public class PhBase64Util {

    /**
     * base64编码
     *
     * @param input
     * @return
     */
    public static byte[] encodeBase64(byte[] input) {
        return Base64.encode(input, Base64.DEFAULT);
    }

    /**
     * base64编码
     *
     * @param s
     * @return
     */
    public static String encodeBase64(String s) {
        return Base64.encodeToString(s.getBytes(), Base64.DEFAULT);
    }

    /**
     * base64解码
     *
     * @param input
     * @return
     */
    public static byte[] decodeBase64(byte[] input) {
        return Base64.decode(input, Base64.DEFAULT);
    }

    /**
     * base64解码
     *
     * @param s
     * @return
     */
    public static String decodeBase64(String s) {
        return new String(Base64.decode(s, Base64.DEFAULT));
    }

}
