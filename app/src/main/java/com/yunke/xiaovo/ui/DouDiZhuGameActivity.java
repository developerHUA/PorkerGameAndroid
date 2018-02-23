package com.yunke.xiaovo.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;
import com.yunke.xiaovo.R;
import com.yunke.xiaovo.base.BaseActivity;
import com.yunke.xiaovo.bean.DDZPorker;
import com.yunke.xiaovo.bean.IntentConstants;
import com.yunke.xiaovo.bean.MusicConstants;
import com.yunke.xiaovo.bean.PlayPorker;
import com.yunke.xiaovo.bean.RoomResult;
import com.yunke.xiaovo.bean.SocketBean;
import com.yunke.xiaovo.bean.User;
import com.yunke.xiaovo.fragment.DDZFourFragment;
import com.yunke.xiaovo.fragment.DDZSocketNotify;
import com.yunke.xiaovo.fragment.DDZThreeFragment;
import com.yunke.xiaovo.manage.GameMusicManager;
import com.yunke.xiaovo.manage.MusicManager;
import com.yunke.xiaovo.manage.PorkerGameWebSocketManager;
import com.yunke.xiaovo.manage.UserManager;
import com.yunke.xiaovo.utils.LogUtil;
import com.yunke.xiaovo.utils.StringUtil;
import com.yunke.xiaovo.utils.ToastUtils;
import com.yunke.xiaovo.widget.CommonButton;
import com.yunke.xiaovo.widget.CommonTextView;
import com.yunke.xiaovo.widget.PorkerListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;

/**
 *
 */
public class DouDiZhuGameActivity extends BaseActivity {

    private static final java.lang.String TAG = DouDiZhuGameActivity.class.getName();

    @BindView(R.id.iv_user_headimg)
    ImageView ivUserHeadimg;
    @BindView(R.id.tv_nick_name)
    TextView tvNickname;
    @BindView(R.id.iv_no_play)
    ImageView ivNoPlay;
    @BindView(R.id.pv_porker_view)
    PorkerListView porkerView;
    @BindView(R.id.btn_out_porker)
    Button btnOutPorker;
    @BindView(R.id.pv_play_view)
    PorkerListView pvPlayView; // 用户出牌view
    @BindView(R.id.ll_buttons)
    LinearLayout llButtons;
    @BindView(R.id.btn_no_play)
    Button btnNoPlay;
    @BindView(R.id.btn_ready)
    CommonButton btnReady;
    @BindView(R.id.btn_landlord)
    Button btnLandlord;
    @BindView(R.id.btn_no_landlord)
    Button btnNoLandlord;
    @BindView(R.id.tv_room_number)
    TextView tvRoomNumber;
    @BindView(R.id.btn_back)
    CommonButton btnBack;
    @BindView(R.id.tv_score)
    CommonTextView tvScore;
    @BindView(R.id.iv_is_landlord)
    ImageView ivIsLandlord;
    @BindView(R.id.pv_landlord_porker)
    PorkerListView pvLandlordPorker;

    private int userId;
    private PorkerGameWebSocketManager mSocketManager = PorkerGameWebSocketManager.getInstance();
    private RoomResult.Result room;
    private DDZSocketNotify fSocketNotify; // 通知fragment
    public User leftUser; // 左边玩家
    public User topUser; // 上边玩家
    public User rightUser;// 右边玩家
    private List<DDZPorker> currentPorker = new ArrayList<>();
    private boolean isFirstPlay;
    private boolean isLandlord; // 是否为地主
    private NetworkChange mNetworkChange;
    private String score; // 当前分数

    @Override
    public void initView() {
        btnOutPorker.setOnClickListener(this);
        btnReady.setOnClickListener(this);
        btnLandlord.setOnClickListener(this);
        btnNoPlay.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        porkerView.isClick(true);
        pvPlayView.isClick(false);
        btnNoLandlord.setOnClickListener(this);
        pvPlayView.setPorkerWidthAndHeight(getResources().getDimension(R.dimen.y99), getResources().getDimension(R.dimen.x129));
        pvLandlordPorker.setPorkerWidthAndHeight(getResources().getDimension(R.dimen.y71), getResources().getDimension(R.dimen.x92));

        showProgressDialog("正在连接...");
    }


    @Override
    public void initData() {
        userId = UserManager.getInstance().getUserId();
        User mUser = UserManager.getInstance().getUser();
        room = (RoomResult.Result) getIntent().getSerializableExtra(IntentConstants.ROOM_KEY);
        Picasso.with(this).load(mUser.getHeadimgurl()).into(ivUserHeadimg);
        tvNickname.setText(mUser.getNickname());
        tvRoomNumber.setText(String.format("房间号：%s", room.roomNumber));
        if (room.users.size() == 2) {
            leftUser = room.users.get(0);
        } else if (room.users.size() == 3) {
            rightUser = room.users.get(0);
            if (room.playType == RoomResult.D_D_Z_FOUR_TYPE) {
                rightUser = room.users.get(1);
            } else {
                leftUser = room.users.get(1);
            }
        } else if (room.users.size() == 4) {
            rightUser = room.users.get(0);
            topUser = room.users.get(1);
            leftUser = room.users.get(2);
        }
        mSocketManager.init(room.roomNumber, mUser.getToken(), userId, new NotifyHandler());
        if (room.playType == RoomResult.D_D_Z_THREE_TYPE) {
            initThreeDDZFragment();
        } else if (room.playType == RoomResult.D_D_Z_FOUR_TYPE) {
            initFourDDZFragment();
        }
        processReadyUI();
    }

    private void initNetBroadcast() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        mNetworkChange = new NetworkChange();
        registerReceiver(mNetworkChange, filter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_out_porker:
                outPorker();
                break;
            case R.id.btn_ready:
                if (btnReady.getText().equals("取消准备")) {
                    ready(PorkerGameWebSocketManager.CANCEL_READY);

                } else {
                    ready(PorkerGameWebSocketManager.READY);
                }
                break;
            case R.id.btn_no_play:
                noPlay();
                break;
            case R.id.btn_landlord:
                landlord();
                break;
            case R.id.btn_no_landlord:
                noLandlord();
                break;
            case R.id.btn_back:
                showExitDialog();
                break;

        }
    }

    /**
     * 不叫地主
     */
    private void noLandlord() {
        showProgressDialog("加载中...");
        String json = SocketBean.messageFromType(userId, PorkerGameWebSocketManager.NO_LANDLORD);
        mSocketManager.sendText(json);
    }

    /**
     * 出牌
     */
    private void outPorker() {
        showProgressDialog("加载中...");
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
        showProgressDialog("加载中...");
        btnLandlord.setEnabled(false);
        String message = SocketBean.messageFromType(userId, PorkerGameWebSocketManager.LANDLORD);
        mSocketManager.sendText(message);
    }

    /**
     * 不出
     */
    private void noPlay() {
        showProgressDialog("加载中...");
        btnNoPlay.setEnabled(false);
        String message = SocketBean.messageFromType(userId, PorkerGameWebSocketManager.NO_PLAY);
        mSocketManager.sendText(message);
    }

    /**
     * 准备
     */
    private void ready(int type) {
        showProgressDialog("加载中...");
        btnReady.setEnabled(false);
        String message = SocketBean.messageFromType(userId, type);
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

    private void initFourDDZFragment() {
        FragmentManager fm = getSupportFragmentManager();
        DDZFourFragment ddzFourFragment = new DDZFourFragment();
        fSocketNotify = ddzFourFragment;
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.fl_fragment, ddzFourFragment);
        fragmentTransaction.commit();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_ddz_game;
    }


    /**
     * 更新准备UI
     */
    private void processReadyUI() {
        llButtons.setVisibility(View.VISIBLE);
        pvPlayView.clear();
        porkerView.clear();
        btnReady.setVisibility(View.VISIBLE);
        btnReady.setEnabled(true);
        btnNoLandlord.setVisibility(View.GONE);
        btnLandlord.setVisibility(View.GONE);
        btnNoPlay.setVisibility(View.GONE);
        btnOutPorker.setVisibility(View.GONE);
        ivNoPlay.setVisibility(View.GONE);
        tvScore.setText(getString(R.string.game_score, room.getDefaultScore()));
        pvLandlordPorker.upDatePorker(room.getLandlordPorkerCount());
    }

    /**
     * 更新准备出牌UI
     */
    private void processReadyPlayPorkerUI() {
        llButtons.setVisibility(View.VISIBLE);
        btnNoPlay.setVisibility(isFirstPlay ? View.GONE : View.VISIBLE);
        pvPlayView.clear();
        pvPlayView.setVisibility(View.VISIBLE);
        btnOutPorker.setVisibility(View.VISIBLE);
        btnOutPorker.setEnabled(true);
        btnNoPlay.setEnabled(true);
        btnNoLandlord.setVisibility(View.GONE);
        btnReady.setVisibility(View.GONE);
        ivNoPlay.setVisibility(View.GONE);
        btnReady.setEnabled(false);
        btnLandlord.setVisibility(View.GONE);
        ivIsLandlord.setVisibility(View.GONE);
        if (isLandlord) {
            ivIsLandlord.setVisibility(View.VISIBLE);
        } else {
            ivIsLandlord.setVisibility(View.GONE);
        }


    }

    /**
     * 更新出牌UI
     */
    private void processPlayPorkerUI(List<DDZPorker> playPorkers, List<DDZPorker> porkers) {
        llButtons.setVisibility(View.GONE);
        pvPlayView.setVisibility(View.VISIBLE);
        pvPlayView.clear();
        pvPlayView.upDatePorker(playPorkers);
        porkerView.clearIndex();
        porkerView.upDatePorker(porkers);
    }

    /**
     * 更新不出牌UI
     */
    private void processNoPlayPorkerUI() {
        llButtons.setVisibility(View.GONE);
        ivNoPlay.setVisibility(View.VISIBLE);
        ivNoPlay.setImageResource(R.drawable.game_no_play);
        pvPlayView.setVisibility(View.GONE);
        porkerView.clearIndex();
        pvPlayView.clear();
    }

    /**
     * 更新准备叫地主UI
     */
    private void processReadyLandlordUI() {
        llButtons.setVisibility(View.VISIBLE);
        btnLandlord.setVisibility(View.VISIBLE);
        btnNoLandlord.setVisibility(View.VISIBLE);
        btnNoLandlord.setEnabled(true);
        btnLandlord.setEnabled(true);
        btnNoPlay.setVisibility(View.GONE);
        btnOutPorker.setVisibility(View.GONE);
        btnReady.setVisibility(View.GONE);
    }


    /**
     * 更新不叫地主UI
     */
    private void processNoLandlordUI() {
        llButtons.setVisibility(View.GONE);
        ivNoPlay.setVisibility(View.VISIBLE);
        ivNoPlay.setImageResource(R.drawable.no_landlord);
    }

    private class NotifyHandler extends Handler {


        
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            hideProgressDialog();
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
                case PorkerGameWebSocketManager.CONNECT_SUCCESS: // 连接成功
                    initNetBroadcast();
                    break;
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
                case PorkerGameWebSocketManager.LANDLORD_COUNT_FINISH: // 不叫地主次数已用完(直接是地主)
                    GameMusicManager.getInstance().playMusicByType(GameMusicManager.LANDLORD);
                    processLandlord(userId, json);
                    break;
                case PorkerGameWebSocketManager.NO_LANDLORD: // 不叫地主
                    btnNoLandlord.setEnabled(false);
                    GameMusicManager.getInstance().playMusicByType(GameMusicManager.NO_LANDLORD);
                    processNoLandlord(userId);
                    break;
                case PorkerGameWebSocketManager.CURRENT_IS_LANDLORD: // 地主信号
                    processIsLandlord(userId);
                    break;
                case PorkerGameWebSocketManager.SURPLUS_ONE: // 剩余一张牌
                    GameMusicManager.getInstance().playMusicByType(GameMusicManager.SURPLUS_ONE);
                    break;
                case PorkerGameWebSocketManager.SURPLUS_TWO: // 剩余两张牌
                    GameMusicManager.getInstance().playMusicByType(GameMusicManager.SURPLUS_TWO);
                    break;
                case PorkerGameWebSocketManager.NO_PLAY: // 不出牌
                    GameMusicManager.getInstance().playMusicByType(GameMusicManager.NO_PLAY);
                    processNoPlay(userId);
                    break;
                case PorkerGameWebSocketManager.CANCEL_READY: // 取消准备
                    processCancelReady(userId);
                    break;
                case PorkerGameWebSocketManager.DEAL_PORKER: // 发牌
                    processSendPoker(json);
                    break;
                case PorkerGameWebSocketManager.LANDLORD_VICTORY: // 游戏结束,地主胜利
                    processLandlordVictory();
                    break;
                case PorkerGameWebSocketManager.FARMER_VICTORY: // 游戏结束,地主胜利
                    processFarmerVictory();
                    break;
                case PorkerGameWebSocketManager.UNKNOWN_PORKER: // 牌型不正确
                    processUnknownType();
                    break;
                case PorkerGameWebSocketManager.SCORE_CHANGED: // 当前游戏分数发生改变
                    processScoreChanged(json);
                    break;

            }
        }
    }

    /**
     * 处理当前游戏分数发生改变
     */
    private void processScoreChanged(String json) {
        Type type = new TypeToken<SocketBean<String>>() {
        }.getType();
        SocketBean<String> socketBean = StringUtil.jsonToObject(json, type);
        if (socketBean != null) {
            score = socketBean.params;
            tvScore.setText(getString(R.string.game_score, score));
        }
    }

    /**
     * 处理农民胜利
     */
    private void processFarmerVictory() {
        processReadyUI();
        fSocketNotify.processGameOver();
    }

    /**
     * 处理地主胜利
     */
    private void processLandlordVictory() {
        processReadyUI();
        fSocketNotify.processGameOver();

    }


    /**
     * 当前出的牌型不正确
     */
    private void processUnknownType() {
        processReadyPlayPorkerUI();
        ToastUtils.showToast("牌型不正确");
    }


    /**
     * 处理用户取消准备
     */
    private void processCancelReady(int userId) {
        if (userId == this.userId) {
            processReadyUI();
        } else {
            fSocketNotify.processCancelReady(userId);
        }
    }


    /**
     * 处理用户准备
     */
    private void processReady(int userId) {
        btnReady.setEnabled(true);
        isLandlord = false;
        if (userId == this.userId) {
            btnReady.setVisibility(View.GONE);
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
        llButtons.setVisibility(View.GONE);
        SocketBean<ArrayList<DDZPorker>> socketBean = StringUtil.jsonToObject(message, type);
        if (socketBean != null) {
            currentPorker = socketBean.params;
            Collections.sort(currentPorker);
            porkerView.upDatePorker(currentPorker);
            fSocketNotify.processSendPoker();
        }
    }


    /**
     * 处理用户出牌
     */
    private void processPlayPorker(String message) {
        Type type = new TypeToken<SocketBean<PlayPorker>>() {
        }.getType();
        SocketBean<PlayPorker> socketBean = StringUtil.jsonToObject(message, type);
        if (socketBean != null && socketBean.params != null) {
            int porkerNumber = DDZPorker.getPorkerNumberBySize(socketBean.params.getPorkerSize());
            GameMusicManager.getInstance().playMusicByType(socketBean.params.getType(),porkerNumber);
            if (socketBean.uid == this.userId) {
                for (int i = currentPorker.size() - 1; i >= 0; i--) {
                    for (int j = socketBean.params.getPorkerList().size() - 1; j >= 0; j--) {
                        if (currentPorker.get(i).porkerId == socketBean.params.getPorkerList().get(j).porkerId) {
                            currentPorker.remove(i);
                            break;
                        }
                    }
                }
                // 当前用户出牌了 更新UI
                processPlayPorkerUI(socketBean.params.getPorkerList(), currentPorker);
                fSocketNotify.processCountDown(rightUser.getUserId());
            } else {
                isFirstPlay = false;
                if (socketBean.uid == leftUser.getUserId()) {
                    // 该当前用户出牌了 更新准备出牌UI

                    processReadyPlayPorkerUI();
                }
                // 其他用户出牌了，通知更新UI
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
            processNoPlayPorkerUI();
            fSocketNotify.processCountDown(rightUser.getUserId());
        } else {
            if (leftUser.getUserId() == userId) {
                // 该当前用户出牌
                processReadyPlayPorkerUI();
            } else if (rightUser.getUserId() == userId) {
                isFirstPlay = true;
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
        if (socketBean != null && socketBean.params.getUserId() != userId) {
            if (room.playType == RoomResult.D_D_Z_THREE_TYPE) {
                if (rightUser == null) {
                    rightUser = socketBean.params;
                } else {
                    leftUser = socketBean.params;
                }
            } else {
                if (rightUser == null) {
                    rightUser = socketBean.params;
                } else if (topUser == null) {
                    topUser = socketBean.params;
                } else {
                    leftUser = socketBean.params;
                }
            }
            fSocketNotify.processJoin(socketBean.params);
        }
    }

    /**
     * 处理用户退出房间
     */
    private void processExit(int userId) {
        if (userId != this.userId) {
            fSocketNotify.processExit(userId);
        }
    }


    /**
     * 处理地主信号
     *
     * @param userId 地主的用户id
     */
    private void processIsLandlord(int userId) {
        if (userId == this.userId) {
            processReadyLandlordUI();
        } else {
            fSocketNotify.processCountDown(userId);
        }
    }

    /**
     * 处理用户叫地主
     */
    private void processLandlord(int userId, String message) {
        Type type = new TypeToken<SocketBean<ArrayList<DDZPorker>>>() {
        }.getType();
        SocketBean<ArrayList<DDZPorker>> socketBean = StringUtil.jsonToObject(message, type);

        if (socketBean != null) {
            if (userId == this.userId) {
                for (int i = 0; i < socketBean.params.size(); i++) {
                    socketBean.params.get(i).isClick = true;
                    currentPorker.add(socketBean.params.get(i));
                }
                Collections.sort(currentPorker);
                porkerView.upDatePorker(currentPorker);
                isFirstPlay = true;
                isLandlord = true;
                pvLandlordPorker.upDatePorker(socketBean.params);
                processReadyPlayPorkerUI();
            } else {
                llButtons.setVisibility(View.GONE);
                fSocketNotify.processLandlord(userId);
            }

        }
    }


    /**
     * 处理用户不叫地主
     *
     * @param userId 不叫地主的用户id
     */
    private void processNoLandlord(int userId) {
        if (userId == this.userId) {
            processNoLandlordUI();
            fSocketNotify.processCountDown(rightUser.getUserId());
        } else {
            fSocketNotify.processNoLandlord(userId);
        }
    }




    @Override
    public void onBackPressed() {
        showExitDialog();
    }

    private void showExitDialog() {
        showConfirmDialog("确定要退出房间吗？", "", "");
    }

    @Override
    protected void dialogConfirm() {
        super.dialogConfirm();
        mSocketManager.sendText(SocketBean.messageFromType(userId, PorkerGameWebSocketManager.EXIT_ROOM));
        finish();
    }


    public class NetworkChange extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            NetworkInfo wifiInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            LogUtil.i(TAG, "NetworkChange");
            if (!networkInfo.isConnected() && !wifiInfo.isConnected()) {
                // 网络不可用!
                ToastUtils.showToast("无网络!");
            } else {
                if (wifiInfo.isConnected()) {
                    // wifi
                    mSocketManager.reconnect();
                }
                if (networkInfo.isConnected()) {
                    // 移动网络
                    mSocketManager.reconnect();
                }
            }

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        MusicManager.getInstance().playMusic(MusicConstants.GAMW);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSocketManager.disconnect();
        if (mNetworkChange != null) {
            unregisterReceiver(mNetworkChange);
        }
    }
}
