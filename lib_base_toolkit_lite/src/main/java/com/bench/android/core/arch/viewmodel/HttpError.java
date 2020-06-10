package com.bench.android.core.arch.viewmodel;

import com.bench.android.core.net.http.base.RequestContainer;

import org.json.JSONObject;

public class HttpError {

    public RequestContainer request;

    public JSONObject jsonObject;

    public boolean netFailed;

    public HttpError(RequestContainer request, JSONObject jsonObject, boolean netFailed) {
        this.request = request;
        this.jsonObject = jsonObject;
        this.netFailed = netFailed;
    }
}
