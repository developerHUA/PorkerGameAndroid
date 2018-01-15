package com.hurenkeji.porkergame.ui;

import android.view.View;
import android.widget.Button;

import com.hurenkeji.porkergame.R;
import com.hurenkeji.porkergame.base.BaseActivity;
import com.hurenkeji.porkergame.bean.WXLoginParams;
import com.hurenkeji.porkergame.net.HRRequstUtil;
import com.hurenkeji.porkergame.wxapi.WXConstants;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import butterknife.BindView;

import static com.hurenkeji.porkergame.net.HRNetConfig.WX_LOGIN;

/**
 * 登录界面
 */
public class LoginActivity extends BaseActivity {

    @BindView(R.id.btn_wx_login)
    Button btnWxLogin;
    private IWXAPI api;


    @Override
    public void initData() {
        super.initData();
        api = WXAPIFactory.createWXAPI(this, WXConstants.APP_ID);

    }

    @Override
    public void initView() {
        super.initView();

        btnWxLogin.setOnClickListener(this);
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_wx_login:
                wxLogin();
                break;
        }
    }


    private void requestLogin() {

        WXLoginParams wxLoginParams = new WXLoginParams();

        HRRequstUtil.postJson(WX_LOGIN, wxLoginParams, new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {

            }

            @Override
            public void onError(Response<String> response) {
                super.onError(response);
            }
        });


    }




    private void wxLogin() {

        final SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "wechat_sdk_demo_test";
        api.sendReq(req);
    }


}
