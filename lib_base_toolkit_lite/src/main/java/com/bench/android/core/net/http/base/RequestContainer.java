package com.bench.android.core.net.http.base;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.Nullable;

import com.bench.android.core.net.http.body.Body;
import com.bench.android.core.net.http.params.BaseRequestHttpName;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by zhouyi on 2016/7/8 12:16.
 */
public abstract class RequestContainer implements Body {

    /**
     * 发起网络请求的个性化头部
     */
    private HashMap<String, String> mRequestHeader = null;

    protected HashMap<String, String> map = new HashMap<>();

    protected BaseRequestHttpName requestEnum = null;

    //是否设置埋点
    public boolean isPutDbParams;

    //如果一个服务通过不同的type返回相应数据，则为了识别当前是哪种请求，可以设置一个tag
    protected Object tag;

    /**
     * 这个tag外部不需要设置，主要是方便通用api请求使用的，外部如果需要使用的话，直接使用tag就行了。
     */
    private String mRequestTag;

    //是否签名
    protected boolean isSign;

    //签名方式
    public int signType;

    protected String requestTimeStamp;

    protected String requestUrl;

    protected List<RequestContainer> mSubDependencyList = new ArrayList<>();

    private Class genericClass;

    private View mClickView;

    //是否使用加密请求
    private boolean mEncrypt = true;

    /**
     * 是否是post请求，我们公司默认使用post请求，部分其他公司的地址，可能会强制必须使用get
     * ，例如使用阿里云获取动态域名
     */
    private boolean usePost = true;
    /**
     * 构造的响应,json格式:
     * {
     * "success":false,
     * "error":{
     * "name":"",
     * "message":""
     * },
     * "nowTimestamp":1563335289345,
     * "nowDate":"2019-07-17 11:48:09",
     * "jumpLogin":false,
     * }
     */
    @Nullable
    private JSONObject fakeResponse;


    /**
     * 是否使用json作为Request的body作为请求参数，默认情况是key-value的方式，如果是
     * 前后端分离的方式，则使用json作为请求体。
     */
    protected boolean isUseJsonRequest;

    public RequestContainer(BaseRequestHttpName requestEnum, boolean sign) {
        this(requestEnum, sign, false);
    }

    /**
     * @param requestEnum   请求的服务名
     * @param sign          是否需要签名，必须登录才能获取数据
     * @param isPutDbParams 是否需要设置埋点
     */
    public RequestContainer(BaseRequestHttpName requestEnum, boolean sign, boolean isPutDbParams) {
        this.requestEnum = requestEnum;
        this.isSign = sign;
        this.isPutDbParams = isPutDbParams;
//        if (requestEnum != null) {
//            put("service", requestEnum.getName());
//        }
    }

    public String encodeValue(String value) {
        if (!TextUtils.isEmpty(value)) {
            //这边做一下字符转换第一个空格是"no-breaking space" utf-8编码%C2%A0,服务端无法识别
            //需要转换为第二个正常的空格,utf-8编码%20,服务端可以识别
            //输入这个空格的快捷键是alt+数字键0160
            value = value.replace(" ", " ");
        }

        return value;
    }

    public void setClickView(View view) {
        mClickView = view;
    }

    public View getClickView() {
        return mClickView;
    }

    /**
     * 设置是否需要埋点
     *
     * @param putDbParams
     */
    public void setPutDbParams(boolean putDbParams) {
        isPutDbParams = putDbParams;
    }

    public boolean isSign() {
        return isSign;
    }

    public int getSignType() {
        return signType;
    }

    public Object getTag() {
        return tag;
    }

    public void setTag(Object tag) {
        this.tag = tag;
    }

    public BaseRequestHttpName getRequestEnum() {
        return requestEnum;
    }

    public void setRequestEnum(BaseRequestHttpName requestEnum) {
        this.requestEnum = requestEnum;
    }

    public String getRequestTimeStamp() {
        return requestTimeStamp;
    }

    public void setRequestTimeStamp(String requestTimeStamp) {
        this.requestTimeStamp = requestTimeStamp;
    }

    public void setGenericClaz(Class genericClass) {
        this.genericClass = genericClass;
    }

    public Class getGenericClaz() {
        return genericClass;
    }

    public String getRequestUrl() {
        return requestUrl;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    @Nullable
    public JSONObject getFakeResponse() {
        return fakeResponse;
    }

    public void setFakeResponse(@Nullable JSONObject fakeResponse) {
        this.fakeResponse = fakeResponse;
    }

    public List<RequestContainer> getSubDependencyList() {
        return mSubDependencyList;
    }

    public RequestContainer addSubDependencyRequest(final RequestContainer requestContainer) {
        setSubDependencyListener(new SubDependencyListener() {
            @Override
            public void execute(List<RequestContainer> nextRequestList) {
                mSubDependencyList.add(requestContainer);
            }
        });
        return this;
    }

    public RequestContainer addSubDependencyRequest(final List<RequestContainer> requestContainerList) {
        setSubDependencyListener(new SubDependencyListener() {
            @Override
            public void execute(List<RequestContainer> nextRequestList) {
                mSubDependencyList.add((RequestContainer) requestContainerList);
            }
        });
        return this;
    }

    public int getInt(String key) {
        return Integer.parseInt(getFiledsMap().get(key));
    }

    public String getString(String key) {
        return getFiledsMap().get(key);
    }

    public boolean getBoolean(String key) {
        return Boolean.parseBoolean(getFiledsMap().get(key));
    }

    public HashMap<String, String> getFiledsMap() {
//        if (!map.containsKey("timestamp")) {
//            map.put("timestamp", String.valueOf(System.currentTimeMillis()));
//        }
        return map;
    }

    public void showNetworkTimeoutTip(Context applicationContext) {
    }

    /**
     * 有依赖请求的服务名以及对应的回调
     */
    private HashMap<BaseRequestHttpName, SubDependencyListener> mSubDependencyListenerMap = new HashMap<>();

    /**
     * @param requestEnum           接下来还有依赖请求的枚举
     * @param subDependencyListener 依赖请求的监听
     * @return
     */
    public RequestContainer setSubDependencyListener(BaseRequestHttpName requestEnum, SubDependencyListener subDependencyListener) {
        if (!mSubDependencyListenerMap.containsKey(requestEnum)) {
            mSubDependencyListenerMap.put(requestEnum, subDependencyListener);
        }
        return this;
    }

    /**
     * 添加依赖于当前RequestContainer的请求
     *
     * @param subDependencyListener
     * @return
     */
    public RequestContainer setSubDependencyListener(SubDependencyListener subDependencyListener) {
        setSubDependencyListener(requestEnum, subDependencyListener);
        return this;
    }

    private void setSubDependencyListenerMap(HashMap<BaseRequestHttpName, SubDependencyListener> mSubDependencyListenerMap) {
        this.mSubDependencyListenerMap = mSubDependencyListenerMap;
    }

    public SubDependencyListener getSubDependencyListener() {
        return mSubDependencyListenerMap.get(requestEnum);
    }

    /**
     * 执行依赖请求,并把下个依赖的子请求放进去
     */
    public List<RequestContainer> nextExecute() {
        SubDependencyListener subDependencyListener = mSubDependencyListenerMap.remove(requestEnum);
        subDependencyListener.execute(mSubDependencyList);
        for (RequestContainer requestContainer : mSubDependencyList) {
            if (mSubDependencyListenerMap.containsKey(requestContainer.requestEnum)) {
                requestContainer.setSubDependencyListenerMap(mSubDependencyListenerMap);
            }
        }
        return mSubDependencyList;
    }

    public boolean isEncrypt() {
        return mEncrypt;
    }

    public void setEncrypt(boolean encrypt) {
        this.mEncrypt = encrypt;
    }

    //清空请求参数
    public void clearRequestMap() {
        map.clear();
    }

    public boolean isUsePost() {
        return usePost;
    }

    public void setUsePost(boolean usePost) {
        this.usePost = usePost;
    }

    public String getRequestTag() {
        return mRequestTag;
    }

    public void setRequestTag(String requestTag) {
        this.mRequestTag = requestTag;
    }

    public void setUseJsonRequest(boolean useJsonRequest) {
        this.isUseJsonRequest = useJsonRequest;
    }

    public HashMap<String, String> getRequestHeader() {
        return mRequestHeader;
    }

    public void setRequestHeader(HashMap<String, String> requestHeader) {
        this.mRequestHeader = requestHeader;
    }

    public interface SubDependencyListener {
        void execute(List<RequestContainer> nextRequestList);
    }


}
