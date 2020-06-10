package com.bench.android.core.net.http.processor;

import org.json.JSONObject;

/**
 * Created by lingjiu on 2019/1/24.
 */
public interface IHttpResponseHandler {

    void executorSuccess(JSONObject jo);

    void executorFalse(JSONObject jo);

    void executorFailed(int code, Exception e);
}
