package com.hurenkeji.porkergame.bean;

/**
 * Created by huayaowei on 2018/1/21.
 */

public class RoomParams extends Params {

    public RoomParams(Params params) {
        this.params = params;
    }


    public static class Params {

        public int roomNumber;
        public int userId;
        public String token;


    }


}
