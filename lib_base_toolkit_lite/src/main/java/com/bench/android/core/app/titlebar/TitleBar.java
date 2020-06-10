package com.bench.android.core.app.titlebar;


import android.view.View;

import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;

public class TitleBar {
    /**
     * 是否显示返回按钮
     */
    private boolean isShowBackIcon;

    /**
     * 是否将 titleBar 标题颜色设置为 #333333  背景改为#FFFFFF 返回键变成黑色图标
     */
    private boolean isLightTheme;

    /**
     * 返回键文本
     */
    private String backText;

    /**
     * 返回键Icon
     */
    private int backIconRes;

    /**
     * 中间标题
     */
    private String title;

    /**
     * 中间标题颜色
     */
    private int tittleColorRes = -1;

    /**
     * 返回事件
     */
    private View.OnClickListener backClickListener;

    /**
     * 资源文件
     */
    private int extraRightRes;

    /**
     * 右侧按钮文字
     */
    private String extraStr;

    /**
     * 右侧文字颜色资源
     */
    private int extraStrColorRes = -1;

    /**
     * 右侧点击事件
     */
    private View.OnClickListener extraMoreListener;

    /**
     * 头部双击事件
     */
    private View.OnClickListener onDoubleTapListener;

    /**
     * 底部的横线是否显示
     */
    private boolean bottomLineVisible = true;

    /**
     * 背景ResId
     */
    private int backgroundResId;

    /**
     * 构造函数设置title
     */
    public TitleBar(String title) {
        this.title = title;
    }

    /**
     * 设置标题的颜色
     *
     * @param tittleColorRes
     * @return
     */
    public TitleBar setTittleColorRes(@ColorRes int tittleColorRes) {
        this.tittleColorRes = tittleColorRes;
        return this;
    }

    /**
     * @param isLightTheme 亮色titleBar
     */
    public TitleBar setLightTheme(boolean isLightTheme) {
        this.isLightTheme = isLightTheme;
        return this;
    }

    public TitleBar setBackgroundResId(@DrawableRes int backgroundResId) {
        this.backgroundResId = backgroundResId;
        return this;
    }

    /**
     * @param showBackIcon 设置返回按钮
     */
    public TitleBar setShowBackIcon(boolean showBackIcon) {
        isShowBackIcon = showBackIcon;
        return this;
    }

    /**
     * @param showBackIcon 设置返回按钮
     * @param backText     返回键文本
     */
    public TitleBar setShowBackIcon(boolean showBackIcon, String backText) {
        isShowBackIcon = showBackIcon;
        this.backText = backText;
        return this;
    }

    /**
     * @param showBackIcon 设置返回按钮
     * @param backIconRes  返回键图标
     */
    public TitleBar setShowBackIcon(boolean showBackIcon, @DrawableRes int backIconRes) {
        isShowBackIcon = showBackIcon;
        this.backIconRes = backIconRes;
        return this;
    }

    /**
     * @param backClickListener 返回监听
     */
    public TitleBar setBackClickListener(View.OnClickListener backClickListener) {
        this.backClickListener = backClickListener;
        return this;
    }

    /**
     * @param extraStr 右上角文本
     */
    public TitleBar setExtraStr(String extraStr) {
        this.extraStr = extraStr;
        return this;
    }


    /**
     * @param extraStrColor 右上角文本
     */
    public TitleBar setExtraStrColor(int extraStrColor) {
        this.extraStrColorRes = extraStrColor;
        return this;
    }

    /**
     * @param extraRightRes 右上角资源图片
     */
    public TitleBar setExtraRightRes(int extraRightRes) {
        this.extraRightRes = extraRightRes;
        return this;
    }

    /**
     * @param extraMoreListener 右上角点击事件
     */
    public TitleBar setExtraMoreListener(View.OnClickListener extraMoreListener) {
        this.extraMoreListener = extraMoreListener;
        return this;
    }

    /**
     * @param onDoubleTapListener 头部双击事件
     */
    public TitleBar setDoubleTapListener(View.OnClickListener onDoubleTapListener) {
        this.onDoubleTapListener = onDoubleTapListener;
        return this;
    }

    public TitleBar setBottomLineVisible(boolean visible) {
        this.bottomLineVisible = visible;
        return this;
    }

    public void build(ITitleBar iTitleBar) {
        if (isLightTheme) {
            iTitleBar.setLightTheme();
        }
        if (extraStrColorRes != -1) {
            iTitleBar.setRightExtraTextColor(extraStrColorRes);
        }
        if (tittleColorRes != -1) {
            iTitleBar.setTittleTextColorRes(tittleColorRes);
        }
        iTitleBar.initTitleBar(isShowBackIcon, backIconRes, backText, title, backClickListener,
                extraRightRes, extraStr, extraMoreListener, onDoubleTapListener, backgroundResId, bottomLineVisible);
    }
}
