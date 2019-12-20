package com.panghu.appcommon.utils;

import android.content.Context;
import android.text.ClipboardManager;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

/**
 * PhStringUtil
 *
 * @desc
 * @autor lijiangping
 * @wechat ljphhj
 * @date 2019年12月19日
 *
 **/
public class PhStringUtil {

    public static final String EMPTY = "";

    public static final int INDEX_NOT_FOUND = -1;

    /**
     * <p>The maximum size to which the padding constant(s) can expand.</p>
     */
    private static final int PAD_LIMIT = 8192;

    public PhStringUtil() {
        super();
    }

    public static boolean isNull(String str){
        return str == null || str.length() == 0 || str/*.toLowerCase()*/.equals("null");
    }

    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }


    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }


    // 判断一个字符串是否都为数字
    public static boolean isNumber(String strNum) {
        return strNum.matches("[0-9]{1,}");
    }

    /**
     * 判断该字符串是否同时有数字和字母
     *
     * @param str
     * @return
     */
    public static boolean hasNumAndLetter(String str) {
        boolean bl = false;
        if (isEmpty(str)) {
            return bl;
        }
        if (Pattern.compile("(?i)[a-z]").matcher(str).find() && Pattern.compile("(?i)[0-9]")
                .matcher(str)
                .find()) {
            bl = true;
        }
        return bl;
    }

    /**
     * 是否包含空格
     *
     * @param str
     * @return
     */
    public static boolean hasSpace(String str) {
        boolean has = false;
        if (!isEmpty(str)) {
            has = str.contains(" ");
        }
        return has;
    }

    public static long getLong(String value) {
        try {
            return Long.valueOf(value);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    public static Boolean getBoolean(String value) {
        try {
            return Boolean.valueOf(value);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    /**
     * 半角转换为全角
     *
     * @param input
     * @return
     */
    public static String ToDBC(String input) {
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 12288) {
                c[i] = (char) 32;
                continue;
            }
            if (c[i] > 65280 && c[i] < 65375)
                c[i] = (char) (c[i] - 65248);
        }
        return new String(c);
    }

    /**
     * 拼接给定字符
     *
     * @param str
     * @return
     */
    public static String buildString(Object... str) {
        int size = str.length;
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < size; i++) {
            if(str[i] != null) {
                builder.append(String.valueOf(str[i]));
            }
        }
        return builder.toString();
    }

    /**
     * 复制到剪切板
     *
     * @param context
     * @param strContent
     */
    public static boolean copyToClickboard(Context context, String strContent) {
        try {
            if (TextUtils.isEmpty(strContent)) {
                // Use.showToast(context,"复制内容不能为空");
                return false;
            }
            ClipboardManager cmb = (ClipboardManager) context
                    .getSystemService(Context.CLIPBOARD_SERVICE);
            cmb.setText(strContent);
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 批量查询，是否包含
     *
     * @param str       被查询的字符串 eg： sdfsdfsdf
     * @param searchStr 查询字符串数组 eg： ["sdf","wesd"]
     * @return
     */
    public static boolean contains(String str, String... searchStr) {
        if (str == null || searchStr == null) {
            return false;
        }
        for (String s : searchStr) {
            if (str.contains(s)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 是否包含中文
     * @param str
     * @return
     */
    public static Boolean isContainChinese(String str) {
        Boolean isChinese = false;
        String chinese = "[\u0391-\uFFE5]";
        if (!isEmpty(str)) {
            for (int i = 0; i < str.length(); i++) {
                String temp = str.substring(i, i + 1);
                isChinese = temp.matches(chinese);
            }
        }
        return isChinese;
    }
}
