package com.yunke.xiaovo.manage;

import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;

import com.yunke.xiaovo.bean.MusicConstants;

import java.io.IOException;

/**
 * 游戏音乐管理
 */
public class GameMusicManager {

    private static GameMusicManager mMusicManager;
    private MediaPlayer mMediaPlayer;
    private static final int ONE = 1; //单牌
    private static final int DOUBLE = 2; // 对子
    private static final int THREE_AND_ONE = 3; // 三带一
    private static final int THREE_NO_AND = 4; // 三不带
    private static final int SHUN_ZI = 5; // 顺子类型
    private static final int AIRCRAFT = 8; // 飞机类型
    private static final int DOUBLE_SHUN_ZI = 9; // 连对
    private static final int FOUR_AND_TWO = 10; // 四带二
    private static final int THREE_BOMB = 11; // 三炸
    private static final int TWO_BOMB = 12; // 二炸
    private static final int BOMB = 13; // 炸弹类型
    private static final int THREE_BOMB_FOUR = 14; // 三炸四张
    private static final int KING_BOMB = 15; // 王炸类型
    public static final int LANDLORD = 16; // 叫地主
    public static final int NO_LANDLORD = 17; // 不叫地主
    public static final int NO_PLAY = 18; // 不出
    public static final int SURPLUS_TWO = 19; // 剩两张牌
    public static final int SURPLUS_ONE = 20; // 剩一张牌

    private String sex = MusicConstants.MAN;

    public static GameMusicManager getInstance() {
        if (mMusicManager == null) {
            synchronized (GameMusicManager.class) {
                if (mMusicManager == null) {
                    mMusicManager = new GameMusicManager();
                }
            }
        }
        return mMusicManager;
    }


    private GameMusicManager() {
        mMediaPlayer = new MediaPlayer();
    }

    public void playMusic(String musicName) {
        if (mMediaPlayer == null) {
            return;
        }
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.stop();
        }
        mMediaPlayer.reset();
        try {
            AssetFileDescriptor afd = AppManager.getInstance().getAssets().openFd(musicName);
            mMediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            mMediaPlayer.prepare();
            mMediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void playMusicByType(int type, int porkerNumber) {
        String musicName = "";
        switch (type) {
            case ONE:
                musicName = sex + porkerNumber + MusicConstants.FILE_FORMAT;
                break;
            case DOUBLE:
                musicName = sex + "dui" + porkerNumber + MusicConstants.FILE_FORMAT;
                break;
            case THREE_AND_ONE:
                musicName = sex + "sandaiyi" + MusicConstants.FILE_FORMAT;
                break;
            case THREE_NO_AND:
                musicName = sex + "tuple1" + porkerNumber + MusicConstants.FILE_FORMAT;
                break;
            case SHUN_ZI:
                musicName = sex + "shunzi" + MusicConstants.FILE_FORMAT;
                break;
            case AIRCRAFT:
                musicName = sex + "feiji" + MusicConstants.FILE_FORMAT;
                break;
            case DOUBLE_SHUN_ZI:
                musicName = sex + "liandui" + MusicConstants.FILE_FORMAT;
                break;
            case FOUR_AND_TWO:
                musicName = sex + "sidaier" + MusicConstants.FILE_FORMAT;
                break;
            case THREE_BOMB:
            case TWO_BOMB:
            case BOMB:
            case THREE_BOMB_FOUR:
                musicName = sex + "zhadan" + MusicConstants.FILE_FORMAT;
                break;
            case KING_BOMB:
                musicName = sex + "wangzha" + MusicConstants.FILE_FORMAT;
                break;
            case LANDLORD:
                musicName = sex + "landlord" + MusicConstants.FILE_FORMAT;
                break;
            case NO_LANDLORD:
                musicName = sex + "no_landlord" + MusicConstants.FILE_FORMAT;
                break;
            case NO_PLAY:
                musicName = sex + "buyao" + MusicConstants.FILE_FORMAT;
                break;
            case SURPLUS_TWO:
                musicName = sex + "baojing2" + MusicConstants.FILE_FORMAT;
                break;
            case SURPLUS_ONE:
                musicName = sex + "baojing1" + MusicConstants.FILE_FORMAT;
                break;
        }
        playMusic(musicName);
    }

    public void playMusicByType(int type) {
        playMusicByType(type, 0);
    }

    public void stopMusic() {
        if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
            mMediaPlayer.stop();
        }
    }

}
