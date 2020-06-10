package com.bench.android.core.net.http.body;


import android.text.TextUtils;

import com.bench.android.core.net.http.okhttp.ProgressRequestBody;

import java.io.File;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * okhttp带文件/二进制上传
 * Created by qiaomu on 2017/6/13.
 */

public class MutilFormBody implements Body {
    private MultipartBody.Builder builder = new MultipartBody.Builder();

    @Override
    public RequestBody getRequestBody() {
        //如果 multiFiled 为空,会抛出异常 throw new IllegalStateException("Multipart body must have at least one part.");
        builder.setType(MultipartBody.FORM);
        return builder.build();
    }

    @Override
    public Body put(String key, String value) {
        if (!TextUtils.isEmpty(value))
            builder.addFormDataPart(key, value);
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
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);
        builder.addFormDataPart(name, filename, requestBody);
        return this;
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
        ProgressRequestBody requestBody = new ProgressRequestBody(file.getPath(), progressListener);
        builder.addFormDataPart(name, filename, requestBody);
        return this;
    }

    @Override
    public Body putFormDataPart(String name, String filename, byte[] content) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/octet-stream"), content);
        builder.addFormDataPart(name, filename, requestBody);
        return this;
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
        ProgressRequestBody requestBody = new ProgressRequestBody(content, progressListener);
        builder.addFormDataPart(name, filename, requestBody);
        return this;
    }

    @Override
    public Body putPointInfo(String db_from, String db_to) {
        return null;
    }

    public Body putFormDataPart(String name, String filename, RequestBody body) {
        builder.addFormDataPart(name, filename, body);
        return this;
    }

    @Override
    public HashMap<String, String> getFiledMap() {
        return null;
    }
}
