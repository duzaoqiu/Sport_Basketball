package com.bench.android.core.arch.common.api;

import com.bench.android.core.net.http.base.RequestContainer;

/**
 * @author zhouyi
 */
public interface ICommonApi {

    <T extends ICommonApi> T as(T t);

    <T extends ICommonApi> T asList();

    <T extends ICommonApi> T asQueue();

    void start();

    void start(CommonApiValidateCallback commonApiValidateCallback);

    /**
     * 如果可以执行接下去的请求，则需要调用doNext。
     *
     * @param requestContainer
     */
    void doNext(RequestContainer requestContainer);


    /**
     * 如果一个请求回来，不符合条件，可以调用failed，然后回收资源
     *
     * @param model
     */
    void failed(ApiResponseFalseModel model);

    /**
     * 网络请求成功
     *
     * @param requestContainer
     * @param o
     */
    void onApiResponseSuccess(RequestContainer requestContainer, Object o);

    /**
     * 网络请求失败
     *
     * @param requestContainer
     * @param o
     */
    void onApiResponseFalse(RequestContainer requestContainer, Object o, boolean netError);
}
