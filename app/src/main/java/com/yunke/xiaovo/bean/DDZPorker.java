package com.yunke.xiaovo.bean;

import android.support.annotation.NonNull;


/**
 *
 */
public class DDZPorker extends Porker implements Comparable<DDZPorker> {

    public static final int ONE_SIZE = 11;
    public static final int TWO_SIZE = 12;
    public static final int SMALL_KING_SIZE = 13;
    public static final int BIG_KING_SIZE = 14;
    public int porkerSize;
    public boolean isClick;


    @Override
    public int compareTo(@NonNull DDZPorker ddzPorker) {
        int temp2 = this.porkerSize;
        int temp1 = ddzPorker.porkerSize;
        int sort = temp1 - temp2;
        return sort == 0 ?
                this.porkerType - ddzPorker.porkerType : sort;
    }


    public static int getPorkerNumberBySize(int porkerSize) {
        if (porkerSize == ONE_SIZE) {
            return 1;
        } else if (porkerSize == TWO_SIZE) {
            return 2;
        } else if (porkerSize == SMALL_KING_SIZE) {
            return 14;
        } else if (porkerSize == BIG_KING) {
            return 15;
        } else {
            return porkerSize + 3;
        }
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
