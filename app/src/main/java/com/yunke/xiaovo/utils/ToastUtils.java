package com.yunke.xiaovo.utils;

import android.view.View;
import android.widget.Toast;

import com.yunke.xiaovo.R;
import com.yunke.xiaovo.manage.AppManager;
import com.yunke.xiaovo.widget.CommonTextView;

/**
 * 作者：滑尧伟
 * 邮箱：hyw88866@163.com
 * 公司:北京拓维信息（高能壹佰）股份制有限公司
 * 公司网址: https://www.yunke.com
 * 创建于 2017/12/29 17:34
 */
public class ToastUtils {





    public static void showToast(String text) {
        View toastView = View.inflate(AppManager.getInstance(), R.layout.view_toast,null);
        CommonTextView tvToastView = toastView.findViewById(R.id.tv_toast_message);
        tvToastView.setText(text);
        Toast toast = new Toast(AppManager.getInstance());
        toast.setDuration(Toast.LENGTH_SHORT);
//        toast.setGravity(Gravity.BOTTOM,20,20);
        toast.setView(toastView);
        toast.show();
    }

}
