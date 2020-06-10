package org.opencv.samples.facedetect.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 视频回放数据
 *
 * @author zhouyi
 */
public class VideoPlayBackModel {

    /**
     * 帧数
     */
    private HashMap<Integer, FrameData> frameMap;

    /**
     * 区域分数图
     */
    private List<PointLocationData> areaList;

    /**
     * 视频长度
     */
    private int videoDuration;

    /**
     * 坐标基准宽度
     */
    private int pointWidth;

    /**
     * 坐标基准高度
     */
    private int pointHeight;


    /**
     * 总投数
     */
    private int scoreAllShootCount;

    /**
     * 总投中次数
     */
    private int scoreAllShootWin;

    /**
     * 2分总投数
     */
    private int score2ShootCount;

    /**
     * 2分总投中次数
     */
    private int score2ShootWin;

    /**
     * 3分总投数
     */
    private int score3ShootCount;

    /**
     * 3分总投中次数
     */
    private int score3ShootWin;

    /**
     * 总帧数
     */
    private int frameCount;

    /**
     * 点位分数
     */
    public static class PointLocationData {

        /**
         * 出手次数
         */
        public int shootCount;

        /**
         * 投中次数
         */
        public int winCount;

        @Override
        public String toString() {
            return "PointLocationData{" +
                    "shootCount=" + shootCount +
                    ", winCount=" + winCount +
                    '}';
        }
    }

    public List<FrameData> getFrameList() {
        if(frameMap==null){
            return null;
        }
        List<FrameData> list = new ArrayList<>();
        for (Integer i : frameMap.keySet()) {
            list.add(frameMap.get(i));
        }
        return list;
    }

    public HashMap<Integer, FrameData> getFrameMap() {
        return frameMap;
    }

    public void setFrameMap(HashMap<Integer, FrameData> frameMap) {
        this.frameMap = frameMap;
    }

    public List<PointLocationData> getData() {
        return areaList;
    }

    public void setData(List<PointLocationData> data) {
        this.areaList = data;
    }

    public int getVideoDuration() {
        return videoDuration;
    }

    public void setVideoDuration(int videoDuration) {
        this.videoDuration = videoDuration;
    }

    public int getPointWidth() {
        return pointWidth;
    }

    public void setPointWidth(int pointWidth) {
        this.pointWidth = pointWidth;
    }

    public int getPointHeight() {
        return pointHeight;
    }

    public void setPointHeight(int pointHeight) {
        this.pointHeight = pointHeight;
    }

    public int getScoreAllShootCount() {
        return scoreAllShootCount;
    }

    public void setScoreAllShootCount(int scoreAllShootCount) {
        this.scoreAllShootCount = scoreAllShootCount;
    }

    public int getScoreAllShootWin() {
        return scoreAllShootWin;
    }

    public void setScoreAllShootWin(int scoreAllShootWin) {
        this.scoreAllShootWin = scoreAllShootWin;
    }

    public int getScore2ShootCount() {
        return score2ShootCount;
    }

    public void setScore2ShootCount(int score2ShootCount) {
        this.score2ShootCount = score2ShootCount;
    }

    public int getScore2ShootWin() {
        return score2ShootWin;
    }

    public void setScore2ShootWin(int score2ShootWin) {
        this.score2ShootWin = score2ShootWin;
    }

    public int getScore3ShootCount() {
        return score3ShootCount;
    }

    public void setScore3ShootCount(int score3ShootCount) {
        this.score3ShootCount = score3ShootCount;
    }

    public int getScore3ShootWin() {
        return score3ShootWin;
    }

    public void setScore3ShootWin(int score3ShootWin) {
        this.score3ShootWin = score3ShootWin;
    }

    public static class FrameData implements Parcelable {

        /**
         * 是否投中
         */
        public boolean win;

        /**
         * 第几帧数据
         */
        public int frameCount;

        /**
         * x轴
         */
        public int x;

        /**
         * y轴
         */
        public int y;

        public boolean isWin() {
            return win;
        }

        public void setWin(boolean win) {
            this.win = win;
        }

        public int getFrameCount() {
            return frameCount;
        }

        public void setFrameCount(int frameCount) {
            this.frameCount = frameCount;
        }

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


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeByte(this.win ? (byte) 1 : (byte) 0);
            dest.writeInt(this.frameCount);
            dest.writeInt(this.x);
            dest.writeInt(this.y);
        }

        public FrameData() {
        }

        protected FrameData(Parcel in) {
            this.win = in.readByte() != 0;
            this.frameCount = in.readInt();
            this.x = in.readInt();
            this.y = in.readInt();
        }

        public static final Parcelable.Creator<FrameData> CREATOR = new Parcelable.Creator<FrameData>() {
            @Override
            public FrameData createFromParcel(Parcel source) {
                return new FrameData(source);
            }

            @Override
            public FrameData[] newArray(int size) {
                return new FrameData[size];
            }
        };
    }

    public int getFrameCount() {
        return frameCount;
    }

    public void setFrameCount(int frameCount) {
        this.frameCount = frameCount;
    }
}
