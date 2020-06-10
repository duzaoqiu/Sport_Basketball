package com.bench.android.core.arch.common.api;

import com.bench.android.core.net.http.base.RequestContainer;

/**
 * 为了实现异步请求，需要实现该类，用于异步链式调用
 * @author zhouyi
 */
public interface ISingleRequest {
    RequestContainer getRequestBody();
}
