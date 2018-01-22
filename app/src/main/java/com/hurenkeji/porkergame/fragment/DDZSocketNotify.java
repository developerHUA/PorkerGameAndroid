package com.hurenkeji.porkergame.fragment;

import com.hurenkeji.porkergame.bean.DDZPorker;
import com.hurenkeji.porkergame.bean.SocketBean;
import com.hurenkeji.porkergame.bean.User;

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

    void processPlayPorker(SocketBean<ArrayList<DDZPorker>> socketBean);

    void processSurplus(int userId, int surplus);

    void processLandlord(int userId);
}
