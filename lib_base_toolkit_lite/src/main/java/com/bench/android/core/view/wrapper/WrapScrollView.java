package com.bench.android.core.view.wrapper;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.ScrollView;

import androidx.annotation.RequiresApi;

/**
 * ScrollView包装类
 *
 * @author zhouyi
 */
public class WrapScrollView extends ScrollView {
    private OnScrollChangeListener mScrollChangeListener;

    public WrapScrollView(Context context) {
        super(context);
    }

    public WrapScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WrapScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public WrapScrollView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    /**
     * 获取滚动监听接口
     *
     * @return
     */
    public OnScrollChangeListener getOnScrollChangeListener() {
        return mScrollChangeListener;
    }

    /**
     * 设置滚动监听接口
     *
     * @param scrollChangeListener
     */
    public void setOnScrollChangeListener(OnScrollChangeListener scrollChangeListener) {
        this.mScrollChangeListener = scrollChangeListener;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (mScrollChangeListener != null) {
            mScrollChangeListener.onScroll(l, t, oldl, oldt);
        }
    }

    /**
     * ScrollView 滚动监听接口
     */
    public interface OnScrollChangeListener {
        /**
         * 滚动时回调
         *
         * @param l
         * @param t
         * @param oldl
         * @param oldt
         */
        void onScroll(int l, int t, int oldl, int oldt);
    }
}
