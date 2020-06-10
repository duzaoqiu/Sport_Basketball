package com.bench.android.core.net.http.processor;

import com.bench.android.core.net.http.base.RequestContainer;

/**
 * Created by lingjiu on 2019/1/24.
 */
public class VolleyHttpHelper implements IHttpProcessor {
    /**
     * post请求
     *
     * @param requestContainer
     * @param callback
     * @param activityName
     */
    @Override
    public void httpPost(RequestContainer requestContainer, IHttpResponseHandler callback, String activityName) {

    }

    /**
     * 取消对应tag的请求
     *
     * @param tag
     */
    @Override
    public void cancelHttpCall(String tag) {

    }

    /**
     * 取消所有请求
     */
    @Override
    public void cancelAllHttpCall() {

    }


    /*private final RequestQueue requestQueue;

    private VolleyHttpHelper() {
        requestQueue = Volley.newRequestQueue(null);
    }

    private static VolleyHttpHelper volleyHttpHelper;

    public static VolleyHttpHelper getDefault() {
        if (volleyHttpHelper == null) {
            volleyHttpHelper = new VolleyHttpHelper();
        }
        return volleyHttpHelper;
    }

    @Override
    public void httpPost(RequestContainer requestContainer, final IHttpResponseHandler mCallback, String activityName) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, "http://m.weather.com.cn/data/101010100.html",
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getBoolean("success")) {
                                mCallback.executorSuccess(response);
                            } else {
                                mCallback.executorFalse(response);
                            }
                            Log.d("TAG", response.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", error.getMessage(), error);
                //mCallback.executorFailed();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }*/
}
