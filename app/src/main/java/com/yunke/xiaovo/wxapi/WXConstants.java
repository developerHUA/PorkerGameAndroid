package com.yunke.xiaovo.wxapi;

import android.app.Activity;

import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

/**
 * 微信配置
 */
public class WXConstants {

    public static final String APP_ID = "wx92d4da0a6b7c8b47";
    public static final String APP_KEY = "7dd0b412758935ce9798d6812cdd27c3";

    public static boolean isInstallWX(Activity activity) {
        IWXAPI api = WXAPIFactory.createWXAPI(activity, WXConstants.APP_ID, true);
        return api.isWXAppInstalled();
    }
}
