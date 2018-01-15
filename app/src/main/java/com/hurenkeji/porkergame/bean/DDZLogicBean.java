package com.hurenkeji.porkergame.bean;


import java.util.ArrayList;
import java.util.List;

public class DDZLogicBean {

    public static final int UNKNOWN = -1; //不正确牌
    public static final int ONE = 1; //单牌
    public static final int DOUBLE = 2; // 对子
    public static final int THREE_AND_ONE = 3; // 三带一
    public static final int THREE_NO_AND = 4; // 三不带
    public static final int SHUN_ZI = 5; // 顺子类型
    public static final int AIRCRAFT = 8; // 飞机类型
    public static final int DOUBLE_SHUN_ZI = 9; // 连对
    public static final int FOUR_AND_TWO = 10; // 四带二
    public static final int THREE_BOMB = 11; // 三炸
    public static final int TWO_BOMB = 12; // 二炸
    public static final int BOMB = 13; // 炸弹类型
    public static final int THREE_BOMB_FOUR = 14; // 三炸四张
    public static final int KING_BOMB = 15; // 王炸类型

    public static int[] getPorkerType(List<DDZPorker> porkers) {

        int type = UNKNOWN;
        int size = 0;

        switch (porkers.size()) {
            case 1:
                type = ONE;
                size = porkers.get(0).porkerSize;
                break;
            case 2:
                size = isDouble(porkers);
                if (size != UNKNOWN) {
                    if (porkers.get(0).porkerSize == 0) {
                        type = THREE_BOMB;
                    } else if (porkers.get(0).porkerSize == DDZPorker.TWO_SIZE) {
                        type = TWO_BOMB;
                    } else {
                        type = DOUBLE;
                    }

                } else if (isKingBomb(porkers)) {
                    size = DDZPorker.BIG_KING_SIZE / 2;
                    type = KING_BOMB;
                }
                break;
            case 3:
                if ((size = isThreeNoAnd(porkers)) != UNKNOWN) {
                    type = THREE_NO_AND;
                }
                break;

            case 4:
                if ((size = isBomb(porkers)) != UNKNOWN) {
                    if (porkers.get(0).porkerSize == 0) {
                        type = THREE_BOMB_FOUR;
                    } else {
                        type = BOMB;
                    }
                } else if ((size = isThreeAndOne(porkers)) != UNKNOWN) {
                    type = THREE_AND_ONE;
                } else if ((size = isShunZi(porkers)) != UNKNOWN) {
                    type = SHUN_ZI;
                } else if ((size = isDoubleShunZI(porkers)) != UNKNOWN) {
                    type = DOUBLE_SHUN_ZI;
                }
                break;
            default:

                if ((size = isShunZi(porkers)) != UNKNOWN) {
                    type = SHUN_ZI;
                } else if ((size = isDoubleShunZI(porkers)) != UNKNOWN) {
                    type = DOUBLE_SHUN_ZI;
                } else if ((size = isAircraft(porkers)) != UNKNOWN) {
                    type = AIRCRAFT;
                } else if ((size = isFourAndTwo(porkers)) != UNKNOWN) {
                    type = FOUR_AND_TWO;
                }

                break;

        }

        return new int[]{type, size};

    }


    public static String typeToString(int[] typeAndSize) {

        String str = "牌型不正确";

        switch (typeAndSize[0]) {
            case ONE:
                str = DDZPorker.getSizeStr(typeAndSize[1]);
                break;
            case DOUBLE:
                str = "对" + DDZPorker.getSizeStr(typeAndSize[1]);
                break;
            case THREE_AND_ONE:
                str = "三带一";
                break;

            case THREE_NO_AND:
                str = "三个" + DDZPorker.getSizeStr(typeAndSize[1]);
                break;

            case SHUN_ZI:
                str = "顺子";
                break;
            case BOMB:
                str = "炸蛋";
                break;

            case KING_BOMB:
                str = "王炸";
                break;

            case DOUBLE_SHUN_ZI:
                str = "连对";
                break;
            case AIRCRAFT:
                str = "飞机" + typeAndSize[1];
                break;
            case THREE_BOMB:
                str = "三炸";
                break;
            case THREE_BOMB_FOUR:
                str = "三炸四张";
                break;
            case TWO_BOMB:
                str = "二炸";
                break;
            case FOUR_AND_TWO:
                str = "四带二";
                break;
        }

        return str;

    }


    public static boolean comparablePorker(List<DDZPorker> current, List<DDZPorker> last) {
        int[] lastType = getPorkerType(last);
        int[] currentType = getPorkerType(current);

        return comparablePorker(currentType, lastType);

    }

    public static boolean comparablePorker(int currentType[], int lastType[]) {

        boolean isBig = false;

        if (currentType[0] >= THREE_BOMB && lastType[0] < THREE_BOMB) {
            return true;
        }

        if (lastType[0] >= THREE_BOMB && currentType[0] > lastType[0]) {
            return true;
        }


        switch (lastType[0]) {
            case ONE:
            case DOUBLE:
            case THREE_AND_ONE:
            case THREE_NO_AND:
            case SHUN_ZI:
            case AIRCRAFT:
            case FOUR_AND_TWO:
            case DOUBLE_SHUN_ZI:
                isBig = currentType[0] == lastType[0] && currentType[1] > lastType[1];
                break;
            case THREE_BOMB:
                isBig = currentType[0] > THREE_BOMB;
                break;
            case TWO_BOMB:
                isBig = currentType[0] > TWO_BOMB;
                break;
            case BOMB:
                isBig = (currentType[0] == BOMB && currentType[1] > lastType[1]);
                break;
            case THREE_BOMB_FOUR:
                isBig = currentType[0] == KING_BOMB;
                break;
            case KING_BOMB:
                isBig = false;
                break;

        }


        return isBig;

    }


    /**
     * 牌是否为对子
     */
    public static int isDouble(List<DDZPorker> porkers) {

        if (porkers.size() != 2) {
            return UNKNOWN;
        }
        if (porkers.get(0).porkerSize == porkers.get(1).porkerSize) {
            return porkers.get(0).porkerSize;
        }

        return UNKNOWN;

    }

    /**
     * 是否为四带二
     */
    public static int isFourAndTwo(List<DDZPorker> porkers) {
        if (porkers.size() != 6) {
            return UNKNOWN;
        }


        int xiangTong = 1;
        int temp = -1;
        for (int i = 0; i < porkers.size(); i++) {

            if (temp == porkers.get(i).porkerSize) {
                xiangTong++;
            } else {
                xiangTong = 1;
            }

            if (xiangTong == 4) {
                return porkers.get(i).porkerSize * xiangTong;
            }

            temp = porkers.get(i).porkerSize;
        }


        return UNKNOWN;
    }

    /**
     * 牌是否为飞机
     */
    public static int isAircraft(List<DDZPorker> porkers) {

        if (porkers.size() < 6) {
            return UNKNOWN;
        }

        List<Integer> number = new ArrayList<>();
        // 连续三张一样的放进集合 比如 333444 或者 444555...
        for (int i = 0; i < porkers.size(); i++) {

            if (number.size() > 0) {

                if (porkers.get(i).porkerSize == number.get(number.size() - 1)) {
                    if (number.size() - 3 > -1) {
                        if (porkers.get(i).porkerSize == number.get(number.size() - 3)) {
                            number.remove(number.size() - 3);
                            number.add(porkers.get(i).porkerSize);
                        } else if (porkers.get(i).porkerSize == number.get(number.size() - 2)) {
                            number.add(porkers.get(i).porkerSize);
                        } else {
                            number.add(porkers.get(i).porkerSize);
                        }
                    } else if (number.size() - 2 > -1) {
                        if (porkers.get(i).porkerSize == number.get(number.size() - 2)) {
                            number.add(porkers.get(i).porkerSize);
                        }
                    } else {
                        number.add(porkers.get(i).porkerSize);
                    }

                } else if (number.size() >= 3 && number.get(number.size() - 1) == number.get(number.size() - 3) &&
                        number.get(number.size() - 1) - porkers.get(i).porkerSize == 1 && porkers.size() - i >= 3
                        && porkers.get(i).porkerSize == porkers.get(i + 2).porkerSize) {
                    number.add(porkers.get(i).porkerSize);

                } else if (number.size() <= 3) {
                    number.clear();
                    number.add(porkers.get(i).porkerSize);
                }

            } else {
                number.add(porkers.get(i).porkerSize);
            }


        }


        if (porkers.size() - number.size() == 0 || porkers.size() - number.size() == number.size() / 3) {
            int size = 0;
            for (int i = 0; i < number.size(); i += 3) {
                size += number.get(i);
            }
            return size + (number.size() / 3 * 100);
        } else {

            int sanPan = number.size() / 3 - porkers.size() - number.size();

            if (sanPan % 3 == 0) {
                int size = 0;
                int length = number.size() / 3 - sanPan;
                for (int i = 0; i < length; i += 3) {
                    size += number.get(i);
                }
                return size + (length * 100);
            }


        }


        return UNKNOWN;

    }

    /**
     * 牌是否为飞机
     */
    public static boolean isAircraft2(List<DDZPorker> porkers) {

        boolean isAircraftNoAnd = true; //是否为飞机不带

        for (int i = 0; i < porkers.size(); i += 3) {
            if (i + 2 >= porkers.size()) {
                isAircraftNoAnd = false;
                break;
            }

            if (i + 5 >= porkers.size()) {
                continue;

            }

            if (porkers.get(i).porkerSize == porkers.get(i + 1).porkerSize && porkers.get(i).porkerSize == porkers.get(i + 2).porkerSize
                    && porkers.get(i).porkerSize - porkers.get(i + 5).porkerSize == 1) {
                continue;
            }

            isAircraftNoAnd = false;

        }


        if (isAircraftNoAnd) {
            return true;
        }


        // 飞机有附带的牌

        for (int i = 0; i < porkers.size(); i++) {
            if (i + 2 >= porkers.size()) {
                return false;
            }


            if (porkers.get(i).porkerSize == porkers.get(i + 1).porkerSize && porkers.get(i).porkerSize == porkers.get(i + 2).porkerSize) {

                int aircraftCount = 1;
                for (int j = i; j < porkers.size(); j += 3) {

                    if (j + 5 >= porkers.size()) {
                        if (aircraftCount == 1 || porkers.size() != aircraftCount * 3 + aircraftCount) {
                            return false;
                        }


                        continue;
                    }

                    DDZPorker dd1 = porkers.get(j);
                    DDZPorker dd2 = porkers.get(j + 1);
                    DDZPorker dd3 = porkers.get(j + 2);
                    DDZPorker dd4 = porkers.get(j + 3);
                    DDZPorker dd5 = porkers.get(j + 4);
                    DDZPorker dd6 = porkers.get(j + 5);


                    if (dd1.porkerSize == dd2.porkerSize &&
                            dd1.porkerSize == dd3.porkerSize &&
                            dd1.porkerSize - porkers.get(j + 3).porkerSize == 1 &&
                            dd1.porkerSize - dd4.porkerSize == 1 && dd4.porkerSize == dd5.porkerSize && dd4.porkerSize == dd6.porkerSize) {

                        aircraftCount++;

                        continue;
                    } else if (aircraftCount != 1 && porkers.size() == aircraftCount * 3 + aircraftCount) {
                        continue;
                    }

                    return false;
                }


                break;

            }


        }


        return true;

    }

    /**
     * 牌是否为连队
     */
    public static int isDoubleShunZI(List<DDZPorker> porkers) {

        if (porkers.size() < 4 || porkers.get(0).porkerSize == DDZPorker.TWO_SIZE || porkers.size() % 2 != 0) {
            return UNKNOWN;
        }

        // 是否全部为对子
        for (int i = 0; i < porkers.size(); i += 2) {
            if (i + 1 >= porkers.size()) {
                continue;
            }

            if (porkers.get(i).porkerSize == porkers.get(i + 1).porkerSize) {
                continue;
            }
            return UNKNOWN;

        }
        int size = 0;
        // 是否连续
        for (int i = 0; i < porkers.size(); i += 2) {

            if (i + 2 >= porkers.size()) {
                continue;
            }
            if (porkers.get(i).porkerSize - porkers.get(i + 2).porkerSize == 1) {
                size += (porkers.get(i).porkerSize * 2);
                continue;
            }

            return UNKNOWN;
        }
        return size;

    }


    /**
     * 牌是否为三不带
     */
    public static int isThreeNoAnd(List<DDZPorker> porkers) {

        if (porkers.size() != 3) {
            return UNKNOWN;
        }

        if (((porkers.get(0).porkerSize == porkers.get(1).porkerSize &&
                porkers.get(0).porkerSize == porkers.get(2).porkerSize))) {

            return porkers.get(0).porkerSize;
        }

        return UNKNOWN;
    }

    /**
     * 牌是否为三带一
     */
    public static int isThreeAndOne(List<DDZPorker> porkers) {

        if (porkers.size() != 4) {
            return UNKNOWN;
        }

        if (((porkers.get(0).porkerSize == porkers.get(1).porkerSize &&
                porkers.get(0).porkerSize == porkers.get(2).porkerSize &&
                porkers.get(0).porkerSize != porkers.get(3).porkerSize))) {

            return porkers.get(0).porkerSize * 3;
        } else if ((porkers.get(0).porkerSize != porkers.get(1).porkerSize &&
                porkers.get(1).porkerSize == porkers.get(2).porkerSize &&
                porkers.get(1).porkerSize == porkers.get(3).porkerSize)) {
            return porkers.get(1).porkerSize * 3;
        }

        return UNKNOWN;
    }


    /**
     * 牌是否为顺子
     */
    public static int isShunZi(List<DDZPorker> porkers) {

        if (porkers.size() < 4) {
            return UNKNOWN;
        }

        int size = 0;

        for (int i = 0; i < porkers.size(); i++) {
            if (i + 1 >= porkers.size()) {
                continue;
            }

            if (porkers.get(i).porkerSize != DDZPorker.TWO_SIZE && porkers.get(i).porkerSize != DDZPorker.SMALL_KING_SIZE &&
                    porkers.get(i).porkerSize != DDZPorker.BIG_KING_SIZE &&
                    porkers.get(i).porkerSize - porkers.get(i + 1).porkerSize == 1) {
                size += porkers.get(i).porkerSize;
                continue;
            }

            size = UNKNOWN;
            break;
        }
        return size;

    }


    /**
     * 是否为炸蛋
     */


    public static int isBomb(List<DDZPorker> porkers) {
        if (porkers.size() != 4) {
            return UNKNOWN;
        }

        if (porkers.get(0).porkerSize == porkers.get(1).porkerSize && porkers.get(0).porkerSize == porkers.get(2).porkerSize
                && porkers.get(0).porkerSize == porkers.get(3).porkerSize) {
            if (porkers.get(0).porkerSize == 0) {
                return 13 * 4;
            }
            return porkers.get(0).porkerSize * 4;
        }

        return UNKNOWN;
    }


    /**
     * 是否为王炸
     */


    public static boolean isKingBomb(List<DDZPorker> porkers) {

        return porkers.size() == 2 && porkers.get(0).porkerSize == DDZPorker.BIG_KING_SIZE && porkers.get(0).porkerSize > porkers.get(1).porkerSize;
    }


}
