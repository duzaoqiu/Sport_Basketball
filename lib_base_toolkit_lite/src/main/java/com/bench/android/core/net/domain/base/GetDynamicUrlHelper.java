package com.bench.android.core.net.domain.base;

import android.os.Handler;

import com.bench.android.core.app.application.BaseApplication;
import com.bench.android.core.app.toast.ToastUtils;
import com.bench.android.core.content.sharePreference.SharedPreferUtil;
import com.bench.android.core.util.LogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Edit by zhoushen 2018/10/31
 * 域名获取步骤:
 * 由于服务端的域名可能无效，所以，服务端会将域名存储在远程服务器上，比如阿里云上
 * 我们默认，从阿里云上拿到地址（拿不到就请求备用域名），只要能拿到就是有效的
 * 逻辑如下
 * 1、判断缓存url地址是否有效，如果无效，从存储服务器上获取我们服务端的url，然后去请求
 * 2、如果没有缓存url，从存储服务器上获取我们服务端的url，然后去请求
 *
 * @author Created by zaozao on 2018/5/17.
 */

public class GetDynamicUrlHelper {
    private final static String TAG = GetDynamicUrlHelper.class.getSimpleName();


    /**
     * 默认最大尝试次数
     */
    private final int MAX_ALL_TRY_COUNT = 6;

    /**
     * 请求动态url
     */
    private final int TYPE_GET_URL = 1;

    /**
     * 测试缓存Url是否有效
     */
    private final int TYPE_TEST_URL = 2;

    private int mMaxRetryCount = MAX_ALL_TRY_COUNT;

    private int mFirstDomainRetryCount;

    private String mFirstDomainRequestUrl = "";

    private String mSecondDomainRequestUrl = "";

    //    /*** 本地保存的域名*/
    private String mNativeUrl = "";

    private RequestDoMainSuccessListener mListener;

    private String[] mJsonDomainKey;

    private OkHttpClient mClient;

    private Handler mHandler;

    private int mHaveRetryCount = 0;

    public GetDynamicUrlHelper(Builder builder) {
        if (mMaxRetryCount != 0) {
            mMaxRetryCount = builder.maxRetryCount;
        }
        mFirstDomainRetryCount = builder.firstDomainRetryCount;
        mFirstDomainRequestUrl = builder.firstDomainRequestUrl;
        mSecondDomainRequestUrl = builder.secondDomainRequestUrl;
        mListener = builder.listener;
        mJsonDomainKey = builder.jsonDomainKey;
        mClient = TrustAllManager.getInstance().getOkClient();
        mHandler = new Handler();
    }


    /**
     * 开始
     */
    public void startRequestUrl() {

        if (mFirstDomainRetryCount > mMaxRetryCount) {
            throw new IllegalArgumentException("第一域名请求次数不能大于总的请求次数");
        }
        String domainUrl = getUrlBySharePreference();
        if (domainUrl != null) {
            mNativeUrl = domainUrl;
            AppDomainManager.getInstance().setApiDomain(domainUrl);//一开始的时候从本地获取的
        }

        getUrlByFirstDomain();
    }


    /**
     * 验证我们服务端的地址是否有效
     */
    private void isUrlValid(String url, String domainKey) {
        LogUtils.e(TAG, "检测地址是否有效" + AppDomainManager.getInstance().getApiDomain());
        postHttpRequest(url, TYPE_TEST_URL, domainKey);
    }

    /**
     * 从第一个阿里云地址请求域名
     */
    private void getUrlByFirstDomain() {
        postHttpRequest(mFirstDomainRequestUrl, TYPE_GET_URL, null);
        LogUtils.e(TAG, "阿里云地址---------请求第" + mHaveRetryCount + "次");
    }

    /**
     * 从备用地址请求域名
     */
    private void getUrlBySecondDomain() {
        postHttpRequest(mSecondDomainRequestUrl, TYPE_GET_URL, null);
        LogUtils.e(TAG, "备用地址-----------请求第" + mHaveRetryCount + "次");
    }


    private void postRequest() {
        mHandler.postDelayed(mGetDomain, 500);
    }


    Runnable mGetDomain = new Runnable() {
        @Override
        public void run() {
            if (mHaveRetryCount < mFirstDomainRetryCount) {
                getUrlByFirstDomain();
            } else if (mHaveRetryCount < mMaxRetryCount) {
                getUrlBySecondDomain();
            } else {
                //尝试失败，不去获取
                mListener.onFailed(null);
            }
        }
    };


    private void postHttpRequest(String requestUrl, final int type, final String domainKey) {
        Request request = new Request.Builder().url(requestUrl).build();
        LogUtils.i(TAG, "requestUrl ===" + requestUrl);

        if (type == TYPE_GET_URL) {
            mHaveRetryCount++;
        }

        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                //抛到main线程执行
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        //无效域名
                        resultFailed(type, "域名请求失败" + e.toString());
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) {
                //抛到main线程执行
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (response.isSuccessful()) {
                            requestSuccess(type, response, domainKey);
                        } else {
                            resultFailed(type, "域名请求失败" + response);
                        }
                    }
                });
            }
        });

    }

    private void requestSuccess(int type, Response response, String domainKey) {
        try {
            LogUtils.i(TAG, "test:response ===" + response.code());
            //返回代码在200-300之间
            switch (type) {
                case TYPE_GET_URL:
                    /**获取域名成功*/
                    handleResponseSuccessGetUrl(response.body().string());
                    break;
                case TYPE_TEST_URL:
                    /**测试本地域名成功*/
                    LogUtils.e(TAG, "测试域名成功test:response ===" + response);
                    mListener.requestDoMainSuccess(response.request().url().toString(), false, domainKey);
                    break;
                default:
                    break;
            }
        } catch (IOException e) {
            resultFailed(type, e.toString());
        }
    }

    /**
     * @param type 阿里云请求或验证失败，继续备用地址请求postRequest
     *             备用地址请求或验证失败  就直接onFailed
     */
    private void resultFailed(int type, String errorMessage) {

        if (mHaveRetryCount < mFirstDomainRetryCount) {
            mHaveRetryCount = mFirstDomainRetryCount;
            LogUtils.e(TAG, "阿里云请求或检测域名无效，继续备用地址请求postRequest");
            postRequest();
        } else if (mHaveRetryCount <= mMaxRetryCount) {

            if (type == TYPE_TEST_URL) {
                mListener.onFailed("本地和网络上的域名都无效了" + mNativeUrl);
            } else {
                mListener.onFailed(errorMessage);
            }
        }
    }


    private void handleResponseSuccessGetUrl(String response) {
        LogUtils.i(TAG, "response ===" + response);
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(response);
            if (mJsonDomainKey != null) {
                for (int i = 0; i < mJsonDomainKey.length; i++) {
                    parseResponseByDomainKey(response, jsonObject, i);
                }
            } else {
                resultFailed(TYPE_GET_URL, "mJsonDomainKey 没有配置" + "  mJsonDomainKey=null");
            }
        } catch (JSONException e) {
            e.printStackTrace();
            resultFailed(TYPE_GET_URL, "请求成功后数据处理抛异常" + response);
        }
    }

    private void parseResponseByDomainKey(String response, JSONObject jsonObject, int index) throws JSONException {
        String domainKey = mJsonDomainKey[index];
        if (jsonObject.has(domainKey)) {
            String url = jsonObject.getString(domainKey);
            //默认主域名key是第一个,非主域名不走以下验证的逻辑
            if (index > 0) {
                mListener.requestDoMainSuccess(url, true, domainKey);
                return;
            }
//自测用的
//                if(mHaveRetryCount==1){
//                    url = "http://user1-mapi.caihongtest11.net/";
//                }else{
//
//                }
            //如果和当前域名一样，就测试一下是否有效
            if (url.equals(mNativeUrl)) {
                if (mHaveRetryCount > 1) {
                    LogUtils.e(TAG, "备用地址和当前域名一样，直接结束");
                    mListener.onFailed("本地和网络上的域名都无效了" + mNativeUrl);
                } else {
                    LogUtils.e(TAG, "阿里云拿到的和当前域名一样，就测试一下是否有效");
                    isUrlValid(mNativeUrl, domainKey);
                }
            } else {//和本地不一样，说明有更新，直接替换，通知页面更新,或者第一次进入
                checkDomain(url, domainKey);
            }
        } else {
            resultFailed(TYPE_GET_URL, "该彩店" + mJsonDomainKey + "没有找到对应的配置域名,请求结果如下：" + response);
        }
    }


    /**
     * 检测获取到域名url是否规范
     *
     * @param domainUrl
     */
    private void checkDomain(String domainUrl, String domainKey) {
        try {
            AppDomainManager.host = new URL(domainUrl).getHost();
            LogUtils.e(TAG, "AppDomainManager.host --------" + AppDomainManager.host);
            //网络请求回来的，经检测过得
            AppDomainManager.getInstance().setApiDomain(domainUrl);
            //网络请求回来的，经检测过得
            saveUrlToSharePreference(domainUrl);
            mListener.requestDoMainSuccess(domainUrl, true, domainKey);
            LogUtils.e(TAG, mNativeUrl + "第一次进入app或者和当前域名不一样，测试后---符合规范" + domainUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            mListener.onFailed("当前彩店配置的域名----" + domainUrl + "---不符合规范，请联系客服人员处理");
        }
    }

    private void showToastTestMemo(String memo) {
        if (AppDomainManager.Debug) {
            ToastUtils.showTip(BaseApplication.getContext(), memo);
        }
    }

    private String getUrlBySharePreference() {
        String domainUrl = SharedPreferUtil.getInstance().getString("APP_DOMAIN", null);
        return domainUrl;
    }

    private void saveUrlToSharePreference(String url) {
        SharedPreferUtil.getInstance().putString("APP_DOMAIN", url);
    }


    public interface RequestDoMainSuccessListener {
        void requestDoMainSuccess(String response, boolean needUpdate, String domainKey);

        void onFailed(String errorMessage);
    }

    public static class Builder {

        /**
         * 阿里云获取域名地址
         */
        private String firstDomainRequestUrl;

        /**
         * 备用地址
         */
        private String secondDomainRequestUrl;

        /**
         * 获取域名对应的key
         */
        private String[] jsonDomainKey;

        /**
         * 总的最大请求次数（包括  阿里云访问地址 和 备用地址）
         */
        private int maxRetryCount;

        /**
         * 第一个域名请求最大次数
         */
        private int firstDomainRetryCount;

        /**
         * 请求成功回调
         */
        private RequestDoMainSuccessListener listener;

        public Builder setFirstDomain(String firstDomainRequestUrl) {
            this.firstDomainRequestUrl = firstDomainRequestUrl;
            return this;
        }

        public Builder setSecondDomain(String secondDomainRequestUrl) {
            this.secondDomainRequestUrl = secondDomainRequestUrl;
            return this;
        }

        public Builder setMaxRetryCount(int maxRetryCount) {
            this.maxRetryCount = maxRetryCount;
            return this;
        }

        public Builder setFirstDomainRetryCount(int firstDomainRetryCount) {
            this.firstDomainRetryCount = firstDomainRetryCount;
            return this;
        }

        public Builder setListener(RequestDoMainSuccessListener listener) {
            this.listener = listener;
            return this;
        }

        public Builder setJsonDomainName(String... jsonDomainName) {
            this.jsonDomainKey = jsonDomainName;
            return this;
        }

        public GetDynamicUrlHelper build() {
            return new GetDynamicUrlHelper(this);
        }
    }
}

