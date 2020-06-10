package com.bench.android.core.app.titlebar;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.TextView;

import androidx.annotation.ColorRes;
import androidx.core.content.ContextCompat;

import com.android.library.R;
import com.bench.android.core.util.StatusBarCompat;
import com.bench.android.core.util.dimension.PxConverter;


public class DefaultTitleBar implements ITitleBar {

    private Activity mActivity;

    private long mPreClickTime;

    public DefaultTitleBar(Activity activity) {
        this.mActivity = activity;
    }

    /**
     * 处理titleBar  updated by zixia   2018/7/17
     *
     * @param isShowBackIcon    是否显示返回按钮
     * @param backText          返回文本
     * @param title             标题
     * @param backClickListener 返回事件
     * @param extraRightRes     资源文件
     * @param extraStr          右侧按钮文字
     * @param extraMoreListener 右侧点击事件
     */
    @SuppressLint("ResourceType")
    @Override
    public void initTitleBar(boolean isShowBackIcon,
                             int backResId,
                             String backText,
                             String title,
                             final View.OnClickListener backClickListener,
                             int extraRightRes,
                             String extraStr,
                             View.OnClickListener extraMoreListener,
                             final View.OnClickListener onDoubleTapListener,
                             int backgroundResId,
                             boolean bottomLineVisible) {
        TextView extraRightTv = mActivity.findViewById(R.id.extraRightTv);
        if (extraRightRes != 0) {
            //设置右侧按钮资源文件
            extraRightTv.setVisibility(View.VISIBLE);
            Drawable drawable = mActivity.getResources().getDrawable(extraRightRes);
            if (Math.abs(drawable.getIntrinsicHeight() - drawable.getIntrinsicWidth()) < 10) {
                drawable.setBounds(0, 0, PxConverter.dp2px(mActivity, 16), PxConverter.dp2px(mActivity, 16));
                extraRightTv.setCompoundDrawables(drawable, null, null, null);
            } else {
                //非方形图,不做尺寸变换
                extraRightTv.setCompoundDrawablesRelativeWithIntrinsicBounds(drawable, null, null, null);
            }
        }
        if (extraStr != null) {
            //设置右侧按钮文字内容
            extraRightTv.setVisibility(View.VISIBLE);
            extraRightTv.setText(extraStr);
        }
        if (extraMoreListener != null) {
            //右侧点击事件
            extraRightTv.setOnClickListener(extraMoreListener);
        }
        TextView backIv = mActivity.findViewById(R.id.backIv);
        if (isShowBackIcon) {
            //返回自定义事件
            backIv.setVisibility(View.VISIBLE);
            if (backIv != null) {
                if (backText != null) {
                    backIv.setText(backText);//设置返回文本
                }
                if (backResId > 0) {
                    //设置返回Icon
                    Drawable drawable = ContextCompat.getDrawable(mActivity, backResId);
                    drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                    backIv.setCompoundDrawables(drawable, null, null, null);
                }
                backIv.setVisibility(View.VISIBLE);
                backIv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (backClickListener != null) {
                            backClickListener.onClick(v);
                        } else {
                            mActivity.onBackPressed();
                            //mActivity.finish();
                        }
                    }
                });
            }
        } else {
            backIv.setVisibility(View.GONE);
        }

        TextView titleTv = mActivity.findViewById(R.id.activityTitleTv);
        if (titleTv != null) {
            //设置标题
            titleTv.setText(title);
        }

        View titleLayout = mActivity.findViewById(R.id.titleLayout);
        if (titleLayout != null && onDoubleTapListener != null) {
            titleLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (System.currentTimeMillis() - mPreClickTime <= ViewConfiguration.getDoubleTapTimeout() &&
                            onDoubleTapListener != null) {
                        onDoubleTapListener.onClick(v);
                    }
                    mPreClickTime = System.currentTimeMillis();
                }
            });
        }
        if (backgroundResId > 0) {
            mActivity.findViewById(R.id.titleLayout).setBackgroundResource(backgroundResId);
        }
        //底部横线
        View line = mActivity.findViewById(R.id.view_title_line);
        if (line != null) {
            line.setVisibility(bottomLineVisible ? View.VISIBLE : View.INVISIBLE);
        }
    }



    /**
     * updated  by zixia 2018/7/11
     * titleBar 标题颜色设置为 #333333  背景改为#FFFFFF 返回键变成黑色图标
     */
    @Override
    public void setLightTheme() {
        TextView titleTv = mActivity.findViewById(R.id.activityTitleTv);
        titleTv.setTextColor(mActivity.getResources().getColor(R.color.colorTextG4));//#333333
        mActivity.findViewById(R.id.titleLayout).setBackgroundColor(mActivity.getResources().getColor(R.color.white));//#FFFFFF
        TextView backTv = (mActivity.findViewById(R.id.backIv));
        backTv.setTextColor(mActivity.getResources().getColor(R.color.colorTextG4));//#333333
        Drawable backDrawable = mActivity.getResources().getDrawable(R.drawable.icon_back_black);//黑色返回按钮
        backTv.setCompoundDrawablesWithIntrinsicBounds(backDrawable, null, null, null);
        ((TextView) mActivity.findViewById(R.id.extraRightTv)).setTextColor(mActivity.getResources().getColor(R.color.colorTextG4));//#333333
    }

    @Override
    public void setTittleTextColorRes(@ColorRes int color) {
        ((TextView) mActivity.findViewById(R.id.activityTitleTv)).setTextColor(ContextCompat.getColor(mActivity, color));
    }

    @Override
    public void setRightExtraTextColor(@ColorRes int colorResId) {
        ((TextView) mActivity.findViewById(R.id.extraRightTv)).setTextColor(ContextCompat.getColor(mActivity, colorResId));//#333333
    }


    @Override
    public void onTitleBarDestroy() {
        if (mActivity != null) {
            mActivity = null;
        }
    }
}
