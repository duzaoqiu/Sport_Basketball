package com.bench.android.core.view.recyclerview.layoutmanager;

import android.graphics.PointF;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by qiaomu on 2017/6/9.
 */

public interface ILayoutManager {
    RecyclerView.LayoutManager getLayoutManager();

    int findLastVisiblePosition();

    int findFirstVisiblePosition();

    void scrollPositionWithOffset(int position, int offset);

    PointF computeScrollVector4Position(int targetPosition);

    boolean isStackFromEnd();

    void setStackFromEndIfPossible(boolean stackFromEnd);

    View findViewOfPosition(int position);

    void setScrollVerticalEnable(boolean scrollEnable);
}