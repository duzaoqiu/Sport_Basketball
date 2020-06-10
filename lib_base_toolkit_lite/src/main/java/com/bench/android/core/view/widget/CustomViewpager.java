package com.bench.android.core.view.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

/************************************************************************
 *@Project: ml
 *@Package_Name: com.minglin.android.lib.mission.ui.activity.detail.dynamic
 *@Descriptions: 用于scrollview嵌套viewpager嵌套RecyclerView
 *@Author: xingjiu
 *@Date: 2019/6/24 
 *************************************************************************/
public class CustomViewpager extends ViewPager {

    public CustomViewpager(@NonNull Context context) {
        super(context);
    }

    public CustomViewpager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = 0;
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (child.getVisibility() != GONE && getCurrentItem() == i) {
                child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
                int h = child.getMeasuredHeight();
                if (h > height) {
                    height = h;
                }
            }
        }

        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
