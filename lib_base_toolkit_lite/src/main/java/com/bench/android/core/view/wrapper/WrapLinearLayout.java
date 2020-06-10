package com.bench.android.core.view.wrapper;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

public class WrapLinearLayout extends LinearLayout {
    public WrapLinearLayout(Context context) {
        super(context);
    }

    public WrapLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WrapLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setOnClickListener(@Nullable final OnClickListener l) {
        super.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AutoClickUtils.mAutoClickEnable) {
                    ViewWrapper viewWrapper = new ViewWrapper(WrapLinearLayout.this.getId());
                    String name = WrapLinearLayout.this.getContext().getClass().getName();
                    viewWrapper.setContextName(name);
                    ClickManager.addWidget(viewWrapper);
                }
                l.onClick(v);
            }
        });

    }
}
