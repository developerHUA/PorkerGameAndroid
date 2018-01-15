package com.hurenkeji.porkergame.net;

import com.hurenkeji.porkergame.bean.Params;
import com.hurenkeji.porkergame.utils.LogUtil;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.Callback;

/**
 * 网络请求工具类
 */
public class HRRequstUtil {

    private static final java.lang.String TAG = HRNetConfig.class.getName();

    public static void postJson(String url, Object params, Callback<String> callback) {
        Params baseParams = new Params();
        baseParams.params = params;
        url = String.format(HRNetConfig.URL, url);
        String paramsStr = baseParams.toJson();
        LogUtil.i(TAG, url);
        LogUtil.i(TAG, paramsStr);
        OkGo.<String>post(url).upJson(paramsStr).execute(callback);
    }


    public static void get(String url,Callback<String> callback) {
        OkGo.<String>get(url).execute(callback);
    }

}
