package com.bench.android.core.arch.common.api;


import com.bench.android.core.net.http.HttpRequestConfig;
import com.bench.android.core.net.http.RequestFormBody;
import com.bench.android.core.net.http.base.RequestContainer;
import com.bench.android.core.net.http.params.BaseRequestHttpName;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

/**
 * 通用请求接口
 *
 * @author zhouyi
 */
public class BaseCommonRequestModel implements ISingleRequest {

    @ParamExclude
    protected String requestUrl = null;

    @ParamExclude
    protected HashMap<String, String> requestHeader = new HashMap<>();

    /**
     * 接口名
     */
    @ParamExclude
    private BaseRequestHttpName requestServiceName;

    /**
     * gson反序列化类
     */
    @ParamExclude
    private Class gsonClazz;

    @ParamExclude
    protected boolean useJsonRequest = false;

    public BaseCommonRequestModel() {
        this.requestServiceName = requestServiceName;
    }

    public BaseCommonRequestModel(BaseRequestHttpName requestServiceName) {
        this.requestServiceName = requestServiceName;
    }

    public void setRequestServiceName(BaseRequestHttpName requestServiceName) {
        this.requestServiceName = requestServiceName;
    }

    public void setGsonClazz(Class gsonClazz) {
        this.gsonClazz = gsonClazz;
    }

    public boolean isUseJsonRequest() {
        return useJsonRequest;
    }

    public void setUseJsonRequest(boolean useJsonRequest) {
        this.useJsonRequest = useJsonRequest;
    }

    @Override
    public RequestContainer getRequestBody() {
        try {
            Field[] declaredFields = getClass().getDeclaredFields();
            Constructor<RequestFormBody> constructor = RequestFormBody.class.getConstructor(BaseRequestHttpName.class);
            RequestFormBody body = constructor.newInstance(requestServiceName);
            body.setGenericClaz(gsonClazz);
            body.setUseJsonRequest(useJsonRequest);
            body.setRequestHeader(requestHeader);
            //如果不使用mock系统则单独使用每一个系统的地址
            if (!HttpRequestConfig.sUseMock) {
                body.setRequestUrl(requestUrl + requestServiceName.getUrl());
            }
            for (Field f : declaredFields) {
                ParamExclude annotation = f.getAnnotation(ParamExclude.class);
                if (annotation != null) {
                    continue;
                }
                f.setAccessible(true);
                Object o = f.get(this);
                if (o != null) {
                    String value = String.valueOf(o);
                    body.put(f.getName(), value);
                }
            }
            return body;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }
}
