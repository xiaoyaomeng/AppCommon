package com.panghu.appcommon.utils;

import java.lang.reflect.Constructor;
import java.util.Collection;
import java.util.Date;

/**
 * PhClassTypeUtil
 *
 * @desc 类的类型判断类
 * @autor lijiangping
 * @wechat ljphhj
 * @email lijiangping.zz@gmail.com
 *
 **/
public class PhClassTypeUtil {

	/**
	 * 判断类是否是基础数据类型
	 * 目前支持11种
	 *
	 * @param clazz
	 * @return
	 */
	public static boolean isBaseDataType(Class<?> clazz) {
		return clazz.isPrimitive() || clazz.equals(String.class) || clazz.equals(Boolean.class)
				|| clazz.equals(Integer.class) || clazz.equals(Long.class) || clazz.equals(Float.class)
				|| clazz.equals(Double.class) || clazz.equals(Byte.class) || clazz.equals(Character.class)
				|| clazz.equals(Short.class) || clazz.equals(Date.class) || clazz.equals(byte[].class)
				|| clazz.equals(Byte[].class);
	}

    /**
     * 是否是集合类型
     * @param claxx
     * @return
     */
    public static boolean isCollection(Class claxx) {
        return Collection.class.isAssignableFrom(claxx);
    }

    /**
     * 是否是数组类型
     * @param claxx
     * @return
     */
    public static boolean isArray(Class claxx) {
        return claxx.isArray();
    }

}
