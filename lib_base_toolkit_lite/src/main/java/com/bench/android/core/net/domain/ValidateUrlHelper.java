package com.bench.android.core.net.domain;

import android.webkit.URLUtil;

import com.bench.android.core.net.http.HttpRequestHelper;
import com.bench.android.core.net.http.IHttpResponseCallBack;
import com.bench.android.core.net.http.RequestFormBody;
import com.bench.android.core.net.http.WrapperHttpHelper;
import com.bench.android.core.net.http.base.RequestContainer;
import com.bench.android.core.net.http.params.AppParam;
import com.bench.android.core.net.http.params.BaseRequestHttpName;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 验证url可用性已经有效性
 *
 * @author zhouyi
 */
public class ValidateUrlHelper implements IHttpResponseCallBack {

    private List<String> mUrlList = new ArrayList<>();

    private WrapperHttpHelper wrapperHttpHelper;

    private Callback mCallback;

    public ValidateUrlHelper() {
        this.wrapperHttpHelper = new WrapperHttpHelper(this);
    }

    public void addUrl(String url) {
        mUrlList.add(url);
    }

    public void startValidateUrl(Callback callback) {
        mCallback = callback;

        //验证url是否合法
        for (String url : mUrlList) {
            boolean networkUrl = URLUtil.isNetworkUrl(url);
            if (!networkUrl) {
                mUrlList.clear();
                callback.onFailed(url, "无效URL地址");
                return;
            }
        }
        startRequest();

    }

    private void startRequest() {
        ArrayList<RequestContainer> list = new ArrayList<>();
        for (String url : mUrlList) {
            RequestFormBody body = new RequestFormBody(TestURLEnum.TEST);
            body.setRequestUrl(url+AppParam.S_URL);
            list.add(body);
        }
        wrapperHttpHelper.startRequestList(list);
    }


    @Override
    public void onSuccess(RequestContainer request, Object o) throws JSONException {
    }

    @Override
    public void onFailed(RequestContainer request, JSONObject jsonObject, boolean netFailed) throws Exception {
    }

    @Override
    public void onStartRequest() {
    }

    @Override
    public void onFinishRequest(int stateCode) {
        mUrlList.clear();
        if (stateCode== HttpRequestHelper.CODE_SUCCESS) {
            mCallback.onSuccess();
        } else {
            mCallback.onFailed("", "域名请求返回错误");
        }
    }

    public interface Callback {
        void onSuccess();

        void onFailed(String url, String msg);
    }

    private enum TestURLEnum implements BaseRequestHttpName {
        TEST("TEST", AppParam.S_URL);

        private String name;
        private String url;

        TestURLEnum(String name, String url) {
            this.name = name;
            this.url = url;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public String getUrl() {
            return url;
        }
    }
}
