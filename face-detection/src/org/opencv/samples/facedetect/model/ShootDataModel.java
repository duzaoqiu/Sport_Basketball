package org.opencv.samples.facedetect.model;

/**
 * 投篮数据
 * @author zhouyi
 */
public class ShootDataModel {

    private int x;
    private int y;
    private boolean win;

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean isWin() {
        return win;
    }

    public void setWin(boolean win) {
        this.win = win;
    }
}
