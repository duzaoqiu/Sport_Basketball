package com.bench.android.core.net.http.base;

/**
 * Created by zhouyi on 2016/6/28 21:23.
 */
public interface OnHttpResponseWithErrorCodeListener extends OnHttpResponseListener {

    /**
     * 请求结束
     * @param isAllRequestSuccess
     * @param errorCode
     */
    void executorFinish(boolean isAllRequestSuccess,int errorCode);

}
