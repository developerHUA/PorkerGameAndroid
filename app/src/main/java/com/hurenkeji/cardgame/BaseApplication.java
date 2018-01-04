package com.hurenkeji.cardgame;

import android.annotation.SuppressLint;
import android.app.Application;


/**
 *
 */
public class BaseApplication extends Application {



    @SuppressLint("StaticFieldLeak")
    private static BaseApplication instance;


    /**
     * 获得当前app运行的AppContext
     *

     */
    public static BaseApplication getInstance() {
        return instance;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

    }

}
