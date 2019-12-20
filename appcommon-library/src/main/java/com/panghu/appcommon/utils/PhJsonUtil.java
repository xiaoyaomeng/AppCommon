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
 *
 **/
public class PhJsonUtil {


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
