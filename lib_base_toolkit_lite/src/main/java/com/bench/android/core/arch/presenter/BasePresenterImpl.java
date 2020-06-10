package com.bench.android.core.arch.presenter;

import com.bench.android.core.app.activity.BaseActivity;
import com.bench.android.core.app.fragment.BaseFragment;
import com.bench.android.core.arch.CommonBaseView;
import com.bench.android.core.net.http.IHttpResponseCallBack;
import com.bench.android.core.net.http.WrapperHttpHelper;
import com.bench.android.core.net.http.base.RequestContainer;

import org.json.JSONObject;

public abstract class BasePresenterImpl<V extends IBaseView> implements BasePresenter, IHttpResponseCallBack {

    protected V mView = null;

    protected BaseActivity mActivity;

    protected WrapperHttpHelper mHttpHelper;

//    protected Gson gson = new Gson();
  /*  private LoadService<RequestState> loadService;
    private RequestState mRequestState = new RequestState();*/

    public BasePresenterImpl(V mView) {
        this.mView = mView;
        if (mView instanceof BaseActivity) {
            mActivity = (BaseActivity) mView;
        } else if (mView instanceof BaseFragment) {
            mActivity = (BaseActivity) ((BaseFragment) mView).getActivity();
        }
        mHttpHelper = new WrapperHttpHelper(this, getName());
    }

    public V getView() {
        return mView;
    }

    @Override
    public void onDetach() {
        mView = null;
        mHttpHelper.onDestroy();
    }

    @Override
    public String getName() {
        if (mView == null) {
            return null;
        }
        return String.valueOf(mView.hashCode());
    }

    @Override
    public void onFailed(RequestContainer request, JSONObject jsonObject, boolean netFailed) throws Exception {
        if (mActivity != null) {
            mActivity.onFailed(request, jsonObject, netFailed);
        } else if (mView != null) {
            if (mView instanceof BaseActivity) {
                mView.showErrorMsg(jsonObject);
            } else if (mView instanceof BaseFragment) {
                mView.showErrorMsg(jsonObject);
            } else if (mView instanceof CommonBaseView) {
                mView.showErrorMsg(jsonObject);
            }
        }
    }

    @Override
    public abstract void onSuccess(RequestContainer request, Object o);

    @Override
    public void onStartRequest() {
        if (mActivity != null) {
            mActivity.onStartRequest();
        } else if (mView != null) {
            mView.onStartRequest();
        }
    }

    @Override
    public void onFinishRequest(int stateCode) {
        if (mActivity != null) {
            mActivity.onFinishRequest(stateCode);
        } else if (mView != null) {
            mView.hideProgressDialog();
        }
    }

}
