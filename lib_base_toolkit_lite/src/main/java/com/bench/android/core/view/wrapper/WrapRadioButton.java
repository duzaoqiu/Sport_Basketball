package com.bench.android.core.view.wrapper;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatRadioButton;

/**
 * @author zhouyi
 */
public class WrapRadioButton extends AppCompatRadioButton {
    public WrapRadioButton(Context context) {
        super(context);
    }

    public WrapRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WrapRadioButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setOnClickListener(@Nullable OnClickListener l) {
        super.setOnClickListener(l);
     }
}
