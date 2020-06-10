package com.bench.android.core.view.emoji.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by zhouyi on 2016/1/5 17:50.
 */
public class EmojiBean implements Parcelable {

    private String drawableName;

    private String name;

    private String message;

    private int resId;

    private String uploadMessage;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    public String getUploadMessage() {
        return uploadMessage;
    }

    public void setUploadMessage(String uploadMessage) {
        this.uploadMessage = uploadMessage;
    }

    public EmojiBean() {
    }

    public String getDrawableName() {
        return drawableName;
    }

    public void setDrawableName(String drawableName) {
        this.drawableName = drawableName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.drawableName);
        dest.writeString(this.name);
        dest.writeString(this.message);
        dest.writeInt(this.resId);
        dest.writeString(this.uploadMessage);
    }

    protected EmojiBean(Parcel in) {
        this.drawableName = in.readString();
        this.name = in.readString();
        this.message = in.readString();
        this.resId = in.readInt();
        this.uploadMessage = in.readString();
    }

    public static final Creator<EmojiBean> CREATOR = new Creator<EmojiBean>() {
        @Override
        public EmojiBean createFromParcel(Parcel source) {
            return new EmojiBean(source);
        }

        @Override
        public EmojiBean[] newArray(int size) {
            return new EmojiBean[size];
        }
    };
}
