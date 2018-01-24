package com.yunke.xiaovo.bean;


/**
 *
 */
public class Porker {

    public static final int RED_HEART = 1; //红桃
    public static final int PLUM_BLOSSOM = 2; //梅花
    public static final int BLACK_HEART = 3;  //黑桃
    public static final int BLOCK = 4; //方块
    public static final int SMALL_KING = 5;
    public static final int BIG_KING = 6;

    public int porkerType;
    public int porkerId;


    public int getResourceId() {
        int id = 0;
        switch (porkerType) {
            case RED_HEART:
                id = PorkerResourceId.RED_HEART_ID[porkerId];
                break;
            case PLUM_BLOSSOM:
                id = PorkerResourceId.PLUM_BLOSSOM_ID[porkerId];
                break;
            case BLACK_HEART:
                id = PorkerResourceId.BLACK_HEART_ID[porkerId];
                break;
            case BLOCK:
                id = PorkerResourceId.BLOCK_ID[porkerId];
                break;
            case SMALL_KING:
                id = PorkerResourceId.SMALL_KING_ID;
                break;
            case BIG_KING:
                id = PorkerResourceId.BIG_KING_ID;
                break;
        }
        return id;
    }


    @Override
    public String toString() {
        String str = "";

        switch (porkerType) {
            case RED_HEART:
                str = "红桃" + getSizeStr();
                break;
            case PLUM_BLOSSOM:
                str = "梅花" + getSizeStr();
                break;
            case BLACK_HEART:
                str = "黑桃" + getSizeStr();
                break;
            case BLOCK:
                str = "方块" + getSizeStr();
                break;
            case SMALL_KING:
                str = "小王";
                break;
            case BIG_KING:
                str = "大王";
                break;
        }

        return str;
    }

    public String getSizeStr() {
        String str;
        if (porkerId == 0) {
            str = "A";
        } else if (porkerId == 10) {
            str = "J";
        } else if (porkerId == 11) {
            str = "Q";
        } else if (porkerId == 12) {
            str = "K";
        } else {
            str = porkerId+1 + "";
        }
        return str;
    }
}
