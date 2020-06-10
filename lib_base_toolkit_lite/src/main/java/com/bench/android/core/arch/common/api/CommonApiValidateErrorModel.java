package com.bench.android.core.arch.common.api;

/**
 * 验证错误返回消息的模型
 * @author zhouyi
 */
public class CommonApiValidateErrorModel {

    /**
     * 返回的消息
     */
    private String msg;

    /**
     * 接口名
     */
    private String requestName;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getRequestName() {
        return requestName;
    }

    public void setRequestName(String requestName) {
        this.requestName = requestName;
    }
}
