package com.yunke.xiaovo.bean;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.yunke.xiaovo.net.HRNetConfig;
import com.yunke.xiaovo.utils.CommonUtil;
import com.yunke.xiaovo.utils.DeviceUtil;

/**
 * 公共请求参数
 */
public class Params {

    /**
     * i=ios,a=android
     */
    public String u = "a";
    /**
     * 当前版本号=2
     */
    public String v = DeviceUtil.getApplicationV();
    public String key;
    public long time = System.currentTimeMillis();
    public Object params;

    public String toJson() {
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        String paramsJson = gson.toJson(this.params);
        this.key = CommonUtil.md5(CommonUtil.md5(paramsJson + this.time + HRNetConfig.SALT));
        return gson.toJson(this);
    }
}
