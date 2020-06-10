package com.bench.android.core.net.domain.base;

public class GetDomainNoTestUtil extends BaseGetDomain {

    public GetDomainNoTestUtil(Builder builder) {
        super(builder);
    }

    @Override
    public void startRequestUrl() {
        getUrlByFirstDomain();
    }

    @Override
    public void responseSuccessGetUrl(String response) {
        mListener.requestDoMainSuccess(response);
    }

    @Override
    protected void responseFailedGetUrl() {
        mListener.onFailed(null);
    }
}
