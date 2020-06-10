package com.bench.android.core.view.wrapper;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

public class WrapRelativeLayout extends RelativeLayout {
    public WrapRelativeLayout(Context context) {
        super(context);
    }

    public WrapRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WrapRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public WrapRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void setOnClickListener(@Nullable final OnClickListener l) {
        super.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AutoClickUtils.mAutoClickEnable) {
                    ViewWrapper viewWrapper = new ViewWrapper(WrapRelativeLayout.this.getId());
                    String name = WrapRelativeLayout.this.getContext().getClass().getName();
                    viewWrapper.setContextName(name);
                    ClickManager.addWidget(viewWrapper);
                }
                l.onClick(v);
            }
        });
     }
}
