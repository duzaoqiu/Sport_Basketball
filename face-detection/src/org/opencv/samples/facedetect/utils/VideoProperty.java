package org.opencv.samples.facedetect.utils;

/**
 * 视频属性
 * @author zhouyi 
 */
public class VideoProperty {

    /**
     * 帧数
     */
    private int frameRate;

    /**
     * 总共长度
     */
    private long duration;

    public int getFrameRate() {
        return frameRate;
    }

    public void setFrameRate(int frameRate) {
        this.frameRate = frameRate;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
