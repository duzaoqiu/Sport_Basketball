package org.opencv.samples.facedetect.utils;

import com.bench.android.core.net.http.params.AppParam;
import com.bench.android.core.net.http.params.BaseRequestHttpName;

public enum UrlEnum implements BaseRequestHttpName {

    USER_LOGIN_SEND_SMS_ACK("USER_LOGIN_SEND_SMS_ACK", AppParam.S_URL),

    USER_LOGIN("USER_LOGIN", AppParam.S_URL),

    TRAIN_RECORD_CREATE("TRAIN_RECORD_CREATE", AppParam.S_URL),

    TRAIN_RECORD_COMPLETE("TRAIN_RECORD_COMPLETE", AppParam.S_URL),

    TRAIN_RECORD_QUERY("TRAIN_RECORD_QUERY", AppParam.S_URL),

    TRAIN_RECORD_STATISTICS_QUERY("TRAIN_RECORD_STATISTICS_QUERY", AppParam.S_URL),

    TRAIN_RECORD_SHARE("TRAIN_RECORD_SHARE", AppParam.S_URL),

    SET_USER_LOGIN_NAME("SET_USER_LOGIN_NAME", AppParam.S_URL),

    MODIFY_STAR("MODIFY_STAR", AppParam.S_URL),

    SCAN("SCAN", AppParam.S_URL),

    IDEA("IDEA", AppParam.S_URL),

    GET_STAR_LIST("GET_STAR_LIST", AppParam.S_URL);

    private String name;
    private String url;

    UrlEnum(String name, String url) {
        this.name = name;
        this.url = url;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getUrl() {
        return url;
    }
}
