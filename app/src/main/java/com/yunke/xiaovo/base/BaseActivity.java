package com.yunke.xiaovo.base;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.yunke.xiaovo.manage.AppManager;

import butterknife.ButterKnife;


public abstract class BaseActivity extends AppCompatActivity implements  View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //去掉虚拟按键全屏显示
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        hideBottomUIMenu();
        if (getLayoutId() != 0) {
            setContentView(getLayoutId());
        }

        // 添加当前Activity到栈
        AppManager.getInstance().addActivity(this);
        ButterKnife.bind(this);
        initView();
        initData();
    }


    public void initData() {

    }

    public void initView() {
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            AppManager.getInstance().removeActivity(Class.forName(getClass().getName()));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化布局
     */
    protected int getLayoutId() {
        return 0;
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
