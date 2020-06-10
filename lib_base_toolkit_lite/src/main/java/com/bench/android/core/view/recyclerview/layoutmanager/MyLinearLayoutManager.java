package com.bench.android.core.view.recyclerview.layoutmanager;

import android.content.Context;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by qiaomnu on 17/6/9.
 */
public class MyLinearLayoutManager extends LinearLayoutManager implements ILayoutManager {
    private boolean mScrollEnable=true;

    public MyLinearLayoutManager(Context context) {
        super(context);
    }

    public MyLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    public MyLinearLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    @Override
    public RecyclerView.LayoutManager getLayoutManager() {
        return this;
    }

    @Override
    public int findLastVisiblePosition() {
        return findLastVisibleItemPosition();
    }

    @Override
    public int findFirstVisiblePosition() {
        return findFirstVisibleItemPosition();
    }

    @Override
    public void scrollPositionWithOffset(int position, int offset) {
        super.scrollToPositionWithOffset(position, offset);
    }

    @Override
    public PointF computeScrollVector4Position(int targetPosition) {
        return computeScrollVectorForPosition(targetPosition);
    }

    @Override
    public boolean isStackFromEnd() {
        return getStackFromEnd();
    }

    @Override
    public void setStackFromEndIfPossible(boolean stackFromEnd) {
        setStackFromEnd(stackFromEnd);
    }

    @Override
    public View findViewOfPosition(int position) {
        return this.findViewByPosition(position);
    }

    @Override
    public void setScrollVerticalEnable(boolean scrollEnable) {
        mScrollEnable = scrollEnable;
    }
    @Override
    public boolean canScrollVertically() {
        return mScrollEnable && super.canScrollVertically();
    }


}
