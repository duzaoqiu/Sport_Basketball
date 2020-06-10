package com.bench.android.core.view.widget;

import android.content.Context;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.view.Gravity;

import com.bench.android.core.view.wrapper.WrapTextView;

/**
 * @author zhouyi
 */
public class TimerCountButton extends WrapTextView {

    private long millisInFuture;

    private long countDownInterval;

    private MyTimeCount myTimeCount;

    private int originalColor;

    /**
     * 不可点击颜色
     */
    private int changedColor = 0xffbfbfbf;

    private boolean isWorking;

    public TimerCountButton(Context context) {
        super(context);
        setGravity(Gravity.CENTER);
    }

    public TimerCountButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        setGravity(Gravity.CENTER);
    }

    public TimerCountButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setGravity(Gravity.CENTER);
    }

    public void setTimer(long secondsInFuture) {
        setTimer(secondsInFuture, 1);
    }

    public void setTimer(long secondsInFuture, long secondInterval) {
        this.millisInFuture = secondsInFuture * 1000;
        this.countDownInterval = secondInterval * 1000;
    }

    public void startTimeCount() {
        setEnabled(false);
        myTimeCount = new MyTimeCount(millisInFuture, countDownInterval);
        originalColor = getCurrentTextColor();
        setTextColor(changedColor);
        myTimeCount.start();
        isWorking = true;
    }

    private class MyTimeCount extends CountDownTimer {

        MyTimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            setText((millisUntilFinished / 1000) + "s");
        }

        @Override
        public void onFinish() {
            setText("获取验证码");
            setEnabled(true);
            setTextColor(originalColor);
            isWorking = false;
        }
    }

    public boolean isWorking() {
        return isWorking;
    }

    public void reset() {
        if (myTimeCount == null) {
            throw new NullPointerException("CountDownTimer has not been initialized");
        }
        myTimeCount.onFinish();
    }

    public void closeTimeCount() {
        if (myTimeCount != null) {
            myTimeCount.cancel();
        }
        isWorking = false;
    }

}
