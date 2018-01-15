package com.hurenkeji.porkergame.utils;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.hurenkeji.porkergame.manage.AppManager;

/**
 * 设备信息工具类
 */
public class DeviceUtil {

    /**
     * 获取当前应用版本号
     */
    public static String getApplicationV() {

        PackageManager manager = AppManager.getInstance().getPackageManager();
        try {
            PackageInfo packageInfo = manager.getPackageInfo(AppManager.getInstance().getPackageName(), 0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }


}
