package com.bench.android.core.util.bean;

import org.json.JSONObject;

/**
 * Created by danao on 2019/2/21.
 */
public class ErrorBean {

    public ErrorEntity error;

    public boolean success;

    public String nowDate;

    public String detailMessage;

    public boolean jumpLogin;

    public ErrorBean(JSONObject jo) {
        this.detailMessage = jo.optString("detailMessage");
        this.jumpLogin = jo.optBoolean("jumpLogin");
        if (jo.has("error")) {
            JSONObject errorJo = jo.optJSONObject("error");
            this.error = new ErrorEntity(errorJo);
        }
    }

    public boolean isJumpLogin() {
        return jumpLogin;
    }

    public void setError(ErrorEntity error) {
        this.error = error;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setNowDate(String nowDate) {
        this.nowDate = nowDate;
    }

    public ErrorEntity getError() {
        return error;
    }

    public boolean getSuccess() {
        return success;
    }

    public String getNowDate() {
        return nowDate;
    }

    public String getDetailMessage() {
        return detailMessage;
    }

    public void setDetailMessage(String detailMessage) {
        this.detailMessage = detailMessage;
    }

    public static class ErrorEntity {

        public String message;
        public String name;

        public ErrorEntity(JSONObject jo) {
            if (jo != null) {
                message = jo.optString("message");
                name = jo.optString("name");
            }
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getMessage() {
            return message;
        }

        public String getName() {
            return name;
        }
    }
}
