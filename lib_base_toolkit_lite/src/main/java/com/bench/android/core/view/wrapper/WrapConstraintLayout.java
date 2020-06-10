package com.bench.android.core.view.wrapper;

import android.content.Context;
import android.util.AttributeSet;

import androidx.constraintlayout.widget.ConstraintLayout;

public class WrapConstraintLayout extends ConstraintLayout {
    public WrapConstraintLayout(Context context) {
        super(context);
    }

    public WrapConstraintLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WrapConstraintLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
