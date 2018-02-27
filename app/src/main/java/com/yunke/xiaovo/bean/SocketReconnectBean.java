package com.yunke.xiaovo.bean;

import java.util.List;

/**
 * 客户端重连时返回遗漏的信号
 */
public class SocketReconnectBean {
    public static final int GAME_OVER = 1;//未游戏
    public static final int IN_THE_GAME = 2;//游戏中

    private int gameStatus; // 当前游戏是否在进行中 1 未游戏  2 有游戏中

    // 游戏进行中需要通知客户端更新的数据
    private int landlordUserId;// 当前地主ID
    private String gameScore;//当前分数
    private List<PlayPorker> playPorkers;// 玩家出过的牌
    private List<DDZPorker> currentUserPorker; // 当前玩家剩余的牌
    private int nextPlayUserId;

    // 游戏结束需要通知客户端更新的数据
    private List<UserScore> userScoreList;

    public int getGameStatus() {
        return gameStatus;
    }

    public void setGameStatus(int gameStatus) {
        this.gameStatus = gameStatus;
    }

    public int getLandlordUserId() {
        return landlordUserId;
    }

    public void setLandlordUserId(int landlordUserId) {
        this.landlordUserId = landlordUserId;
    }

    public String getGameScore() {
        return gameScore;
    }

    public void setGameScore(String gameScore) {
        this.gameScore = gameScore;
    }

    public List<PlayPorker> getPlayPorkers() {
        return playPorkers;
    }

    public void setPlayPorkers(List<PlayPorker> playPorkers) {
        this.playPorkers = playPorkers;
    }

    public List<DDZPorker> getCurrentUserPorker() {
        return currentUserPorker;
    }

    public void setCurrentUserPorker(List<DDZPorker> currentUserPorker) {
        this.currentUserPorker = currentUserPorker;
    }

    public List<UserScore> getUserScoreList() {
        return userScoreList;
    }

    public void setUserScoreList(List<UserScore> userScoreList) {
        this.userScoreList = userScoreList;
    }

    public int getNextPlayUserId() {
        return nextPlayUserId;
    }

    public void setNextPlayUserId(int nextPlayUserId) {
        this.nextPlayUserId = nextPlayUserId;
    }
}
