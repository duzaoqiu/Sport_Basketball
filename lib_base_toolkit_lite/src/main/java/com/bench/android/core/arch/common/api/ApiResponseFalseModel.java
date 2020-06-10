package com.bench.android.core.arch.common.api;

import com.bench.android.core.net.http.base.RequestContainer;

import org.json.JSONObject;

/**
 * 调用api返回错误的model
 *
 * @author zhouyi
 */
public class ApiResponseFalseModel {

    /**
     * 处理过的数据
     */
    public JSONObject data;

    /**
     * 通知触发doNext的操作或者failed操作
     */
    public BaseCommonApi api;

    /**
     * 当前请求成功之后request
     */
    public RequestContainer request;

    /**
     * 是否网络错误
     */
    public boolean netFailed;

    /**
     * 如果是取消请求，请使用该构造函数
     * @param api
     * @param request
     */
    public ApiResponseFalseModel(BaseCommonApi api, RequestContainer request) {
        this.api = api;
        this.request = request;
    }

    /**
     * 如果是正常返回success=false,说明是网络连接正常，可以使用该构造器
     * @param data
     * @param api
     * @param request
     */
    public ApiResponseFalseModel(JSONObject data, BaseCommonApi api, RequestContainer request) {
        this.data = data;
        this.api = api;
        this.request = request;
    }

    /**
     * 如果是包含网络错误，请使用该构造器
     * @param data
     * @param api
     * @param request
     * @param netFailed
     */
    public ApiResponseFalseModel(JSONObject data, BaseCommonApi api, RequestContainer request, boolean netFailed) {
        this.data = data;
        this.api = api;
        this.request = request;
        this.netFailed = netFailed;
    }
}
