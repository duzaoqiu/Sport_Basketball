package com.bench.android.core.net.http.base;

import org.json.JSONObject;

/**
 * Created by zhouyi on 2016/6/28 21:23.
 */
public interface OnHttpResponseListener {
    /**
     * 请求开始
     */
    void executorStart();

    void executorSuccess(RequestContainer request, JSONObject jo);

    void executorFalse(RequestContainer request, JSONObject jo);

    void executorFailed(RequestContainer request, int code, Exception e);

    /**
     * 请求结束
     * @param stateCode 状态码
     */
    void executorFinish(int stateCode);

}
