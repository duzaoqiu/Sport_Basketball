package com.bench.android.core.app.activity;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;

import com.alibaba.android.arouter.launcher.ARouter;
import com.bench.android.core.app.dialog.DialogHelper;
import com.bench.android.core.app.titlebar.DefaultTitleBar;
import com.bench.android.core.app.titlebar.ITitleBar;
import com.bench.android.core.app.toast.ToastUtils;
import com.bench.android.core.arch.presenter.IBaseView;
import com.bench.android.core.arch.viewmodel.HttpError;
import com.bench.android.core.arch.viewmodel.HttpViewModel;
import com.bench.android.core.net.LoadSirManager;
import com.bench.android.core.net.NetworkUtils;
import com.bench.android.core.net.domain.AppDomainGetUtil;
import com.bench.android.core.net.domain.base.AppDomainManager;
import com.bench.android.core.net.domain.base.ShowErrorDetailMessageActivity;
import com.bench.android.core.net.http.IHttpResponseCallBack;
import com.bench.android.core.net.http.WrapperHttpHelper;
import com.bench.android.core.net.http.base.RequestContainer;
import com.bench.android.core.net.http.processor.HttpUtil;
import com.bench.android.core.util.BasePair;
import com.bench.android.core.util.LibAppUtil;
import com.bench.android.core.util.ToolbarUtils;
import com.bench.android.core.util.TransparentThemeFix;
import com.bench.android.core.view.toolbar.CustomToolbar;
import com.kingja.loadsir.callback.Callback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by danao on 2019/2/13.
 */
public abstract class BaseActivity extends PointCutActivity implements View.OnClickListener, IBaseView, IHttpResponseCallBack, Callback.OnReloadListener {

    public final String TAG = getClass().getSimpleName();

    protected Context mContext;

    /**
     * 现在推荐使用toolbar，默认的样式的是一个返回箭头和一个标题栏
     * 使用它有两种方法：1、如果只是简单的改变title，只需要在activity里面重写 getToolbarTitle 即可
     * 2、如果标题栏右边也有文字按钮什么的，复写 getToolBar 自己构建即可
     */
    @Deprecated
    public ITitleBar iTitleBar;
    protected ProgressDialog progressDialog;
    protected WrapperHttpHelper mHttpHelper;

    /**
     * 是否在网络请求介绍的时候自动关闭progressBar
     */
    private boolean mAutoHideProgressBar = true;

    /**
     * 是否在网络请求的时候自动显示progressBar
     */
    private boolean mAutoShowProgressBar = true;
    protected CustomToolbar customToolbar;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        TransparentThemeFix.fix(this);
        super.onCreate(savedInstanceState);
        mContext = this;

        //registerReceiver(netBroadcastReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        iTitleBar = new DefaultTitleBar(this);

        progressDialog = new ProgressDialog(mContext);

        mHttpHelper = new WrapperHttpHelper(this);

        ARouter.getInstance().inject(this);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        //在onAttachedToWindow操作的原因是在BaseActivity里面的onCreate方法里面还不能拿到布局
        initToolbar();
    }

    protected void initToolbar() {
        customToolbar = getToolBar();
        View v = null;
        if (getTopPaddingViewId() != 0) {
            v = findViewById(getTopPaddingViewId());
        }
        ToolbarUtils.init(this, customToolbar, v);
    }

    /**
     * 设置状态栏高度的topPadding的viewId
     * 默认是布局的第一个控件，有时候布局比较复杂会引起问题，
     * 重写这个方法，手动设置顶部需要加padding的控件
     *
     * @return 布局id
     */
    protected int getTopPaddingViewId() {
        return 0;
    }

    /**
     * 获取toolbar标题栏
     * <p>
     * 如果最简单的只有一个标题和一个返回按钮，直接复写getToolbarTitle即可
     * 否则复写getToolBar
     *
     * @return toolbar标题栏
     */
    protected CustomToolbar getToolBar() {
        //获取标题，最简单的一种toolbar
        String toolbarTitle = getToolbarTitle();
        if (TextUtils.isEmpty(toolbarTitle)) {
            return null;
        }
        //构造toolbar
        return new CustomToolbar.Builder(this)
                .title(toolbarTitle)
                .build();
    }

    protected String getToolbarTitle() {
        return null;
    }


    /**
     * 是否需要上面的padding
     * 一般都需要，像有些比如图片直接从状态栏开始布局，就不需要
     *
     * @return 默认返回true
     */
    public boolean isNeedTopPadding() {
        return true;
    }

    /**
     * 状态栏是否是浅色背景，如果是浅色，把状态栏图标变成黑色
     *
     * @return 默认true
     */
    public boolean isLightColor() {
        return true;
    }


    protected BroadcastReceiver netBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int netWorkState = NetworkUtils.getNetworkState(context);
            onNetChange(NetworkUtils.NETWORK_NONE != netWorkState);
        }
    };

    /**
     * 网络变化回调
     */
    protected void onNetChange(boolean isNetConnected) {

    }

    /**
     * 设置网络状态和success为false的Observers
     */
    public void setHttpErrorAndHttpStatusObservers(LifecycleOwner owner, HttpViewModel httpViewModel) {
        httpViewModel.httpErrorLiveData.observe(owner, new Observer<HttpError>() {
            @Override
            public void onChanged(@Nullable HttpError httpError) {
                operateErrorResponseMessage(httpError.jsonObject);
            }
        });

        httpViewModel.loadStateLiveData.observe(owner, new Observer<BasePair<Integer, Integer>>() {

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
                        LoadSirManager.getInstance().onFinishRequest(String.valueOf(BaseActivity.this.hashCode()), statusCode);
                        break;
                }
            }
        });
    }

    /**
     * 自动显示progressbar
     */
    public void notAutoHideProgressBar() {
        mAutoHideProgressBar = false;
    }

    /**
     * 关闭自动显示progressbar
     */
    public void notAutoShowProgressBar() {
        mAutoShowProgressBar = false;
    }

    /**
     * 打开自动显示progressbar
     */
    public void autoShowProgressBar() {
        mAutoShowProgressBar = true;
    }

    @Override
    public void onClick(View v) {

    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onMessageEvent(Object event) {
    }

    public boolean isActivityFinished() {
        if (Build.VERSION.SDK_INT >= 17)
            return isFinishing() || isDestroyed();
        return isFinishing();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        HttpUtil.getDefault().cancelHttpCall(TAG);
        EventBus.getDefault().unregister(this);
        //unregisterReceiver(netBroadcastReceiver);
        if (iTitleBar != null) {
            iTitleBar.onTitleBarDestroy();
        }
        ErrorUtils.getInstance().onDetach();
        mHttpHelper.onDestroy();
        EasyActivityResult.releaseActivity(this);
        LoadSirManager.getInstance().removeLoadService(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        EasyActivityResult.dispatch(this, requestCode, resultCode, data);
    }

    @Override
    public void showProgressDialog(String loadingTxt) {
        if (progressDialog != null && !progressDialog.isShowing()) {
            if (!TextUtils.isEmpty(loadingTxt)) {
                progressDialog.setMessage(loadingTxt);
            }
            progressDialog.show();
        }
    }

    @Override
    public void hideProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
//        mAutoHideProgressBar = true;
    }

    /**
     * 进度弹窗是否在显示
     *
     * @return
     */
    public boolean isProgressDialogShowing() {
        return progressDialog != null && progressDialog.isShowing();
    }

    @Override
    public void showErrorMsg(JSONObject jsonObject) {
        operateErrorResponseMessage(jsonObject);
    }

    public void operateErrorResponseMessage(JSONObject jo) {
        try {
            operateErrorResponseMessage(jo, true);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void operateErrorResponseMessage(JSONObject jo, boolean showTip) throws JSONException {
        ErrorUtils.getInstance().operateErrorResponseMessage(this, jo, showTip);
    }

    @Override
    public void showToast(String message) {
        ToastUtils.show(message);
    }

    @Override
    public void onSuccess(RequestContainer request, Object o) throws JSONException {

    }

    @Override
    public void onFailed(RequestContainer request, JSONObject jsonObject, boolean netFailed) throws Exception {
        if (!judgeDomainIsNull()) {
            operateErrorResponseMessage(jsonObject);
        }
    }


    @Override
    public void onStartRequest() {
        if (mAutoShowProgressBar) {
            showProgressDialog("数据加载中..");
        }
    }


    @Override
    public void onFinishRequest(int stateCode) {
        if (mAutoHideProgressBar) {
            hideProgressDialog();
        }
    }

    public boolean judgeDomainIsNull() {
        if (AppDomainManager.userDynamicDomain && TextUtils.isEmpty(AppDomainManager.getInstance().getApiDomain())) {
            ToastUtils.showTip(this, "当前域名为空，请稍后刷新或者退出重试");
            AppDomainGetUtil.getInstance().start(new AppDomainGetUtil.GetDomainListener() {
                @Override
                public void getDomainResult(boolean success, String errorMessage) {
                    if (!success) {
                        showFailedDialog(errorMessage);
                    } else {
                        //刷新界面
                    }
                }

            });
            return true;
        }
        return false;
    }

    public void showFailedDialog(final String errorMsg) {
        DialogHelper.showMessageDialog(this, "", "确定", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        }, "显示详情", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowErrorDetailMessageActivity.startShowErrorDetailMessageActivity(BaseActivity.this, formatMessage(errorMsg));
                finish();
            }
        }, "动态获取域名失败，请退出app重试！");
    }


    private String formatMessage(String message) {
        StringBuilder sb = new StringBuilder();
        sb.append(message);
        sb.append("\n----------------------------------------------------------------\n");
        sb.append("phone message:\n");
        sb.append(Build.VERSION.RELEASE + "-" + Build.MODEL + "\n");
        sb.append("----------------------------------------------------------------\n");
        sb.append("app version:\n");
        sb.append(LibAppUtil.getApkVersionName(this));
        return sb.toString();
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
