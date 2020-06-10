package com.bench.android.core.net.http.processor;

import android.os.StrictMode;
import android.text.TextUtils;

import com.bench.android.core.net.http.HttpRequestConfig;
import com.bench.android.core.net.http.base.RequestContainer;
import com.bench.android.core.net.http.okhttp.OkHttpResponseHandler;
import com.bench.android.core.util.LogUtils;

import org.json.JSONObject;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Call;
import okhttp3.Dispatcher;
import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * okHttp处理网络
 * Created by lingjiu on 2019/1/24.
 */
public class OkHttpHelper implements IHttpProcessor {

    public static OkHttpClient okHttpClient;

    public static boolean DEBUG = true;

    public OkHttpHelper() {
        init();
    }

    /**
     * 添加拦截器
     *
     * @param interceptors
     */
    public void addInterceptors(Interceptor... interceptors) {
        OkHttpClient.Builder builder = okHttpClient.newBuilder();
        for (Interceptor interceptor : interceptors) {
            builder.addInterceptor(interceptor);
        }
        okHttpClient = builder.build();
    }

    /**
     * 添加安全证书
     *
     * @param sslSocketFactory
     */
    public void addSSLFactory(SSLSocketFactory sslSocketFactory) {
        OkHttpClient.Builder builder = okHttpClient.newBuilder();
        builder.sslSocketFactory(sslSocketFactory);
        okHttpClient = builder.build();
    }

    /**
     * okhttp初始化
     * 添加拦截器/请求超时配置等
     */
    public void init() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.retryOnConnectionFailure(true);
        builder.connectTimeout(20, TimeUnit.SECONDS);
        builder.readTimeout(20, TimeUnit.SECONDS);
        builder.writeTimeout(20, TimeUnit.SECONDS);
        if (DEBUG) {
            setDefaultTrustManager(builder);
        }
        okHttpClient = builder.build();
    }

    /**
     * 设置安全证书
     *
     * @param builder
     */
    private static void setDefaultTrustManager(OkHttpClient.Builder builder) {
        /**
         * 支持请求所有的https前缀URL
         */
        X509TrustManager xtm = new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                X509Certificate[] x509Certificates = new X509Certificate[0];
                return x509Certificates;
            }
        };

        SSLContext sslContext = null;
        try {
            sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, new TrustManager[]{xtm}, new SecureRandom());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };
        //不加这两行,会报异常NetworkOnMainThreadException
        //        at android.os.StrictMode$AndroidBlockGuardPolicy.onNetwork
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        //是否关闭证书验证   针对https请求
        builder.sslSocketFactory(sslContext.getSocketFactory()).hostnameVerifier(DO_NOT_VERIFY);
    }

    /**
     * 网络请求发起打印日志
     *
     * @param url
     * @param formBody
     */
    private static void showLog(String url, RequestBody formBody) {
        if (DEBUG && formBody instanceof FormBody) {
            StringBuilder sb = new StringBuilder();
            FormBody body = (FormBody) formBody;
            String serviceName = "";
            for (int i = 0; i < body.size(); i++) {
                if (i != 0) {
                    sb.append("&");
                }
                sb.append(body.encodedName(i) + "=" + body.encodedValue(i));
                if ("service".equals(body.encodedName(i))) {
                    serviceName = body.encodedValue(i);
                }
            }
            LogUtils.e(serviceName, url + "?" + sb);
        }
    }

    //使用默认请求RequestBody
    public RequestBody getDefaultRequestBody(RequestContainer requestContainer) {
        FormBody.Builder builder = new FormBody.Builder();
        builder.add("service", requestContainer.getRequestEnum().getName());
        HashMap<String, String> filedsMap = requestContainer.getFiledsMap();
        for (String key : filedsMap.keySet()) {
            //如果加密请求了，则不需要再进行一遍Encode URL
            if (requestContainer.isEncrypt()) {
                builder.addEncoded(key, filedsMap.get(key));
            } else {
                builder.add(key, filedsMap.get(key));
            }

        }
        return builder.build();
    }

    /**
     * 发起post请求
     *
     * @param requestContainer
     * @param callback
     * @param activityName
     */
    @Override
    public void httpPost(RequestContainer requestContainer, final IHttpResponseHandler callback, String activityName) {
        String requestUrl = requestContainer.getRequestUrl();
        RequestBody requestBody = requestContainer.getRequestBody();
        //如果没有重写requestBody，使用默认请求body
        if (requestBody == null) {
            requestBody = getDefaultRequestBody(requestContainer);
        }
        showLog(requestUrl, requestBody);
        Request.Builder requestBuilder = new Request.Builder();

        //添加自定义header信息
        HashMap<String, String> requestHeader = requestContainer.getRequestHeader();
        if(requestHeader!=null){
            for (String key : requestHeader.keySet()) {
                requestBuilder.addHeader(key, requestHeader.get(key));
            }
        }


        if (HttpRequestConfig.sUseMock) {
            requestBuilder.addHeader("Web-Exterface-RequestPath", requestContainer.getRequestEnum().getUrl());
        }
        requestBuilder.url(requestUrl);
        if (requestContainer.isUsePost()) {
            requestBuilder.post(requestBody);
        } else {
            requestBuilder.get();
        }
        requestBuilder.tag(activityName);
        if (activityName != null) {
            requestBuilder.addHeader(HTTP_HEADER_APP_REFER, activityName);
        }
        if (requestContainer.isEncrypt()) {
            requestBuilder.addHeader("encrypt", "1");
        }
        final Call call = okHttpClient.newCall(requestBuilder.build());

        OkHttpResponseHandler okHttpResponseHandler = new OkHttpResponseHandler() {

            @Override
            public void executorSuccess(JSONObject jo) {
                callback.executorSuccess(jo);
            }

            @Override
            public void executorFalse(JSONObject jo) {
                callback.executorFalse(jo);
            }

            @Override
            public void executorFailed(int code, Exception e) {
                callback.executorFailed(code, e);
            }
        };
        try {
            Response response = call.execute();
            okHttpResponseHandler.onResponse(call, response);
        } catch (IOException e) {
            okHttpResponseHandler.onFailure(call, e);
        }
    }

    /**
     * 取消对应tag的请求
     *
     * @param tag
     */
    @Override
    public void cancelHttpCall(String tag) {
        Dispatcher dispatcher = okHttpClient.dispatcher();
        for (Call call : dispatcher.queuedCalls()) {
            if (TextUtils.equals(call.request().tag().toString(), tag)) {
                if (!call.isCanceled()) {
                    call.cancel();
                }
            }
        }
        for (Call call : dispatcher.runningCalls()) {
            if (TextUtils.equals(call.request().tag().toString(), tag)) {
                if (!call.isCanceled()) {
                    call.cancel();
                }
            }
        }
    }

    /**
     * 取消所有请求
     */
    @Override
    public void cancelAllHttpCall() {
        Dispatcher dispatcher = okHttpClient.dispatcher();
        dispatcher.cancelAll();
    }


}
