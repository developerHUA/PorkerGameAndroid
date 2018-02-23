package com.yunke.xiaovo.bean;

import java.util.List;

public class PlayPorker {

    private int type;
    private int porkerSize;
    private List<DDZPorker> porkerList;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getPorkerSize() {
        return porkerSize;
    }

    public void setPorkerSize(int porkerSize) {
        this.porkerSize = porkerSize;
    }

    public List<DDZPorker> getPorkerList() {
        return porkerList;
    }

    public void setPorkerList(List<DDZPorker> porkerList) {
        this.porkerList = porkerList;
    }
}
