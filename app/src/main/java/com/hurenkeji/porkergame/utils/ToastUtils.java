package com.hurenkeji.porkergame.utils;

import android.widget.Toast;

import com.hurenkeji.porkergame.manage.AppManager;

/**
 * 作者：滑尧伟
 * 邮箱：hyw88866@163.com
 * 公司:北京拓维信息（高能壹佰）股份制有限公司
 * 公司网址: https://www.yunke.com
 * 创建于 2017/12/29 17:34
 */
public class ToastUtils {

    public static void showToast(String text) {

        Toast.makeText(AppManager.getInstance(), text, Toast.LENGTH_SHORT).show();
    }

}
