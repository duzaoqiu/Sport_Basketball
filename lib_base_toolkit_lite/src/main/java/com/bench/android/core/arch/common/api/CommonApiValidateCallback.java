package com.bench.android.core.arch.common.api;

/**
 * 通用接口错误回调
 *
 * @author zhouyi
 */
public interface CommonApiValidateCallback {

    /**
     * 接口数据验证错误回调
     * @param model
     */
    void onValidateError(CommonApiValidateErrorModel model);

    /**
     * api返回success:false
     * @param model
     */
    void onApiResponseFalse(ApiResponseFalseModel model);

    /**
     * 网络请求完成
     */
    void onApiRequestFinish(int stateCode);
}
