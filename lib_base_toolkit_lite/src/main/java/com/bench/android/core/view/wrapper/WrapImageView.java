package com.bench.android.core.view.wrapper;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

/**
 * ImageView包装类，目前用处是使用OnClick的拦截、放重复点击等操作
 *
 * @author zhouyi
 */
public class WrapImageView extends AppCompatImageView {
    public WrapImageView(Context context) {
        super(context);
    }

    public WrapImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public WrapImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setOnClickListener(@Nullable final OnClickListener l) {
        super.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AutoClickUtils.mAutoClickEnable) {
                    ViewWrapper viewWrapper = new ViewWrapper(WrapImageView.this.getId());
                    String name = WrapImageView.this.getContext().getClass().getName();
                    viewWrapper.setContextName(name);
                    ClickManager.addWidget(viewWrapper);
                }
                l.onClick(v);
            }
        });

    }


}
