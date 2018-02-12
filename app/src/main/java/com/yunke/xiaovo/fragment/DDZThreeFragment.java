package com.yunke.xiaovo.fragment;

import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.yunke.xiaovo.R;
import com.yunke.xiaovo.base.BaseFragment;
import com.yunke.xiaovo.bean.DDZPorker;
import com.yunke.xiaovo.bean.SocketBean;
import com.yunke.xiaovo.bean.User;
import com.yunke.xiaovo.ui.DouDiZhuGameActivity;
import com.yunke.xiaovo.widget.CommonTextView;
import com.yunke.xiaovo.widget.PorkerListView;

import java.util.ArrayList;

import butterknife.BindView;


public class DDZThreeFragment extends BaseFragment implements DDZSocketNotify {
    @BindView(R.id.iv_left_user)
    ImageView ivLeftUser;
    @BindView(R.id.iv_right_user)
    ImageView ivRightUser;
    @BindView(R.id.pv_left_porker)
    PorkerListView leftPlayPorker;
    @BindView(R.id.pv_right_porker)
    PorkerListView rightPlayPorker;
    @BindView(R.id.iv_left_ready)
    ImageView ivLeftReady;
    @BindView(R.id.iv_right_ready)
    ImageView ivRightReady;
    @BindView(R.id.iv_left_no_play)
    ImageView ivLeftNoPlay;
    @BindView(R.id.iv_right_no_play)
    ImageView ivRightNoPlay;
    @BindView(R.id.tv_left_nick_name)
    CommonTextView tvLeftNickname;
    @BindView(R.id.tv_right_nick_name)
    CommonTextView tvRightNickname;
    @BindView(R.id.iv_left_is_landlord)
    ImageView ivLeftIsLandlord;
    @BindView(R.id.iv_right_is_landlord)
    ImageView ivRightIsLandlord;
    @BindView(R.id.iv_left_count_down)
    ImageView ivLeftCountDown;
    @BindView(R.id.iv_right_count_down)
    ImageView ivRightCountDown;
    private User leftUser;
    private User rightUser;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_three_ddz;
    }


    @Override
    protected void initView(View view) {
        super.initView(view);
        leftPlayPorker.setGravity(PorkerListView.LEFT);
        rightPlayPorker.setGravity(PorkerListView.RIGHT);
    }


    private void updateLeftUI() {
        if (leftUser != null) {
            Picasso.with(getActivity()).load(leftUser.getHeadimgurl()).into(ivLeftUser);
            tvLeftNickname.setText(leftUser.getNickname());
        } else {
            Picasso.with(getActivity()).load(R.drawable.room_user_default_head).into(ivLeftUser);
            tvRightNickname.setText("");
        }
    }

    private void updateRightUI() {
        if (rightUser != null) {
            Picasso.with(getActivity()).load(rightUser.getHeadimgurl()).into(ivRightUser);
            tvRightNickname.setText(rightUser.getNickname());
        } else {
            Picasso.with(getActivity()).load(R.drawable.room_user_default_head).into(ivRightUser);
            tvRightNickname.setText("");
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


    private void notifyLeftUpdateCountDownUI() {
        if(leftUser != null) {
            ivRightCountDown.setVisibility(View.GONE);
            processCountDown(leftUser.getUserId());
        }
    }

    @Override
    public void processReady(int userId) {
        if (rightUser != null && rightUser.getUserId() == userId) {
            ivRightReady.setVisibility(View.VISIBLE);
        } else if (leftUser != null && leftUser.getUserId() == userId) {
            ivLeftReady.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void processCancelReady(int userId) {
        if (rightUser != null && rightUser.getUserId() == userId) {
            ivRightReady.setVisibility(View.GONE);
        } else if (leftUser != null && leftUser.getUserId() == userId) {
            ivLeftReady.setVisibility(View.GONE);
        }
    }

    @Override
    public void processNoPlay(int userId) {
        if (rightUser != null && rightUser.getUserId() == userId) {
            ivRightNoPlay.setVisibility(View.VISIBLE);
            ivRightNoPlay.setImageResource(R.drawable.game_no_play);
            rightPlayPorker.clear();
            notifyLeftUpdateCountDownUI();
        } else if (leftUser != null && leftUser.getUserId() == userId) {
            ivLeftNoPlay.setVisibility(View.VISIBLE);
            ivLeftNoPlay.setImageResource(R.drawable.game_no_play);
            ivLeftCountDown.setVisibility(View.GONE);
            leftPlayPorker.clear();
        }
    }

    @Override
    public void processSendPoker() {
        ivLeftReady.setVisibility(View.GONE);
        ivRightReady.setVisibility(View.GONE);
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
            ivRightNoPlay.setVisibility(View.GONE);
            notifyLeftUpdateCountDownUI();
        } else if (leftUser != null && leftUser.getUserId() == socketBean.uid) {
            leftPlayPorker.upDatePorker(socketBean.params);
            ivLeftNoPlay.setVisibility(View.GONE);
        }
    }

    @Override
    public void processSurplus(int userId, int surplus) {

    }

    @Override
    public void processLandlord(int userId) {
        if (rightUser != null && rightUser.getUserId() == userId) {
            ivRightIsLandlord.setVisibility(View.VISIBLE);
        } else if (leftUser != null && leftUser.getUserId() == userId) {
            ivLeftIsLandlord.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void processGameOver() {
        ivRightIsLandlord.setVisibility(View.GONE);
        ivLeftIsLandlord.setVisibility(View.GONE);
        ivLeftReady.setVisibility(View.GONE);
        ivRightReady.setVisibility(View.GONE);
        ivLeftNoPlay.setVisibility(View.GONE);
        ivRightNoPlay.setVisibility(View.GONE);
        ivRightCountDown.setVisibility(View.GONE);
        ivLeftCountDown.setVisibility(View.GONE);
    }

    @Override
    public void processNoLandlord(int userId) {
        if (rightUser != null && rightUser.getUserId() == userId) {
            ivRightNoPlay.setImageResource(R.drawable.no_landlord);
            ivRightCountDown.setVisibility(View.GONE);
            ivRightNoPlay.setVisibility(View.VISIBLE);
            notifyLeftUpdateCountDownUI();
        } else if (leftUser != null && leftUser.getUserId() == userId) {
            ivLeftNoPlay.setImageResource(R.drawable.no_landlord);
            ivLeftNoPlay.setVisibility(View.VISIBLE);
            ivLeftCountDown.setVisibility(View.GONE);
        }
    }

    @Override
    public void processCountDown(int userId) {
        if (rightUser != null && rightUser.getUserId() == userId) {
            ivRightReady.setVisibility(View.GONE);
            ivRightNoPlay.setVisibility(View.GONE);
            ivRightCountDown.setVisibility(View.VISIBLE);
            rightPlayPorker.clear();
        } else if (leftUser != null && leftUser.getUserId() == userId) {
            ivLeftReady.setVisibility(View.GONE);
            ivLeftNoPlay.setVisibility(View.GONE);
            ivLeftCountDown.setVisibility(View.VISIBLE);
            leftPlayPorker.clear();
        }
    }





}
