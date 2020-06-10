package com.bench.android.core.net.http;


import com.bench.android.core.net.http.base.RequestContainer;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;

/**
 * Created by qiaomu on 2017/6/7.
 */

public interface IHttpResponseListener<T> extends Type {
    void onSuccess(RequestContainer request, T t) throws JSONException;

    void onFailed(RequestContainer request, JSONObject jsonObject, boolean netFailed) throws Exception;

}
