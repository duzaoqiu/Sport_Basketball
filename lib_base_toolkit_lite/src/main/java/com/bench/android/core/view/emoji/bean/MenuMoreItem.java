package com.bench.android.core.view.emoji.bean;

public class MenuMoreItem {
    private String txt;
    private int imgRes;

    public MenuMoreItem(String txt, int imgRes) {
        this.txt = txt;
        this.imgRes = imgRes;
    }

    public String getTxt() {
        return txt;
    }

    public void setTxt(String txt) {
        this.txt = txt;
    }

    public int getImgRes() {
        return imgRes;
    }

    public void setImgRes(int imgRes) {
        this.imgRes = imgRes;
    }
}