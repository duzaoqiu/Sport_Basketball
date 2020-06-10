package com.bench.android.core.arch.common.api;

import com.bench.android.core.net.http.RequestFormBody;

public interface OnFinishRequest {
    /**
     * 如果执行成功之后，可以接下去的请求，则需要调用onFinish
     * @param body
     */
    void onFinish(RequestFormBody body);

    /**
     * 如果执行失败，不想接下去的请求，可以调用onFailed
     */
    void failed(RequestFormBody body);
}
