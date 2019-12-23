package com.panghu.appcommon.utils;

import java.util.Random;

/**
 * PhRandomUtil
 *
 * @desc 随机数工具类
 * @autor lijiangping
 * @wechat ljphhj
 * @email lijiangping.zz@gmail.com
 *
 **/
public class PhRandomUtil {

    public static final String NUMBERS_AND_LETTERS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String NUMBERS             = "0123456789";
    public static final String LETTERS             = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String CAPITAL_LETTERS     = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String LOWER_CASE_LETTERS  = "abcdefghijklmnopqrstuvwxyz";

    private PhRandomUtil() {
        throw new AssertionError();
    }

    /**
     * 获取一个指定长度的随机字符串，包括字母和数字
     * @param length
     * @return
     */
    public static String getRandomNumbersAndLetters(int length) {
        return getRandom(NUMBERS_AND_LETTERS, length);
    }

    /**
     * 获取一个指定长度的随机字符串，仅数字
     * @param length
     * @return
     */
    public static String getRandomNumbers(int length) {
        return getRandom(NUMBERS, length);
    }


    /**
     * 获取一个指定长度的随机字符串，仅字母
     * @param length
     * @return
     */
    public static String getRandomLetters(int length) {
        return getRandom(LETTERS, length);
    }

    /**
     * 获取一个指定长度的随机字符串，仅大写字母
     * @param length
     * @return
     */
    public static String getRandomUpperCaseLetters(int length) {
        return getRandom(CAPITAL_LETTERS, length);
    }

    /**
     * 获取一个指定长度的随机字符串，仅小写字母
     * @param length
     * @return
     */
    public static String getRandomLowerCaseLetters(int length) {
        return getRandom(LOWER_CASE_LETTERS, length);
    }

    /**
     * 从源字符串中获取长度length的随机字符串
     * @param source 字符串
     * @param length
     * @return
     */
    public static String getRandom(String source, int length) {
        return source == null ? null : getRandom(source.toCharArray(), length);
    }

    /**
     * 从源字符串中获取长度length的随机字符串
     * @param sourceChar 字符数组
     * @param length
     * @return
     */
    public static String getRandom(char[] sourceChar, int length) {
        if (sourceChar == null || sourceChar.length == 0 || length < 0) {
            return null;
        }

        StringBuilder str = new StringBuilder(length);
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            str.append(sourceChar[random.nextInt(sourceChar.length)]);
        }
        return str.toString();
    }

    /**
     * 获取0-max之前的随机int数值
     * @param max
     * @return
     */
    public static int getRandom(int max) {
        return getRandom(0, max);
    }


    /**
     * 获取min-max之前的随机int数值
     * @param min
     * @param max
     * @return
     */
    public static int getRandom(int min, int max) {
        if (min > max) {
            return 0;
        }
        if (min == max) {
            return min;
        }
        return min + new Random().nextInt(max - min);
    }
}
