package com.hurenkeji.porkergame.ui;

import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.gson.reflect.TypeToken;
import com.hurenkeji.porkergame.R;
import com.hurenkeji.porkergame.base.BaseActivity;
import com.hurenkeji.porkergame.bean.DDZPorker;
import com.hurenkeji.porkergame.bean.IntentConstants;
import com.hurenkeji.porkergame.bean.RoomResult;
import com.hurenkeji.porkergame.bean.SocketBean;
import com.hurenkeji.porkergame.bean.User;
import com.hurenkeji.porkergame.fragment.DDZSocketNotify;
import com.hurenkeji.porkergame.fragment.DDZThreeFragment;
import com.hurenkeji.porkergame.manage.PorkerGameWebSocketManager;
import com.hurenkeji.porkergame.manage.UserManager;
import com.hurenkeji.porkergame.utils.StringUtil;
import com.hurenkeji.porkergame.utils.ToastUtils;
import com.hurenkeji.porkergame.widget.DDZPorkerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 *
 */
public class DouDiZhuGameActivity extends BaseActivity {

    private static final java.lang.String TAG = DouDiZhuGameActivity.class.getName();


    @BindView(R.id.pv_porker_view)
    DDZPorkerView porkerView;
    @BindView(R.id.btn_out_porker)
    Button btnOutPorker;
    @BindView(R.id.pv_play_view)
    DDZPorkerView pvPlayView; // 用户出牌view
    @BindView(R.id.ll_buttons)
    LinearLayout llButtons;
    @BindView(R.id.btn_no_play)
    Button btnNoPlay;
    @BindView(R.id.btn_ready)
    Button btnReady;
    @BindView(R.id.btn_landlord)
    Button btnLandlord;

    private int userId;
    private String token;
    private PorkerGameWebSocketManager mSocketManager = PorkerGameWebSocketManager.getInstance();
    private RoomResult.Result room;
    private DDZSocketNotify fSocketNotify; // 通知fragment
    public User leftUser; // 左边玩家
    public User topUser; // 上边玩家
    public User rightUser;// 右边玩家
    private List<DDZPorker> currentPorker = new ArrayList<>();

    @Override
    public void initView() {
        btnOutPorker.setOnClickListener(this);
        btnNoPlay.setOnClickListener(this);
        btnLandlord.setOnClickListener(this);
        btnNoPlay.setOnClickListener(this);
        porkerView.isClick(true);
        pvPlayView.isClick(false);
    }


    @Override
    public void initData() {
        userId = UserManager.getInstance().getUserId();
        token = UserManager.getInstance().getToken();
        room = (RoomResult.Result) getIntent().getSerializableExtra(IntentConstants.ROOM_KEY);
        mSocketManager.init(room.roomNumber, token, userId, new NotifyHandler());
        if (room.playType == RoomResult.D_D_Z_THREE_TYPE) {
            initThreeDDZFragment();
        } else if (room.playType == RoomResult.D_D_Z_FOUR_TYPE) {

        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_out_porker:
                outPorker();
                break;
            case R.id.btn_ready:
                ready();
                break;
            case R.id.btn_no_play:
                noPlay();
                break;
            case R.id.btn_landlord:
                landlord();
                break;

        }
    }

    /**
     * 出牌
     */
    private void outPorker() {
        btnOutPorker.setEnabled(false);
        List<Integer> indexList = porkerView.getClickIndex();
        SocketBean<List<DDZPorker>> socketBean = new SocketBean<>();
        List<DDZPorker> outPorker = new ArrayList<>();
        for (int i = 0; i < indexList.size(); i++) {
            outPorker.add(currentPorker.get(indexList.get(i)));
        }
        socketBean.uid = userId;
        socketBean.params = outPorker;
        socketBean.type = PorkerGameWebSocketManager.PLAY_PORKER;
        mSocketManager.sendText(StringUtil.objectToJson(socketBean));
    }

    /**
     * 叫地主
     */
    private void landlord() {
        btnLandlord.setEnabled(false);
        String message = SocketBean.messageFromType(userId, PorkerGameWebSocketManager.PLAY_PORKER);
        mSocketManager.sendText(message);
    }

    /**
     * 不出
     */
    private void noPlay() {
        btnNoPlay.setEnabled(false);
        String message = SocketBean.messageFromType(userId, PorkerGameWebSocketManager.NO_PLAY);
        mSocketManager.sendText(message);
    }

    /**
     * 准备
     */
    private void ready() {
        btnReady.setEnabled(false);
        String message = SocketBean.messageFromType(userId, PorkerGameWebSocketManager.READY);
        mSocketManager.sendText(message);

    }


    private void initThreeDDZFragment() {
        FragmentManager fm = getSupportFragmentManager();
        DDZThreeFragment ddzThreeFragment = new DDZThreeFragment();
        fSocketNotify = ddzThreeFragment;
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.fl_fragment, ddzThreeFragment);
        fragmentTransaction.commit();
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_ddz_game;
    }


    private class NotifyHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String json = (String) msg.obj;
            JSONObject jsonObject = null;
            int userId = 0;
            try {
                jsonObject = new JSONObject(json);
                userId = jsonObject.getInt(PorkerGameWebSocketManager.USER_ID_KEY);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (jsonObject == null) {
                return;
            }

            switch (msg.what) {
                case PorkerGameWebSocketManager.READY: // 准备
                    processReady(userId);
                    break;
                case PorkerGameWebSocketManager.PLAY_PORKER: // 出牌
                    processPlayPorker(json);
                    break;
                case PorkerGameWebSocketManager.JOIN_ROOM: // 有人加入房间
                    processJoin(json);
                    break;
                case PorkerGameWebSocketManager.EXIT_ROOM: // 有人退出房间
                    processExit(userId);
                    break;
                case PorkerGameWebSocketManager.LANDLORD: // 叫地主
                    processLandlord(userId);
                    break;
                case PorkerGameWebSocketManager.SURPLUS_ONE: // 剩余一张牌
                    processSurplus(userId, PorkerGameWebSocketManager.SURPLUS_ONE);
                    break;
                case PorkerGameWebSocketManager.SURPLUS_TWO: // 剩余两张牌
                    processSurplus(userId, PorkerGameWebSocketManager.SURPLUS_TWO);
                    break;
                case PorkerGameWebSocketManager.NO_PLAY: // 不出牌
                    processNoPlay(userId);
                    break;
                case PorkerGameWebSocketManager.CANCEL_READY: // 取消准备
                    processCancelReady(userId);
                    break;
                case PorkerGameWebSocketManager.DEAL_PORKER: // 发牌
                    processSendPoker(json);
                    break;
                case PorkerGameWebSocketManager.UNKNOWN_PORKER: // 牌型不正确
                    processUnknownType();
                    break;
            }
        }
    }

    /**
     * 当前出的牌型不正确
     */
    private void processUnknownType() {
        ToastUtils.showToast("牌型不正确");
    }


    /**
     * 处理用户取消准备
     */
    private void processCancelReady(int userId) {
        if (userId == this.userId) {

        } else {
            fSocketNotify.processCancelReady(userId);
        }
    }


    /**
     * 处理用户准备
     */
    private void processReady(int userId) {
        if (userId == this.userId) {

        } else {
            fSocketNotify.processReady(userId);
        }
    }


    /**
     * 处理发牌
     */

    private void processSendPoker(String message) {
        Type type = new TypeToken<SocketBean<ArrayList<DDZPorker>>>() {
        }.getType();
        SocketBean<ArrayList<DDZPorker>> socketBean = StringUtil.jsonToObject(message, type);
        if (socketBean != null) {
            currentPorker = socketBean.params;
            porkerView.upDatePorker(currentPorker);
            fSocketNotify.processSendPoker();
        }
    }


    /**
     * 处理用户出牌
     */
    private void processPlayPorker(String message) {
        Type type = new TypeToken<SocketBean<ArrayList<DDZPorker>>>() {
        }.getType();
        SocketBean<ArrayList<DDZPorker>> socketBean = StringUtil.jsonToObject(message, type);
        if (socketBean != null) {
            if (socketBean.uid == this.userId) {
                pvPlayView.setVisibility(View.VISIBLE);
                llButtons.setVisibility(View.GONE);
                btnOutPorker.setEnabled(true);
                pvPlayView.upDatePorker(socketBean.params);
            } else {
                if (userId == leftUser.getUserId()) {
                    llButtons.setVisibility(View.VISIBLE);
                }
                fSocketNotify.processPlayPorker(socketBean);
            }
        }

    }

    /**
     * 处理用户不出牌
     */
    private void processNoPlay(int userId) {

        if (userId == this.userId) {
            // 当前用户不出牌
            llButtons.setVisibility(View.GONE);
        } else {
            if (leftUser.getUserId() == userId) {
                // 该当前用户出牌
                llButtons.setVisibility(View.VISIBLE);
            }
            fSocketNotify.processNoPlay(userId);
        }

    }


    /**
     * 处理用户加入房间
     */
    private void processJoin(String message) {
        Type type = new TypeToken<SocketBean<User>>() {
        }.getType();

        SocketBean<User> socketBean = StringUtil.jsonToObject(message, type);
        if (socketBean != null) {
            fSocketNotify.processJoin(socketBean.params);
        }
    }

    /**
     * 处理用户退出房间
     */
    private void processExit(int userId) {
        if (userId == this.userId) {

        } else {
            fSocketNotify.processExit(userId);
        }
    }

    /**
     * 处理用户叫地主
     */
    private void processLandlord(int userId) {
        if (userId == this.userId) {
            llButtons.setVisibility(View.GONE);
        } else {
            fSocketNotify.processLandlord(userId);
        }


    }

    /**
     * 处理用户剩牌
     *
     * @param userId
     */
    private void processSurplus(int userId, int surplus) {
        if (userId == this.userId) {

            ToastUtils.showToast("我就剩两张牌了，注意点");

        } else {
            ToastUtils.showToast("别人剩两张牌了，注意点");
            fSocketNotify.processSurplus(userId, surplus);
        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSocketManager.disconnect();
    }
}