package com.yunke.xiaovo.base;

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
}
