package com.hurenkeji.porkergame.bean;


public class RoomParams {

    public int roomNumber;
    public int userId;
    public String token;
    public int playType;
    public int ruleType;

    public RoomParams(int roomNumber, int userId, String token) {
        this.roomNumber = roomNumber;
        this.userId = userId;
        this.token = token;
    }

    public RoomParams(int userId, String token, int type, int abandonType) {
        this.userId = userId;
        this.token = token;
        this.playType = type;
        this.ruleType = abandonType;
    }


}
