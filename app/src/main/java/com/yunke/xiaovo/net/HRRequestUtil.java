package com.yunke.xiaovo.net;

import com.yunke.xiaovo.bean.Params;
import com.yunke.xiaovo.utils.LogUtil;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.Callback;

/**
 * 网络请求工具类
 */
public class HRRequestUtil {


    public static void postJson(String url, Object params, Callback<String> callback) {
        Params baseParams = new Params();
        baseParams.params = params;
        url = String.format(HRNetConfig.URL, url);
        String paramsStr = baseParams.toJson();
        OkGo.<String>post(url).upJson(paramsStr).execute(callback);
    }


    public static void get(String url,Callback<String> callback) {
        OkGo.<String>get(url).execute(callback);
    }

}
