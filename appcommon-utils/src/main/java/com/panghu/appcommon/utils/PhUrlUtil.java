package com.panghu.appcommon.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * PhUrlUtil
 *
 * @desc Url操作类
 * @autor lijiangping
 * @wechat ljphhj
 * @email lijiangping.zz@gmail.com
 *
 **/
public class PhUrlUtil {

    private static final String CHARSET_NAME = "UTF-8";

    /**
     * url编码
     *
     * @param url
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String encode(String url) throws UnsupportedEncodingException {
        return URLEncoder.encode(url, CHARSET_NAME);
    }

    /**
     * url解码
     *
     * @param url
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String decode(String url) throws UnsupportedEncodingException {
        return URLDecoder.decode(url, CHARSET_NAME);
    }

}
