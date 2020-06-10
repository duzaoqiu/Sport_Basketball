package com.bench.android.core.net.http;

import com.bench.android.core.net.http.base.RequestContainer;

import org.json.JSONObject;

/**
 * @author zhouyi
 * 拦截器
 */
public interface HttpRequestInterceptor {

    /**
     * 服务端返回true
     *
     * @param requestContainer
     * @param jo
     * @return
     */
    JSONObject onResponseSuccess(RequestContainer requestContainer, JSONObject jo);

    /**
     * 服务端返回false
     *
     * @param requestContainer
     * @param jo
     * @return
     */
    JSONObject onResponseFalse(RequestContainer requestContainer, JSONObject jo);

    /**
     * 发起请求拦截
     * @param requestContainer
     * @return
     */
    RequestContainer postRequest(RequestContainer requestContainer);
}
