package com.bench.android.core.util.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 通用类型，主要用于Json对象里面包含（name,message）
 * Created by zhouyi on 2016/3/4 16:03.
 */
public class NormalObjectBean implements Parcelable,IHttpResponseBean {

    private String name;

    private String message;

    private int value;

    public NormalObjectBean(String name, String message) {
        this.name = name;
        this.message = message;
    }

    public NormalObjectBean() {}

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

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.message);
        dest.writeInt(this.value);
    }

    protected NormalObjectBean(Parcel in) {
        this.name = in.readString();
        this.message = in.readString();
        this.value = in.readInt();
    }

    public static final Creator<NormalObjectBean> CREATOR = new Creator<NormalObjectBean>() {
        @Override
        public NormalObjectBean createFromParcel(Parcel source) {
            return new NormalObjectBean(source);
        }

        @Override
        public NormalObjectBean[] newArray(int size) {
            return new NormalObjectBean[size];
        }
    };
}
