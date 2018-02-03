package com.yunke.xiaovo.net;

/**
 * 网络请求配置信息
 */
public class HRNetConfig {

    public static final String SALT = "huarenkeji2018";

    public static final String HTTP = "http://";
    public static final String HOST = "47.94.44.114:8080/";
    public static final String URL = HTTP + HOST + "%s";
    public static final String PORKER_GAME_SOCKET_URL = "ws://47.94.44.114:8080/porkerGame/socket";




//    public static final String HTTP = "http://";
//    public static final String HOST = "192.168.0.14/";
//    public static final String URL = HTTP + HOST + "%s";
//    public static final String PORKER_GAME_SOCKET_URL = "ws://192.168.0.14/porkerGame/socket";


    public static final String WX_LOGIN = "/user/wxLogin";
    public static final String CREATE_ROOM_URL = "/room/create";
    public static final String JOIN_ROOM_URL = "/room/join";

}
