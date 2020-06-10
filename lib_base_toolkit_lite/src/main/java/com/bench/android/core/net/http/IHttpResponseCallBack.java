package com.bench.android.core.net.http;

/**
 * 增加请求完成回调
 * Created by lingjiu on 2018/10/22.
 */
public interface IHttpResponseCallBack<T> extends IHttpResponseListener<T> {

    void onStartRequest();

    void onFinishRequest(int stateCode);
}
