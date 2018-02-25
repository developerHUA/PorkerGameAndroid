package com.yunke.xiaovo.bean;

import android.text.TextUtils;

import java.io.Serializable;
import java.util.List;


public class RoomResult extends Result {

    public static final int D_D_Z_THREE_TYPE = 3; //三人斗地主类型
    public static final int D_D_Z_FOUR_TYPE = 4; //四人人斗地主类型


    public static final int NO_REMOVE = 0; // 不去牌（只有三个人斗地主才能不去牌）
    public static final int REMOVE_DOUBLE_TWO = 1; // 去掉两个2
    public static final int REMOVE_DOUBLE_THREE = 2; // 去掉两个3
    public static final int REMOVE_ONE_AND_TWO = 3; // 去掉一个2一个A

    public Result result;

    public static class Result implements Serializable {
        public int playType; //房间类型
        public int ruleType; //去牌类型
        public List<User> users; // 房间内用户
        public int roomNumber; //房间号
        public String defaultScore;

        public String getDefaultScore() {
            if (TextUtils.isEmpty(defaultScore)) {
                return "1";
            }
            return defaultScore;
        }

        public int getLandlordPorkerCount() {
            if (playType == D_D_Z_THREE_TYPE && ruleType == NO_REMOVE) {
                return 3;
            }
            return 4;
        }


        public int getRoomPersonCount() {
            if (this.playType == D_D_Z_FOUR_TYPE) {
                return 4;
            } else if (this.playType == D_D_Z_THREE_TYPE) {
                return 3;
            }

            return 0;
        }

    }

}
