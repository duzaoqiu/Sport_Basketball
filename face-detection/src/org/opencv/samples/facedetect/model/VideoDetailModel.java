package org.opencv.samples.facedetect.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class VideoDetailModel implements   Parcelable {

    public String imgUrl;
    public String videoUrl;
    public String title;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.imgUrl);
        dest.writeString(this.videoUrl);
        dest.writeString(this.title);
    }

    public VideoDetailModel() {
    }

    protected VideoDetailModel(Parcel in) {
        this.imgUrl = in.readString();
        this.videoUrl = in.readString();
        this.title = in.readString();
    }

    public static final Creator<VideoDetailModel> CREATOR = new Creator<VideoDetailModel>() {
        @Override
        public VideoDetailModel createFromParcel(Parcel source) {
            return new VideoDetailModel(source);
        }

        @Override
        public VideoDetailModel[] newArray(int size) {
            return new VideoDetailModel[size];
        }
    };
}
