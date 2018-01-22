package com.hurenkeji.porkergame.utils;


import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.lang.reflect.Type;

public class StringUtil {


    public static <T> T jsonToObject(String json, Class<T> clazz) {

        Gson gson = new Gson();
        try {
            return gson.fromJson(json, clazz);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static <T> T jsonToObject(String json, Type type) {

        Gson gson = new Gson();
        try {
            return gson.fromJson(json, type);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String objectToJson(Object object) {
        Gson gson = new Gson();
        return gson.toJson(object);
    }


}
