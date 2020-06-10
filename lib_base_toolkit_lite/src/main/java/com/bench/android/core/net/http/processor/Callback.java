package com.bench.android.core.net.http.processor;

import com.bench.android.core.app.application.BaseApplication;
import com.bench.android.core.app.toast.ToastUtils;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 使用例如:
 * RequestContainer map = new RequestFormBody(UrlEnum.DISCOVERY_SUBJECT_QUERY, false);
 * HttpUtil.getDefault().httpPost(map, new Callback<TopicObjectBean>() {
 *
 * @Override public void onSuccess(TopicObjectBean o) {
 * <p>
 * }
 * @Override public void onFalse(JSONObject jsonObject) {
 * <p>
 * }
 * });
 * <p>
 * Created by lingjiu on 2019/4/11.
 */
public abstract class Callback<T> implements IHttpResponseHandler {
    private Gson gson;
    private Class<T> t;
    //false的泛型就不处理了,兼顾到以前基础处理类最后都是JSONObject处理的
    //private Class<F> f;


    public Callback() {
        gson = new Gson();
        Type genericSuperclass = getClass().getGenericSuperclass();
        if (genericSuperclass instanceof ParameterizedType) {
            ParameterizedType type = (ParameterizedType) genericSuperclass;
            if (type != null) {
                Type[] actualTypeArguments = type.getActualTypeArguments();
                if (actualTypeArguments != null && actualTypeArguments.length > 0) {
                    t = (Class<T>) actualTypeArguments[0];
                }
            }
        }
    }

    /**
     * 返回success true
     *
     * @param t
     */
    public abstract void onSuccess(T t);

    /**
     * 返回success false
     *
     * @param jo
     */
    public abstract void onFalse(JSONObject jo);

    /**
     * 请求完成回调
     *
     * @param isSuccess true 请求成功 走了onsuccess ,false 请求成功 走了onFalse 或者网络异常
     */
    public void onCompleted(boolean isSuccess) {

    }

    /**
     * 网络错误,如有特殊处理,可复写
     *
     * @param code
     * @param e
     */
    public void onNetFailed(int code, Exception e) {

    }

    @Override
    public void executorSuccess(JSONObject jo) {
        try {
            Object obj = jo;
            if (t != null) {
                obj = gson.fromJson(jo.toString(), t);
            }
            onSuccess((T) obj);
            onCompleted(true);
        } catch (Exception e) {
            throw new RuntimeException("解析出错了" + "---错误信息:" + e.toString());
        }
    }

    @Override
    public void executorFalse(JSONObject jo) {
        onFalse(jo);
        onCompleted(false);
    }

    @Override
    public void executorFailed(int code, Exception e) {
        ToastUtils.showTip(BaseApplication.getContext(), "网络有问题或服务器开小差了~稍后再试吧");
        onNetFailed(code, e);
        onCompleted(false);
    }
}
