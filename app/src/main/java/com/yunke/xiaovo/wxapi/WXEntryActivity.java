package com.yunke.xiaovo.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yunke.xiaovo.bean.Result;
import com.yunke.xiaovo.bean.User;
import com.yunke.xiaovo.bean.UserResult;
import com.yunke.xiaovo.manage.AppManager;
import com.yunke.xiaovo.manage.UserManager;
import com.yunke.xiaovo.net.HRNetConfig;
import com.yunke.xiaovo.net.HRRequestUtil;
import com.yunke.xiaovo.ui.LoginActivity;
import com.yunke.xiaovo.ui.RoomActivity;
import com.yunke.xiaovo.utils.LogUtil;
import com.yunke.xiaovo.utils.StringUtil;
import com.yunke.xiaovo.utils.ToastUtils;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.lang.reflect.Type;

/**
 * 作者：滑尧伟
 * 邮箱：hyw88866@163.com
 * 公司:北京拓维信息（高能壹佰）股份制有限公司
 * 公司网址: https://www.yunke.com
 * 创建于 2018/1/15 16:44
 */
public class WXEntryActivity extends Activity implements IWXAPIEventHandler {

    private static final java.lang.String TAG = WXEntryActivity.class.getName();
    private IWXAPI api;
    private String code;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //去掉虚拟按键全屏显示
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        hideBottomUIMenu();
        api = WXAPIFactory.createWXAPI(this, WXConstants.APP_ID);
        api.handleIntent(getIntent(), this);
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    //发送到微信请求的响应结果
    @Override
    public void onResp(BaseResp resp) {
        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK: //发送成功
                switch (resp.getType()) {
                    case ConstantsAPI.COMMAND_SENDAUTH:
                        //登录回调,处理登录成功的逻辑
                        code = ((SendAuth.Resp) resp).code; //即为所需的code
                        getAccessToken();
                        break;
                    case ConstantsAPI.COMMAND_SENDMESSAGE_TO_WX:
                        //分享回调,处理分享成功后的逻辑
                        ToastUtils.showToast("分享成功");
                        finish();
                        break;
                    default:
                        break;
                }


                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL: //发送取消
                switch (resp.getType()) {
                    case ConstantsAPI.COMMAND_SENDAUTH:
                        ToastUtils.showToast("登录取消");
                        break;
                    case ConstantsAPI.COMMAND_SENDMESSAGE_TO_WX:
                        ToastUtils.showToast("分享取消");
                        break;
                }
                finish();
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED: //发送被拒绝
                finish();
                break;
            default://发送返回

                break;
        }

    }


    private void getUserInfo(String openId, String accessToken) {
        String url = "https://api.weixin.qq.com/sns/userinfo?access_token=" + accessToken + "&openid=" + openId;
        HRRequestUtil.get(url, new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                requestWXLogin(StringUtil.jsonToObject(response.body(), User.class));
            }

            @Override
            public void onError(Response<String> response) {
                super.onError(response);
                ToastUtils.showToast("获取微信用户信息失败");
                finish();

            }
        });

    }

    private void requestWXLogin(User user) {

        HRRequestUtil.postJson(HRNetConfig.WX_LOGIN, user, new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                LogUtil.i(TAG, "response" + response.body());
                UserResult userResult = StringUtil.jsonToObject(response.body(), UserResult.class);
                if (userResult != null && userResult.result != null) {
                    UserManager.getInstance().upDateUser(userResult.result);
                    AppManager.getInstance().finishActivity(LoginActivity.class);
                    finish();
                    startActivity(new Intent(WXEntryActivity.this, RoomActivity.class));
                } else {
                    ToastUtils.showToast("登录失败");
                    finish();
                }
            }

            @Override
            public void onError(Response<String> response) {
                super.onError(response);
                ToastUtils.showToast("登录失败");
                finish();

            }


        });

    }

    private void getAccessToken() {
        String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + WXConstants.APP_ID +
                "&secret=" + WXConstants.APP_KEY + "&code=" + code + "&grant_type=authorization_code";
        HRRequestUtil.get(url, new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                User wxLoginParams = new Gson().fromJson(response.body(), User.class);
                getUserInfo(wxLoginParams.getOpenid(), wxLoginParams.getAccess_token());
            }

            @Override
            public void onError(Response<String> response) {
                super.onError(response);
                ToastUtils.showToast("获取微信信息失败");
                finish();

            }
        });
    }


    /**
     * 隐藏虚拟按键，并且全屏
     */
    protected void hideBottomUIMenu() {
        //隐藏虚拟按键，并且全屏
        if (Build.VERSION.SDK_INT > 14 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }
}
