package com.bench.android.core.view.recyclerview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.OverScroller;

import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.material.appbar.AppBarLayout;

import java.lang.reflect.Field;

/**
 * 解决appcompat-v7的AppBarLayout快速下拉回弹问题
 * 在AppBarLayout标签下加入 app:layout_behavior="com.bench.android.core.view.RecyclerView.AppBarLayoutBehavior" 即可
 * Created by danao on 2019/2/18.
 */
public class AppBarLayoutBehavior extends CoordinatorLayout.Behavior {

    private static final int TYPE_FLING = 1;
    private boolean isFlinging;
    private boolean shouldBlockNestedScroll;

    public AppBarLayoutBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public boolean onInterceptTouchEvent(CoordinatorLayout parent, AppBarLayout child, MotionEvent ev) {
        this.shouldBlockNestedScroll = this.isFlinging;
        switch (ev.getActionMasked()) {
            case 0:
                this.stopAppbarLayoutFling(child);
            default:
                return super.onInterceptTouchEvent(parent, child, ev);
        }
    }

    private void stopAppbarLayoutFling(AppBarLayout appBarLayout) {
        try {
            Class<?> headerBehaviorType = this.getClass().getSuperclass().getSuperclass();
            Field flingRunnableField = headerBehaviorType.getDeclaredField("mFlingRunnable");
            Field scrollerField = headerBehaviorType.getDeclaredField("mScroller");
            flingRunnableField.setAccessible(true);
            scrollerField.setAccessible(true);
            Runnable flingRunnable = (Runnable) flingRunnableField.get(this);
            OverScroller overScroller = (OverScroller) scrollerField.get(this);
            if (flingRunnable != null) {
                appBarLayout.removeCallbacks(flingRunnable);
                flingRunnableField.set(this, null);
            }
            if (overScroller != null && !overScroller.isFinished()) {
                overScroller.abortAnimation();
            }
        } catch (NoSuchFieldException var7) {
            var7.printStackTrace();
        } catch (IllegalAccessException var8) {
            var8.printStackTrace();
        }
    }

    public boolean onStartNestedScroll(CoordinatorLayout parent, AppBarLayout child, View directTargetChild, View target, int nestedScrollAxes, int type) {
        this.stopAppbarLayoutFling(child);
        return super.onStartNestedScroll(parent, child, directTargetChild, target, nestedScrollAxes, type);
    }

    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, AppBarLayout child, View target, int dx, int dy, int[] consumed, int type) {
        if (type == 1) {
            this.isFlinging = true;
        }
        if (!this.shouldBlockNestedScroll) {
            super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type);
        }
    }

    public void onNestedScroll(CoordinatorLayout coordinatorLayout, AppBarLayout child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type) {
        if (!this.shouldBlockNestedScroll) {
            super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type);
        }
    }

    public void onStopNestedScroll(CoordinatorLayout coordinatorLayout, AppBarLayout abl, View target, int type) {
        super.onStopNestedScroll(coordinatorLayout, abl, target, type);
        this.isFlinging = false;
        this.shouldBlockNestedScroll = false;
    }
}
