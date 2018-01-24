package com.yunke.xiaovo.bean;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 */
public class DDZPorker extends Porker implements Comparable<DDZPorker> {

    public static final int TWO_SIZE = 12;
    public static final int SMALL_KING_SIZE = 13;
    public static final int BIG_KING_SIZE = 14;
    public int porkerSize;
    public boolean isClick;

    public static List<DDZPorker> getAllPoker() {
        List<DDZPorker> allPoker = new ArrayList<>();
        int type = 1;
        int id = 0;
        int porkerSize = 11;
        for (int i = 1; i <= 52; i++) {
            if (porkerSize > 12) {
                porkerSize = 0;
            }
            DDZPorker porker = new DDZPorker();
            porker.porkerId = id;
            porker.porkerType = type;
            porker.porkerSize = porkerSize;
            porkerSize++;
            allPoker.add(porker);
            id++;
            if (i % 13 == 0) {
                id = 0;
                type++;
            }
        }
        DDZPorker porker = new DDZPorker();
        porker.porkerId = 0;
        porker.porkerType = SMALL_KING;
        porker.porkerSize = 13;
        DDZPorker porker2 = new DDZPorker();
        porker2.porkerId = 0;
        porker2.porkerType = BIG_KING;
        porker2.porkerSize = 14;
        allPoker.add(porker);
        allPoker.add(porker2);
        Collections.sort(allPoker);
        return allPoker;
    }

    public static List<DDZPorker> getShuffleAllPoker() {
        List<DDZPorker> allPoker = getAllPoker();
        Collections.shuffle(allPoker);
        return allPoker;
    }


    public static List<DDZPorker> getMoveShufflePoker() {
        List<DDZPorker> allPoker = getAllPoker();
        allPoker.remove(3);
        allPoker.remove(2);
        Collections.shuffle(allPoker);
        return allPoker;
    }


    @Override
    public int compareTo(@NonNull DDZPorker ddzPorker) {
        int temp2 = this.porkerSize;
        int temp1 = ddzPorker.porkerSize;
        int sort = temp1 - temp2;
        return sort == 0 ?
                this.porkerType - ddzPorker.porkerType : sort;
    }

    @Override
    public String getSizeStr() {
        return getSizeStr(porkerSize);
    }

    public static String getSizeStr(int porkerSize) {
        String str;
        if (porkerSize == TWO_SIZE) {
            str = "2";
        } else if (porkerSize == SMALL_KING_SIZE) {
            str = "小王";
        } else if (porkerSize == BIG_KING_SIZE) {
            str = "大王";
        } else if (porkerSize == 11) {
            str = "A";
        } else if (porkerSize + 3 == 13) {
            str = "K";
        } else if (porkerSize + 3 == 12) {
            str = "Q";
        } else if (porkerSize + 3 == 11) {
            str = "J";
        } else {
            str = porkerSize + 3 + "";
        }
        return str;
    }
}
