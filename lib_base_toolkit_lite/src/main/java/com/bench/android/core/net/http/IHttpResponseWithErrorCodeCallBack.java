package com.bench.android.core.net.http;

/**
 * 增加请求完成回调
 * Created by lingjiu on 2018/10/22.
 */
public interface IHttpResponseWithErrorCodeCallBack<T> extends IHttpResponseCallBack<T> {


    /**
     * 是否所有请求成功
     * @param success
     * @param errorCode
     */
    void onFinishRequest(boolean success,int errorCode);
}
