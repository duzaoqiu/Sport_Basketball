package com.bench.android.core.view.wrapper;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class WrapSwipeRefreshLayout extends SwipeRefreshLayout {
    public WrapSwipeRefreshLayout(@NonNull Context context) {
        super(context);
    }

    public WrapSwipeRefreshLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setOnClickListener(@Nullable OnClickListener l) {
        super.setOnClickListener(l);
    }
}
