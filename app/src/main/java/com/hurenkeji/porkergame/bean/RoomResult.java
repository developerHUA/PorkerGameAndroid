package com.hurenkeji.porkergame.bean;

import java.util.List;

/**
 * Created by huayaowei on 2018/1/21.
 */

public class RoomResult extends Result {

    public static final int D_D_Z_THREE_TYPE = 1; //三人斗地主类型
    public static final int D_D_Z_FOUR_TYPE = 2; //四人人斗地主类型
    public int type; //房间类型

    public List<User> users; // 房间内用户
    public int roomNumber; //房间号

}
