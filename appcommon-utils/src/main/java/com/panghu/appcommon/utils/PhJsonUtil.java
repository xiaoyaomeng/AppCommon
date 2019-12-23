package com.panghu.appcommon.utils;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * PhJsonUtil
 *
 * @desc json转换类
 * @autor lijiangping
 * @wechat ljphhj
 * @email lijiangping.zz@gmail.com
 *
 **/
public class PhJsonUtil {


    /**
     * 转换：Object -> json
     *
     * @param obj
     * @return
     */
    public static String toJson(Object obj) {
        Gson gson = new Gson();
        return gson.toJson(obj);
    }

    /**
     * 转换：json -> Object
     *
     * @param json
     * @param clazz
     * @return
     */
    public static <T> T jsonToObject(String json, Class<T> clazz) {
        Gson gson = new Gson();
        return gson.fromJson(json, clazz);
    }

    /**
     *
     * 转换：json -> List<?>
     *
     * @param json
     * @return
     */
    public static <T> List<T> jsonToList(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, new TypeToken<List<T>>() {
        }.getType());
    }

    /**
     * 转换：json -> ArrayList<?>
     *
     * @param json
     * @return
     */
    public static <T> ArrayList<T> jsonToArrayList(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, new TypeToken<ArrayList<T>>() {
        }.getType());
    }

    /**
     * 转换：json -> Map<?, ?>
     *
     * @param json
     * @return
     */
    public static <K, V> Map<K, V> jsonToMap(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, new TypeToken<Map<K, V>>() {
        }.getType());
    }

    /**
     * 转换：json -> Generic<T>
     *
     * @param json
     * @param <T>
     * @return
     */
    public static <T> T jsonToGeneric(String json, TypeToken<T> token) {
        Gson gson = new Gson();
        return gson.fromJson(json, token.getType());
    }

    public static <T> T fromJson(String json, Class<T> clazz) {
        Gson gson = new Gson();
        T t = gson.fromJson(json, clazz);
        return t;
    }

    public static <T> Map<String, T> fromJson2Map(String json) {
        Gson gson = new Gson();
        Map<String, T> map = gson.fromJson(json,
                new TypeToken<Map<String, T>>() {
                }.getType());
        return map;
    }

    public static Map<String, JsonElement> fromJson2MapJson(String json) {
        Gson gson = new Gson();
        Map<String, JsonElement> map = gson.fromJson(json,
                new TypeToken<Map<String, JsonElement>>() {
                }.getType());
        return map;
    }

    public static <T> ArrayList<T> fromJson2List(String json, Class<T> clazz) {
        ArrayList<T> resultList = new ArrayList<T>();
        if (PhStringUtil.isNull(json))
            return resultList;
        try {
            Gson gson = new Gson();
            ArrayList<JsonObject> list = gson.fromJson(json, new TypeToken<List<JsonObject>>() {

            }.getType());
            for (JsonObject t : list) {
                resultList.add(new Gson().fromJson(t, clazz));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return resultList;
    }

    public static <T> ArrayList<T> fromJson2List(JsonElement json, Class<T> clazz) {
        Gson gson = new Gson();
        ArrayList<JsonObject> list = null;
        list = gson.fromJson(json, new TypeToken<List<JsonObject>>() {
        }.getType());
        ArrayList<T> resultList = new ArrayList<T>();
        for (JsonObject t : list) {
            resultList.add(new Gson().fromJson(t, clazz));
        }
        return resultList;
    }

}
