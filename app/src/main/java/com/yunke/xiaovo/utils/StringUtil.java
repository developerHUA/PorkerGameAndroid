package com.yunke.xiaovo.utils;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.yunke.xiaovo.bean.Result;
import com.yunke.xiaovo.bean.RoomResult;

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
    public static String objectToJsonBuilder(Object object) {
        Gson gson = new GsonBuilder()
                .disableHtmlEscaping()
                .create();

        return gson.toJson(object);
    }

}
