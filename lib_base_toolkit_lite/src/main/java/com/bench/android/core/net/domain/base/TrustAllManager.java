package com.bench.android.core.net.domain.base;

import android.os.StrictMode;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;


/**
 * Created by zaozao on 2018/6/28.
 * 解决下面这个问题
 * android 使用https时 证书如果是用来测试的会爆出下面问题：
 * javax.net.ssl.SSLHandshakeException:
 * java.security.cert.CertPathValidatorException:
 * Trust anchor for certification path not found
 */

public class TrustAllManager {

    private OkHttpClient mOkHttpClient = null;

    private static TrustAllManager trustAllManager;

    public static TrustAllManager getInstance() {

        if (trustAllManager == null) {

            return new TrustAllManager();
        } else {
            return trustAllManager;
        }
    }

    /**
     * 获得Okhttp对象,并且初始化一次.
     *
     * @return
     */
    public OkHttpClient getOkClient() {
        if (mOkHttpClient == null) {
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

            mOkHttpClient = new OkHttpClient().newBuilder()
                    .retryOnConnectionFailure(true)
                    .readTimeout(10L, TimeUnit.SECONDS)
                    .connectTimeout(10L, TimeUnit.SECONDS)
                    .sslSocketFactory(sslContext.getSocketFactory())
                    .hostnameVerifier(DO_NOT_VERIFY)//是否关闭证书验证   针对https请求
                    .build();

        }
        return mOkHttpClient;

    }

}
