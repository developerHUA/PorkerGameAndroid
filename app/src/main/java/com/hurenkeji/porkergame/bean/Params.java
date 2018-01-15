package com.hurenkeji.porkergame.bean;

import com.google.gson.Gson;
import com.hurenkeji.porkergame.net.HRNetConfig;
import com.hurenkeji.porkergame.utils.CommonUtil;
import com.hurenkeji.porkergame.utils.DeviceUtil;

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
        Gson gson = new Gson();
        String paramsJson = gson.toJson(this.params);
        this.key = CommonUtil.md5(CommonUtil.md5(paramsJson + this.time + HRNetConfig.SALT));
        return gson.toJson(this);
    }
}
