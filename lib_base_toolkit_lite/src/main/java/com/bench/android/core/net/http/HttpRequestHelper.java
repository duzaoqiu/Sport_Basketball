package com.bench.android.core.net.http;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.Nullable;


import com.bench.android.core.net.domain.base.AppDomainManager;
import com.bench.android.core.net.http.base.OnHttpResponseListener;
import com.bench.android.core.net.http.base.RequestContainer;
import com.bench.android.core.net.http.httphelpinterceptor.HttpEncryptInterceptor;
import com.bench.android.core.net.http.httphelpinterceptor.InterceptorConstants;
import com.bench.android.core.net.http.params.BaseRequestHttpName;
import com.bench.android.core.net.http.params.HttpRequestResponseCode;
import com.bench.android.core.net.http.processor.HttpUtil;
import com.bench.android.core.net.http.processor.IHttpProcessor;
import com.bench.android.core.net.http.processor.IHttpResponseHandler;
import com.bench.android.core.util.LogUtils;
import com.google.gson.JsonParseException;

import org.json.JSONObject;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import okhttp3.internal.Util;


/**
 * tip:轮询不要和普通请求使用相同的HttpHelper，不然或出现错误。
 * 请使用这个服务{@link com.bench.android.core.net.httpLoop.LoopService}
 *
 * @author zhouyi
 * @date 2016/6/28 20:58
 */
public class HttpRequestHelper implements IHttpProcessor {

    public static final int CODE_SUCCESS = -1;
    public static final int CODE_NET_ERROR = 10000;
    public static final int CODE_SERVER_ERROR = 10001;

    private final String TAG = getClass().getSimpleName();

    private OnHttpResponseListener mExecutor;

    /**
     * 发起一次请求，则生成一个Tag。如果返回的数据的tag与请求的tag不一致，则抛弃这个请求。
     */
    private String mRequestTag = String.valueOf(System.currentTimeMillis());

    private String mTag;

    private int mHaveRequestCount = 0;

    private int mNeedRequestCount = 0;

    /**
     * 如果接口需要加密，会在接口请求失败之后，将接口存在该map当中，等密钥上传成功之后，将接口继续请求
     */
    private HashMap<BaseRequestHttpName, RequestContainer> mWaitToRetryMap = new HashMap<>();

    /**
     * 是否使用请求拦截，当第一次请求发起之后，很快发起第二次请求，是否拦截第一次请求，防止第一次请求回调出去
     */
    private boolean mUseRequestTag = true;

    /**
     * 网络请求状态码，用于网络状态显示
     */
    private int stateCode = CODE_SUCCESS;

    /**
     * 是否执行同步请求
     */
    private boolean mSyncRequest = false;

    private final static List<HttpRequestInterceptor> sRequestInterceptor = new ArrayList<>();

    private final static Handler sHandler = new Handler(Looper.getMainLooper());

    static {
        //默认拦截器添加
        sRequestInterceptor.add(new HttpEncryptInterceptor());
    }

    public HttpRequestHelper(OnHttpResponseListener executor, String tag) {
        this.mExecutor = executor;
        this.mTag = tag;
    }

    public HttpRequestHelper(OnHttpResponseListener executor) {
        this.mExecutor = executor;
        this.mTag = executor != null ? executor.getClass().getSimpleName() : null;
    }

    /**
     * 是否执行同步请求，如果执行同步，则在同一个线程
     *
     * @param sync
     */
    public void setSyncRequest(boolean sync) {
        mSyncRequest = sync;
    }

    public void startRequest(RequestContainer requestEnum) {
        List<RequestContainer> list = new ArrayList<>();
        list.add(requestEnum);
        startRequest(list);
        stateCode = CODE_SUCCESS;
    }

    /**
     * 如果多次点击操作，只取最后一次有效的，因为发起一次请求，会记录一次时间戳，如果发现时间戳变化了
     * 就抛弃这次请求，如果使用轮询请求，请使用这个服务LoopService
     *
     * @param list
     */
    public void startRequest(List<RequestContainer> list) {
        mRequestTag = String.valueOf(System.currentTimeMillis());
        mHaveRequestCount = 0;
        mNeedRequestCount = calculateRequestCount(list);
        if (mExecutor != null) {
            mExecutor.executorStart();
        }
        for (int i = 0; i < list.size(); i++) {
            list.get(i).setRequestTimeStamp(mRequestTag);
            httpPost(list.get(i), createIHttpResponseHandler(list.get(i)), mTag);
        }
    }


    /**
     * 增加多个请求的话，则需要使用多线程，防止，单线程阻塞。
     * 由于依赖请求后面还有请求，则要继续添加
     */
    private void addRequest(List<RequestContainer> list) {
        for (int i = 0; i < list.size(); i++) {
            list.get(i).setRequestTimeStamp(mRequestTag);
            //由于有多个发起，所以使用多个线程，进行请求，提高开发效率
            httpPost(list.get(i), createIHttpResponseHandler(list.get(i)), mTag);
        }
    }

    /**
     * 如果是继续添加一个请求的话，使用同步，减少线程创建花费的时间
     *
     * @param requestEnum
     */
    private void addRequest(RequestContainer requestEnum) {
        requestEnum.setRequestTimeStamp(mRequestTag);
        //单个请求，直接放在当前线程当中
        doPost(requestEnum, createIHttpResponseHandler(requestEnum), mTag);
    }

    private int calculateRequestCount(List<RequestContainer> list) {
        return list.size();
    }


    /**
     * 是否已经请求完毕
     *
     * @return
     */
    public boolean requestFinish() {
        return mNeedRequestCount <= mHaveRequestCount;
    }


    class MyHttpResponseHandler implements IHttpResponseHandler {

        private final RequestContainer request;

        public MyHttpResponseHandler(RequestContainer requestContainer) {
            this.request = requestContainer;
        }

        @Override
        public void executorSuccess(JSONObject jo) {
            //对jo进行处理，如果没任何问题，返回给下一步
            JSONObject json = jo;
            for (HttpRequestInterceptor interceptor : sRequestInterceptor) {
                json = interceptor.onResponseSuccess(request, json);
                if (json == null) {
                    responseFailed(request, -1, new JsonParseException(interceptor.getClass().getName() + "json解析错误"));
                    return;
                }
            }
            responseSuccess(request, json);
        }

        @Override
        public void executorFalse(JSONObject jo) {
            JSONObject json = jo;
            for (HttpRequestInterceptor interceptor : sRequestInterceptor) {
                json = interceptor.onResponseFalse(request, json);
                if (json == null) {
                    responseFailed(request, -1, new JsonParseException("json解析错误"));
                    return;
                } else if (json == InterceptorConstants.RETRY_JSON) {
                    addRequest(request);
                    return;
                } else if (json == InterceptorConstants.FAILED_JSON) {
                    responseFailed(request, -1, null);
                    return;
                }
            }
            responseFalse(request, json);
        }

        @Override
        public void executorFailed(int code, Exception e) {
            responseFailed(request, code, e);
        }
    }

    private IHttpResponseHandler createIHttpResponseHandler(final RequestContainer request) {
        return new MyHttpResponseHandler(request);
    }

    private void responseSuccess(final RequestContainer request, final JSONObject jo) {
        //网络请求成功切面
        sHandler.post(new Runnable() {
            @Override
            public void run() {
               // PointCutManager.getInstance().executeAdvice(PointCutConstants.POINTCUT_HTTP_REQUEST_HELPER_ON_RESPONSE, this, new Object[]{request, jo});
            }
        });

        if (!mRequestTag.equals(request.getRequestTimeStamp()) && mUseRequestTag) {
            return;
        }
        mHaveRequestCount++;

        if (mSyncRequest) {
            mExecutor.executorSuccess(request, jo);
        } else {
            sHandler.post(new Runnable() {
                @Override
                public void run() {
                    mExecutor.executorSuccess(request, jo);
                }
            });
        }

        if (request.getSubDependencyListener() != null) {
            List<RequestContainer> list = request.nextExecute();
            mNeedRequestCount += list.size();
            addRequest(list);
            return;
        }
        if (mHaveRequestCount == mNeedRequestCount) {
            if (mSyncRequest) {
                setSingleRequestFinish(request);
                mExecutor.executorFinish(stateCode);
            } else {
                sHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        setSingleRequestFinish(request);
                        mExecutor.executorFinish(stateCode);
                    }
                });
            }
        }
    }

    private void responseFalse(final RequestContainer request, final JSONObject jo) {
        sHandler.post(new Runnable() {
            @Override
            public void run() {
               // PointCutManager.getInstance().executeAdvice(PointCutConstants.POINTCUT_HTTP_REQUEST_HELPER_ON_RESPONSE, this, new Object[]{request, jo});

            }
        });
        if (!mRequestTag.equals(request.getRequestTimeStamp()) && mUseRequestTag) {
            return;
        }
        mHaveRequestCount++;
        if (mSyncRequest) {
            mExecutor.executorFalse(request, jo);
        } else {
            sHandler.post(new Runnable() {
                @Override
                public void run() {
                    mExecutor.executorFalse(request, jo);
                }
            });
        }


        if (mHaveRequestCount == mNeedRequestCount) {
            if (mSyncRequest) {
                setSingleRequestFinish(request);
                mExecutor.executorFinish(stateCode);
            } else {
                sHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        setSingleRequestFinish(request);
                        mExecutor.executorFinish(stateCode);
                    }
                });
            }

        }

    }


    private void responseFailed(final RequestContainer request, final int code, final Exception e) {
        if (!mRequestTag.equals(request.getRequestTimeStamp()) && mUseRequestTag) {
            return;
        }
        //UnknownHostException 的优先级为最高
        //如果有一个请求失败，说明当前请求没有完全成功
        if (e instanceof UnknownHostException) {
            stateCode = CODE_NET_ERROR;
        } else {
            if (stateCode != CODE_NET_ERROR) {
                stateCode = CODE_SERVER_ERROR;
            }
        }
        mHaveRequestCount++;

        //如果网络请求的返回代码大于0，说明网络连接没有问题，否则通过本地判断是什么错误
        final int[] netCode = new int[]{code};
        if (code <= 0) {
            netCode[0] = stateCode;
        }

        if (mSyncRequest) {
            mExecutor.executorFailed(request, netCode[0], e);
        } else {
            sHandler.post(new Runnable() {
                @Override
                public void run() {
                    mExecutor.executorFailed(request, netCode[0], e);
                }
            });
        }
        if (mHaveRequestCount == mNeedRequestCount) {
            if (mSyncRequest) {
                setSingleRequestFinish(request);
                mExecutor.executorFinish(stateCode);
            } else {
                sHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        setSingleRequestFinish(request);
                        mExecutor.executorFinish(stateCode);
                    }
                });
            }
        }
    }


    /**
     * @param requestContainer
     */
    private void setSingleRequestFinish(RequestContainer requestContainer) {
        //一个按钮如果发球请求的时候，不希望该按钮还有用处，可以在网络初始化的时候设置为false
        //请求完成之后，将该按钮设置为有效
        if (requestContainer.getClickView() != null) {
            requestContainer.getClickView().setEnabled(true);
        }
    }


    @Override
    public void httpPost(final RequestContainer requestContainer, final IHttpResponseHandler callback, final String activityName) {
        if (mSyncRequest) {
            doPost(requestContainer, callback, activityName);
        } else {
            //开始在线程池里面执行
            executorService().execute(new Runnable() {
                @Override
                public void run() {
                    doPost(requestContainer, callback, activityName);
                }
            });
        }
    }

    private void doPost(final RequestContainer requestContainer, IHttpResponseHandler callback, String activityName) {
        //多域名添加的
        if (AppDomainManager.userDynamicDomain && AppDomainManager.getInstance().getApiDomain() == null) {
            if (mSyncRequest) {
                mExecutor.executorFailed(requestContainer, HttpRequestResponseCode.DOMAIN_NULL, new Exception());
                stateCode = CODE_NET_ERROR;
                mExecutor.executorFinish(stateCode);
            } else {
                sHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mExecutor.executorFailed(requestContainer, HttpRequestResponseCode.DOMAIN_NULL, new Exception());
                        stateCode = CODE_NET_ERROR;
                        mExecutor.executorFinish(stateCode);
                    }
                });
            }
            return;
        }

        if (requestContainer.getClickView() != null) {
            requestContainer.getClickView().setEnabled(false);
        }

        //执行一次处理器，之后就需要将上传的参数，进行加密
        removeSignKey(requestContainer.getFiledsMap());
        HttpUtil.getDefault().executeInterceptors(requestContainer);
        if (requestContainer.getFakeResponse() != null) {
            responseFalse(requestContainer, requestContainer.getFakeResponse());
            return;
        }

        //内部拦截器调用
        RequestContainer tempRequestContainer = requestContainer;
        for (HttpRequestInterceptor interceptor : sRequestInterceptor) {
            tempRequestContainer = interceptor.postRequest(tempRequestContainer);
            if (tempRequestContainer == null) {
                LogUtils.e(requestContainer.getRequestEnum().getName() + "网络请求错误");
                return;
            }
        }
        HttpUtil.getDefault().httpPost(tempRequestContainer, callback, activityName);
    }

    private void removeSignKey(HashMap<String, String> map) {
        if (map.size() > 0) {
            map.remove("sign");
        }
    }

    public boolean isUseRequestTag() {
        return mUseRequestTag;
    }

    public void setUseRequestTag(boolean useRequestTag) {
        this.mUseRequestTag = useRequestTag;
    }

    /**
     * 取消对应tag的请求
     *
     * @param tag
     */
    @Override
    public void cancelHttpCall(String tag) {
        HttpUtil.getDefault().cancelHttpCall(tag);
    }

    /**
     * 取消所有请求
     */
    @Override
    public void cancelAllHttpCall() {
        HttpUtil.getDefault().cancelAllHttpCall();
    }

    public void onDestroy() {
        mExecutor = null;
    }

    //-----------------------添加自己的线程池----------------------------------

    /**
     * Executes calls. Created lazily.
     */
    private @Nullable
    static ExecutorService executorService;

    public synchronized static ExecutorService executorService() {
        if (executorService == null) {
            executorService = new ThreadPoolExecutor(0, Integer.MAX_VALUE, 60, TimeUnit.SECONDS,
                    new SynchronousQueue<Runnable>(), Util.threadFactory("OkHttp Dispatcher", false));
        }
        return executorService;
    }


}
