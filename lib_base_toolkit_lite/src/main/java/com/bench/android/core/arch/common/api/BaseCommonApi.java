package com.bench.android.core.arch.common.api;

import com.bench.android.core.net.http.HttpRequestHelper;
import com.bench.android.core.net.http.IHttpResponseCallBack;
import com.bench.android.core.net.http.WrapperHttpHelper;
import com.bench.android.core.net.http.base.RequestContainer;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * 需要在主线程调用，不要在子线程调用，这边线程不安全，子线程调用可能会出问题
 *
 * @author zhouyi
 */
public abstract class BaseCommonApi implements IHttpResponseCallBack, ICommonApi {

    /**
     * 空闲状态
     */
    private final int IDLE = 1000;

    /**
     * 运行状态
     */
    private final int RUNNING = 1000;

    private int mRequestStatus = 0;

    protected WrapperHttpHelper mRequestHelper = null;

    protected List<List<SimplePack>> mRequestList = null;

    protected List<SimplePack> mTempList = null;

    protected HashMap<String, RequestPack> mRequestMap = null;

    protected List<CommonApiValidateErrorModel> mValidateErrorList = new ArrayList<>();

    protected HashMap<String, CommonApiValidateCallback> mCallbackMap = new HashMap<>();

    public BaseCommonApi() {
        mRequestHelper = new WrapperHttpHelper(this);
        mRequestHelper.setUseRequestTag(false);
        mRequestList = new ArrayList<>();
        mTempList = new ArrayList<>();
        mRequestMap = new HashMap<>();
    }

    @Override
    public <T extends ICommonApi> T as(T t) {
        if (t == null) {
            throw new IllegalArgumentException("转换参数不能为空!!!");
        }
        BaseCommonApi t1 = (BaseCommonApi) t;
        t1.mRequestHelper = mRequestHelper;
        t1.mRequestList = mRequestList;
        t1.mTempList = mTempList;
        t1.mRequestMap = mRequestMap;
        t1.mValidateErrorList = mValidateErrorList;
        t1.mCallbackMap = mCallbackMap;
        return t;
    }

    /**
     * 作为队列请求,串行请求
     */
    @Override
    public <T extends ICommonApi> T asQueue() {
        if (mTempList.size() == 0) {
            return (T) this;
        }
        for (int i = 0; i < mTempList.size(); i++) {
            List<SimplePack> pack = new ArrayList<>();
            pack.add(mTempList.get(i));
            mRequestList.add(pack);
        }
        mTempList.clear();
        return (T) this;
    }


    /**
     * 作为列表并行请求
     *
     * @return
     */
    @Override
    public <T extends ICommonApi> T asList() {
        if (mTempList.size() == 0) {
            return (T) this;
        }
        List<SimplePack> list = new ArrayList<>();
        list.addAll(mTempList);
        mRequestList.add(list);
        mTempList.clear();
        return (T) this;
    }

    protected void addRequest(ISingleRequest request) {
        if (mValidateErrorList.size() > 0) {
            return;
        }
        mTempList.add(new SimplePack(request, this));
    }

    protected void addValidateError(CommonApiValidateErrorModel model) {
        mValidateErrorList.add(model);
    }

    @Override
    public void start() {
        startInternal(true, null, null);
    }

    /**
     * 如果请求的参数需要验证，需要在这里添加一个回调，目前只支持，如果一个错误了，就暂停后续的数据验证
     *
     * @param validateCallback 添加验证错误回调
     */
    @Override
    public void start(CommonApiValidateCallback validateCallback) {
        //如果验证错误了，就停止当前请求
        if (mValidateErrorList.size() > 0) {
            CommonApiValidateErrorModel errorModel = mValidateErrorList.get(0);
            validateCallback.onValidateError(errorModel);
            //清空错误处理
            mValidateErrorList.clear();
        } else {
            startInternal(true, null, validateCallback);
        }
    }


    /**
     * 是否是初始化开始请求
     *
     * @param initStart
     */
    private void startInternal(boolean initStart, String mapKey, CommonApiValidateCallback errorCallback) {
        //如果开始请求，但是请求列表为空，不进行后续操作
        if (initStart && mRequestList.size() == 0) {
            return;
        }

        //如果是初始化请求，构建一个新的包
        List<List<SimplePack>> requestList = null;
        RequestPack requestPack;
        if (initStart) {
            //创建一个请求包的tag，用于存储在HashMap中
            String tag = createTag();
            requestList = new ArrayList<>();
            requestList.addAll(mRequestList);
            for (List<SimplePack> l1 : requestList) {
                for (SimplePack s : l1) {
                    s.tag = tag;
                }
            }
            mRequestList.clear();
            requestPack = new RequestPack();
            requestPack.list = requestList;
            mRequestMap.put(tag, requestPack);
            mCallbackMap.put(tag, errorCallback);
        } else {
            requestPack = mRequestMap.get(mapKey);
            requestList = mRequestMap.get(mapKey).list;
        }

        if (requestList.size() > 0) {
            List<SimplePack> list = requestList.remove(0);
            requestPack.currentRequestCount = list.size();
            ArrayList<RequestContainer> requestFormBodies = new ArrayList<>();
            for (final SimplePack pack : list) {
                final RequestContainer requestBody = pack.request.getRequestBody();
                requestPack.addRequest(requestBody, pack.api);
                requestBody.setRequestTag(pack.tag);
                requestFormBodies.add(requestBody);
            }
            mRequestHelper.startRequestList(requestFormBodies);
        } else {
            //请求完成,需要移除请求队列
            mRequestMap.remove(mapKey);
            CommonApiValidateCallback remove = mCallbackMap.remove(mapKey);
            remove.onApiRequestFinish(HttpRequestHelper.CODE_SUCCESS);
        }
    }

    /**
     * 每一次请求都需要一个tag用于标识当前请求,方便管理
     *
     * @return
     */
    private String createTag() {
        return UUID.randomUUID().toString();
    }


    /**
     * 业务层,当请求返回成功之后，业务层如果执行完业务没问题，确认可以执行下一步的话，需要调用doNext
     *
     * @param body
     */
    @Override
    public void doNext(RequestContainer body) {
        RequestPack requestPack = mRequestMap.get(body.getRequestTag());
        requestPack.currentRequestCount--;
        //如果是列表请求，则需要所有列表请求完毕之后，才会执行后续请求
        if (requestPack.currentRequestCount == 0) {
            startInternal(false, body.getRequestTag(), null);
        }
    }


    /**
     * 业务层，如果执行完业务之后，或者请求返回false之后，不想要执行，之后的请求，需要调用failed方法
     *
     * @param model
     */
    @Override
    public void failed(ApiResponseFalseModel model) {
        RequestContainer body = model.request;
        JSONObject jo = model.data;
        if (mRequestMap.get(body.getRequestTag()) == null) {
            return;
        }
        //如果执行失败，则清空网络列表
        mRequestMap.remove(body.getRequestTag());

        //如果jo为空说明，说明取消当前请求，并不需要通过json去判断
        if(jo==null){
            //如果是网络错误，则显示错误界面
            if (model.netFailed) {
                try {
                    int errorCode = jo.getJSONObject("error").getInt("code");
                    CommonApiValidateCallback callback = mCallbackMap.remove(body.getRequestTag());
                    callback.onApiRequestFinish(errorCode);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                CommonApiValidateCallback callback = mCallbackMap.remove(body.getRequestTag());
                callback.onApiRequestFinish(HttpRequestHelper.CODE_SUCCESS);
            }
        }else{
            CommonApiValidateCallback callback = mCallbackMap.remove(body.getRequestTag());
            callback.onApiRequestFinish(HttpRequestHelper.CODE_SUCCESS);
        }

    }

    /**
     * 网络请求成功
     *
     * @param requestContainer
     * @param o
     */
    @Override
    public void onApiResponseSuccess(RequestContainer requestContainer, Object o) {

    }

    /**
     * 网络请求失败
     *
     * @param requestContainer
     * @param o
     */
    @Override
    public void onApiResponseFalse(RequestContainer requestContainer, Object o, boolean netError) {

    }

    /**
     * 如果服务端返回的success=false如果需要交个外部处理，则需要调用这个方法
     *
     * @param requestContainer
     * @param jo
     */
    public void notifyResponseFalse(RequestContainer requestContainer, JSONObject jo, boolean netError) {
        ApiResponseFalseModel model = new ApiResponseFalseModel(jo, this, requestContainer, netError);
        mCallbackMap.get(requestContainer.getRequestTag()).onApiResponseFalse(model);
    }


    private class RequestPack {
        int currentRequestCount;
        List<SingleRequestPack> currentRequestList = new ArrayList<>();
        List<List<SimplePack>> list;

        void addRequest(RequestContainer container, BaseCommonApi api) {
            currentRequestList.add(new SingleRequestPack(container, api));
        }
    }

    private class SingleRequestPack {
        RequestContainer request;
        BaseCommonApi api;

        public SingleRequestPack(RequestContainer request, BaseCommonApi api) {
            this.request = request;
            this.api = api;
        }
    }

    class SimplePack {
        ISingleRequest request;
        BaseCommonApi api;
        String tag;

        public SimplePack(ISingleRequest request, BaseCommonApi api) {
            this.request = request;
            this.api = api;
        }
    }

    @Override
    public void onStartRequest() {

    }

    @Override
    public void onFinishRequest(int stateCode) {

    }

    @Override
    public void onSuccess(RequestContainer request, Object o) throws JSONException {
        String requestTag = request.getRequestTag();
        RequestPack requestPack = mRequestMap.get(requestTag);
        if (requestPack == null) {
            return;
        }
        List<SingleRequestPack> currentRequestList = requestPack.currentRequestList;
        for (SingleRequestPack p : currentRequestList) {
            if (p.request == request) {
                p.api.onApiResponseSuccess(request, o);
                break;
            }
        }
    }

    @Override
    public void onFailed(RequestContainer request, JSONObject jsonObject, boolean netFailed) throws Exception {
        String requestTag = request.getRequestTag();
        RequestPack requestPack = mRequestMap.get(requestTag);
        if (requestPack == null) {
            return;
        }
        List<SingleRequestPack> currentRequestList = requestPack.currentRequestList;
        for (SingleRequestPack p : currentRequestList) {
            if (p.request == request) {
                p.api.onApiResponseFalse(request, jsonObject, netFailed);
                break;
            }
        }
    }
}
