package com.bench.android.core.arch.viewmodel;


import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.bench.android.core.net.http.IHttpResponseCallBack;
import com.bench.android.core.net.http.WrapperHttpHelper;
import com.bench.android.core.net.http.base.RequestContainer;
import com.bench.android.core.util.BasePair;

import org.json.JSONException;
import org.json.JSONObject;

public class HttpViewModel extends ViewModel implements IHttpResponseCallBack {

    public static final int STATE_START = 1;
    public static final int STATE_SUCCESS = 2;
    public static final int STATE_FINISH = 3;
    public static final int STATE_ERROR = 4;


    public MutableLiveData<HttpError> httpErrorLiveData = new MutableLiveData<>();
    public MutableLiveData<BasePair<Integer, Integer>> loadStateLiveData = new MutableLiveData<>();

    protected WrapperHttpHelper httpHelper = new WrapperHttpHelper(this);

    @Override
    protected void onCleared() {
        super.onCleared();
        httpHelper.onDestroy();
    }


    @Override
    public void onStartRequest() {
        loadStateLiveData.postValue(new BasePair(STATE_START, 0));
    }



    @Override
    public void onFinishRequest(int stateCode) {
        loadStateLiveData.postValue(new BasePair(STATE_FINISH, stateCode));
    }

    @Override
    public void onSuccess(RequestContainer request, Object o) throws JSONException {

    }

    @Override
    public void onFailed(RequestContainer request, JSONObject jsonObject, boolean netFailed) throws Exception {
        HttpError httpError = new HttpError(request, jsonObject, netFailed);
        httpErrorLiveData.postValue(httpError);
    }
}