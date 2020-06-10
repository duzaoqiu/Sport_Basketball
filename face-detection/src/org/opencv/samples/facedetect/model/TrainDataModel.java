package org.opencv.samples.facedetect.model;

import java.util.List;

/**
 * 训练数据模型
 *
 * @author zhouyi
 */
public class TrainDataModel {
    private int date;
    private List<VideoPlayBackModel.FrameData> frameData;
    private VideoPlayBackModel model;

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public List<VideoPlayBackModel.FrameData> getFrameData() {
        return frameData;
    }

    public void setFrameData(List<VideoPlayBackModel.FrameData> frameData) {
        this.frameData = frameData;
    }

    public VideoPlayBackModel getModel() {
        return model;
    }

    public void setModel(VideoPlayBackModel model) {
        this.model = model;
    }
}
