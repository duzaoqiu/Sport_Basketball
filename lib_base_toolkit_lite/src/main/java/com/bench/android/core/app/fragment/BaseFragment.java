package com.bench.android.core.app.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;

import com.bench.android.core.app.activity.BaseActivity;
import com.bench.android.core.app.activity.EasyActivityResult;
import com.bench.android.core.app.toast.ToastUtils;
import com.bench.android.core.arch.presenter.IBaseView;
import com.bench.android.core.arch.viewmodel.HttpError;
import com.bench.android.core.arch.viewmodel.HttpViewModel;
import com.bench.android.core.net.LoadSirManager;
import com.bench.android.core.net.http.IHttpResponseCallBack;
import com.bench.android.core.net.http.base.RequestContainer;
import com.bench.android.core.net.http.processor.HttpUtil;
import com.bench.android.core.util.BasePair;
import com.bench.android.core.util.StatusBarCompat;
import com.kingja.loadsir.callback.Callback;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author danao
 * @date 2019/2/13
 */
public abstract class BaseFragment extends PointCutFragment implements View.OnClickListener, IBaseView, IHttpResponseCallBack, Callback.OnReloadListener {

    protected final String TAG = getClass().getSimpleName();
    //需要context,最好引用这个成员变量
    protected BaseActivity mActivity;
    protected View rootView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (bindEvenBus()) {
            EventBus.getDefault().register(this);
        }
    }

    /**
     * 是否注册EventBus
     * 如果要注册EventBus 可重写该方法，返回true
     * 且必须实现EventBus 的注解方法
     * 如下:
     * //@Override  public boolean bindEvenBus() { return true; }
     * //@Subscribe() public void onMessageEvent(object event) {}
     *
     * @return 是否注册EventBus
     */
    public boolean bindEvenBus() {
        return false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(getLayoutId(), container, false);
        onCreateView();
        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (BaseActivity) getActivity();
    }

    protected abstract int getLayoutId();

    protected abstract void onCreateView();

    /**
     * 设置网络状态和success为false的Observers
     */
    protected void setHttpErrorAndHttpStatusObservers(HttpViewModel httpViewModel) {
        httpViewModel.httpErrorLiveData.observe(this, new Observer<HttpError>() {
            @Override
            public void onChanged(@Nullable HttpError httpError) {
                mActivity.operateErrorResponseMessage(httpError.jsonObject);
            }
        });

        httpViewModel.loadStateLiveData.observe(this, new Observer<BasePair<Integer, Integer>>() {

            @Override
            public void onChanged(@Nullable BasePair<Integer, Integer> value) {
                int status = value.first;
                //保留字段，网络请求完成，如果是所有response都返回success=true，则success为true，负责为false
                int statusCode = value.second;
                switch (status) {
                    case HttpViewModel.STATE_START:
                        onStartRequest();
                        break;
                    case HttpViewModel.STATE_FINISH:
                        onFinishRequest(statusCode);
                        LoadSirManager.getInstance().onFinishRequest(String.valueOf(BaseFragment.this.hashCode()), statusCode);
                        break;
                }
            }
        });
    }

    /*给顶部view增加一个高度和padding,使得布局显示正常*/
    public void fitsSystemWindows(View view) {
        StatusBarCompat.fitsSystemWindows(view);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (bindEvenBus()) {
            EventBus.getDefault().unregister(this);
        }
        HttpUtil.getDefault().cancelHttpCall(TAG);
        EasyActivityResult.releaseFragment(this);
        LoadSirManager.getInstance().removeLoadService(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        EasyActivityResult.dispatch(this, requestCode, resultCode, data);
    }

    @Override
    public void showProgressDialog(String loadingTxt) {
        mActivity.showProgressDialog(loadingTxt);
    }

    @Override
    public void hideProgressDialog() {
        mActivity.hideProgressDialog();
    }

    @Override
    public void showErrorMsg(JSONObject jsonObject) {
        mActivity.operateErrorResponseMessage(jsonObject);
    }

    @Override
    public void showToast(String message) {
        ToastUtils.showTip(mActivity, message);
    }

    @Override
    public void onStartRequest() {
        mActivity.onStartRequest();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    public void onFailed(RequestContainer request, JSONObject jsonObject, boolean netFailed) throws Exception {
        mActivity.onFailed(request, jsonObject, netFailed);
    }

    /**
     * @param stateCode 状态码
     */
    @Override
    public void onFinishRequest(int stateCode) {
        mActivity.onFinishRequest(stateCode);
    }

    @Override
    public void onSuccess(RequestContainer request, Object o) throws JSONException {

    }

    /**
     * loadsir的重新加载
     *
     * @param v v
     */
    @Override
    public void onReload(View v) {

    }

}
