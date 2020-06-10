package com.bench.android.core.net.domain.base;

import android.os.Handler;
import android.os.Looper;

import com.bench.android.core.util.LogUtils;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public abstract class BaseGetDomain {

    private final static String TAG = GetDynamicUrlHelper.class.getSimpleName();

    //默认最大尝试次数
    public final int DEFAULT_ALL_TRY_COUNT = 6;

    //请求动态url
    private final int TYPE_GET_URL = 1;

    //测试缓存Url是否有效
    private final int TYPE_TEST_URL = 2;

    private int mMaxRetryCount = DEFAULT_ALL_TRY_COUNT;

    private int mFirstDomainRetryCount = DEFAULT_ALL_TRY_COUNT / 2;

    private String mFirstDomain = "";

    private String mSecondDomain = "";

    private String mNativeUrl = "";

    protected BaseGetDomain.RequestDoMainSuccessListener mListener;

    private OkHttpClient mClient;

    private Handler mHandler;

    private int mRetry = 0;

    public BaseGetDomain(BaseGetDomain.Builder builder) {
        if (builder.maxRetryCount != 0) {
            mMaxRetryCount = builder.maxRetryCount;
        }
        if (builder.firstDomainRetryCount != 0) {
            mFirstDomainRetryCount = builder.firstDomainRetryCount;
        }
        mFirstDomain = builder.firstDomain;
        mSecondDomain = builder.secondDomain;
        mListener = builder.listener;
        mClient = TrustAllManager.getInstance().getOkClient();
        mHandler = new Handler(Looper.getMainLooper());
    }

    /**
     * 开始
     */
    public abstract void startRequestUrl();


    //从第一个阿里云地址请求域名
    protected void getUrlByFirstDomain() {
        postHttpRequest(mFirstDomain, TYPE_GET_URL);
        LogUtils.e("kkk", "阿里云地址---------请求第" + mRetry + "次");
    }

    //从备用地址请求域名
    protected void getUrlBySecondDomain() {
        postHttpRequest(mSecondDomain, TYPE_GET_URL);
        LogUtils.e("kkk", "备用地址-----------请求第" + mRetry + "次");
    }


    private void postRequest() {
        mHandler.postDelayed(mGetDomain, 500);
    }


    Runnable mGetDomain = new Runnable() {
        @Override
        public void run() {
            if (mRetry < mFirstDomainRetryCount) {
                getUrlByFirstDomain();
            } else if (mRetry < mMaxRetryCount) {
                getUrlBySecondDomain();
            } else {
                //尝试失败，不去获取
                responseFailedGetUrl();
            }
        }
    };


    protected void postHttpRequest(String requestUrl, final int type) {
        Request request = new Request.Builder().url(requestUrl).build();
        LogUtils.i(TAG, "requestUrl ===" + requestUrl);
        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                //抛到main线程执行
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        //无效域名
                        LogUtils.i(TAG, "failed:response ===" + e.toString());
                        switch (type) {
                            case TYPE_GET_URL:   //获取域名失败----继续请求备用地址
                                postRequest();
                                break;
                            case TYPE_TEST_URL:  //检测本地域名无效
                                responseFailedTestUrl();
                                break;
                        }
                    }
                });

            }

            @Override
            public void onResponse(Call call, final Response response) {
                //抛到main线程执行
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            LogUtils.i(TAG, "test:response ===" + response.code());
                            //返回代码在200-300之间
                            if (response.isSuccessful()) {
                                switch (type) {
                                    case TYPE_GET_URL:
                                        responseSuccessGetUrl(response.body().string());  //获取域名成功
                                        break;
                                    case TYPE_TEST_URL:
                                        responseSuccessTestUrl(response.body().string());  //测试本地域名成功
                                        break;
                                }

                            } else {
                                if (type == TYPE_GET_URL) {
                                    //获取不到线上地址
                                    postRequest();
                                } else {
                                    responseFailedTestUrl();
                                }
                            }
                        } catch (IOException e) {
                            postRequest();
                        }
                    }
                });
            }
        });
        mRetry++;
    }


    public abstract void responseSuccessGetUrl(String response);

    protected abstract void responseFailedGetUrl();

    private void responseSuccessTestUrl(String response) {
        LogUtils.i(TAG, "test:response ===" + response);
        mListener.requestDoMainSuccess(mNativeUrl);
    }

    private void responseFailedTestUrl() {
        postRequest();
    }


    public interface RequestDoMainSuccessListener {
        void requestDoMainSuccess(String response);

        void onFailed(Exception e);
    }

    public static class Builder {

        private String firstDomain;   //阿里云获取域名地址

        private String secondDomain;  //备用地址

        private String jsonDomainName; //获取域名对应的key

        private int maxRetryCount;

        private int firstDomainRetryCount;

        private BaseGetDomain.RequestDoMainSuccessListener listener;

        public BaseGetDomain.Builder setFirstDomain(String firstDomain) {
            this.firstDomain = firstDomain;
            return this;
        }

        public BaseGetDomain.Builder setSecondDomain(String secondDomain) {
            this.secondDomain = secondDomain;
            return this;
        }

        public BaseGetDomain.Builder setMaxRetryCount(int maxRetryCount) {
            this.maxRetryCount = maxRetryCount;
            return this;
        }

        public BaseGetDomain.Builder setFirstDomainRetryCount(int firstDomainRetryCount) {
            this.firstDomainRetryCount = firstDomainRetryCount;
            return this;
        }

        public BaseGetDomain.Builder setListener(BaseGetDomain.RequestDoMainSuccessListener listener) {
            this.listener = listener;
            return this;
        }

        public BaseGetDomain.Builder setJsonDomainName(String jsonDomainName) {
            this.jsonDomainName = jsonDomainName;
            return this;
        }
    }

}
