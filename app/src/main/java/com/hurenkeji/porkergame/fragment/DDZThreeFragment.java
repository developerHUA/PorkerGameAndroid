package com.hurenkeji.porkergame.fragment;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hurenkeji.porkergame.R;
import com.hurenkeji.porkergame.base.BaseFragment;
import com.hurenkeji.porkergame.bean.DDZPorker;
import com.hurenkeji.porkergame.bean.SocketBean;
import com.hurenkeji.porkergame.bean.User;
import com.hurenkeji.porkergame.ui.DouDiZhuGameActivity;
import com.hurenkeji.porkergame.utils.ToastUtils;
import com.hurenkeji.porkergame.widget.DDZPorkerView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;


public class DDZThreeFragment extends BaseFragment implements DDZSocketNotify {
    @BindView(R.id.iv_left_user)
    ImageView ivLeftUser;
    @BindView(R.id.iv_right_user)
    ImageView ivRightUser;
    @BindView(R.id.pv_left_porker)
    DDZPorkerView leftPlayPorker;
    @BindView(R.id.pv_right_porker)
    DDZPorkerView rightPlayPorker;
    @BindView(R.id.tv_left_ready)
    TextView tvLeftReady;
    @BindView(R.id.tv_right_ready)
    TextView tvRightReady;
    @BindView(R.id.iv_left_no_play)
    ImageView ivLeftNoPlay;
    @BindView(R.id.iv_right_no_play)
    ImageView ivLRightNoPlay;
    private User leftUser;
    private User rightUser;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_three_ddz;
    }


    @Override
    protected void initView(View view) {
        super.initView(view);
        leftPlayPorker.setGravity(DDZPorkerView.LEFT);
        rightPlayPorker.setGravity(DDZPorkerView.RIGHT);
    }


    private void updateLeftUI() {
        if(rightUser != null) {
            Picasso.with(getActivity()).load(leftUser.getHeadimgurl()).into(ivLeftUser);
        }else {
            Picasso.with(getActivity()).load(R.mipmap.ic_launcher).into(ivLeftUser);
        }
    }

    private void updateRightUI() {
        if (rightUser != null) {
            Picasso.with(getActivity()).load(rightUser.getHeadimgurl()).into(ivRightUser);
        }else {
            Picasso.with(getActivity()).load(R.mipmap.ic_launcher).into(ivRightUser);
        }

    }

    @Override
    protected void initData() {
        super.initData();
        DouDiZhuGameActivity activity = (DouDiZhuGameActivity) getActivity();
        if (activity != null) {
            leftUser = activity.leftUser;
            rightUser = activity.rightUser;
            updateRightUI();
            updateLeftUI();
        }
    }

    @Override
    public void processReady(int userId) {
        if (rightUser != null && rightUser.getUserId() == userId) {
            tvRightReady.setVisibility(View.VISIBLE);
        } else if (leftUser != null && leftUser.getUserId() == userId) {
            tvLeftReady.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void processCancelReady(int userId) {
        if (rightUser != null && rightUser.getUserId() == userId) {
            tvRightReady.setVisibility(View.GONE);
        } else if (leftUser != null && leftUser.getUserId() == userId) {
            tvLeftReady.setVisibility(View.GONE);
        }
    }

    @Override
    public void processNoPlay(int userId) {
        if (rightUser != null && rightUser.getUserId() == userId) {
            ivLRightNoPlay.setVisibility(View.VISIBLE);
        } else if (leftUser != null && leftUser.getUserId() == userId) {
            ivLeftNoPlay.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void processSendPoker() {
        tvLeftReady.setVisibility(View.GONE);
        tvRightReady.setVisibility(View.GONE);
    }

    @Override
    public void processJoin(User user) {
        if (rightUser == null) {
            rightUser = user;
            updateRightUI();
        } else {
            leftUser = user;
            updateLeftUI();
        }
    }

    @Override
    public void processExit(int userId) {
        if (rightUser != null && rightUser.getUserId() == userId) {
            rightUser = null;
            updateRightUI();
        } else if (leftUser != null && leftUser.getUserId() == userId) {
            rightUser = null;
            updateLeftUI();
        }
    }

    @Override
    public void processPlayPorker(SocketBean<ArrayList<DDZPorker>> socketBean) {
        if (rightUser != null && rightUser.getUserId() == socketBean.uid) {
            rightPlayPorker.upDatePorker(socketBean.params);
        } else if (leftUser != null && leftUser.getUserId() == socketBean.uid) {
            leftPlayPorker.upDatePorker(socketBean.params);
        }
    }

    @Override
    public void processSurplus(int userId, int surplus) {

    }

    @Override
    public void processLandlord(int userId) {

    }
}
