package com.bench.android.core.service;

import android.os.Handler;
import android.os.Looper;

import com.alibaba.android.arouter.facade.annotation.ServiceImpl;
import com.bench.android.core.net.http.IHttpResponseListener;
import com.bench.android.core.net.http.WrapperHttpHelper;
import com.bench.android.core.net.http.base.RequestContainer;
import com.bench.android.core.net.httpLoop.AddJobMessage;
import com.bench.android.core.net.httpLoop.LoopMessage;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 替代{@link com.bench.android.core.net.httpLoop.LoopService}
 * 由于LoopService继承Service,导致app退到后台，在Android 8.0以上的
 * 手机会出现通知栏，用户体验不好。
 *
 * @author zhouyi
 */
@ServiceImpl
public class CustomLoopService implements ICustomLoopService, IHttpResponseListener {

    private HashMap<String, RequestContainer> mRequestParams = new HashMap<>();

    private HashMap<String, ScheduledThreadPoolExecutor> mTimers = new HashMap<>();

    private WrapperHttpHelper mHttpHelper;

    private Handler mHandler;

    public void onInit() {
        mHttpHelper = new WrapperHttpHelper(this);
        mHttpHelper.setUseRequestTag(false);
        EventBus.getDefault().register(this);
        mHandler = new Handler(Looper.getMainLooper());
    }

    class MyTimerTask extends ScheduledThreadPoolExecutor {

        private String mServiceName;

        public MyTimerTask(int corePoolSize, String serviceName) {
            super(corePoolSize);
            this.mServiceName = serviceName;
        }

        public String getServiceName() {
            return mServiceName;
        }
    }

    private void putJob(RequestContainer requestContainer, int duration) {
        String tempKey = createRequestKey(requestContainer);
        //如果包含相同的服务名和相同的请求参数，就不添加该任务，任务栈中已经包含了该任务
        for (String request : mTimers.keySet()) {
            if (request.equals(tempKey)) {
                return;
            }
        }
        mRequestParams.put(tempKey, requestContainer);


        final MyTimerTask schedule = new MyTimerTask(1, tempKey);
        schedule.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        RequestContainer container = mRequestParams.get(schedule.getServiceName());
                        if (container != null) {
                            mHttpHelper.startRequest(container);
                        }
                    }
                });
            }
        }, 300, duration, TimeUnit.MILLISECONDS);

        mTimers.put(tempKey, schedule);
    }

    /**
     * 生成一个请求key，通过参数和请求服务名组成一个key
     */
    private String createRequestKey(RequestContainer requestContainer) {
        //当使用同一个RequestContainer,返回旧的key
        //因为需要签名时,第一次请求后会在拦截器添加其余的参数,所以会导致第一次和之后的key不一致.
        if (mRequestParams.containsValue(requestContainer)) {
            for (Map.Entry<String, RequestContainer> entry : mRequestParams.entrySet()) {
                if (Objects.equals(entry.getValue(), requestContainer)) {
                    return entry.getKey();
                }
            }
        }
        HashMap<String, String> filedsMap = requestContainer.getFiledsMap();
        StringBuilder sb = new StringBuilder();

        sb.append(requestContainer.getRequestEnum().getName() + "_");
        for (String key : filedsMap.keySet()) {
            //时间戳不加到key中，因为每次发起请求都会改变时间戳，
            // 服务端会对时间戳做校验，多次使用相同的时间戳，会返回错误
            if ("timestamp".equals(key)) {
                continue;
            }
            String v = filedsMap.get(key);
            sb.append(v);
            sb.append("_");
        }
        return sb.toString();
    }

    private void removeJob(RequestContainer requestContainer) {
        String requestKey = createRequestKey(requestContainer);
        mRequestParams.remove(requestKey);
        ScheduledThreadPoolExecutor timers = mTimers.get(requestKey);
        if (timers != null) {
            timers.shutdown();
            mTimers.remove(requestKey);
        }
    }

    private void removeAllJob() {
        mRequestParams = new HashMap<>();
        for (Map.Entry<String, ScheduledThreadPoolExecutor> entry : mTimers.entrySet()) {
            entry.getValue().shutdown();
        }
        mTimers = new HashMap<>();
    }

    @Subscribe
    public void onEvent(AddJobMessage message) {
        switch (message.getWhat()) {
            case AddJobMessage.ADD_JOB:
                putJob(message.getMessage(), message.getDuration());
                break;
            case AddJobMessage.REMOVE_JOB:
                removeJob(message.getMessage());
                break;
            case AddJobMessage.CHANGE_PARAMS_JOB:
                changeRequestParams(message.getMessage(), message.getDuration());
                break;
            case AddJobMessage.REMOVE_ALL_JOB:
                removeAllJob();
                break;
            default:
                break;
        }
    }

    private void changeRequestParams(RequestContainer requestContainer, int duration) {
        String tempKey = null;
        for (String s : mRequestParams.keySet()) {
            if (s.contains(requestContainer.getRequestEnum().getName())) {
                tempKey = s;
                break;
            }
        }
        if (tempKey != null) {
            //mRequestParams.put(tempKey, requestContainer);
            mRequestParams.remove(tempKey);
            mRequestParams.put(createRequestKey(requestContainer), requestContainer);
            ScheduledThreadPoolExecutor timer = mTimers.remove(tempKey);
            if (timer != null) {
                mTimers.put(createRequestKey(requestContainer), timer);
            }
        } else {
            //没有这个任务则添加这个任务
            putJob(requestContainer, duration);
        }
    }

    @Override
    public void onSuccess(RequestContainer request, Object o) throws JSONException {
        EventBus.getDefault().post(new LoopMessage(request, o));
    }

    @Override
    public void onFailed(RequestContainer request, JSONObject jsonObject, boolean netFailed) throws Exception {

    }



}
