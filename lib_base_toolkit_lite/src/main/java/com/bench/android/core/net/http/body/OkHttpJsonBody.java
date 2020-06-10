package com.bench.android.core.net.http.body;

import com.bench.android.core.net.http.okhttp.ProgressRequestBody;
import com.bench.android.core.util.LogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * 请求体为json的格式
 */
public class OkHttpJsonBody implements Body {

    private JSONObject jsonObject = new JSONObject();

    @Override
    public RequestBody getRequestBody() {
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString().getBytes());
        LogUtils.e("json", jsonObject.toString());
        return requestBody;
    }

    @Override
    public HashMap<String, String> getFiledMap() {
        return null;
    }

    @Override
    public Body put(String key, String value) {
        try {
            jsonObject.put(key, value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return this;
    }

    @Override
    public Body put(String key, boolean value) {
        return put(key, String.valueOf(value));
    }

    @Override
    public Body put(String key, int value) {
        return put(key, String.valueOf(value));
    }

    @Override
    public Body put(String key, long value) {
        return put(key, String.valueOf(value));
    }

    @Override
    public Body put(String key, float value) {
        return put(key, String.valueOf(value));
    }

    @Override
    public Body put(String key, double value) {
        return put(key, String.valueOf(value));
    }

    @Override
    public Body putFormDataPart(String name, String filename, File file) {
        return null;
    }

    @Override
    public Body putFormDataPart(String name, String filename, File file, ProgressRequestBody.ProgressListener progressListener) {
        return null;
    }

    @Override
    public Body putFormDataPart(String name, String filename, byte[] content) {
        return null;
    }

    @Override
    public Body putFormDataPart(String name, String filename, byte[] content, ProgressRequestBody.ProgressListener progressListener) {
        return null;
    }

    @Override
    public Body putPointInfo(String db_from, String db_to) {
        return null;
    }
}
