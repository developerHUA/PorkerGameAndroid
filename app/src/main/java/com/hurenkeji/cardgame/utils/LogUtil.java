package com.hurenkeji.cardgame.utils;

import android.util.Log;

/**
 *
 */
public class LogUtil {


    public static void i(String tag, String info) {
        Log.i(tag, ">>>>>>>>>> " + info);
    }

    public static void i(String info) {
        Log.i(LogUtil.class.getName(), ">>>>>>>>>> " + info);
    }
}
