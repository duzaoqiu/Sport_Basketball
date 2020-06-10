package com.bench.android.core.app.application;

import android.app.Application;
import android.content.Context;

import com.bench.android.core.content.sharePreference.SharedPreferUtil;
import com.bench.android.core.net.http.HttpRequestConfig;

/**
 * 该类不需要使用了，只是为了兼容老代码
 * @author zhouyi
 */
@Deprecated
public class BaseApplication {

    private static Application sContext;

    public static void onCreate(Application context) {

        sContext = context;

        //EncryptUtils.initCrypto(context);

        SharedPreferUtil.init(context, null);
    }

    public static Context getContext() {
        return sContext;
    }

    /**
     * 是否开启加密请求
     */
    protected void openEncrypt() {
        //加密初始化
        HttpRequestConfig.sUseEncrypt = true;
    }

}
