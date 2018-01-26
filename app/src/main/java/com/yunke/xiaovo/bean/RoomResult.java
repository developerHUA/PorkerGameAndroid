package com.yunke.xiaovo.bean;

import java.io.Serializable;
import java.util.List;


public class RoomResult extends Result {

    public static final int D_D_Z_THREE_TYPE = 3; //三人斗地主类型
    public static final int D_D_Z_FOUR_TYPE = 4; //四人人斗地主类型


    public static final int REMOVE_DOUBLE_TWO = 1; // 去掉两个2
    public static final int REMOVE_DOUBLE_THREE = 2; // 去掉两个3
    public static final int REMOVE_ONE_AND_TWEO = 3; // 去掉一个2一个A

    public Result result;

    public static class Result implements Serializable{
        public int playType; //房间类型
        public int ruletype; //去牌类型
        public List<User> users; // 房间内用户
        public int roomNumber; //房间号
    }

}
