package org.opencv.samples.facedetect.model;

/**
 * 动态数据模型
 *
 * @author zhouyi
 */
public class DynamicModel {

    /**
     * 用户名
     */
    private String userName;

    /**
     * 用户头像地址
     */
    private String userLogoUrl;

    /**
     * 位置
     */
    private String location;

    /**
     * 发布时间
     */
    private String time;

    /**
     * 视频封面地址
     */
    private String videoCoverUrl;

    /**
     * 视频下载地址
     */
    private String videoUrl;

    /**
     * 回放数据
     */
    private VideoPlayBackModel playBackModel;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserLogoUrl() {
        return userLogoUrl;
    }

    public void setUserLogoUrl(String userLogoUrl) {
        this.userLogoUrl = userLogoUrl;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getVideoCoverUrl() {
        return videoCoverUrl;
    }

    public void setVideoCoverUrl(String videoCoverUrl) {
        this.videoCoverUrl = videoCoverUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public VideoPlayBackModel getPlayBackModel() {
        return playBackModel;
    }

    public void setPlayBackModel(VideoPlayBackModel playBackModel) {
        this.playBackModel = playBackModel;
    }
}
