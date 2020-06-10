package com.bench.android.core.os.loop;

import android.os.Handler;

/**
 * 类似CountDownTask，但是加入delay功能，CountDownTask，没有delay功能
 * <p>
 * <p>
 * Loop loop = Loop.create().take(5).resume(2000, new LoopCallback() {
 *
 * @Override public void run() {
 * LogUtils.e("" + mCount++);
 * }
 * });
 * <p>
 * @author qiaomu
 */

public class TimerLoop extends Handler {

    public int mTakeCount = 0;
    public int mTake = -1;

    private TimerLoop() {
    }

    public static TimerLoop create() {
        return new TimerLoop();
    }

    public TimerLoop take(int take) {
        this.mTake = take;
        return this;
    }

    public TimerLoop start(int interval, LoopCallback loopCallback) {
        return start(interval, 0, loopCallback);
    }

    public TimerLoop startDelay(int delayMills, LoopCallback loopCallback) {
        return start(0, delayMills, loopCallback);
    }

    public TimerLoop start(final int interval, final int delayMills, final LoopCallback callback) {
        stop();
        postDelayed(new Runnable() {
            @Override
            public void run() {
                mTakeCount++;
                callback.run();
                if (mTake != -1) {
                    if (mTakeCount >= mTake) {
                        stop();
                    } else if (interval > 0) {
                        postDelayed(this, interval);
                    } else {
                        stop();
                    }
                } else if (interval > 0) {
                    postDelayed(this, interval);
                } else {
                    stop();
                }
            }
        }, delayMills);
        return this;
    }

    public void stop() {
        removeCallbacksAndMessages(null);
    }
}
