package com.bench.android.core.net.http.body;

import com.bench.android.core.net.http.okhttp.ProgressRequestBody;

import java.io.File;
import java.util.HashMap;

import okhttp3.RequestBody;

/**
 * okhttp表单参数转换类
 * 如果有文件上传使用MultipartBody
 * 单表单形式上传使用FormBody
 * <p>
 * Created by lingjiu on 2019/2/13.
 */
public class OkHttpRequestBody implements Body {
    public Body body;
    public boolean useJsonRequest = false;

    @Override
    public RequestBody getRequestBody() {
        return body.getRequestBody();
    }

    /**
     * 上传文件
     *
     * @param name     key值(可为null)
     * @param filename 文件名(可为null)
     * @param file     文件
     * @return
     */
    @Override
    public Body putFormDataPart(String name, String filename, File file) {
        if (body == null) {
            body = new MutilFormBody();
        }
        body.putFormDataPart(name, filename, file);
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
        if (body == null) {
            body = new MutilFormBody();
        }
        body.putFormDataPart(name, filename, file, progressListener);
        return this;
    }

    /**
     * 上传二进制流
     *
     * @param name     key值(可为null)
     * @param filename 文件名(可为null)
     * @param content  byte数组
     * @return
     */
    @Override
    public Body putFormDataPart(String name, String filename, byte[] content) {
        if (body == null) {
            body = new MutilFormBody();
        }
        body.putFormDataPart(name, filename, content);
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
        if (body == null) {
            body = new MutilFormBody();
        }
        body.putFormDataPart(name, filename, content, progressListener);
        return this;
    }

    @Override
    public Body put(String key, String value) {
        body.put(key, value);
        return this;
    }

    @Override
    public Body put(String key, boolean value) {
        return null;
    }

    @Override
    public Body put(String key, int value) {
        return null;
    }

    @Override
    public Body put(String key, long value) {
        return null;
    }

    @Override
    public Body put(String key, float value) {
        return null;
    }

    @Override
    public Body put(String key, double value) {
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
