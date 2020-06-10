package com.bench.android.core.util.bean;

import java.io.Serializable;

/**
 * Created by danao on 2018/7/9.
 */
public class BaseResponse implements Serializable {

    public boolean success;
    private String nowDate;
    private long nowTimestamp;
    private boolean jumpLogin;
    private String detailMessage;
    public NormalObjectBean error;
    private PaginatorBean paginator;

    public long getNowTimestamp() {
        return nowTimestamp;
    }

    public void setNowTimestamp(long nowTimestamp) {
        this.nowTimestamp = nowTimestamp;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getNowDate() {
        return nowDate;
    }

    public void setNowDate(String nowDate) {
        this.nowDate = nowDate;
    }

    public boolean isJumpLogin() {
        return jumpLogin;
    }

    public void setJumpLogin(boolean jumpLogin) {
        this.jumpLogin = jumpLogin;
    }

    public NormalObjectBean getError() {
        return error;
    }

    public void setError(NormalObjectBean error) {
        this.error = error;
    }

    public PaginatorBean getPaginator() {
        return paginator;
    }

    public void setPaginator(PaginatorBean paginator) {
        this.paginator = paginator;
    }

    public String getDetailMessage() {
        return detailMessage;
    }

    public void setDetailMessage(String detailMessage) {
        this.detailMessage = detailMessage;
    }
}
