package org.opencv.samples.facedetect.activity;

import android.os.Parcel;
import android.os.Parcelable;

public class ItemData implements Parcelable {

    private String all;
    private String allRate;

    private String two;
    private String twoRate;

    private String three;
    private String threeRate;

    private int imageSrc;
    private int tableSrc;

    private String name;
    private String time;
    private String location;

    private String readNum;
    private String zanNum;

    private int logo;

    private String videoPath;

    public String getVideoPath() {
        return videoPath;
    }

    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }

    public int getLogo() {
        return logo;
    }

    public void setLogo(int logo) {
        this.logo = logo;
    }

    public String getAll() {
        return all;
    }

    public void setAll(String all) {
        this.all = all;
    }

    public String getAllRate() {
        return allRate;
    }

    public void setAllRate(String allRate) {
        this.allRate = allRate;
    }

    public String getTwo() {
        return two;
    }

    public void setTwo(String two) {
        this.two = two;
    }

    public String getTwoRate() {
        return twoRate;
    }

    public void setTwoRate(String twoRate) {
        this.twoRate = twoRate;
    }

    public String getThree() {
        return three;
    }

    public void setThree(String three) {
        this.three = three;
    }

    public String getThreeRate() {
        return threeRate;
    }

    public void setThreeRate(String threeRate) {
        this.threeRate = threeRate;
    }

    public int getImageSrc() {
        return imageSrc;
    }

    public void setImageSrc(int imageSrc) {
        this.imageSrc = imageSrc;
    }

    public int getTableSrc() {
        return tableSrc;
    }

    public void setTableSrc(int tableSrc) {
        this.tableSrc = tableSrc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getReadNum() {
        return readNum;
    }

    public void setReadNum(String readNum) {
        this.readNum = readNum;
    }

    public String getZanNum() {
        return zanNum;
    }

    public void setZanNum(String zanNum) {
        this.zanNum = zanNum;
    }


    public ItemData() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.all);
        dest.writeString(this.allRate);
        dest.writeString(this.two);
        dest.writeString(this.twoRate);
        dest.writeString(this.three);
        dest.writeString(this.threeRate);
        dest.writeInt(this.imageSrc);
        dest.writeInt(this.tableSrc);
        dest.writeString(this.name);
        dest.writeString(this.time);
        dest.writeString(this.location);
        dest.writeString(this.readNum);
        dest.writeString(this.zanNum);
        dest.writeInt(this.logo);
        dest.writeString(this.videoPath);
    }

    protected ItemData(Parcel in) {
        this.all = in.readString();
        this.allRate = in.readString();
        this.two = in.readString();
        this.twoRate = in.readString();
        this.three = in.readString();
        this.threeRate = in.readString();
        this.imageSrc = in.readInt();
        this.tableSrc = in.readInt();
        this.name = in.readString();
        this.time = in.readString();
        this.location = in.readString();
        this.readNum = in.readString();
        this.zanNum = in.readString();
        this.logo = in.readInt();
        this.videoPath = in.readString();
    }

    public static final Creator<ItemData> CREATOR = new Creator<ItemData>() {
        @Override
        public ItemData createFromParcel(Parcel source) {
            return new ItemData(source);
        }

        @Override
        public ItemData[] newArray(int size) {
            return new ItemData[size];
        }
    };
}
