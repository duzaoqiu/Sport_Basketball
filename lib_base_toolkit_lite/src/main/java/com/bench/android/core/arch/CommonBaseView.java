package com.bench.android.core.arch;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;

import com.bench.android.core.arch.presenter.BasePresenterImpl;
import com.bench.android.core.arch.presenter.IBaseView;

import org.json.JSONObject;

/************************************************************************
 *@Project: common_lib
 *@Package_Name: com.common.lib.login.widget
 *@Descriptions:
 *@Author: xingjiu
 *@Date: 2019/5/15 
 *************************************************************************/
public abstract class CommonBaseView<V extends BasePresenterImpl, T> extends FrameLayout implements IBaseView {
    protected V mPresenter;
    protected View root;
    protected T listener;

    public CommonBaseView(@NonNull Context context) {
        super(context);
        init(null);
    }

    public CommonBaseView(@NonNull Context context, @NonNull AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public CommonBaseView(@NonNull Context context, @NonNull AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        if (attrs != null) {
            initAttrs(attrs);
        }
        processIntent();
        initView();
        initPresenter();
        initData();
        initEvent();
    }

    protected void processIntent() {
    }

    public void setListener(T listener) {
        this.listener = listener;
    }

    protected abstract void initAttrs(AttributeSet attrs);

    protected abstract void initView();

    protected void initPresenter() {
    }

    protected void initData() {
    }

    protected void initEvent() {
    }

    @Override
    public void onStartRequest() {
        ((IBaseView) getContext()).onStartRequest();
    }

    @Override
    public void showProgressDialog(String loadingTxt) {
        ((IBaseView) getContext()).showProgressDialog(loadingTxt);
    }

    @Override
    public void hideProgressDialog() {
        ((IBaseView) getContext()).hideProgressDialog();
    }

    @Override
    public void showErrorMsg(JSONObject jsonObject) {
        ((IBaseView) getContext()).showErrorMsg(jsonObject);
    }

    @Override
    public void showToast(String message) {
        ((IBaseView) getContext()).showToast(message);
    }
}
