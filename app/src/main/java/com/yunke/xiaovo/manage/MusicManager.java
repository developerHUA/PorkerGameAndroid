package com.yunke.xiaovo.manage;

import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;

import java.io.IOException;

/**
 *  背景音乐管理
 */
public class MusicManager {

    private static MusicManager mMusicManager;
    private MediaPlayer mMediaPlayer;
    public static MusicManager getInstance() {
        if (mMusicManager == null) {
            synchronized (MusicManager.class) {
                if (mMusicManager == null) {
                    mMusicManager = new MusicManager();
                }
            }
        }
        return mMusicManager;
    }


    private MusicManager() {
        mMediaPlayer = new MediaPlayer();
    }

    public void playMusic(String musicName) {
        if(mMediaPlayer == null) {
            return;
        }
        if(mMediaPlayer.isPlaying()) {
            mMediaPlayer.stop();
        }
        mMediaPlayer.reset();
        try {
            AssetFileDescriptor afd = AppManager.getInstance().getAssets().openFd(musicName);
            mMediaPlayer.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());
            mMediaPlayer.prepare();
            mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mMediaPlayer.start();
                }
            });
            mMediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stopMusic() {
        if(mMediaPlayer != null && mMediaPlayer.isPlaying()) {
            mMediaPlayer.stop();
        }
    }

}
