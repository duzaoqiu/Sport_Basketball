package com.bench.android.core.net.http.processor;


import com.bench.android.core.net.http.base.RequestContainer;
import com.bench.android.core.net.http.interceptor.HttpParamsInterceptor;

import java.util.ArrayList;
import java.util.List;

/**
 * 网络处理类
 * Created by lingjiu on 2019/1/24.
 */
public class HttpUtil implements IHttpProcessor {
    private static HttpUtil httpUtil;
    private IHttpProcessor httpProcessor;
    private List<HttpParamsInterceptor> httpInterceptors;

    private HttpUtil() {
        httpInterceptors = new ArrayList<>();
    }

    public static HttpUtil getDefault() {
        if (httpUtil == null) {
            httpUtil = new HttpUtil();
        }
        return httpUtil;
    }

    /**
     * 初始化
     *
     * @param httpProcessor
     */
    public void init(IHttpProcessor httpProcessor) {
        this.httpProcessor = httpProcessor;
    }

    /**
     * 全局添加参数拦截器
     * 可用做签名,埋点,默认域名配置,...
     * ps:签名参数要添加在最后
     *
     * @param httpInterceptors
     */
    public void addHttpInterceptors(HttpParamsInterceptor... httpInterceptors) {
        for (HttpParamsInterceptor httpInterceptor : httpInterceptors) {
            this.httpInterceptors.add(httpInterceptor);
        }
    }

    public void httpPost(RequestContainer requestContainer, IHttpResponseHandler callback) {
        httpPost(requestContainer, callback, null);
    }

    @Override
    public void httpPost(RequestContainer requestContainer, IHttpResponseHandler callback, String activityName) {
        if (httpProcessor == null)
            throw new RuntimeException("请先初始化init() httpProcessor ");
        httpProcessor.httpPost(requestContainer, callback, activityName);
    }

    /**
     * 执行网络请求前的拦截器
     *
     * @hide 外面不需要调用该方法
     */
    public void executeInterceptors(RequestContainer requestContainer) {
        if (httpInterceptors.size() > 0) {
            for (HttpParamsInterceptor httpInterceptor : httpInterceptors) {
                httpInterceptor.intercept(requestContainer);
            }
        }
    }

    /**
     * 取消对应tag的请求
     *
     * @param tag
     */
    @Override
    public void cancelHttpCall(String tag) {
        if (httpProcessor != null) {
            httpProcessor.cancelHttpCall(tag);
        }
    }

    /**
     * 取消所有请求
     */
    @Override
    public void cancelAllHttpCall() {
        if (httpProcessor != null) {
            httpProcessor.cancelAllHttpCall();
        }
    }

}
