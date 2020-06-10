package com.bench.android.core.net.domain;

import com.bench.android.core.net.http.IHttpResponseListener;
import com.bench.android.core.net.http.RequestFormBody;
import com.bench.android.core.net.http.WrapperHttpHelper;
import com.bench.android.core.net.http.base.RequestContainer;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import bolts.Continuation;
import bolts.Task;

/**
 * 本地没有url缓存情况：
 * 1、打开app，各种网络请求，但是这时url地址为空，不能让用户知道，我们正在动态获取url地址，导致网络连接不成功,
 * 所以我们必须获取到地址之后，重新请求用户之前发起的请求，让用户无感知网络地址变化.
 * 本地有url缓存情况:
 * 1、打开app，各种网络请求，但是这时请求可能出现“服务器开小差，请稍后再试”,因为之前缓存的域名无效了,然后我们照样动态获取
 * url地址，然后替换本地的url。
 * 2、本地能够正常请求，但是获取到的动态地址，和本地保存的不一致，照样替换
 * <p>
 * <p>
 * 获取动态域名
 *
 * @author zhouyi
 */
public class DynamicsUrlRequestHelper implements IHttpResponseListener {

    private final int MAX_RETRY_COUNT = 3;
    /**
     * 本地没有网络
     */
    public static final int REQUEST_FAILED_NETWORK_ERROR = 1001;

    /**
     * url不可用
     */
    public static final int REQUEST_FAILED_URL_ERROR = 1002;

    /**
     * 网站系统错误
     */
    public static final int REQUEST_FAILED_URL_SERVICE_ERROR = 1003;

    private WrapperHttpHelper httpHelper;

    private Callback mCallback;

    private List<String> mCurrentRequestDomainList = new ArrayList<>();

    private List<UrlRequestModel> mDomainList = new ArrayList<>();

    private int mRetryCount = 0;

    private String mCurrentUrlKey = null;

    private String[] mJsonKeys = null;

    /**
     * 是否需要验证url的可用性，就是调用一下服务端的TEST接口，返回success=true
     */
    private boolean mValidateURL = false;

    private ValidateUrlHelper mValidateHelper;

    private static DynamicsUrlRequestHelper instance = null;

    public static DynamicsUrlRequestHelper getInstance() {
        if (instance == null) {
            instance = new DynamicsUrlRequestHelper();
        }
        return instance;
    }

    private DynamicsUrlRequestHelper() {
        this.httpHelper = new WrapperHttpHelper(this);
        mValidateHelper = new ValidateUrlHelper();
        this.httpHelper.setUseRequestTag(false);
    }

    /**
     * @param key      可以随便取
     * @param jsonKeys 域名的JSON的key值
     * @param list     获取动态域名的url
     * @param useTest  是否需要验证url的有效性
     */
    public void putUrl(String key, String[] jsonKeys, List<String> list, boolean useTest) {
        UrlRequestModel model = new UrlRequestModel();
        model.key = key;
        model.list = list;
        model.jsonKeys = jsonKeys;
        model.useTest = useTest;
        mDomainList.add(model);
    }

    public void getUrl(Callback callback) {
        if (mDomainList.size() == 0) return;
        this.mCallback = callback;
        setRequestData();
        startRequest();
    }

    private void startRequest() {
        RequestFormBody body = new RequestFormBody(null);
        body.setUsePost(false);
        body.setRequestUrl(mCurrentRequestDomainList.get(0));
        httpHelper.startRequest(body);
    }

    private void setRequestData() {
        UrlRequestModel model = mDomainList.get(0);
        mRetryCount = 0;
        mCurrentUrlKey = model.key;
        mCurrentRequestDomainList = model.list;
        mJsonKeys = model.jsonKeys;
        mValidateURL = model.useTest;
    }

    @Override
    public void onSuccess(RequestContainer request, Object o) throws JSONException {
        JSONObject jo = (JSONObject) o;
        if (mValidateURL) {
            validateURL(jo);
        } else {
            responseSuccessGetUrl(jo);
        }
    }

    private void validateURL(final JSONObject jo) throws JSONException {
        for (String s : mJsonKeys) {
            String url = jo.getString(s);
            mValidateHelper.addUrl(url);
        }
        mValidateHelper.startValidateUrl(new ValidateUrlHelper.Callback() {
            @Override
            public void onSuccess() {
                responseSuccessGetUrl(jo);
            }

            @Override
            public void onFailed(String url, String msg) {
                requestNextUrl();
            }
        });
    }

    private void responseSuccessGetUrl(JSONObject jo) {
        for (String s : mJsonKeys) {
            String url = null;
            try {
                url = jo.getString(s);
            } catch (JSONException e) {
                requestNextUrl();
                return;
            }
            ApiURLManager.getInstance().putUrl(s + mCurrentUrlKey, url);
        }
        mDomainList.remove(0);
        if (mDomainList.size() == 0) {
            if (mCallback != null) {
                mCallback.onSuccess();
            }
        } else {
            if (mDomainList.size() > 0) {
                setRequestData();
                startRequest();
            }
        }
    }


    @Override
    public void onFailed(RequestContainer request, JSONObject jsonObject, boolean netFailed) throws Exception {
        if (mRetryCount < MAX_RETRY_COUNT) {
            mRetryCount++;
            Task.delay(500).continueWith(new Continuation<Void, Object>() {
                @Override
                public Object then(Task<Void> task) throws Exception {
                    startRequest();
                    return null;
                }
            }, Task.UI_THREAD_EXECUTOR);
        } else {
            requestNextUrl();
        }
    }

    private void requestNextUrl() {
        mCurrentRequestDomainList.remove(0);
        if (mCurrentRequestDomainList.size() > 0) {
            mRetryCount = 0;
            startRequest();
        } else {
            if (mCallback != null) {
                mCallback.onFailed(REQUEST_FAILED_URL_ERROR, "app初始化失败,请检查网络连接，或者退出app重试");
            }
        }
    }

    private class UrlRequestModel {
        String key = null;
        List<String> list = new ArrayList<>();
        String[] jsonKeys;

        /**
         * 是否需要测试该url可用性
         */
        boolean useTest;
    }

    public interface Callback {

        void onSuccess();

        void onFailed(int errorCode, String errorMsg);

    }
}
