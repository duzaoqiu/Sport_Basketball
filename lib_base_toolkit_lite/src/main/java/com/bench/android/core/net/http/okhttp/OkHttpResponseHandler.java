package com.bench.android.core.net.http.okhttp;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.android.library.BuildConfig;
import com.bench.android.core.net.http.processor.IHttpResponseHandler;
import com.bench.android.core.util.LogUtils;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by zhouyi on 2017/5/27.
 */

public abstract class OkHttpResponseHandler implements Callback, IHttpResponseHandler {

    //对应HTTP的状态码
    private static final int UNAUTHORIZED = 401;
    private static final int FORBIDDEN = 403;
    private static final int NOT_FOUND = 404;
    private static final int REQUEST_TIMEOUT = 408;
    private static final int INTERNAL_SERVER_ERROR = 500;
    private static final int BAD_GATEWAY = 502;
    private static final int SERVICE_UNAVAILABLE = 503;
    private static final int GATEWAY_TIMEOUT = 504;

    //300>code>200,请求成功,区分success true/false
    private static final int REQUEST_SUCCESS = 1001;
    private static final int REQUEST_FALSE = 1002;
    //请求失败
    private static final int REQUEST_FAIL = 0;


    private Handler requestHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            try {
                switch (msg.what) {
                    case REQUEST_SUCCESS:
                        executorSuccess((JSONObject) msg.obj);
                        break;
                    case REQUEST_FALSE:
                        executorFalse((JSONObject) msg.obj);
                        break;
                    case REQUEST_FAIL:
                        Exception exception;
                        if (msg.obj instanceof Exception) {
                            exception = (Exception) msg.obj;
                        } else if (msg.obj != null) {
                            //比如404,502时,msg.obj 为 String
                            exception = new Exception(msg.obj.toString());
                        } else {
                            exception = new Exception();
                        }
                        executorFailed(msg.arg1, exception);
                        break;
                    default:
                        break;
                }
                super.handleMessage(msg);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };


    @Override
    public void onFailure(Call call, IOException e) {
        if (BuildConfig.DEBUG) {
            Log.e("OkHttpHandler_Failure: ", e == null ? "failure, but exception is null" : e.toString());
        }
        if (call.isCanceled()) {
            //如果是主动取消的情况下,什么都不做
        } else {
            //其他情况下
            Message message = Message.obtain(requestHandler, REQUEST_FAIL);
            message.obj = e;
            requestHandler.sendMessage(message);
        }

    }

    @Override
    public void onResponse(Call call, Response response) {
        int responseCode = -1;
        Object obj = null;
        int code = -1;
        try {
            String body = response.body().string();
            responseCode = response.code();
            showLog(response, body);
            if (response.isSuccessful()) {
                JSONObject responseJson = new JSONObject(body);
                boolean success = false;
                if (responseJson.has("response")) {
                    JSONObject jo;
                    jo = responseJson.getJSONObject("response");
                    obj = jo;
                    success = jo.getBoolean("success");
                } else if (responseJson.has("success")) {
                    //前后端分离没有response但是有success
                    success = responseJson.getBoolean("success");
                    obj = responseJson;
                } else if(responseJson.has("status")&&responseJson.has("code")){
                    boolean status=responseJson.getBoolean("status");
                    int respCode=responseJson.getInt("code");
                    if(status&&respCode==200){
                        success=true;
                    }
                    obj=responseJson;
                }else{
                    success = true;
                    obj = responseJson;
                }
                if (success) {
                    code = REQUEST_SUCCESS;
                } else {
                    code = REQUEST_FALSE;
                }
            } else {
                //502错误
                obj = response.message();
                code = REQUEST_FAIL;
            }
        } catch (Exception e) {
            LogUtils.e("OkHttpHandler_response:", e == null ? "error, but exception is null" : e.toString());
            obj = e;
            code = REQUEST_FAIL;
        } finally {
            handleMessage(responseCode, code, obj);
        }
    }

    private void handleMessage(int responseCode, int code, Object msg) {
        try {
            switch (code) {
                case REQUEST_SUCCESS:
                    executorSuccess((JSONObject) msg);
                    break;
                case REQUEST_FALSE:
                    executorFalse((JSONObject) msg);
                    break;
                case REQUEST_FAIL:
                    Exception exception;
                    if (msg instanceof Exception) {
                        exception = (Exception) msg;
                    } else if (msg != null) {
                        //比如404,502时,msg.obj 为 String
                        exception = new Exception(msg.toString());
                    } else {
                        exception = new Exception();
                    }
                    executorFailed(responseCode, exception);
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 网络请求返回打印日志
     *
     * @param response
     * @param responseBody
     */
    private void showLog(Response response, String responseBody) {
        if (BuildConfig.DEBUG) {
            RequestBody body1 = response.request().body();
            if (body1 instanceof FormBody) {
                FormBody body = (FormBody) body1;
                String serviceName = "";
                for (int i = 0; i < body.size(); i++) {
                    if ("service".equals(body.encodedName(i))) {
                        serviceName = body.encodedValue(i);
                    }
                }
                LogUtils.e(serviceName, responseBody);
            } else if (body1 instanceof MultipartBody) {
                LogUtils.e("文件上传", responseBody);
            } else if (body1 instanceof RequestBody) {
                LogUtils.e("json请求", response.request().url() + "     " + responseBody);
            }
        }
    }

}
