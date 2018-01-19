package com.hurenkeji.porkergame.manage;


import com.hurenkeji.porkergame.net.HRNetConfig;
import com.hurenkeji.porkergame.utils.LogUtil;
import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFactory;
import com.neovisionaries.ws.client.WebSocketFrame;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 游戏的socket管理
 */
public class PorkerGameWebSocketManager {

    private static final java.lang.String TAG = PorkerGameWebSocketManager.class.getName();
    private static PorkerGameWebSocketManager socketManager;
    private WebSocket mWebSocket;


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


    public void init() {
        try {
            mWebSocket = new WebSocketFactory().setConnectionTimeout(10000)
                    .createSocket(HRNetConfig.PORKER_GAME_SOCKET_URL+"/2001/wadsdawde");
            mWebSocket.addListener(new MyWebSocketAdapter());
            mWebSocket.connectAsynchronously();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void disconnect() {
        mWebSocket.disconnect();
    }


    public void sendText(String message) {
        mWebSocket.sendText(message);
    }


    private class MyWebSocketAdapter extends WebSocketAdapter {

        @Override
        public void onConnectError(WebSocket websocket, WebSocketException exception) throws Exception {
            super.onConnectError(websocket, exception);
        }

        @Override
        public void onTextMessage(WebSocket websocket, String text) throws Exception {

            LogUtil.i(TAG,"onTextMessage text = "+text);

        }

        @Override
        public void onConnected(WebSocket websocket, Map<String, List<String>> headers) throws Exception {
            LogUtil.i(TAG,"连接成功");
        }

        @Override
        public void onError(WebSocket websocket, WebSocketException cause) throws Exception {
            LogUtil.i(TAG,"连接失败");
            cause.printStackTrace();
        }

        @Override
        public void onDisconnected(WebSocket websocket, WebSocketFrame serverCloseFrame, WebSocketFrame clientCloseFrame, boolean closedByServer) throws Exception {
            LogUtil.i(TAG,"断开连接");
        }
    }


}
