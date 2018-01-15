package com.hurenkeji.porkergame.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.hurenkeji.porkergame.bean.Constants;
import com.hurenkeji.porkergame.bean.WXLoginParams;
import com.hurenkeji.porkergame.net.HRRequstUtil;
import com.hurenkeji.porkergame.utils.ToastUtils;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

/**
 * 作者：滑尧伟
 * 邮箱：hyw88866@163.com
 * 公司:北京拓维信息（高能壹佰）股份制有限公司
 * 公司网址: https://www.yunke.com
 * 创建于 2018/1/15 16:44
 */
public class WXEntryActivity extends Activity implements IWXAPIEventHandler {

    private IWXAPI api;
    private String code;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = WXAPIFactory.createWXAPI(this, WXConstants.APP_ID);

    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    //发送到微信请求的响应结果
//    @Override
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

                break;
            default://发送返回

                break;
        }

    }


    private void getUserInfo(String openId, String accessToken) {
        String url = "https://api.weixin.qq.com/sns/userinfo?access_token=" + accessToken + "&openid=" + openId;
        HRRequstUtil.get(url, new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                Intent intent = new Intent();
                intent.putExtra(Constants.WX_LOGIN_RESULT_KEY, response.body());
                setResult(RESULT_OK, intent);
            }
        });

    }

    private void getAccessToken() {
        String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + WXConstants.APP_ID +
                "&secret=" + WXConstants.APP_KEY + "&code=" + code + "&grant_type=authorization_code";
        HRRequstUtil.get(url, new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                WXLoginParams wxLoginParams = new Gson().fromJson(response.body(), WXLoginParams.class);
                getUserInfo(wxLoginParams.getOpenId(), wxLoginParams.getAccess_token());
            }

        });
    }
}
