package com.hurenkeji.porkergame.bean;

import com.hurenkeji.porkergame.utils.StringUtil;

/**
 *
 */
public class SocketBean<T> {

    public int uid;
    public int type;
    public T params;


    public static String messageFromType(int userId, int type) {
        SocketBean socketBean = new SocketBean();
        socketBean.type = type;
        socketBean.uid = userId;
        return StringUtil.objectToJson(socketBean);
    }

    public static String messageFromParams(int userId, int type, Object params) {
        SocketBean socketBean = new SocketBean();
        socketBean.type = type;
        socketBean.uid = userId;
        socketBean.params = params;
        return StringUtil.objectToJson(socketBean);
    }


}
