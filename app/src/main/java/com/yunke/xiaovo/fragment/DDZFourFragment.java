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


public class DDZFourFragment extends BaseFragment implements DDZSocketNotify {
    @BindView(R.id.iv_left_user)
    ImageView ivLeftUser;
    @BindView(R.id.iv_right_user)
    ImageView ivRightUser;
    @BindView(R.id.iv_top_user)
    ImageView ivTopUser;
    @BindView(R.id.pv_left_porker)
    PorkerListView leftPlayPorker;
    @BindView(R.id.pv_right_porker)
    PorkerListView rightPlayPorker;
    @BindView(R.id.pv_top_porker)
    PorkerListView topPlayPorker;
    @BindView(R.id.iv_left_ready)
    ImageView ivLeftReady;
    @BindView(R.id.iv_right_ready)
    ImageView ivRightReady;
    @BindView(R.id.iv_top_ready)
    ImageView ivTopReady;
    @BindView(R.id.iv_left_no_play)
    ImageView ivLeftNoPlay;
    @BindView(R.id.iv_right_no_play)
    ImageView ivRightNoPlay;
    @BindView(R.id.iv_top_no_play)
    ImageView ivTopNoPlay;
    @BindView(R.id.tv_top_nick_name)
    CommonTextView tvTopNickName;
    @BindView(R.id.tv_right_nick_name)
    CommonTextView tvRightNickName;
    @BindView(R.id.tv_left_nick_name)
    CommonTextView tvLeftNickName;
    @BindView(R.id.iv_top_is_landlord)
    ImageView ivTopLandlord;
    @BindView(R.id.iv_left_is_landlord)
    ImageView ivLeftLandlord;
    @BindView(R.id.iv_right_is_landlord)
    ImageView ivRightLandlord;
    @BindView(R.id.iv_right_count_down)
    ImageView ivRightCountDown;
    @BindView(R.id.iv_top_count_down)
    ImageView ivTopCountDown;
    @BindView(R.id.iv_left_count_down)
    ImageView ivLeftCountDown;


    private User leftUser;
    private User rightUser;
    private User topUser;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_four_ddz;
    }


    @Override
    protected void initView(View view) {
        super.initView(view);
        leftPlayPorker.setGravity(PorkerListView.LEFT);
        rightPlayPorker.setGravity(PorkerListView.RIGHT);
        topPlayPorker.setGravity(PorkerListView.CENTER);
    }


    private void updateLeftUI() {
        if (leftUser != null) {
            Picasso.with(getActivity()).load(leftUser.getHeadimgurl()).into(ivLeftUser);
            tvLeftNickName.setText(leftUser.getNickname());
        } else {
            Picasso.with(getActivity()).load(R.drawable.room_user_default_head).into(ivLeftUser);
            tvLeftNickName.setText("");
        }
    }

    private void updateRightUI() {
        if (rightUser != null) {
            Picasso.with(getActivity()).load(rightUser.getHeadimgurl()).into(ivRightUser);
            tvRightNickName.setText(rightUser.getNickname());
        } else {
            Picasso.with(getActivity()).load(R.drawable.room_user_default_head).into(ivRightUser);
            tvRightNickName.setText("");
        }
    }


    private void updateTopUI() {
        if (topUser != null) {
            Picasso.with(getActivity()).load(topUser.getHeadimgurl()).into(ivTopUser);
            tvTopNickName.setText(topUser.getNickname());
        } else {
            Picasso.with(getActivity()).load(R.drawable.room_user_default_head).into(ivTopUser);
            tvTopNickName.setText("");
        }
    }


    @Override
    protected void initData() {
        super.initData();
        DouDiZhuGameActivity activity = (DouDiZhuGameActivity) getActivity();
        if (activity != null) {
            leftUser = activity.leftUser;
            rightUser = activity.rightUser;
            topUser = activity.topUser;
            updateRightUI();
            updateLeftUI();
            updateTopUI();
        }
    }

    @Override
    public void processReady(int userId) {
        if (rightUser != null && rightUser.getUserId() == userId) {
            ivRightReady.setVisibility(View.VISIBLE);
        } else if (topUser != null && topUser.getUserId() == userId) {
            ivTopReady.setVisibility(View.VISIBLE);
        } else if (leftUser != null && leftUser.getUserId() == userId) {
            ivLeftReady.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void processCancelReady(int userId) {
        if (rightUser != null && rightUser.getUserId() == userId) {
            ivRightReady.setVisibility(View.GONE);
        } else if (topUser != null && topUser.getUserId() == userId) {
            ivTopReady.setVisibility(View.GONE);
        } else if (leftUser != null && leftUser.getUserId() == userId) {
            ivLeftReady.setVisibility(View.GONE);
        }
    }

    @Override
    public void processNoPlay(int userId) {
        if (rightUser != null && rightUser.getUserId() == userId) {
            ivRightNoPlay.setImageResource(R.drawable.game_no_play);
            ivRightNoPlay.setVisibility(View.VISIBLE);
            rightPlayPorker.clear();
            notifyTopUpdateCountDownUI();
        } else if (topUser != null && topUser.getUserId() == userId) {
            ivTopNoPlay.setImageResource(R.drawable.game_no_play);
            ivTopNoPlay.setVisibility(View.VISIBLE);
            topPlayPorker.clear();
            notifyLeftUpdateCountDownUI();
        } else if (leftUser != null && leftUser.getUserId() == userId) {
            ivRightNoPlay.setImageResource(R.drawable.game_no_play);
            ivLeftNoPlay.setVisibility(View.VISIBLE);
            leftPlayPorker.clear();
        }
    }

    @Override
    public void processSendPoker() {
        ivLeftReady.setVisibility(View.GONE);
        ivRightReady.setVisibility(View.GONE);
        ivTopReady.setVisibility(View.GONE);
    }

    @Override
    public void processJoin(User user) {
        if (rightUser == null) {
            rightUser = user;
            updateRightUI();
        } else if (topUser == null) {
            topUser = user;
            updateTopUI();
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
        } else if (topUser != null && topUser.getUserId() == userId) {
            topUser = null;
            updateTopUI();
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
            notifyTopUpdateCountDownUI();
        } else if (topUser != null && topUser.getUserId() == socketBean.uid) {
            topPlayPorker.upDatePorker(socketBean.params);
            ivTopNoPlay.setVisibility(View.GONE);
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
            ivRightLandlord.setVisibility(View.VISIBLE);
        }else if(topUser != null && topUser.getUserId() == userId) {
            ivTopLandlord.setVisibility(View.VISIBLE);
        } else if (leftUser != null && leftUser.getUserId() == userId) {
            ivLeftLandlord.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void processGameOver() {
        ivLeftReady.setVisibility(View.GONE);
        ivTopReady.setVisibility(View.GONE);
        ivRightReady.setVisibility(View.GONE);

        ivRightLandlord.setVisibility(View.GONE);
        ivTopLandlord.setVisibility(View.GONE);
        ivLeftLandlord.setVisibility(View.GONE);

        ivLeftNoPlay.setVisibility(View.GONE);
        ivRightNoPlay.setVisibility(View.GONE);
        ivTopNoPlay.setVisibility(View.GONE);
    }

    @Override
    public void processNoLandlord(int userId) {
        if (rightUser != null && rightUser.getUserId() == userId) {
            ivRightNoPlay.setImageResource(R.drawable.no_landlord);
            ivRightNoPlay.setVisibility(View.VISIBLE);
        }else if(topUser != null && topUser.getUserId() == userId) {
            ivTopNoPlay.setImageResource(R.drawable.no_landlord);
            ivTopNoPlay.setVisibility(View.VISIBLE);
        } else if (leftUser != null && leftUser.getUserId() == userId) {
            ivLeftNoPlay.setImageResource(R.drawable.no_landlord);
            ivLeftNoPlay.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void processCountDown(int userId) {
        if (rightUser != null && rightUser.getUserId() == userId) {
            ivRightReady.setVisibility(View.GONE);
            ivRightNoPlay.setVisibility(View.GONE);
            ivRightCountDown.setVisibility(View.VISIBLE);
            rightPlayPorker.clear();
        }else if(topUser != null && topUser.getUserId() == userId) {
            ivTopReady.setVisibility(View.GONE);
            ivTopNoPlay.setVisibility(View.GONE);
            ivTopCountDown.setVisibility(View.VISIBLE);
            topPlayPorker.clear();
        } else if (leftUser != null && leftUser.getUserId() == userId) {
            ivLeftReady.setVisibility(View.GONE);
            ivLeftNoPlay.setVisibility(View.GONE);
            ivLeftCountDown.setVisibility(View.VISIBLE);
            leftPlayPorker.clear();
        }
    }



    private void notifyLeftUpdateCountDownUI() {
        if(leftUser != null) {
            ivRightCountDown.setVisibility(View.GONE);
            processCountDown(leftUser.getUserId());
        }
    }

    private void notifyTopUpdateCountDownUI() {
        if(topUser != null) {
            ivRightCountDown.setVisibility(View.GONE);
            processCountDown(topUser.getUserId());
        }
    }
}
