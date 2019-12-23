package com.panghu.appcommon.utils;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * PhArrayUtil
 *
 * @desc
 * @autor lijiangping
 * @wechat ljphhj
 * @email lijiangping.zz@gmail.com
 *
 **/
public class PhArrayUtil {

    public static boolean isEmpty(List list){
        return (list == null || list.size() == 0);
    }

    public static <T> boolean isEmpty(T[] array){
        return (array == null || array.length == 0);
    }

    public static <K, V> boolean isEmpty(Map<K, V> map) {
        return (map == null || map.size() == 0);
    }

    /**
     * 数组->List
     *
     * @param array
     * @param <T>
     * @return
     */
    public static <T> List<T> arrayToList(T[] array){
        if (array == null){
            return null;
        }
        List<T> list = Arrays.asList(array);
        return list;
    }


    /**
     * List->数组
     *
     * @param list
     * @return
     */
    public static Object[] listToArray(Collection<?> list){
        if (list == null){
            return null;
        }
        return list.toArray();
    }


    /**
     * array->Set
     *
     * @param array
     * @param <T>
     * @return
     */
    public static <T> Set<T> arrayToSet(T... array) {
        return new HashSet<T>(arrayToList(array));
    }

    /**
     * list->set
     *
     * @param list
     * @param <T>
     * @return
     */
    public static <T> Set<T> listToSet(List<T> list) {
        return new HashSet<T>(list);
    }

    /**
     * set->list
     *
     * @param set
     * @param <T>
     * @return
     */
    public static <T> List<T> setToList(Set<T> set) {
        return new ArrayList<T>(set);
    }

    /**
     * 将数组转换成字符串输出
     *
     * @param split 分隔符
     * @param array
     * @param <T>
     * @return
     */
    public static <T> String traverseArrayToString(String split, T[] array) {
        if (!isEmpty(array)) {
            int len = array.length;
            StringBuilder builder = new StringBuilder(len);
            int i = 0;
            for (T t : array) {
                if (t == null) {
                    continue;
                }
                builder.append(t.toString());
                i++;
                if (i < len) {
                    builder.append(split);
                }
            }
            return builder.toString();
        }
        return null;
    }

    /**
     * 将collection转换成字符串
     *
     * @param split 分隔符
     * @param iterable
     * @return
     */
    public static String traverseListToString(String split, Iterable iterable) {
        return iterable == null ? "" : TextUtils.join(split, iterable);
    }

    /**
     * 将Map转换成字符串
     *
     * @param map
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K, V> String traverseMapToString(String split, Map<K, V> map) {
        if (!isEmpty(map)) {
            int len = map.size();
            StringBuilder builder = new StringBuilder(len);
            int i = 0;
            for (Map.Entry<K, V> entry : map.entrySet()) {
                if (entry == null) {
                    continue;
                }
                builder.append(entry.getKey().toString() + ":" + entry.getValue().toString());
                i++;
                if (i < len) {
                    builder.append(split);
                }
            }
            return builder.toString();
        }
        return null;
    }

}
