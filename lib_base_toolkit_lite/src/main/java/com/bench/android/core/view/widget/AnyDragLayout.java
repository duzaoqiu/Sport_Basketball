package com.bench.android.core.view.widget;

import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.customview.widget.ViewDragHelper;

/**
 * 可以设置浮动自由拖拽的按钮，手指抬起的时候按钮会自动贴到布局的左边或者右边 的RelativeLayout
 * 使用方式，在布局里面引用{@link AnyDragLayout},然后代码里面设置可以拖拽的view{@link #setCanDragView(View)}
 *
 * @author :  malong    luomingbear@163.com
 * @date :  2019/4/29
 * @see ViewDragHelper
 **/
public class AnyDragLayout extends RelativeLayout {
    private ViewDragHelper mDragHelper;
    private View mCanDragView;
    private Point mCanDragViewPoint = new Point();

    public AnyDragLayout(Context context) {
        super(context);
        init();
    }

    public AnyDragLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AnyDragLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mDragHelper = ViewDragHelper.create(this, 1F, callback);
    }

    /**
     * 设置可以拖拽的view
     *
     * @param canDragView
     */
    public void setCanDragView(View canDragView) {
        this.mCanDragView = canDragView;
    }

    /**
     * 获取可以拖拽的view
     *
     * @return
     */
    public View getCanDragView() {
        return mCanDragView;
    }

    private ViewDragHelper.Callback callback = new ViewDragHelper.Callback() {
        @Override
        public boolean tryCaptureView(@NonNull View child, int pointerId) {
            return child == mCanDragView;
        }

        @Override
        public int getViewHorizontalDragRange(@NonNull View child) {
            return getWidth() - child.getWidth();
        }

        @Override
        public int getViewVerticalDragRange(@NonNull View child) {
            return getHeight() - child.getHeight();
        }

        @Override
        public int clampViewPositionHorizontal(@NonNull View child, int left, int dx) {
            int x = Math.min(left, getWidth() - child.getWidth() - getPaddingRight());
            x = Math.max(x, getPaddingLeft());
            return x;
        }

        @Override
        public int clampViewPositionVertical(@NonNull View child, int top, int dy) {
            int y = Math.min(top, getHeight() - child.getHeight() - getPaddingBottom());
            y = Math.max(y, getPaddingTop());
            return y;
        }

        @Override
        public void onViewReleased(@NonNull View releasedChild, float xvel, float yvel) {
            if (releasedChild == mCanDragView) {
                LayoutParams params = (LayoutParams) releasedChild.getLayoutParams();
                int x = getPaddingLeft() + params.leftMargin;
                if (releasedChild.getLeft() > getWidth() / 2) {
                    x = getWidth() - releasedChild.getWidth() - getPaddingLeft() - params.rightMargin;
                }
                int y = mCanDragView.getTop();
                y = Math.max(getPaddingTop() + params.topMargin, y);
                y = Math.min(getHeight() - getPaddingBottom() - releasedChild.getHeight() - params.bottomMargin, y);
                mCanDragViewPoint.x = x;
                mCanDragViewPoint.y = y;
                mDragHelper.settleCapturedViewAt(x, y);
                invalidate();
            }
        }
    };

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mCanDragView != null) {
            mDragHelper.continueSettling(true);
            invalidate();
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        //解决布局刷新之后拖拽的位置就清除掉了，view又回到了初始位置的情况
        if (mCanDragViewPoint.x == 0 && mCanDragViewPoint.y == 0 && mCanDragView != null) {
            mCanDragViewPoint.x = mCanDragView.getLeft();
            mCanDragViewPoint.y = mCanDragView.getTop();
        }
        if (mCanDragView != null) {
            mCanDragView.layout(
                    mCanDragViewPoint.x,
                    mCanDragViewPoint.y,
                    mCanDragViewPoint.x + mCanDragView.getWidth(),
                    mCanDragViewPoint.y + mCanDragView.getHeight());
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mDragHelper.processTouchEvent(event);
        return super.onTouchEvent(event);
    }
}
