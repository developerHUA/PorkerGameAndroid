package com.hurenkeji.porkergame.wxapi;

import android.app.Activity;

import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

/**
 * 微信配置
 */
public class WXConstants {

    public static final String APP_ID = "wx6892c4434a905e28";
    public static final String APP_KEY = "wx6892c4434a905e28";

    public static boolean isInstallWX(Activity activity) {
        IWXAPI api = WXAPIFactory.createWXAPI(activity, WXConstants.APP_ID, true);
        return api.isWXAppInstalled();
    }
}
