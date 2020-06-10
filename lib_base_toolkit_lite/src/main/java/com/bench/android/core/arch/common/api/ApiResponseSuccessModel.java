package com.bench.android.core.arch.common.api;

import com.bench.android.core.net.http.base.RequestContainer;

/**
 * 通用api回调成功数据
 * @author zhouyi
 */
public class ApiResponseSuccessModel<T> {

    /**
     * 处理过的数据
     */
    public T data;

    /**
     * 通知触发doNext的操作或者failed操作
     */
    public BaseCommonApi api;

    /**
     * 当前请求成功之后request
     */
    public RequestContainer request;

    public ApiResponseSuccessModel(T data, BaseCommonApi api, RequestContainer request) {
        this.data = data;
        this.api = api;
        this.request = request;
    }
}
