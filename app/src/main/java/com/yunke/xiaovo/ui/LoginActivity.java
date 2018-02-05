package com.yunke.xiaovo.ui;

import android.content.Intent;
import android.view.View;

import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.yunke.xiaovo.R;
import com.yunke.xiaovo.base.BaseActivity;
import com.yunke.xiaovo.bean.User;
import com.yunke.xiaovo.bean.UserResult;
import com.yunke.xiaovo.manage.AppManager;
import com.yunke.xiaovo.manage.UserManager;
import com.yunke.xiaovo.net.HRNetConfig;
import com.yunke.xiaovo.net.HRRequestUtil;
import com.yunke.xiaovo.utils.StringUtil;
import com.yunke.xiaovo.utils.ToastUtils;
import com.yunke.xiaovo.widget.CommonButton;
import com.yunke.xiaovo.wxapi.WXConstants;

import butterknife.BindView;

/**
 * 登录界面
 */
public class LoginActivity extends BaseActivity {

    @BindView(R.id.btn_wx_login)
    CommonButton btnWxLogin;
    private IWXAPI api;

    @Override
    public void initData() {
        super.initData();
        api = WXAPIFactory.createWXAPI(this, WXConstants.APP_ID);
//        User user = new User();
//        user.setOpenid("120190sd934");
//        user.setSex(2);
//        user.setUnionid("321312ds3");
//        user.setNickname("司马仲达");
//        user.setHeadimgurl("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1517637195346&di=08758661e3c1ca3c2fabb4caf8df9c7c&imgtype=jpg&src=http%3A%2F%2Fimg3.imgtn.bdimg.com%2Fit%2Fu%3D801756313%2C1418308637%26fm%3D214%26gp%3D0.jpg");
//        UserManager.getInstance().upDateUser(user);
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
                if (UserManager.getInstance().getUser() != null) {
                    showProgressDialog("正在登录...", false);
                    requestWXLogin(UserManager.getInstance().getUser());
                } else {
                    wxLogin();
                }
                break;
        }
    }


    private void requestWXLogin(User user) {

        HRRequestUtil.postJson(HRNetConfig.WX_LOGIN, user, new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                hideProgressDialog();
                UserResult userResult = StringUtil.jsonToObject(response.body(), UserResult.class);
                if (userResult != null && userResult.result != null) {
                    UserManager.getInstance().upDateUser(userResult.result);
                    AppManager.getInstance().finishActivity(LoginActivity.class);
                    finish();
                    startActivity(new Intent(LoginActivity.this, RoomActivity.class));
                } else {
                    ToastUtils.showToast("登录失败");
                }
            }

            @Override
            public void onError(Response<String> response) {
                super.onError(response);
                hideProgressDialog();
                ToastUtils.showToast("登录失败");
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void wxLogin() {

        final SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "wechat_sdk_demo_test";
        api.sendReq(req);
    }


}
