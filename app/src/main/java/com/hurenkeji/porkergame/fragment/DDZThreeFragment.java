package com.hurenkeji.porkergame.fragment;

import android.view.View;
import android.widget.ImageView;

import com.hurenkeji.porkergame.R;
import com.hurenkeji.porkergame.base.BaseFragment;
import com.hurenkeji.porkergame.bean.DDZPorker;
import com.hurenkeji.porkergame.bean.SocketBean;
import com.hurenkeji.porkergame.bean.User;
import com.hurenkeji.porkergame.utils.ToastUtils;

import java.util.ArrayList;

import butterknife.BindView;


public class DDZThreeFragment extends BaseFragment implements DDZSocketNotify {
    @BindView(R.id.iv_left_user)
    ImageView ivLeftUser;
    @BindView(R.id.iv_right_user)
    ImageView ivRightUser;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_three_ddz;
    }


    @Override
    protected void initView(View view) {
        super.initView(view);
    }


    @Override
    protected void initData() {
        super.initData();
    }

    @Override
    public void processReady(int userId) {

    }

    @Override
    public void processCancelReady(int userId) {

    }

    @Override
    public void processNoPlay(int userId) {

    }

    @Override
    public void processSendPoker() {

    }

    @Override
    public void processJoin(User user) {

    }

    @Override
    public void processExit(int userId) {

    }

    @Override
    public void processPlayPorker(SocketBean<ArrayList<DDZPorker>> socketBean) {

    }

    @Override
    public void processSurplus(int userId, int surplus) {

    }

    @Override
    public void processLandlord(int userId) {

    }
}
