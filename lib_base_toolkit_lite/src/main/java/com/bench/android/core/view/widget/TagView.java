package com.bench.android.core.view.widget;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Checkable;
import android.widget.FrameLayout;

import androidx.annotation.RequiresApi;

/**
 *
 * TagView的用法:
 * 子控件需要调用
 *    android:clickable="false"  //子控件不可点击
 *    android:duplicateParentState="true"//子空间保持和父控件相同的状态
 *
 * 当父控件的状态变化时，会强制子控件也重绘
 * Created by zaozao on 2018/7/9.
 */
public class TagView extends FrameLayout implements Checkable {

    private boolean isChecked;
    private static final int[] CHECK_STATE = new int[]{android.R.attr.state_checked};

    public TagView(Context context) {
        super(context);
    }

    public TagView( Context context,AttributeSet attrs) {
        super(context, attrs, 0);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public TagView( Context context,  AttributeSet attrs,int defStyleAttr) {
        super(context, attrs, defStyleAttr, 0);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public TagView(Context context, AttributeSet attrs, int defStyleAttr,int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public View getTagView() {
        return getChildAt(0);
    }

    @Override
    public int[] onCreateDrawableState(int extraSpace) {
        int[] states = super.onCreateDrawableState(extraSpace + 1);
        if (isChecked()) {
            mergeDrawableStates(states, CHECK_STATE);
        }
        return states;
    }


    @Override
    public void setChecked(boolean checked) {
        if (this.isChecked != checked) {
            this.isChecked = checked;
            refreshDrawableState();
        }
    }

    @Override
    public boolean isChecked() {
        return isChecked;
    }

    @Override
    public void toggle() {
        setChecked(!isChecked);
    }

}
