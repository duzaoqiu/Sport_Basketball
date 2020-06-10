package com.bench.android.core.net.http.httphelpinterceptor;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 拦截器常用参数
 *
 * @author zhouyi
 */
public class InterceptorConstants {

    /**
     * 重试JSON
     */
    public final static JSONObject RETRY_JSON;


    /**
     * 执行失败，需要通知UI，连接失败
     */
    public final static JSONObject FAILED_JSON;


    static {
        RETRY_JSON = new JSONObject();
        FAILED_JSON = new JSONObject();
        try {
            RETRY_JSON.put("name", "RETRY");
            FAILED_JSON.put("name", "FAILED");
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
}
