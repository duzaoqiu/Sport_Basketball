package com.bench.android.core.net.http;

import com.bench.android.core.net.LoadSirManager;
import com.bench.android.core.net.http.base.OnHttpResponseListener;
import com.bench.android.core.net.http.base.RequestContainer;
import com.bench.android.core.net.http.params.AppParam;
import com.bench.android.core.util.LogUtils;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * Created by qiaomu on 2017/6/7.
 */

public class WrapperHttpHelper implements OnHttpResponseListener {

    protected HttpRequestHelper helper;
    private IHttpResponseListener listener;

    /**
     * GSON解析数据
     */
    private static final Gson GSON = new Gson();

    private String tag;

    private boolean mUseRequestTag = true;

    public WrapperHttpHelper(IHttpResponseListener listener) {
        this(listener, listener == null ? null : String.valueOf(listener.hashCode()));
    }

    //tag为http请求标识
    public WrapperHttpHelper(IHttpResponseListener listener, String tag) {
        this.helper = new HttpRequestHelper(this, tag);
        this.listener = listener;
        this.tag = tag;
        //this.gson = new GsonBuilder().create();
    }

    /**
     * 请求开始
     */
    @Override
    public void executorStart() {
        if (listener == null) {
            return;
        }
        if (listener instanceof IHttpResponseCallBack) {
            ((IHttpResponseCallBack) listener).onStartRequest();
        }

    }

    @Override
    public void executorSuccess(RequestContainer request, JSONObject jo) {
        if (listener == null) {
            return;
        }
        try {
            if (request.getGenericClaz() != null && jo != null) {
                Object o = GSON.fromJson(jo.toString(), request.getGenericClaz());
                listener.onSuccess(request, o);
            } else {
                listener.onSuccess(request, jo);
            }
        } catch (JsonSyntaxException e) {
            LogUtils.e("http", "Gson解析出错了,请求链接:" + request.getRequestEnum() + "---错误信息:" + e.toString());
        } catch (JSONException e) {
            LogUtils.e("http", "json解析出错" + " 请求链接" + request.getRequestEnum() + "---错误信息" + e.toString());
        } catch (Exception e) {
            LogUtils.e("http", e.toString());
        }
    }

    @Override
    public void executorFalse(RequestContainer request, JSONObject jo) {
        if (listener != null) {
            try {
                listener.onFailed(request, jo, false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void executorFailed(RequestContainer request, int code, Exception e) {
        if (listener != null) {
            try {
                JSONObject jsonObject = new JSONObject();
                JSONObject errorObject = new JSONObject();
                errorObject.put("name", "netError");
                errorObject.put("message", "网络或者服务器开小差了~稍后再试吧" + code);
                errorObject.put("code", code);
                jsonObject.put("error", errorObject);
                listener.onFailed(request, jsonObject, true);
            } catch (Exception e1) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void executorFinish(int stateCode) {
        if (listener == null) {
            return;
        }
        if (listener instanceof IHttpResponseCallBack) {
            LoadSirManager.getInstance().onFinishRequest(tag, stateCode);
            ((IHttpResponseCallBack) listener).onFinishRequest(stateCode);
        }
    }

    /**
     * 单个网络请求，或者链式有依赖请求的使用此方法
     * <p>
     * 链式依赖展示如下:
     * RequestContainer requestContainer=new RequestContainer();
     * requestContainer.setSubDependencyListener( new SubDependencyListener {
     * void execute(List<RequestContainer> nextRequestList){
     * <p>
     * }
     * });
     *
     * @param container
     */
    public void startRequest(RequestContainer container) {
        helper.setUseRequestTag(mUseRequestTag);
        helper.startRequest(container);
    }

    /**
     * 多个依赖并行请求,添加依赖请求与单个请求一致
     *
     * @param list
     */
    public void startRequestList(ArrayList<RequestContainer> list) {
        helper.setUseRequestTag(mUseRequestTag);
        helper.startRequest(list);
    }

    /*ftapi */
    public void startRequestFT(RequestContainer container) {
        container.setRequestUrl(AppParam.FT_API_DOMAIN + AppParam.FT_API_S_URL);
        helper.startRequest(container);
    }

    /**
     * 设置是否是同步请求
     *
     * @param sync
     */
    public void setSyncRequest(boolean sync) {
        helper.setSyncRequest(sync);
    }

    public boolean isRequestFinished() {
        return helper.requestFinish();
    }

    public boolean isUseRequestTag() {
        return mUseRequestTag;
    }

    public void setUseRequestTag(boolean useRequestTag) {
        this.mUseRequestTag = useRequestTag;
    }

    public void onDestroy() {
        helper.cancelHttpCall(tag);
        helper.onDestroy();
        listener = null;
    }

}
