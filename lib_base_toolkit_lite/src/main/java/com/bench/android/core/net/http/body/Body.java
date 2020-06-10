package com.bench.android.core.net.http.body;


import com.bench.android.core.net.http.okhttp.ProgressRequestBody;

import java.io.File;
import java.util.HashMap;

/**
 * Created by qiaomu on 2017/6/13.
 */

public interface Body {

    <T> T getRequestBody();

    HashMap<String, String> getFiledMap();

    Body put(String key, String value);


    Body put(String key, boolean value);


    Body put(String key, int value);


    Body put(String key, long value);

    Body put(String key, float value);

    Body put(String key, double value);

    /**
     * 上传文件
     *
     * @param name     key值(可为null)
     * @param filename 文件名(可为null)
     * @param file     文件
     * @return
     */
    Body putFormDataPart(String name, String filename, File file);


    /**
     * 上传文件,带进度监听
     *
     * @param name     key值(可为null)
     * @param filename 文件名(可为null)
     * @param file     文件
     * @return
     */
    Body putFormDataPart(String name, String filename, File file, ProgressRequestBody.ProgressListener progressListener);


    /**
     * 上传二进制流
     *
     * @param name     key值(可为null)
     * @param filename 文件名(可为null)
     * @param content  byte数组
     * @return
     */
    Body putFormDataPart(String name, String filename, byte[] content);

    /**
     * 上传二进制流,带进度监听
     *
     * @param name     key值(可为null)
     * @param filename 文件名(可为null)
     * @param content  byte数组
     * @return
     */
    Body putFormDataPart(String name, String filename, byte[] content, ProgressRequestBody.ProgressListener progressListener);


    Body putPointInfo(String db_from, String db_to);

}
