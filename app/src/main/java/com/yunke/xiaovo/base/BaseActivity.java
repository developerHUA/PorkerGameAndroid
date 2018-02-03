package com.yunke.xiaovo.base;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.yunke.xiaovo.R;
import com.yunke.xiaovo.manage.AppManager;
import com.yunke.xiaovo.widget.CommonButton;
import com.yunke.xiaovo.widget.CommonTextView;
import com.yunke.xiaovo.widget.DialogView;

import butterknife.BindView;
import butterknife.ButterKnife;


public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.dialog_progress)
    DialogView progressDialog;
    @BindView(R.id.iv_loading)
    ImageView ivLoading;
    @BindView(R.id.tv_content)
    CommonTextView tvContent;
    @BindView(R.id.ll_progress)
    LinearLayout llProgress;
    @BindView(R.id.tv_message)
    CommonTextView tvMessage;
    @BindView(R.id.ll_confirm_dialog)
    LinearLayout llConfirmDialog;
    @BindView(R.id.btn_dialog_cancel)
    CommonButton btnDialogCancel;
    @BindView(R.id.btn_dialog_confirm)
    CommonButton btnDialogConfirm;
    @BindView(R.id.dv_confirm)
    DialogView dvConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            setHideVirtualKey(getWindow());
        }
        if (getLayoutId() != 0) {
            setContentView(getLayoutId());
        }
        addContentView(View.inflate(this, R.layout.view_progress_dialog, null),
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        addContentView(View.inflate(this, R.layout.view_dialog_confirm, null),
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
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


    protected void showConfirmDialog(String message, String confirmText, String cancelText) {
        showConfirmDialog(message, cancelText, confirmText, false);
    }

    protected void showConfirmDialog(String message, String confirmText) {
        showConfirmDialog(message, "取消", confirmText, false);
    }

    protected void showConfirmDialog(String message, String cancelText, String confirmText, boolean cancelable) {
        tvMessage.setText(message);
        llConfirmDialog.setVisibility(View.VISIBLE);
        llConfirmDialog.setClickable(!cancelable);
        btnDialogConfirm.setText(confirmText);
        btnDialogCancel.setText(cancelText);
        btnDialogCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogCancel();
            }
        });

        btnDialogConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogConfirm();
            }
        });

        dvConfirm.reSize();
    }

    protected void hideConfirmDialog() {
        llConfirmDialog.setVisibility(View.GONE);
        btnDialogCancel.clearAnimation();
        btnDialogConfirm.clearAnimation();
    }


    protected void dialogCancel() {
        hideConfirmDialog();
    }


    protected void dialogConfirm() {
        hideConfirmDialog();
    }


    protected void showProgressDialog(String content, boolean cancelable) {
        tvContent.setText(content);
        llProgress.setVisibility(View.VISIBLE);
        llProgress.setClickable(!cancelable);
        progressDialog.reSize();
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.loading_image_animation);
        animation.setInterpolator(new LinearInterpolator());
        ivLoading.startAnimation(animation);
    }

    protected void hideProgressDialog() {
        ivLoading.clearAnimation();
        llProgress.setVisibility(View.GONE);
    }


    @Override
    public void onBackPressed() {
        backClick();
    }


    private void backClick() {
        if (llProgress.getVisibility() == View.VISIBLE) {
            if (!llProgress.isClickable()) {
                hideProgressDialog();
            }
        } else if (llConfirmDialog.getVisibility() == View.VISIBLE) {
            if (!llConfirmDialog.isClickable()) {
                hideConfirmDialog();
            }
        } else {
            finish();
        }
    }

    protected void showProgressDialog(String content) {
        showProgressDialog(content, false);
    }


    @Override
    protected void onResume() {
        super.onResume();
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

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public static void setHideVirtualKey(final Window window) {
        //保持布局状态
        int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                //布局位于状态栏下方
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                //全屏
                View.SYSTEM_UI_FLAG_FULLSCREEN |
                //隐藏导航栏
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
        if (Build.VERSION.SDK_INT >= 19) {
            uiOptions |= 0x00001000;
        } else {
            uiOptions |= View.SYSTEM_UI_FLAG_LOW_PROFILE;
        }
        window.getDecorView().setSystemUiVisibility(uiOptions);
        window.getDecorView().setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                setHideVirtualKey(window);
            }
        });
    }

}
