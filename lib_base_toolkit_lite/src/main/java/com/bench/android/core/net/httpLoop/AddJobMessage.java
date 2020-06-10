package com.bench.android.core.net.httpLoop;


import com.bench.android.core.net.http.base.RequestContainer;

public class AddJobMessage {

    //增加一个任务
    public static final int ADD_JOB = 1;

    //删除一个任务
    public static final int REMOVE_JOB = 2;

    //任务修改参数
    public static final int CHANGE_PARAMS_JOB = 3;

    //删除全部任务
    public static final int REMOVE_ALL_JOB = 4;

    private int duration;
    private RequestContainer message;
    private int what;

    public AddJobMessage(RequestContainer message, int duration, int what) {
        this.message = message;
        this.what = what;
        this.duration = duration;
    }

    public AddJobMessage(RequestContainer message, int what) {
        this.message = message;
        this.what = what;
    }

    public RequestContainer getMessage() {
        return message;
    }

    public void setMessage(RequestContainer message) {
        this.message = message;
    }

    public int getWhat() {
        return what;
    }

    public void setWhat(int what) {
        this.what = what;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
