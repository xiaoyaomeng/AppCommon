package com.panghu.appcommon.utils;

import java.util.List;

/**
 * PhArrayUtil
 *
 * @desc
 * @autor lijiangping
 * @wechat ljphhj
 * @date 2019年12月19日
 *
 **/
public class PhArrayUtil {

    public static boolean isEmpty(List list){
        return (list == null || list.size() == 0);
    }

    public static boolean isEmpty(Object[] array){
        return (array == null || array.length == 0);
    }
}
