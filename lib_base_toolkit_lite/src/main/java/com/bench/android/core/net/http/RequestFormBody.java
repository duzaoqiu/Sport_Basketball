package com.bench.android.core.net.http;

import android.text.TextUtils;

import com.bench.android.core.net.http.base.RequestContainer;
import com.bench.android.core.net.http.body.Body;
import com.bench.android.core.net.http.body.FormBody;
import com.bench.android.core.net.http.body.OkHttpJsonBody;
import com.bench.android.core.net.http.body.OkHttpRequestBody;
import com.bench.android.core.net.http.okhttp.ProgressRequestBody;
import com.bench.android.core.net.http.params.BaseRequestHttpName;

import java.io.File;
import java.util.HashMap;
import java.util.List;

/**
 * Created by qiaomu on 2017/6/13.
 */

public class RequestFormBody extends RequestContainer {

    public Body body;

    public RequestFormBody(BaseRequestHttpName requestHttpName) {
        this(requestHttpName, true);
    }

    public RequestFormBody(BaseRequestHttpName requestHttpName, boolean sign) {
        this(requestHttpName, sign, 0);
    }

    public RequestFormBody(BaseRequestHttpName requestHttpName, boolean sign, int signTypeEnum) {
        super(requestHttpName, sign);
        this.signType = signTypeEnum;
        body = new OkHttpRequestBody();
    }

    public void setBody(Body body) {
        this.body = body;
    }

    @Override
    public Body put(String key, String value) {
        if (value != null) {
            value = encodeValue(value);
            map.put(key, value);
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
        put(key, String.valueOf(value));
        return this;
    }

    /**
     * 设置埋点
     *
     * @param db_from
     * @param db_to
     * @return
     */
    @Override
    public Body putPointInfo(String db_from, String db_to) {
        if (TextUtils.isEmpty(db_from)) {
            db_from = "null";
        }
        if (TextUtils.isEmpty(db_to)) {
            db_to = "null";
        }
        put("db_from", db_from);
        put("db_to", db_to);
        return this;
    }

    @Override
    public Body putFormDataPart(String name, String filename, File file) {
        return this.body.putFormDataPart(name, filename, file);
    }

    /**
     * 上传文件,带进度监听
     *
     * @param name             key值(可为null)
     * @param filename         文件名(可为null)
     * @param file             文件
     * @param progressListener
     * @return
     */
    @Override
    public Body putFormDataPart(String name, String filename, File file, ProgressRequestBody.ProgressListener progressListener) {
        return this.body.putFormDataPart(name, filename, file, progressListener);
    }

    @Override
    public Body putFormDataPart(String name, String filename, byte[] content) {
        return this.body.putFormDataPart(name, filename, content);
    }

    /**
     * 上传二进制流,带进度监听
     *
     * @param name             key值(可为null)
     * @param filename         文件名(可为null)
     * @param content          byte数组
     * @param progressListener
     * @return
     */
    @Override
    public Body putFormDataPart(String name, String filename, byte[] content, ProgressRequestBody.ProgressListener progressListener) {
        return this.body.putFormDataPart(name, filename, content, progressListener);
    }

    /**
     * okHttp body参数
     *
     * @return
     */
    @Override
    public Object getRequestBody() {
        //这边代码主要是处理轮询请求使用相同的RequestFromBody，为了方便每次都生成新的网络请求Body，需要生成新的FormBody
        if (body instanceof OkHttpRequestBody) {
            OkHttpRequestBody data = (OkHttpRequestBody) body;
            data.useJsonRequest = isUseJsonRequest;
            if (data.useJsonRequest) {
                data.body = new OkHttpJsonBody();
            } else {
                data.body = new FormBody();
            }
        }

        for (String key : map.keySet()) {
            body.put(key, map.get(key));
        }

        return body.getRequestBody();
    }


    public Body putStringList(String messageId, List<String> list) {
        if (list == null || list.size() <= 0) {
            return this;
        }

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            builder.append(list.get(i));
            if (i != list.size() - 1) {
                builder.append(",");
            }

        }
        return put(messageId, builder.toString());
    }

    public Body putStringArray(String messageId, String... array) {
        if (array == null || array.length <= 0) {
            return this;
        }
        List list = java.util.Arrays.asList(array);
        return putStringList(messageId, list);
    }

    public void getSignParams() {

    }

    @Override
    public HashMap<String, String> getFiledMap() {
        return map;
    }

}
