package com.bench.android.core.net.http.processor;


import com.bench.android.core.net.http.base.RequestContainer;

/**
 * 网络处理顶层接口
 * Created by lingjiu on 2019/1/24.
 */
public interface IHttpProcessor {

    String HTTP_HEADER_APP_REFER = "appReferer";

    /**
     * post请求
     *
     * @param requestContainer
     * @param callback
     * @param activityName
     */
    void httpPost(RequestContainer requestContainer, IHttpResponseHandler callback, String activityName);

    /**
     * 取消对应tag的请求
     *
     * @param tag
     */
    void cancelHttpCall(String tag);

    /**
     * 取消所有请求
     */
    void cancelAllHttpCall();
}
