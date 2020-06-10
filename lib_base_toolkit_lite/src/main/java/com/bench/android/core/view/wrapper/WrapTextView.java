package com.bench.android.core.view.wrapper;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import com.android.library.R;
import com.bench.android.core.util.LogUtils;

/**
 * TextView包装类，目前用处是使用OnClick的拦截、放重复点击等操作
 *
 * @author zhouyi
 */
public class WrapTextView extends AppCompatTextView {
    /**
     * 是否执行防重复点击
     */
    private boolean mAntiRepeat = false;
    private long mStartTime = 0;

    /**
     * 延迟时间
     */
    private int mDelayTime = 1000;

    public WrapTextView(Context context) {
        super(context);
    }

    public WrapTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.WrapXXXView);
        mAntiRepeat = ta.getBoolean(R.styleable.WrapXXXView_anti_repeat, false);
        int delayTime = ta.getInteger(R.styleable.WrapXXXView_delay_time, 0);
        if (delayTime > 0) {
            mDelayTime = delayTime;
        }
        ta.recycle();

    }

    public WrapTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 设置延迟时间
     */
    public void setDelayTime(int delayTime) {
        mDelayTime = delayTime;
    }

    @Override
    public void setOnClickListener(@Nullable final OnClickListener l) {
        super.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AutoClickUtils.mAutoClickEnable) {
                    ViewWrapper viewWrapper = new ViewWrapper(WrapTextView.this.getId());
                    String name = WrapTextView.this.getContext().getClass().getName();
                    viewWrapper.setContextName(name);
                    ClickManager.addWidget(viewWrapper);
                }

                //如果反重复点击
                if (mAntiRepeat) {
                    if (System.currentTimeMillis() - mStartTime > mDelayTime) {
                        l.onClick(v);
                        //PointCutManager.getInstance().executeAdvice(PointCutConstants.POINTCUT_VIEW_SET_ON_CLICK_LISTENER, this, new Object[]{v});
                        mStartTime = System.currentTimeMillis();
                    } else {
                        LogUtils.e("时间太短");
                    }
                } else {
                    l.onClick(v);
                   // PointCutManager.getInstance().executeAdvice(PointCutConstants.POINTCUT_VIEW_SET_ON_CLICK_LISTENER, this, new Object[]{v});
                }
            }
        });
    }
}
