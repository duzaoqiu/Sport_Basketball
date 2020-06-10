package com.bench.android.core.net.httpLoop;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.bench.android.core.net.http.IHttpResponseListener;
import com.bench.android.core.net.http.WrapperHttpHelper;
import com.bench.android.core.net.http.base.RequestContainer;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 轮询请求服务器，可以使用该类。
 *
 * @author zhouyi
 */
public class LoopService extends Service implements IHttpResponseListener {

    private HashMap<String, RequestContainer> mRequestParams = new HashMap<>();

    private HashMap<String, Timer> mTimers = new HashMap<>();

    private WrapperHttpHelper mHttpHelper;

    private Handler mHandler;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mHttpHelper = new WrapperHttpHelper(this);
        mHttpHelper.setUseRequestTag(false);
        EventBus.getDefault().register(this);
        mHandler = new Handler(Looper.getMainLooper());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "my_channel";
            NotificationChannel channel = new NotificationChannel(channelId, "通用通知", NotificationManager.IMPORTANCE_DEFAULT);

            ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(channel);
            Notification notification = new NotificationCompat.Builder(this, channelId)
                    .setContentTitle("")
                    .setContentText("").build();
            startForeground(1, notification);
        }
    }

    class MyTimerTask extends TimerTask {

        protected String mServiceName;

        public MyTimerTask(String serviceName) {
            this.mServiceName = serviceName;
        }

        @Override
        public void run() {

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
        Timer timer = new Timer();
        timer.schedule(new MyTimerTask(tempKey) {
            @Override
            public void run() {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        RequestContainer container = mRequestParams.get(mServiceName);
                        if (container != null) {
                            mHttpHelper.startRequest(container);
                        }
                    }
                });
            }
        }, 300, duration);

        mTimers.put(tempKey, timer);

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
        Timer timers = mTimers.get(requestKey);
        if (timers != null) {
            timers.cancel();
            mTimers.remove(requestKey);
        }
    }

    private void removeAllJob() {
        mRequestParams = new HashMap<>();
        for (Map.Entry<String, Timer> entry : mTimers.entrySet()) {
            entry.getValue().cancel();
        }
        mTimers = new HashMap<>();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
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
            Timer timer = mTimers.remove(tempKey);
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
        /*//这个操作主要为了，解决，循环请求过程中，如果RequestContainer中间参数有变化，

        ILoopSuccessInterceptor interceptor= (ILoopSuccessInterceptor) request.getTag();
        interceptor.onSuccess(request,o);*/
        EventBus.getDefault().post(new LoopMessage(request, o));
    }

    @Override
    public void onFailed(RequestContainer request, JSONObject jsonObject, boolean netFailed) throws Exception {

    }


}
