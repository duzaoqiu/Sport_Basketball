package com.bench.android.core.app.activity;

import android.view.View;

import com.bench.android.core.arch.presenter.IBaseView;


/**
 * <页面基础公共功能抽象>
 * <p>
 * Created by lingjiu on 2017/10/23.
 */
public interface PresentationLayerFunc extends IBaseView {
    /**
     * 弹出消息
     *
     * @param msg
     */
    void showToastMessage(String msg);

    /**
     * 显示软键盘
     *
     * @param focusView
     */
    void showSoftKeyboard(View focusView);

    /**
     * 隐藏软键盘
     */
    void hideSoftKeyboard();
}
