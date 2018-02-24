package com.yunke.xiaovo.fragment;

import com.yunke.xiaovo.bean.DDZPorker;
import com.yunke.xiaovo.bean.PlayPorker;
import com.yunke.xiaovo.bean.SocketBean;
import com.yunke.xiaovo.bean.SocketUserScore;
import com.yunke.xiaovo.bean.User;

import java.util.ArrayList;

/**
 *
 */
public interface DDZSocketNotify {


    void processReady(int userId);
    void processCancelReady(int userId);
    void processNoPlay(int userId);
    void processSendPoker();
    void processJoin(User user);
    void processExit(int userId);

    void processPlayPorker(SocketBean<PlayPorker> socketBean);

    void processSurplus(int userId, int surplus);

    void processLandlord(int userId);

    void processGameOver();

    void processNoLandlord(int userId);


    void processCountDown(int userId);

    void processUserScoreChanged(SocketBean<SocketUserScore> socketBean);
}
