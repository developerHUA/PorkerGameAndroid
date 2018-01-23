package com.hurenkeji.porkergame.manage;

import com.hurenkeji.porkergame.base.BaseApplication;
import com.lzy.okgo.OkGo;

/**
 *
 */
public class AppManager extends BaseApplication {

    private static AppManager appManager;



    public static AppManager getInstance() {
        return appManager;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        appManager = this;
        initOkGo();
        UserManager.getInstance().init();


    }

    private void initOkGo() {
        OkGo.getInstance().init(this);
    }
}
