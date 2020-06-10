package com.bench.android.core.net.http.interceptor;


import com.bench.android.core.net.http.base.RequestContainer;

/**
 * 参数拦截器,添加参数签名,埋点,默认域名配置,...
 * Created by lingjiu on 2019/1/22.
 */
public interface HttpParamsInterceptor {
    RequestContainer intercept(RequestContainer map);
}
