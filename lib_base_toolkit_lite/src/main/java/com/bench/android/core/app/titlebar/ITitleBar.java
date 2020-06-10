package com.bench.android.core.app.titlebar;


import android.view.View;

import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;

public interface ITitleBar {
    /**
     * @param isShowBackIcon      是否显示返回按钮
     * @param backResId           返回Icon
     * @param backText            返回文本
     * @param title               标题
     * @param backClickListener   返回事件
     * @param extraRightRes       资源文件
     * @param extraStr            右侧按钮文字
     * @param extraMoreListener   右侧点击事件
     * @param onDoubleTapListener 头部双击事件
     * @param bottomLineVisible   底部的横线是否可见
     */
    void initTitleBar(boolean isShowBackIcon, int backResId, String backText, String title,
                      final View.OnClickListener backClickListener, @DrawableRes int extraRightRes,
                      String extraStr, View.OnClickListener extraMoreListener, View.OnClickListener onDoubleTapListener,
                      @DrawableRes int backgroundResId, boolean bottomLineVisible);

    /**
     * titleBar 标题颜色设置为 #333333  背景改为#FFFFFF 返回键变成黑色图标
     */
    void setLightTheme();


    /**
     * 设置中间标题的颜色
     *
     * @param color
     */
    void setTittleTextColorRes(@ColorRes int color);

    /**
     * 设置右边文字的颜色
     *
     * @param colorResId
     */
    void setRightExtraTextColor(@ColorRes int colorResId);

    void onTitleBarDestroy();
}
