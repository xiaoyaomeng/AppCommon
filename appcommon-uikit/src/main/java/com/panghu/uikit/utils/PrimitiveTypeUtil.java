package com.panghu.uikit.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * The basic type util.
 * Created by panghu on 6/26/16.
 */
public class PrimitiveTypeUtil {
    public static ArrayList<Long> convertLongArray(long[] array) {
        if (array == null || array.length <= 0) {
            return null;
        }
        ArrayList<Long> list = new ArrayList<>(array.length);
        for (long element : array) {
            list.add(element);
        }
        return list;
    }

    public static long[] convertLongListToArray(final List<Long> list) {
        long[] array = new long[list.size()];
        for (int i = 0; i < list.size(); i++) {
            array[i] = list.get(i);
        }
        return array;
    }

    public static boolean isNullOrEmpty(final Collection<?> c) {
        return c == null || c.isEmpty();
    }

    public static boolean isSame(final List<?> list1, final List<?> list2) {

        if ((list1 == null && list2 != null) || (list1 != null && list2 == null)) {
            return false;
        }

        if ((list1 == null && list2 == null) || (list1.isEmpty() && list2.isEmpty())) {
            return true;
        }

        if (list1.size() != list2.size()) {
            return false;
        }

        List<?> temp = new ArrayList<>(list2);
        temp.removeAll(list1);
        if (temp.isEmpty()) {
            return true;
        }
        return false;
    }
}
