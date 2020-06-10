package com.bench.android.core.view.widget;

import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.view.WindowInsets;
import android.widget.LinearLayout;

import androidx.annotation.RequiresApi;

/**
 * @author xingjiu
 * 设置了fitsSystemWindows 属性后，会有一个statusbar高度的padding,使用这个可以消除padding
 */
public class NoTopPaddingLinearLayout extends LinearLayout {
    public NoTopPaddingLinearLayout(Context context) {
        super(context);
    }

    public NoTopPaddingLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NoTopPaddingLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected boolean fitSystemWindows(Rect insets) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            insets.left = 0;
            insets.top = 0;
            insets.right = 0;
        }
        return super.fitSystemWindows(insets);
    }
 
    @RequiresApi(api = Build.VERSION_CODES.KITKAT_WATCH)
    @Override
    public WindowInsets onApplyWindowInsets(WindowInsets insets) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            return super.onApplyWindowInsets(insets.replaceSystemWindowInsets(0, 0, 0, insets.getSystemWindowInsetBottom()));
        } else {
            return insets;
        }
    }
}