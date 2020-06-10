package com.bench.android.core.net.http.body;

import android.text.TextUtils;

import com.bench.android.core.net.http.okhttp.ProgressRequestBody;

import java.io.File;
import java.util.HashMap;

/**
 * okhttp表单上传
 * Created by qiaomu on 2017/6/13.
 */

public class FormBody implements Body {

    private okhttp3.FormBody.Builder builder = new okhttp3.FormBody.Builder();

    @Override
    public okhttp3.FormBody getRequestBody() {
        return builder.build();
    }

    @Override
    public Body put(String key, String value) {

        if (!TextUtils.isEmpty(value)) {
            //如果是加密数据，则不需要进行url encoder
            if (key.contains("ciphertext")) {
                builder.addEncoded(key, value);
            } else {
                builder.add(key, value);
            }

        } else if (TextUtils.equals(key, "db_from") || TextUtils.equals(key, "db_to")) {
            builder.add(key, "null");
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
    public Body putFormDataPart(String name, String filename, File body) {
        return null;
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
        return null;
    }

    @Override
    public Body putFormDataPart(String name, String filename, byte[] content) {
        return null;
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
        return null;
    }

    @Override
    public Body putPointInfo(String db_from, String db_to) {
        return null;
    }

    @Override
    public HashMap<String, String> getFiledMap() {
        return null;
    }
}
