package com.yunke.xiaovo.manage;


import android.os.Handler;
import android.os.Message;

import com.yunke.xiaovo.net.HRNetConfig;
import com.yunke.xiaovo.utils.LogUtil;
import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFactory;
import com.neovisionaries.ws.client.WebSocketFrame;

import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 游戏的socket管理
 */
public class PorkerGameWebSocketManager {
    private static final String MESSAGE_TYPE_KEY = "type"; //消息类型
    public static final String USER_ID_KEY = "uid"; //用户id
    public static final String MESSAGE_PARAMS_KEY = "message_params_key"; //用户id


    // APP常量
    public static final int CONNECT_SUCCESS = -1;


    // 服务端常量
    public static final int READY = 1; //准备
    public static final int PLAY_PORKER = 2;//出牌
    public static final int JOIN_ROOM = 3; // 有人加入房间
    public static final int EXIT_ROOM = 4; //有人退出房间
    public static final int LANDLORD = 5; // 叫地主
    public static final int SURPLUS_ONE = 6;//剩余一张牌
    public static final int SURPLUS_TWO = 7; //剩余两张牌
    public static final int NO_PLAY = 8; // 不出牌
    public static final int CANCEL_READY = 9; //取消准备
    public static final int DEAL_PORKER = 10; //发牌
    public static final int UNKNOWN_PORKER = 11; // 牌型不正确
    public static final int NO_LANDLORD = 12;// 处理不叫地主
    public static final int CURRENT_IS_LANDLORD = 13;// 当前是地主
    public static final int LANDLORD_COUNT_FINISH = 14;// 不叫地主次数已用完
    public static final int LANDLORD_VICTORY = 15;// 游戏结束,地主胜利
    public static final int FARMER_VICTORY = 16;//游戏结束,农民胜利
    public static final int SCORE_CHANGED = 18;// 当前分数发生变化

    private static final java.lang.String TAG = PorkerGameWebSocketManager.class.getName();
    private static PorkerGameWebSocketManager socketManager;
    private WebSocket mWebSocket;
    private Handler mHandler;
    private static final int TIME_OUT = 10000;
    private static final int FRAME_QUEUE_SIZE = 5;
    private String url;

    private PorkerGameWebSocketManager() {
    }

    public static PorkerGameWebSocketManager getInstance() {
        if (socketManager == null) {
            synchronized (PorkerGameWebSocketManager.class) {
                if (socketManager == null) {
                    socketManager = new PorkerGameWebSocketManager();
                }
            }
        }

        return socketManager;

    }


    public void init(int roomNumber, String token, int userId, Handler notifyHandler) {

        this.mHandler = notifyHandler;
        url = HRNetConfig.PORKER_GAME_SOCKET_URL + "/" + roomNumber + "/" + token + "/" + userId;
        connect();
    }


    private void connect() {
        try {
            mWebSocket = new WebSocketFactory()
                    .createSocket(url, TIME_OUT)
                    .setFrameQueueSize(FRAME_QUEUE_SIZE)//设置帧队列最大值为5
                    .setMissingCloseFrameAllowed(false);//设置不允许服务端关闭连接却未发送关闭帧;
            mWebSocket.addListener(new DDZSocketAdapter());
            mWebSocket.connectAsynchronously();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reconnect() {
        if (mWebSocket != null && !mWebSocket.isOpen()) {
            LogUtil.i(TAG,"reconnect... ");
            connect();
        }
    }

    public void disconnect() {
        mWebSocket.disconnect();
    }


    public void sendText(String message) {
        mWebSocket.sendText(message);
    }


    public class DDZSocketAdapter extends WebSocketAdapter {


        @Override
        public void onConnectError(WebSocket websocket, WebSocketException exception) throws Exception {
            super.onConnectError(websocket, exception);
        }

        @Override
        public void onTextMessage(WebSocket websocket, String text) throws Exception {
            LogUtil.i(TAG, "onTextMessage text = " + text);
            JSONObject jsonObject = new JSONObject(text);
            int type = jsonObject.getInt(MESSAGE_TYPE_KEY);
            Message message = Message.obtain();
            message.what = type;
            message.obj = text;
            mHandler.sendMessage(message);

        }

        @Override
        public void onConnected(WebSocket websocket, Map<String, List<String>> headers) throws Exception {
            LogUtil.i(TAG, "连接成功");
            Message message = Message.obtain();
            message.what = CONNECT_SUCCESS;
            message.obj = "{\"message\":\"连接成功\",\"uid\":0}";
            mHandler.sendMessage(message);
        }

        @Override
        public void onError(WebSocket websocket, WebSocketException cause) throws Exception {
            LogUtil.i(TAG, "连接失败");
            cause.printStackTrace();
        }

        @Override
        public void onDisconnected(WebSocket websocket, WebSocketFrame serverCloseFrame,
                                   WebSocketFrame clientCloseFrame, boolean closedByServer) throws Exception {
            LogUtil.i(TAG, "断开连接");
            LogUtil.i(TAG, closedByServer + "  ");
        }


    }


}
