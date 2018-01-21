package com.hurenkeji.porkergame.utils;


import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

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


}
