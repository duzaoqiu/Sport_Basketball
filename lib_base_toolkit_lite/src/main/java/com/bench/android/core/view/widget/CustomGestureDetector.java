package com.bench.android.core.view.widget;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;

/************************************************************************
 *@Project: mingliao
 *@Package_Name: com.android.library.tools.Utils
 *@Descriptions:从系统的GestureDetector copy了一份修改了一下，使其支持长按松开的事件
 *@Author: xingjiu
 *@Date: 2019/4/26 
 *************************************************************************/
public class CustomGestureDetector {
    public interface OnGestureListener {

        boolean onDown(MotionEvent e);

        void onShowPress(MotionEvent e);

        boolean onSingleTapUp(MotionEvent e);


        boolean onLongPressTapUp(MotionEvent e);

        void onLongPressCancel();

        boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY);

        void onLongPress(MotionEvent e);

        boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY);
    }


    public static class SimpleOnGestureListener implements CustomGestureDetector.OnGestureListener {

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return false;
        }

        @Override
        public boolean onLongPressTapUp(MotionEvent e) {
            return false;
        }

        @Override
        public void onLongPressCancel() {

        }

        @Override
        public void onLongPress(MotionEvent e) {
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2,
                                float distanceX, float distanceY) {
            return false;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                               float velocityY) {
            return false;
        }

        @Override
        public void onShowPress(MotionEvent e) {
        }

        @Override
        public boolean onDown(MotionEvent e) {
            return false;
        }

    }

    private int mTouchSlopSquare;
    private int mMinimumFlingVelocity;
    private int mMaximumFlingVelocity;

    private static final int LONGPRESS_TIMEOUT = ViewConfiguration.getLongPressTimeout();
    private static final int TAP_TIMEOUT = ViewConfiguration.getTapTimeout();

    private static final int SHOW_PRESS = 1;
    private static final int LONG_PRESS = 2;
    private static final int TAP = 3;

    private final Handler mHandler;
    private final CustomGestureDetector.OnGestureListener mListener;

    private boolean mInLongPress;
    private boolean mInContextClick;
    private boolean mAlwaysInTapRegion;
    private boolean mIgnoreNextUpEvent;

    private MotionEvent mCurrentDownEvent;
    private MotionEvent mPreviousUpEvent;


    private float mLastFocusX;
    private float mLastFocusY;
    private float mDownFocusX;
    private float mDownFocusY;

    private boolean mIsLongpressEnabled;

    private VelocityTracker mVelocityTracker;


    private class GestureHandler extends Handler {
        GestureHandler() {
            super();
        }

        GestureHandler(Handler handler) {
            super(handler.getLooper());
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SHOW_PRESS:
                    mListener.onShowPress(mCurrentDownEvent);
                    break;

                case LONG_PRESS:
                    dispatchLongPress();
                    break;

                default:
                    throw new RuntimeException("Unknown message " + msg); //never
            }
        }
    }


    public CustomGestureDetector(Context context, CustomGestureDetector.OnGestureListener listener) {
        this(context, listener, null);
    }

    public CustomGestureDetector(Context context, CustomGestureDetector.OnGestureListener listener, Handler handler) {
        if (handler != null) {
            mHandler = new GestureHandler(handler);
        } else {
            mHandler = new GestureHandler();
        }
        mListener = listener;
        init(context);
    }

    private void init(Context context) {
        if (mListener == null) {
            throw new NullPointerException("OnGestureListener must not be null");
        }
        mIsLongpressEnabled = true;

        // Fallback to support pre-donuts releases
        int touchSlop;
        if (context == null) {
            //noinspection deprecation
            touchSlop = ViewConfiguration.getTouchSlop();
            //noinspection deprecation
            mMinimumFlingVelocity = ViewConfiguration.getMinimumFlingVelocity();
            mMaximumFlingVelocity = ViewConfiguration.getMaximumFlingVelocity();
        } else {
            final ViewConfiguration configuration = ViewConfiguration.get(context);
            touchSlop = configuration.getScaledTouchSlop();
            mMinimumFlingVelocity = configuration.getScaledMinimumFlingVelocity();
            mMaximumFlingVelocity = configuration.getScaledMaximumFlingVelocity();
        }
        mTouchSlopSquare = touchSlop * touchSlop;
    }


    public void setIsLongpressEnabled(boolean isLongpressEnabled) {
        mIsLongpressEnabled = isLongpressEnabled;
    }

    public boolean isLongpressEnabled() {
        return mIsLongpressEnabled;
    }

    public boolean onTouchEvent(MotionEvent ev) {

        final int action = ev.getAction();

        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(ev);

        final boolean pointerUp =
                (action & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_POINTER_UP;
        final int skipIndex = pointerUp ? ev.getActionIndex() : -1;
        final boolean isGeneratedGesture =
                (ev.getFlags() & 0x8) != 0;

        // Determine focal point
        float sumX = 0, sumY = 0;
        final int count = ev.getPointerCount();
        for (int i = 0; i < count; i++) {
            if (skipIndex == i) {
                continue;
            }
            sumX += ev.getX(i);
            sumY += ev.getY(i);
        }
        final int div = pointerUp ? count - 1 : count;
        final float focusX = sumX / div;
        final float focusY = sumY / div;

        boolean handled = false;

        switch (action & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_POINTER_DOWN:
                mDownFocusX = mLastFocusX = focusX;
                mDownFocusY = mLastFocusY = focusY;
                // Cancel long press and taps
                cancelTaps();
                break;

            case MotionEvent.ACTION_POINTER_UP:
                mDownFocusX = mLastFocusX = focusX;
                mDownFocusY = mLastFocusY = focusY;

                // Check the dot product of current velocities.
                // If the pointer that left was opposing another velocity vector, clear.
                mVelocityTracker.computeCurrentVelocity(1000, mMaximumFlingVelocity);
                final int upIndex = ev.getActionIndex();
                final int id1 = ev.getPointerId(upIndex);
                final float x1 = mVelocityTracker.getXVelocity(id1);
                final float y1 = mVelocityTracker.getYVelocity(id1);
                for (int i = 0; i < count; i++) {
                    if (i == upIndex) {
                        continue;
                    }

                    final int id2 = ev.getPointerId(i);
                    final float x = x1 * mVelocityTracker.getXVelocity(id2);
                    final float y = y1 * mVelocityTracker.getYVelocity(id2);

                    final float dot = x + y;
                    if (dot < 0) {
                        mVelocityTracker.clear();
                        break;
                    }
                }
                break;

            case MotionEvent.ACTION_DOWN:

                mDownFocusX = mLastFocusX = focusX;
                mDownFocusY = mLastFocusY = focusY;
                if (mCurrentDownEvent != null) {
                    mCurrentDownEvent.recycle();
                }
                mCurrentDownEvent = MotionEvent.obtain(ev);
                mAlwaysInTapRegion = true;
                mInLongPress = false;

                if (mIsLongpressEnabled) {
                    mHandler.removeMessages(LONG_PRESS);
                    mHandler.sendEmptyMessageAtTime(LONG_PRESS,
                            mCurrentDownEvent.getDownTime() + LONGPRESS_TIMEOUT);
                }
                mHandler.sendEmptyMessageAtTime(SHOW_PRESS,
                        mCurrentDownEvent.getDownTime() + TAP_TIMEOUT);
                handled |= mListener.onDown(ev);
                break;

            case MotionEvent.ACTION_MOVE:
                if (mInLongPress || mInContextClick) {
                    break;
                }
                final float scrollX = mLastFocusX - focusX;
                final float scrollY = mLastFocusY - focusY;
                if (mAlwaysInTapRegion) {
                    final int deltaX = (int) (focusX - mDownFocusX);
                    final int deltaY = (int) (focusY - mDownFocusY);
                    int distance = (deltaX * deltaX) + (deltaY * deltaY);
                    int slopSquare = isGeneratedGesture ? 0 : mTouchSlopSquare;
                    if (distance > slopSquare) {
                        handled = mListener.onScroll(mCurrentDownEvent, ev, scrollX, scrollY);
                        mLastFocusX = focusX;
                        mLastFocusY = focusY;
                        mAlwaysInTapRegion = false;
                        mHandler.removeMessages(TAP);
                        mHandler.removeMessages(SHOW_PRESS);
                        mHandler.removeMessages(LONG_PRESS);
                    }
                } else if ((Math.abs(scrollX) >= 1) || (Math.abs(scrollY) >= 1)) {
                    handled = mListener.onScroll(mCurrentDownEvent, ev, scrollX, scrollY);
                    mLastFocusX = focusX;
                    mLastFocusY = focusY;
                }
                break;

            case MotionEvent.ACTION_UP:
                MotionEvent currentUpEvent = MotionEvent.obtain(ev);
                if (mInLongPress) {
                    mHandler.removeMessages(TAP);
                    mInLongPress = false;
                    handled = mListener.onLongPressTapUp(ev);
                } else if (mAlwaysInTapRegion && !mIgnoreNextUpEvent) {
                    handled = mListener.onSingleTapUp(ev);
                } else if (!mIgnoreNextUpEvent) {

                    // A fling must travel the minimum tap distance
                    final VelocityTracker velocityTracker = mVelocityTracker;
                    final int pointerId = ev.getPointerId(0);
                    velocityTracker.computeCurrentVelocity(1000, mMaximumFlingVelocity);
                    final float velocityY = velocityTracker.getYVelocity(pointerId);
                    final float velocityX = velocityTracker.getXVelocity(pointerId);

                    if ((Math.abs(velocityY) > mMinimumFlingVelocity)
                            || (Math.abs(velocityX) > mMinimumFlingVelocity)) {
                        handled = mListener.onFling(mCurrentDownEvent, ev, velocityX, velocityY);
                    }
                }
                if (mPreviousUpEvent != null) {
                    mPreviousUpEvent.recycle();
                }
                // Hold the event we obtained above - listeners may have changed the original.
                mPreviousUpEvent = currentUpEvent;
                if (mVelocityTracker != null) {
                    // This may have been cleared when we called out to the
                    // application above.
                    mVelocityTracker.recycle();
                    mVelocityTracker = null;
                }
                mIgnoreNextUpEvent = false;
                mHandler.removeMessages(SHOW_PRESS);
                mHandler.removeMessages(LONG_PRESS);
                break;

            case MotionEvent.ACTION_CANCEL:
                cancel();
                break;
        }
        return handled;
    }

    private void cancel() {
        mHandler.removeMessages(SHOW_PRESS);
        mHandler.removeMessages(LONG_PRESS);
        mHandler.removeMessages(TAP);
        if (mInLongPress) {
            mListener.onLongPressCancel();
        }
        mVelocityTracker.recycle();
        mVelocityTracker = null;
        mAlwaysInTapRegion = false;
        mInLongPress = false;
        mInContextClick = false;
        mIgnoreNextUpEvent = false;
    }

    private void cancelTaps() {
        mHandler.removeMessages(SHOW_PRESS);
        mHandler.removeMessages(LONG_PRESS);
        mHandler.removeMessages(TAP);
        mAlwaysInTapRegion = false;
        mInLongPress = false;
        mInContextClick = false;
        mIgnoreNextUpEvent = false;
    }


    private void dispatchLongPress() {
        mHandler.removeMessages(TAP);
        mInLongPress = true;
        mListener.onLongPress(mCurrentDownEvent);
    }
}
